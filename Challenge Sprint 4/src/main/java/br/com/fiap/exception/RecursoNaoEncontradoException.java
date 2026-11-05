package br.com.fiap.exception;

/**
 * Recurso pesquisado nao existe no banco de dados.
 * Mapeada para HTTP 404 (Not Found).
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
