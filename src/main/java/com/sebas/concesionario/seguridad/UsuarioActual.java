package com.sebas.concesionario.seguridad;

import java.util.Optional;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/** Acceso al usuario logueado desde las Actions y los controladores. */
@Component
public class UsuarioActual {

    public Optional<UsuarioDetalles> obtener() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.getPrincipal() instanceof UsuarioDetalles detalles) {
            return Optional.of(detalles);
        }
        return Optional.empty();
    }

    /** Id del usuario logueado. Solo se llama desde rutas que Spring Security ya obliga a autenticar. */
    public int id() {
        return obtener()
                .map(UsuarioDetalles::getId)
                .orElseThrow(() -> new IllegalStateException("No hay ningún usuario autenticado"));
    }

    public boolean esAdmin() {
        return obtener()
                .map(u -> u.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN")))
                .orElse(false);
    }
}
