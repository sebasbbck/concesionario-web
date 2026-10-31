package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.seguridad.UsuarioActual;

/**
 * GET /login — solo pinta el formulario. El POST /login lo procesa Spring Security
 * antes de llegar a Struts (UsernamePasswordAuthenticationFilter).
 */
public class LoginAction extends BaseAction {

    public LoginAction(UsuarioActual usuarioActual) {
        super(usuarioActual);
    }

    @Override
    public String execute() {
        return usuarioActual.obtener().isPresent() ? "yaLogueado" : SUCCESS;
    }
}
