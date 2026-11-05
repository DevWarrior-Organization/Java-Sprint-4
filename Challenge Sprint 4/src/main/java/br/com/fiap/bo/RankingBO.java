package br.com.fiap.bo;

import br.com.fiap.beans.Usuario;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import org.springframework.stereotype.Component;

/**
 * Camada de regra de negocio do ranking: ordena os usuarios
 * pela pontuacao total, do maior para o menor.
 */
@Component
public class RankingBO {

    public List<Usuario> gerarRanking(List<Usuario> usuarios) {
        if (usuarios == null || usuarios.isEmpty()) {
            return new ArrayList<>();
        }
        List<Usuario> ranking = new ArrayList<>(usuarios);
        ranking.sort(Comparator.comparingDouble(Usuario::getPontosTotal).reversed());
        return ranking;
    }
}
