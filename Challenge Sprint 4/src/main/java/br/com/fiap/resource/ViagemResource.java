package br.com.fiap.resource;

import br.com.fiap.beans.Usuario;
import br.com.fiap.beans.Viagem;
import br.com.fiap.beans.ViagemRequest;
import br.com.fiap.bo.ViagemBO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dao.ViagemDAO;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import java.net.URI;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Endpoints REST da entidade Viagem (tabela T_SP_VIAGEM).
 * Cada escrita recalcula os pontos e o saldo do usuario.
 */
@RestController
@RequestMapping("/viagens")
public class ViagemResource {

    private final ViagemDAO viagemDAO;
    private final UsuarioDAO usuarioDAO;
    private final ViagemBO viagemBO;

    public ViagemResource(ViagemDAO viagemDAO, UsuarioDAO usuarioDAO, ViagemBO viagemBO) {
        this.viagemDAO = viagemDAO;
        this.usuarioDAO = usuarioDAO;
        this.viagemBO = viagemBO;
    }

    /** GET /viagens -> lista todas as viagens (200) */
    @GetMapping
    public List<Viagem> listarTodas() throws SQLException {
        return viagemDAO.listarTodas();
    }

    /** GET /viagens/fatores -> fatores de pontuacao por modal (200) */
    @GetMapping("/fatores")
    public Map<String, Double> listarFatores() {
        return viagemBO.listarFatores();
    }

    /** GET /viagens/{id} -> busca uma viagem por ID (200 / 404) */
    @GetMapping("/{id}")
    public Viagem buscarPorId(@PathVariable long id) throws SQLException {
        return buscarViagem(id);
    }

    /** GET /viagens/usuario/{usuarioId} -> viagens do usuario (200 / 404) */
    @GetMapping("/usuario/{usuarioId}")
    public List<Viagem> listarPorUsuario(@PathVariable long usuarioId) throws SQLException {
        buscarUsuario(usuarioId);
        return viagemDAO.listarPorUsuario(usuarioId);
    }

    /** POST /viagens -> registra a viagem, calcula pontos e credita saldo (201 / 400 / 404) */
    @PostMapping
    public ResponseEntity<Viagem> registrar(@RequestBody ViagemRequest requisicao) throws SQLException {
        if (requisicao.usuarioId() == null) {
            throw new RegraNegocioException("O ID do usuario e obrigatorio.");
        }
        Usuario usuario = buscarUsuario(requisicao.usuarioId());

        String tipoTransporte = requisicao.tipoTransporte();
        double distanciaKm = valor(requisicao.distanciaKm(), "Distancia em km e obrigatoria.");
        viagemBO.validarDadosViagem(tipoTransporte, distanciaKm);

        Viagem viagem = new Viagem();
        viagem.setUsuarioId(usuario.getId());
        viagem.setTipoTransporte(tipoTransporte.trim().toLowerCase());
        viagem.setDistanciaKm(distanciaKm);
        viagem.setPontos(viagemBO.calcularPontos(viagem.getTipoTransporte(), distanciaKm));
        viagem.setDataViagem(requisicao.dataViagem() != null ? requisicao.dataViagem() : LocalDate.now());

        long id = viagemDAO.inserir(viagem);
        if (id <= 0) {
            throw new RegraNegocioException("Nao foi possivel registrar a viagem.");
        }
        viagem.setId(id);

        double novoSaldo = viagemBO.atualizarSaldoPontos(usuario.getPontosTotal(), viagem.getPontos(), "adicionar");
        usuarioDAO.atualizarPontos(usuario.getId(), novoSaldo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(viagem);
    }

    /** PUT /viagens/{id} -> atualiza modal/distancia, recalcula pontos e ajusta saldo (200 / 400 / 404) */
    @PutMapping("/{id}")
    public Viagem atualizar(@PathVariable long id, @RequestBody ViagemRequest requisicao) throws SQLException {
        Viagem viagem = buscarViagem(id);
        Usuario usuario = buscarUsuario(viagem.getUsuarioId());

        if (requisicao.tipoTransporte() != null && !requisicao.tipoTransporte().trim().isEmpty()) {
            viagem.setTipoTransporte(requisicao.tipoTransporte().trim().toLowerCase());
        }
        if (requisicao.distanciaKm() != null) {
            viagem.setDistanciaKm(requisicao.distanciaKm());
        }
        viagemBO.validarDadosViagem(viagem.getTipoTransporte(), viagem.getDistanciaKm());

        double pontosAntigos = viagem.getPontos();
        viagem.setPontos(viagemBO.calcularPontos(viagem.getTipoTransporte(), viagem.getDistanciaKm()));
        if (requisicao.dataViagem() != null) {
            viagem.setDataViagem(requisicao.dataViagem());
        }

        if (!viagemDAO.atualizar(viagem)) {
            throw new RegraNegocioException("Nao foi possivel atualizar a viagem.");
        }

        double saldo = viagemBO.atualizarSaldoPontos(usuario.getPontosTotal(), pontosAntigos, "subtrair");
        saldo = viagemBO.atualizarSaldoPontos(saldo, viagem.getPontos(), "adicionar");
        usuarioDAO.atualizarPontos(usuario.getId(), saldo);

        return viagem;
    }

    /** DELETE /viagens/{id} -> exclui a viagem e debita o saldo do usuario (204 / 404) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) throws SQLException {
        Viagem viagem = buscarViagem(id);
        viagemDAO.excluir(id);

        Usuario usuario = usuarioDAO.buscarPorId(viagem.getUsuarioId());
        if (usuario != null) {
            double saldo = viagemBO.atualizarSaldoPontos(usuario.getPontosTotal(), viagem.getPontos(), "subtrair");
            usuarioDAO.atualizarPontos(usuario.getId(), saldo);
        }
        return ResponseEntity.noContent().build();
    }

    private Viagem buscarViagem(long id) throws SQLException {
        Viagem viagem = viagemDAO.buscarPorId(id);
        if (viagem == null) {
            throw new RecursoNaoEncontradoException("Viagem nao encontrada com o ID: " + id);
        }
        return viagem;
    }

    private Usuario buscarUsuario(long id) throws SQLException {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com o ID: " + id);
        }
        return usuario;
    }

    private double valor(Double numero, String mensagemObrigatoria) {
        if (numero == null) {
            throw new RegraNegocioException(mensagemObrigatoria);
        }
        return numero;
    }
}
