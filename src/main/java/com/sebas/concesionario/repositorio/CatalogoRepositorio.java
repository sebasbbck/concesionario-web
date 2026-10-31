package com.sebas.concesionario.repositorio;

import com.sebas.concesionario.modelo.Color;
import com.sebas.concesionario.modelo.Marca;
import com.sebas.concesionario.modelo.Modelo;
import java.util.List;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/** Tablas auxiliares del catálogo: marcas, modelos y colores. */
@Repository
public class CatalogoRepositorio {

    private final NamedParameterJdbcTemplate jdbc;

    public CatalogoRepositorio(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Marca> marcas() {
        return jdbc.query("SELECT nombre, imagen FROM marcas ORDER BY nombre",
                (rs, i) -> new Marca(rs.getString("nombre"), rs.getString("imagen")));
    }

    public List<Modelo> modelos() {
        return jdbc.query("SELECT id, nombre, marca, imagen FROM modelos ORDER BY marca, nombre",
                (rs, i) -> new Modelo(rs.getInt("id"), rs.getString("nombre"), rs.getString("marca"),
                        rs.getString("imagen")));
    }

    public List<Color> colores() {
        return jdbc.query("SELECT id, nombre, metalizado FROM colores ORDER BY nombre",
                (rs, i) -> new Color(rs.getString("id"), rs.getString("nombre"), rs.getBoolean("metalizado")));
    }

    public boolean existeModelo(int id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM modelos WHERE id = :id",
                new MapSqlParameterSource("id", id), Integer.class);
        return n != null && n > 0;
    }

    public boolean existeColor(String id) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM colores WHERE id = :id",
                new MapSqlParameterSource("id", id), Integer.class);
        return n != null && n > 0;
    }
}
