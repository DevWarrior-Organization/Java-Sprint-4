package br.com.fiap.resource;

import br.com.fiap.beans.Usuario;
import br.com.fiap.bo.RankingBO;
import br.com.fiap.dao.UsuarioDAO;
import java.sql.SQLException;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint REST do ranking de usuarios por pontuacao.
 */
@RestController
@RequestMapping("/ranking")
public class RankingResource {

    private final UsuarioDAO usuarioDAO;
    private final RankingBO rankingBO;

    public RankingResource(UsuarioDAO usuarioDAO, RankingBO rankingBO) {
        this.usuarioDAO = usuarioDAO;
        this.rankingBO = rankingBO;
    }

    /** GET /ranking -> usuarios ordenados por pontos, do maior para o menor (200) */
    @GetMapping
    public List<Usuario> ranking() throws SQLException {
        return rankingBO.gerarRanking(usuarioDAO.listarTodos());
    }
}
