package br.com.fiap.conexoes;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
/**
 * Padrao Connection Factory: ponto unico de criacao de conexoes com o Oracle.
 *
 * As credenciais podem ser sobrescritas por variaveis de ambiente
 * (DB_HOST, DB_USER, DB_PASSWORD), o que permite o deploy na nuvem
 * sem expor dados sensiveis no codigo.
 */
public final class ConnectionFactory {
    private static final String URL = "jdbc:oracle:thin:@%s:1521:ORCL";
    private ConnectionFactory() {
    }
    /**
     * Cria e devolve uma nova conexao com o banco de dados.
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                String.format(URL, env("DB_HOST", "oracle.fiap.com.br")),
                requiredEnv("DB_USER"),
                requiredEnv("DB_PASSWORD"));
    }
    /**
     * Cria tabelas, sequences e indices caso ainda nao existam.
     * Erros de "objeto ja existe" sao ignorados de proposito.
     */
    public static void inicializarBanco() {
        String[] sqls = {
            "CREATE SEQUENCE seq_usuario START WITH 1 INCREMENT BY 1",
            "CREATE SEQUENCE seq_viagem START WITH 1 INCREMENT BY 1",
            "CREATE TABLE T_SP_USUARIO (" +
                "id_usuario NUMBER(10) PRIMARY KEY, " +
                "nm_usuario VARCHAR2(100) NOT NULL, " +
                "nr_cpf VARCHAR2(14) NOT NULL UNIQUE, " +
                "qt_pontos NUMBER(10,2) DEFAULT 0)",
            "CREATE TABLE T_SP_VIAGEM (" +
                "id_viagem NUMBER(10) PRIMARY KEY, " +
                "id_usuario NUMBER(10) NOT NULL, " +
                "ds_tipo_transporte VARCHAR2(20) NOT NULL, " +
                "nr_distancia_km NUMBER(10,2) NOT NULL, " +
                "nr_pontos NUMBER(10,2) NOT NULL, " +
                "dt_viagem DATE NOT NULL, " +
                "CONSTRAINT fk_viagem_usuario FOREIGN KEY (id_usuario) " +
                "REFERENCES T_SP_USUARIO(id_usuario) ON DELETE CASCADE)",
            "CREATE INDEX idx_viagem_usuario ON T_SP_VIAGEM(id_usuario)",
            "CREATE INDEX idx_viagem_data ON T_SP_VIAGEM(dt_viagem)",
            "CREATE INDEX idx_usuario_cpf ON T_SP_USUARIO(nr_cpf)"
        };
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            for (String sql : sqls) {
                try {
                    stmt.execute(sql);
                } catch (SQLException e) {
                    // tabela/sequence/indice ja existe, ignora
                }
            }
            System.out.println("Banco de dados inicializado com sucesso!");
        } catch (SQLException e) {
            System.err.println("Aviso: nao foi possivel inicializar o banco agora: " + e.getMessage());
        }
    }
    private static String requiredEnv(String chave) {
        String valor = System.getenv(chave);
        if (valor == null || valor.isBlank()) {
            throw new IllegalStateException("Configure a variavel de ambiente " + chave);
        }
        return valor;
    }
    private static String env(String chave, String padrao) {
        String valor = System.getenv(chave);
        return (valor == null || valor.isBlank()) ? padrao : valor;
    }
}
