package br.com.fiap.bo;

import br.com.fiap.exception.RegraNegocioException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * Camada de regras de negocio das viagens: validacoes, fatores de
 * pontuacao por modal e calculo do saldo de pontos do usuario.
 */
@Component
public class ViagemBO {

    private static final Map<String, Double> FATORES_PONTUACAO = new HashMap<>();

    static {
        FATORES_PONTUACAO.put("onibus", 1.0);
        FATORES_PONTUACAO.put("metro", 1.2);
        FATORES_PONTUACAO.put("trem", 1.2);
        FATORES_PONTUACAO.put("bicicleta", 2.0);
    }

    /**
     * Calcula os pontos de uma viagem (fator do modal x distancia),
     * arredondados para 2 casas decimais.
     */
    public double calcularPontos(String tipoTransporte, double distanciaKm) {
        Double fator = FATORES_PONTUACAO.get(tipoTransporte.toLowerCase());
        if (fator == null) {
            throw new RegraNegocioException("Modal de transporte invalido: " + tipoTransporte);
        }
        double pontos = fator * distanciaKm;
        return Math.round(pontos * 100.0) / 100.0;
    }

    /**
     * Valida os dados de entrada de uma viagem.
     */
    public void validarDadosViagem(String tipoTransporte, double distanciaKm) {
        if (tipoTransporte == null || tipoTransporte.trim().isEmpty()) {
            throw new RegraNegocioException("Tipo de transporte nao pode ser vazio.");
        }
        if (!FATORES_PONTUACAO.containsKey(tipoTransporte.toLowerCase())) {
            throw new RegraNegocioException(
                    "Modal invalido. Opcoes validas: onibus, metro, trem, bicicleta.");
        }
        if (distanciaKm <= 0) {
            throw new RegraNegocioException("Distancia deve ser maior que zero.");
        }
    }

    /**
     * Atualiza o saldo de pontos do usuario (adicionar ou subtrair).
     */
    public double atualizarSaldoPontos(double pontosAtuais, double pontosViagem, String operacao) {
        switch (operacao.toLowerCase()) {
            case "adicionar":
                return Math.round((pontosAtuais + pontosViagem) * 100.0) / 100.0;
            case "subtrair":
                double novoSaldo = pontosAtuais - pontosViagem;
                return Math.round(Math.max(0, novoSaldo) * 100.0) / 100.0;
            default:
                throw new RegraNegocioException("Operacao invalida. Use: adicionar ou subtrair.");
        }
    }

    public boolean isModalValido(String tipoTransporte) {
        return FATORES_PONTUACAO.containsKey(tipoTransporte.toLowerCase());
    }

    public double getFator(String tipoTransporte) {
        Double fator = FATORES_PONTUACAO.get(tipoTransporte.toLowerCase());
        if (fator == null) {
            throw new RegraNegocioException("Modal invalido: " + tipoTransporte);
        }
        return fator;
    }

    public Map<String, Double> listarFatores() {
        return Map.copyOf(FATORES_PONTUACAO);
    }
}
