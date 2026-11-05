package br.com.fiap.exception;

/**
 * Viola uma regra de negocio validada pela camada BO.
 * Mapeada para HTTP 400 (Bad Request).
 */
public class RegraNegocioException extends RuntimeException {

    public RegraNegocioException(String mensagem) {
        super(mensagem);
    }
}
