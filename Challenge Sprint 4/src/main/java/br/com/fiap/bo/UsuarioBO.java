package br.com.fiap.bo;

import br.com.fiap.exception.RegraNegocioException;
import org.springframework.stereotype.Component;

/**
 * Camada de regras de negocio dos usuarios: validacoes de nome e CPF.
 */
@Component
public class UsuarioBO {

    /**
     * Valida os dados de entrada de um usuario.
     */
    public void validarUsuario(String nome, String cpf) {
        if (nome == null || nome.trim().isEmpty()) {
            throw new RegraNegocioException("Nome do usuario nao pode ser vazio.");
        }
        if (cpf == null || cpf.trim().isEmpty()) {
            throw new RegraNegocioException("CPF nao pode ser vazio.");
        }
        if (nome.trim().length() > 100) {
            throw new RegraNegocioException("Nome deve ter no maximo 100 caracteres.");
        }
        if (cpf.trim().length() > 14) {
            throw new RegraNegocioException("CPF deve ter no maximo 14 caracteres.");
        }
    }
}
