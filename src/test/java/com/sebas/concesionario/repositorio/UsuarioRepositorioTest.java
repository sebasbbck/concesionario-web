package com.sebas.concesionario.repositorio;

import static org.assertj.core.api.Assertions.assertThat;

import com.sebas.concesionario.modelo.Rol;
import com.sebas.concesionario.modelo.Usuario;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(UsuarioRepositorio.class)
class UsuarioRepositorioTest {

    @Autowired
    private UsuarioRepositorio repositorio;

    @Test
    void insertaYDevuelveElIdGenerado() {
        Usuario u = new Usuario();
        u.setNombreUsuario("nuevo");
        u.setContrasenaHash("$2a$10$hash");
        u.setNombre("Nuevo");
        u.setApellido1("Usuario");
        u.setCorreo("nuevo@test.com");
        u.setRol(Rol.CLIENTE);

        int id = repositorio.insertar(u);

        assertThat(id).isGreaterThan(2);   // 1 y 2 son los usuarios demo de V2
        assertThat(repositorio.buscarPorNombreUsuario("nuevo")).get()
                .satisfies(g -> {
                    assertThat(g.getId()).isEqualTo(id);
                    assertThat(g.getRol()).isEqualTo(Rol.CLIENTE);
                });
        assertThat(repositorio.existeCorreo("nuevo@test.com")).isTrue();
    }
}
