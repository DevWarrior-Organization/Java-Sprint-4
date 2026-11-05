package br.com.fiap.beans;

import java.time.LocalDate;

/**
 * Entidade que representa uma viagem sustentavel registrada por um usuario.
 * Espelha a tabela T_SP_VIAGEM do Oracle.
 */
public class Viagem {

    private long id;
    private long usuarioId;
    private String tipoTransporte;
    private double distanciaKm;
    private double pontos;
    private LocalDate dataViagem;

    public Viagem() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(long usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getTipoTransporte() {
        return tipoTransporte;
    }

    public void setTipoTransporte(String tipoTransporte) {
        this.tipoTransporte = tipoTransporte;
    }

    public double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }

    public double getPontos() {
        return pontos;
    }

    public void setPontos(double pontos) {
        this.pontos = pontos;
    }

    public LocalDate getDataViagem() {
        return dataViagem;
    }

    public void setDataViagem(LocalDate dataViagem) {
        this.dataViagem = dataViagem;
    }

    @Override
    public String toString() {
        return "Viagem{" +
                "id=" + id +
                ", usuarioId=" + usuarioId +
                ", tipoTransporte='" + tipoTransporte + '\'' +
                ", distanciaKm=" + distanciaKm +
                ", pontos=" + pontos +
                ", dataViagem=" + dataViagem +
                '}';
    }
}
