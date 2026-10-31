package com.sebas.concesionario.repositorio;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

/**
 * Acceso a la tabla COCHES con JDBC (NamedParameterJdbcTemplate).
 *
 * Todos los valores van como parámetros (:nombre) y JdbcTemplate los pasa a un
 * PreparedStatement: no hay concatenación de texto, así que no hay inyección SQL.
 * JdbcTemplate también abre y cierra la conexión, el statement y el ResultSet.
 */
@Repository
public class CocheRepositorio {

    static final String SELECT_COCHE = """
            SELECT c.bastidor, c.matricula, c.modelo_id, m.nombre AS modelo, m.marca, m.imagen,
                   c.fecha_fabricacion, c.motor, c.color_id, co.nombre AS color, c.cilindrada,
                   c.potencia_cv, c.puertas, c.plazas, c.cambio, c.precio, c.estado
            FROM coches c
            JOIN modelos m ON m.id = c.modelo_id
            JOIN colores co ON co.id = c.color_id
            """;

    static final RowMapper<Coche> MAPPER = CocheRepositorio::mapear;

    private final NamedParameterJdbcTemplate jdbc;

    public CocheRepositorio(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public List<Coche> buscar(FiltroCoches filtro) {
        // Solo se concatenan fragmentos fijos de SQL; los valores van siempre como parámetros
        StringBuilder sql = new StringBuilder(SELECT_COCHE).append(" WHERE 1 = 1");
        MapSqlParameterSource params = new MapSqlParameterSource();
        if (filtro.marca() != null) {
            sql.append(" AND m.marca = :marca");
            params.addValue("marca", filtro.marca());
        }
        if (filtro.modeloId() != null) {
            sql.append(" AND c.modelo_id = :modeloId");
            params.addValue("modeloId", filtro.modeloId());
        }
        if (filtro.motor() != null) {
            sql.append(" AND c.motor = :motor");
            params.addValue("motor", filtro.motor().name());
        }
        if (filtro.cambio() != null) {
            sql.append(" AND c.cambio = :cambio");
            params.addValue("cambio", filtro.cambio().name());
        }
        if (filtro.precioMax() != null) {
            sql.append(" AND c.precio <= :precioMax");
            params.addValue("precioMax", filtro.precioMax());
        }
        if (filtro.plazasMin() != null) {
            sql.append(" AND c.plazas >= :plazasMin");
            params.addValue("plazasMin", filtro.plazasMin());
        }
        if (filtro.estado() != null) {
            sql.append(" AND c.estado = :estado");
            params.addValue("estado", filtro.estado().name());
        }
        sql.append(" ORDER BY m.marca, m.nombre, c.precio");
        return jdbc.query(sql.toString(), params, MAPPER);
    }

    public Optional<Coche> buscarPorBastidor(String bastidor) {
        List<Coche> lista = jdbc.query(SELECT_COCHE + " WHERE c.bastidor = :bastidor",
                new MapSqlParameterSource("bastidor", bastidor), MAPPER);
        return lista.stream().findFirst();
    }

    public boolean existeBastidor(String bastidor) {
        Integer n = jdbc.queryForObject("SELECT COUNT(*) FROM coches WHERE bastidor = :bastidor",
                new MapSqlParameterSource("bastidor", bastidor), Integer.class);
        return n != null && n > 0;
    }

    /** Matrícula usada por un coche distinto de {@code excluirBastidor} (null = ninguno). */
    public boolean existeMatricula(String matricula, String excluirBastidor) {
        MapSqlParameterSource params = new MapSqlParameterSource("matricula", matricula)
                .addValue("excluir", excluirBastidor == null ? "" : excluirBastidor);
        Integer n = jdbc.queryForObject(
                "SELECT COUNT(*) FROM coches WHERE matricula = :matricula AND bastidor <> :excluir",
                params, Integer.class);
        return n != null && n > 0;
    }

    public void insertar(Coche c, int creadoPor) {
        String sql = """
                INSERT INTO coches (bastidor, matricula, modelo_id, fecha_fabricacion, motor, color_id, cilindrada,
                                    potencia_cv, puertas, plazas, cambio, precio, estado, creado_por)
                VALUES (:bastidor, :matricula, :modeloId, :fechaFabricacion, :motor, :colorId, :cilindrada,
                        :potenciaCv, :puertas, :plazas, :cambio, :precio, 'DISPONIBLE', :creadoPor)
                """;
        jdbc.update(sql, parametros(c).addValue("creadoPor", creadoPor));
    }

    /** Actualiza el coche identificado por {@code bastidorOriginal} (el bastidor también se puede cambiar). */
    public int actualizar(String bastidorOriginal, Coche c) {
        String sql = """
                UPDATE coches SET bastidor = :bastidor, matricula = :matricula, modelo_id = :modeloId,
                       fecha_fabricacion = :fechaFabricacion, motor = :motor, color_id = :colorId,
                       cilindrada = :cilindrada, potencia_cv = :potenciaCv, puertas = :puertas,
                       plazas = :plazas, cambio = :cambio, precio = :precio
                WHERE bastidor = :bastidorOriginal
                """;
        return jdbc.update(sql, parametros(c).addValue("bastidorOriginal", bastidorOriginal));
    }

    public int borrar(String bastidor) {
        return jdbc.update("DELETE FROM coches WHERE bastidor = :bastidor",
                new MapSqlParameterSource("bastidor", bastidor));
    }

    /**
     * Pasa el coche a COMPRADO solo si sigue DISPONIBLE y devuelve las filas afectadas.
     * Si dos clientes compran a la vez, solo uno obtiene 1; el otro recibe 0.
     */
    public int marcarComprado(String bastidor) {
        return jdbc.update(
                "UPDATE coches SET estado = 'COMPRADO' WHERE bastidor = :bastidor AND estado = 'DISPONIBLE'",
                new MapSqlParameterSource("bastidor", bastidor));
    }

    private static MapSqlParameterSource parametros(Coche c) {
        return new MapSqlParameterSource()
                .addValue("bastidor", c.getBastidor())
                .addValue("matricula", c.getMatricula())
                .addValue("modeloId", c.getModeloId())
                .addValue("fechaFabricacion", c.getFechaFabricacion())
                .addValue("motor", c.getMotor().name())
                .addValue("colorId", c.getColorId())
                .addValue("cilindrada", c.getCilindrada())
                .addValue("potenciaCv", c.getPotenciaCv())
                .addValue("puertas", c.getPuertas())
                .addValue("plazas", c.getPlazas())
                .addValue("cambio", c.getCambio().name())
                .addValue("precio", c.getPrecio());
    }

    private static Coche mapear(ResultSet rs, int fila) throws SQLException {
        Coche c = new Coche();
        c.setBastidor(rs.getString("bastidor"));
        c.setMatricula(rs.getString("matricula"));
        c.setModeloId(rs.getInt("modelo_id"));
        c.setModelo(rs.getString("modelo"));
        c.setMarca(rs.getString("marca"));
        c.setImagen(rs.getString("imagen"));
        c.setFechaFabricacion(rs.getObject("fecha_fabricacion", LocalDate.class));
        c.setMotor(TipoMotor.valueOf(rs.getString("motor")));
        c.setColorId(rs.getString("color_id"));
        c.setColor(rs.getString("color"));
        c.setCilindrada(rs.getBigDecimal("cilindrada"));
        c.setPotenciaCv(rs.getInt("potencia_cv"));
        c.setPuertas(rs.getInt("puertas"));
        c.setPlazas(rs.getInt("plazas"));
        c.setCambio(TipoCambio.valueOf(rs.getString("cambio")));
        c.setPrecio(rs.getBigDecimal("precio"));
        c.setEstado(EstadoCoche.valueOf(rs.getString("estado")));
        return c;
    }
}
