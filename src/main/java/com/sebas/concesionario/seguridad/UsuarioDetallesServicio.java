package com.sebas.concesionario.seguridad;

import com.sebas.concesionario.repositorio.UsuarioRepositorio;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * Spring Security llama a este servicio en el login para cargar el usuario.
 * La comprobación de la contraseña (BCrypt) la hace Spring con el PasswordEncoder.
 */
@Service
public class UsuarioDetallesServicio implements UserDetailsService {

    private final UsuarioRepositorio usuarios;

    public UsuarioDetallesServicio(UsuarioRepositorio usuarios) {
        this.usuarios = usuarios;
    }

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) {
        return usuarios.buscarPorNombreUsuario(nombreUsuario)
                .map(UsuarioDetalles::new)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
    }
}
