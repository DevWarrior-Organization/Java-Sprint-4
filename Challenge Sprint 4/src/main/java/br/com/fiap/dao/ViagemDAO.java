package br.com.fiap.dao;

import br.com.fiap.beans.Viagem;
import br.com.fiap.conexoes.ConnectionFactory;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 * Padrao DAO: persistencia da entidade Viagem na tabela T_SP_VIAGEM.
 */
@Repository
public class ViagemDAO {

    public long inserir(Viagem viagem) throws SQLException {
        String sql = "INSERT INTO T_SP_VIAGEM (id_viagem, id_usuario, ds_tipo_transporte, nr_distancia_km, nr_pontos, dt_viagem) "
                + "VALUES (seq_viagem.NEXTVAL, ?, ?, ?, ?, ?)";
        long idGerado = -1;
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, new String[]{"id_viagem"})) {
            stmt.setLong(1, viagem.getUsuarioId());
            stmt.setString(2, viagem.getTipoTransporte());
            stmt.setDouble(3, viagem.getDistanciaKm());
            stmt.setDouble(4, viagem.getPontos());
            stmt.setDate(5, java.sql.Date.valueOf(viagem.getDataViagem()));
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) {
                    idGerado = rs.getLong(1);
                }
            }
        }
        return idGerado;
    }

    public Viagem buscarPorId(long id) throws SQLException {
        String sql = "SELECT id_viagem, id_usuario, ds_tipo_transporte, nr_distancia_km, nr_pontos, dt_viagem "
                + "FROM T_SP_VIAGEM WHERE id_viagem = ?";
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

    public List<Viagem> listarPorUsuario(long usuarioId) throws SQLException {
        String sql = "SELECT id_viagem, id_usuario, ds_tipo_transporte, nr_distancia_km, nr_pontos, dt_viagem "
                + "FROM T_SP_VIAGEM WHERE id_usuario = ? ORDER BY dt_viagem DESC";
        List<Viagem> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapear(rs));
                }
            }
        }
        return lista;
    }

    public List<Viagem> listarTodas() throws SQLException {
        String sql = "SELECT id_viagem, id_usuario, ds_tipo_transporte, nr_distancia_km, nr_pontos, dt_viagem "
                + "FROM T_SP_VIAGEM ORDER BY dt_viagem DESC";
        List<Viagem> lista = new ArrayList<>();
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(mapear(rs));
            }
        }
        return lista;
    }

    public boolean atualizar(Viagem viagem) throws SQLException {
        String sql = "UPDATE T_SP_VIAGEM SET ds_tipo_transporte = ?, nr_distancia_km = ?, nr_pontos = ?, dt_viagem = ? "
                + "WHERE id_viagem = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, viagem.getTipoTransporte());
            stmt.setDouble(2, viagem.getDistanciaKm());
            stmt.setDouble(3, viagem.getPontos());
            stmt.setDate(4, java.sql.Date.valueOf(viagem.getDataViagem()));
            stmt.setLong(5, viagem.getId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean excluir(long id) throws SQLException {
        String sql = "DELETE FROM T_SP_VIAGEM WHERE id_viagem = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        }
    }

    public int excluirPorUsuario(long usuarioId) throws SQLException {
        String sql = "DELETE FROM T_SP_VIAGEM WHERE id_usuario = ?";
        try (Connection conn = ConnectionFactory.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, usuarioId);
            return stmt.executeUpdate();
        }
    }

    private Viagem mapear(ResultSet rs) throws SQLException {
        Viagem viagem = new Viagem();
        viagem.setId(rs.getLong("id_viagem"));
        viagem.setUsuarioId(rs.getLong("id_usuario"));
        viagem.setTipoTransporte(rs.getString("ds_tipo_transporte"));
        viagem.setDistanciaKm(rs.getDouble("nr_distancia_km"));
        viagem.setPontos(rs.getDouble("nr_pontos"));
        viagem.setDataViagem(rs.getDate("dt_viagem").toLocalDate());
        return viagem;
    }
}
