package com.sebas.concesionario.servicio;

import com.sebas.concesionario.modelo.Rol;
import com.sebas.concesionario.modelo.Usuario;
import com.sebas.concesionario.repositorio.UsuarioRepositorio;
import java.util.Locale;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UsuarioServicio {

    static final String IMAGEN_POR_DEFECTO = "perfiles/usuario.jpg";

    private final UsuarioRepositorio usuarios;
    private final PasswordEncoder passwordEncoder;

    public UsuarioServicio(UsuarioRepositorio usuarios, PasswordEncoder passwordEncoder) {
        this.usuarios = usuarios;
        this.passwordEncoder = passwordEncoder;
    }

    /** Registra un cliente nuevo. La contraseña se guarda con hash BCrypt, nunca en texto plano. */
    @Transactional
    public int registrar(Usuario datos, String contrasena) {
        String nombreUsuario = datos.getNombreUsuario().trim();
        String correo = datos.getCorreo().trim().toLowerCase(Locale.ROOT);
        if (usuarios.existeNombreUsuario(nombreUsuario)) {
            throw new NegocioException("El nombre de usuario ya existe");
        }
        if (usuarios.existeCorreo(correo)) {
            throw new NegocioException("Ya hay una cuenta con ese correo");
        }
        datos.setNombreUsuario(nombreUsuario);
        datos.setCorreo(correo);
        datos.setRol(Rol.CLIENTE);
        datos.setImagen(IMAGEN_POR_DEFECTO);
        datos.setContrasenaHash(passwordEncoder.encode(contrasena));
        return usuarios.insertar(datos);
    }
}
