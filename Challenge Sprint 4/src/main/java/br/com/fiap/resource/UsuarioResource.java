package br.com.fiap.resource;

import br.com.fiap.beans.Usuario;
import br.com.fiap.bo.UsuarioBO;
import br.com.fiap.dao.UsuarioDAO;
import br.com.fiap.dao.ViagemDAO;
import br.com.fiap.exception.RegraNegocioException;
import br.com.fiap.exception.RecursoNaoEncontradoException;
import java.net.URI;
import java.sql.SQLException;
import java.util.List;
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
 * Endpoints REST da entidade Usuario (tabela T_SP_USUARIO).
 */
@RestController
@RequestMapping("/usuarios")
public class UsuarioResource {

    private final UsuarioDAO usuarioDAO;
    private final ViagemDAO viagemDAO;
    private final UsuarioBO usuarioBO;

    public UsuarioResource(UsuarioDAO usuarioDAO, ViagemDAO viagemDAO, UsuarioBO usuarioBO) {
        this.usuarioDAO = usuarioDAO;
        this.viagemDAO = viagemDAO;
        this.usuarioBO = usuarioBO;
    }

    /** GET /usuarios -> lista todos os usuarios (200) */
    @GetMapping
    public List<Usuario> listarTodos() throws SQLException {
        return usuarioDAO.listarTodos();
    }

    /** GET /usuarios/{id} -> busca um usuario por ID (200 / 404) */
    @GetMapping("/{id}")
    public Usuario buscarPorId(@PathVariable long id) throws SQLException {
        return buscarUsuario(id);
    }

    /** POST /usuarios -> cadastra um usuario (201 / 400) */
    @PostMapping
    public ResponseEntity<Usuario> cadastrar(@RequestBody Usuario usuario) throws SQLException {
        usuarioBO.validarUsuario(usuario.getNome(), usuario.getCpf());
        usuario.setNome(usuario.getNome().trim());
        usuario.setCpf(usuario.getCpf().trim());
        usuario.setPontosTotal(0);

        if (usuarioDAO.buscarPorCpf(usuario.getCpf()) != null) {
            throw new RegraNegocioException("CPF ja cadastrado para outro usuario.");
        }

        long id = usuarioDAO.inserir(usuario);
        if (id <= 0) {
            throw new RegraNegocioException("Nao foi possivel cadastrar o usuario.");
        }
        usuario.setId(id);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(id).toUri();
        return ResponseEntity.created(uri).body(usuario);
    }

    /** PUT /usuarios/{id} -> atualiza nome e/ou CPF (200 / 400 / 404) */
    @PutMapping("/{id}")
    public Usuario atualizar(@PathVariable long id, @RequestBody Usuario dados) throws SQLException {
        Usuario usuario = buscarUsuario(id);
        usuarioBO.validarUsuario(dados.getNome(), dados.getCpf());

        usuario.setNome(dados.getNome().trim());
        usuario.setCpf(dados.getCpf().trim());

        Usuario mesmoCpf = usuarioDAO.buscarPorCpf(usuario.getCpf());
        if (mesmoCpf != null && mesmoCpf.getId() != id) {
            throw new RegraNegocioException("CPF ja cadastrado para outro usuario.");
        }

        if (!usuarioDAO.atualizar(usuario)) {
            throw new RegraNegocioException("Nao foi possivel atualizar o usuario.");
        }
        return usuario;
    }

    /** DELETE /usuarios/{id} -> exclui o usuario e suas viagens (204 / 404) */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable long id) throws SQLException {
        buscarUsuario(id);
        viagemDAO.excluirPorUsuario(id);
        usuarioDAO.excluir(id);
        return ResponseEntity.noContent().build();
    }

    private Usuario buscarUsuario(long id) throws SQLException {
        Usuario usuario = usuarioDAO.buscarPorId(id);
        if (usuario == null) {
            throw new RecursoNaoEncontradoException("Usuario nao encontrado com o ID: " + id);
        }
        return usuario;
    }
}
