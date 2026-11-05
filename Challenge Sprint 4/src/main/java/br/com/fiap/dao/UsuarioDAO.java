package br.com.fiap.dao;

import br.com.fiap.beans.Usuario;
import br.com.fiap.conexoes.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Padrao DAO: persistencia da entidade Usuario na tabela T_SP_USUARIO.
 */
@Repository
public class UsuarioDAO {

    public long inserir(Usuario usuario) throws SQLException {
        String sql = "INSERT INTO T_SP_USUARIO (id_usuario, nm_usuario, nr_cpf, qt_pontos) VALUES (seq_usuario.NEXTVAL, ?, ?, ?)";
        long idGerado = -1;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_usuario"})) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setDouble(3, usuario.getPontosTotal());
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getLong(1);
                }
            }
        }
        return idGerado;
    }

    public Usuario buscarPorId(long id) throws SQLException {
        String sql = "SELECT id_usuario, nm_usuario, nr_cpf, qt_pontos FROM T_SP_USUARIO WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public Usuario buscarPorCpf(String cpf) throws SQLException {
        String sql = "SELECT id_usuario, nm_usuario, nr_cpf, qt_pontos FROM T_SP_USUARIO WHERE nr_cpf = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, cpf);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapear(rs);
                }
            }
        }
        return null;
    }

    public List<Usuario> listarTodos() throws SQLException {
        String sql = "SELECT id_usuario, nm_usuario, nr_cpf, qt_pontos FROM T_SP_USUARIO ORDER BY id_usuario";
        List<Usuario> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean atualizar(Usuario usuario) throws SQLException {
        String sql = "UPDATE T_SP_USUARIO SET nm_usuario = ?, nr_cpf = ?, qt_pontos = ? WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, usuario.getNome());
            stmt.setString(2, usuario.getCpf());
            stmt.setDouble(3, usuario.getPontosTotal());
            stmt.setLong(4, usuario.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean atualizarPontos(long id, double novosPontos) throws SQLException {
        String sql = "UPDATE T_SP_USUARIO SET qt_pontos = ? WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, novosPontos);
            stmt.setLong(2, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(long id) throws SQLException {
        String sql = "DELETE FROM T_SP_USUARIO WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    private Usuario mapear(ResultSet rs) throws SQLException {
        Usuario usuario = new Usuario();
        usuario.setId(rs.getLong("id_usuario"));
        usuario.setNome(rs.getString("nm_usuario"));
        usuario.setCpf(rs.getString("nr_cpf"));
        usuario.setPontosTotal(rs.getDouble("qt_pontos"));
        return usuario;
    }
}
