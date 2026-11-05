package br.com.fiap.config;

import br.com.fiap.conexoes.ConnectionFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.EventListener;

/**
 * Cria tabelas, sequences e indices no Oracle assim que
 * a aplicacao termina de subir.
 */
@Configuration
public class InicializadorBanco {

    @EventListener(ApplicationReadyEvent.class)
    public void criarEstrutura() {
        ConnectionFactory.inicializarBanco();
    }
}
