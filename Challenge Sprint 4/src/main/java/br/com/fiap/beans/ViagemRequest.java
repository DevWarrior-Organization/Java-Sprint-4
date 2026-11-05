package br.com.fiap.beans;

import java.time.LocalDate;

/**
 * Objeto de entrada (DTO) para POST /viagens e PUT /viagens/{id}.
 * No PUT, campos nulos mantem o valor ja existente na viagem.
 */
public record ViagemRequest(
        Long usuarioId,
        String tipoTransporte,
        Double distanciaKm,
        LocalDate dataViagem
) {
}
