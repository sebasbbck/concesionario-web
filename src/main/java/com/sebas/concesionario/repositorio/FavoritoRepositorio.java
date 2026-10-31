package com.sebas.concesionario.repositorio;

import com.sebas.concesionario.modelo.Coche;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class FavoritoRepositorio {

    private final NamedParameterJdbcTemplate jdbc;

    public FavoritoRepositorio(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Coche> listar(int usuarioId) {
        String sql = CocheRepositorio.SELECT_COCHE + """
                 JOIN favoritos f ON f.bastidor = c.bastidor
                WHERE f.usuario_id = :usuarioId
                ORDER BY f.fecha DESC
                """;
        return jdbc.query(sql, new MapSqlParameterSource("usuarioId", usuarioId), CocheRepositorio.MAPPER);
    }

    public Set<String> bastidores(int usuarioId) {
        return new HashSet<>(jdbc.queryForList("SELECT bastidor FROM favoritos WHERE usuario_id = :usuarioId",
                new MapSqlParameterSource("usuarioId", usuarioId), String.class));
    }

    public boolean existe(int usuarioId, String bastidor) {
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM favoritos WHERE usuario_id = :usuarioId AND bastidor = :bastidor",
                params(usuarioId, bastidor), Integer.class);
        return n != null && n > 0;
    }

    public void anadir(int usuarioId, String bastidor) {
        jdbc.update("INSERT INTO favoritos (usuario_id, bastidor) VALUES (:usuarioId, :bastidor)",
                params(usuarioId, bastidor));
    }

    public int quitar(int usuarioId, String bastidor) {
        return jdbc.update("DELETE FROM favoritos WHERE usuario_id = :usuarioId AND bastidor = :bastidor",
                params(usuarioId, bastidor));
    }

    private static MapSqlParameterSource params(int usuarioId, String bastidor) {
        return new MapSqlParameterSource("usuarioId", usuarioId).addValue("bastidor", bastidor);
    }
}
