package br.com.fiap.exception;

import java.time.Instant;

/**
 * Corpo padrao devolvido pela API quando ocorre um erro.
 */
public record RespostaErro(Instant timestamp, int status, String erro) {

    public static RespostaErro de(int status, String mensagem) {
        return new RespostaErro(Instant.now(), status, mensagem);
    }
}
