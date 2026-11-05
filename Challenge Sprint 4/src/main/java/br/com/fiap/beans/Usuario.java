package br.com.fiap.beans;

/**
 * Entidade que representa um usuario da plataforma de mobilidade.
 * Espelha a tabela T_SP_USUARIO do Oracle.
 */
public class Usuario {

    private long id;
    private String nome;
    private String cpf;
    private double pontosTotal;

    public Usuario() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public double getPontosTotal() {
        return pontosTotal;
    }

    public void setPontosTotal(double pontosTotal) {
        this.pontosTotal = pontosTotal;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", cpf='" + cpf + '\'' +
                ", pontosTotal=" + pontosTotal +
                '}';
    }
}
