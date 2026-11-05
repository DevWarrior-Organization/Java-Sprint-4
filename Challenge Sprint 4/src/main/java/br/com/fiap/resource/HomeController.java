package br.com.fiap.resource;

import java.util.List;
import java.util.Map;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Endpoint raiz: pagina inicial da API, usada tambem para
 * verificar se o deploy esta online.
 */
@RestController
public class HomeController {

    @GetMapping
    public Map<String, Object> raiz() {
        return Map.of(
                "solucao", "Plataforma de Mobilidade e Sustentabilidade",
                "equipe", "Dev Warriors",
                "status", "online",
                "endpoints", List.of(
                        "GET /usuarios",
                        "POST /usuarios",
                        "GET /viagens",
                        "POST /viagens",
                        "GET /ranking"));
    }
}
