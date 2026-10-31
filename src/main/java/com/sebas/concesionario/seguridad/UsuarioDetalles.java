package com.sebas.concesionario.seguridad;

import com.sebas.concesionario.modelo.Usuario;
import java.util.Collection;
import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/** Usuario autenticado que guarda Spring Security en la sesión. Añade el id y el nombre para mostrar. */
public class UsuarioDetalles implements UserDetails {

    private final int id;
    private final String nombreUsuario;
    private final String contrasenaHash;
    private final String nombre;
    private final List<GrantedAuthority> roles;

    public UsuarioDetalles(Usuario u) {
        this.id = u.getId();
        this.nombreUsuario = u.getNombreUsuario();
        this.contrasenaHash = u.getContrasenaHash();
        this.nombre = u.getNombre();
        // Spring Security espera el prefijo ROLE_ para hasRole("ADMIN")
        this.roles = List.of(new SimpleGrantedAuthority("ROLE_" + u.getRol().name()));
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String getUsername() {
        return nombreUsuario;
    }

    @Override
    public String getPassword() {
        return contrasenaHash;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }
}
