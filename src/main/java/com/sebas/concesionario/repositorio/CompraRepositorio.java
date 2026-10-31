package com.sebas.concesionario.repositorio;

import com.sebas.concesionario.modelo.Compra;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompraRepositorio {

    private final NamedParameterJdbcTemplate jdbc;

    public CompraRepositorio(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertar(String bastidor, int usuarioId, BigDecimal precio) {
        jdbc.update("INSERT INTO compras (bastidor, usuario_id, precio) VALUES (:bastidor, :usuarioId, :precio)",
                new MapSqlParameterSource("bastidor", bastidor)
                        .addValue("usuarioId", usuarioId)
                        .addValue("precio", precio));
    }

    public boolean existe(String bastidor) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM compras WHERE bastidor = :bastidor",
                new MapSqlParameterSource("bastidor", bastidor), Integer.class);
        return n != null && n > 0;
    }

    public List<Compra> listarPorUsuario(int usuarioId) {
        String sql = """
                SELECT co.id, co.bastidor, CONCAT(m.marca, ' ', m.nombre) AS coche, c.matricula, m.imagen,
                       co.precio, co.fecha
                FROM compras co
                JOIN coches c ON c.bastidor = co.bastidor
                JOIN modelos m ON m.id = c.modelo_id
                WHERE co.usuario_id = :usuarioId
                ORDER BY co.fecha DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("usuarioId", usuarioId),
                (rs, i) -> new Compra(rs.getInt("id"), rs.getString("bastidor"), rs.getString("coche"),
                        rs.getString("matricula"), rs.getString("imagen"), rs.getBigDecimal("precio"),
                        rs.getObject("fecha", LocalDateTime.class)));
    }
}
