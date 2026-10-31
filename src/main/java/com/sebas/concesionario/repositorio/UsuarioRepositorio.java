package com.sebas.concesionario.repositorio;

import com.sebas.concesionario.modelo.Rol;
import com.sebas.concesionario.modelo.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class UsuarioRepositorio {

    private static final RowMapper<Usuario> MAPPER = (rs, i) -> {
        Usuario u = new Usuario();
        u.setId(rs.getInt("id"));
        u.setNombreUsuario(rs.getString("nombre_usuario"));
        u.setContrasenaHash(rs.getString("contrasena_hash"));
        u.setNombre(rs.getString("nombre"));
        u.setApellido1(rs.getString("apellido1"));
        u.setApellido2(rs.getString("apellido2"));
        u.setCorreo(rs.getString("correo"));
        u.setRol(Rol.valueOf(rs.getString("rol")));
        u.setImagen(rs.getString("imagen"));
        return u;
    };

    private final NamedParameterJdbcTemplate jdbc;

    public UsuarioRepositorio(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Usuario> buscarPorNombreUsuario(String nombreUsuario) {
        List<Usuario> lista = jdbc.query("SELECT * FROM usuarios WHERE nombre_usuario = :nombreUsuario",
                new MapSqlParameterSource("nombreUsuario", nombreUsuario), MAPPER);
        return lista.stream().findFirst();
    }

    public boolean existeNombreUsuario(String nombreUsuario) {
        return contar("SELECT COUNT(*) FROM usuarios WHERE nombre_usuario = :valor", nombreUsuario) > 0;
    }

    public boolean existeCorreo(String correo) {
        return contar("SELECT COUNT(*) FROM usuarios WHERE correo = :valor", correo) > 0;
    }

    /** Inserta el usuario y devuelve el id generado por AUTO_INCREMENT. */
    public int insertar(Usuario u) {
        String sql = """
                INSERT INTO usuarios (nombre_usuario, contrasena_hash, nombre, apellido1, apellido2, correo, rol, imagen)
                VALUES (:nombreUsuario, :contrasenaHash, :nombre, :apellido1, :apellido2, :correo, :rol, :imagen)
                """;
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("nombreUsuario", u.getNombreUsuario())
                .addValue("contrasenaHash", u.getContrasenaHash())
                .addValue("nombre", u.getNombre())
                .addValue("apellido1", u.getApellido1())
                .addValue("apellido2", u.getApellido2())
                .addValue("correo", u.getCorreo())
                .addValue("rol", u.getRol().name())
                .addValue("imagen", u.getImagen());
        KeyHolder clave = new GeneratedKeyHolder();
        jdbc.update(sql, params, clave, new String[] {"id"});
        return clave.getKey().intValue();
    }

    private int contar(String sql, String valor) {
        Integer n = jdbc.queryForObject(sql, new MapSqlParameterSource("valor", valor), Integer.class);
        return n == null ? 0 : n;
    }
}
