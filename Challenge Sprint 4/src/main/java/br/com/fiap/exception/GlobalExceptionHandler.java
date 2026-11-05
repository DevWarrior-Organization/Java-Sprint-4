package br.com.fiap.exception;

import java.sql.SQLException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Tratamento centralizado de excecoes: converte erros internos
 * em respostas HTTP com o status e a mensagem adequados.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /** Regra de negocio violada -> 400 */
    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<RespostaErro> regraDeNegocio(RegraNegocioException e) {
        return ResponseEntity.badRequest().body(RespostaErro.de(400, e.getMessage()));
    }

    /** Argumento invalido -> 400 */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<RespostaErro> argumentoInvalido(IllegalArgumentException e) {
        return ResponseEntity.badRequest().body(RespostaErro.de(400, e.getMessage()));
    }

    /** Recurso inexistente -> 404 */
    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ResponseEntity<RespostaErro> naoEncontrado(RecursoNaoEncontradoException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RespostaErro.de(404, e.getMessage()));
    }

    /** Rota inexistente (ex.: /qualquercoisa) -> 404 */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<RespostaErro> rotaInexistente(NoResourceFoundException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(RespostaErro.de(404, "Recurso nao encontrado: " + e.getResourcePath()));
    }

    /** Parametro de path/tipo invalido (ex.: /usuarios/abc) -> 400 */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<RespostaErro> parametroInvalido(MethodArgumentTypeMismatchException e) {
        return ResponseEntity.badRequest()
                .body(RespostaErro.de(400, "Parametro invalido: " + e.getName()));
    }

    /** JSON ausente ou malformado -> 400 */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<RespostaErro> corpoInvalido(HttpMessageNotReadableException e) {
        logger.warn("Falha ao ler o corpo da requisicao: {}", e.getMessage());
        return ResponseEntity.badRequest()
                .body(RespostaErro.de(400, "Corpo da requisicao ausente ou malformado."));
    }

    /** Falha no Oracle -> 500 */
    @ExceptionHandler(SQLException.class)
    public ResponseEntity<RespostaErro> erroBanco(SQLException e) {
        logger.error("Erro no banco de dados", e);
        return ResponseEntity.internalServerError()
                .body(RespostaErro.de(500, "Erro no banco de dados: " + e.getMessage()));
    }

    /** Qualquer outro erro -> 500 */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<RespostaErro> erroInesperado(Exception e) {
        logger.error("Erro interno inesperado", e);
        return ResponseEntity.internalServerError()
                .body(RespostaErro.de(500, "Erro interno inesperado: " + e.getMessage()));
    }
}
