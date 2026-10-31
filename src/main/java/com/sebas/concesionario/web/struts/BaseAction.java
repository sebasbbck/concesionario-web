package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.seguridad.UsuarioActual;
import org.apache.struts2.ActionSupport;

/**
 * Base de las Actions. Struts 2 crea una instancia nueva por petición (a diferencia de los
 * Servlets o los controladores de Spring, que son singleton), así que pueden tener estado:
 * los parámetros de la petición y los datos para la JSP se guardan en atributos.
 */
public abstract class BaseAction extends ActionSupport {

    public static final String NO_ENCONTRADO = "noEncontrado";

    protected final UsuarioActual usuarioActual;

    protected BaseAction(UsuarioActual usuarioActual) {
        this.usuarioActual = usuarioActual;
    }

    protected static Integer aEntero(String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Integer.valueOf(valor.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    protected static <E extends Enum<E>> E aEnum(Class<E> tipo, String valor) {
        if (valor == null || valor.isBlank()) {
            return null;
        }
        try {
            return Enum.valueOf(tipo, valor.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    protected static String vacioANull(String valor) {
        return valor == null || valor.isBlank() ? null : valor.trim();
    }
}
