package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.FavoritoServicio;
import com.sebas.concesionario.servicio.NoEncontradoException;
import java.util.List;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/**
 * Favoritos del cliente. Una Action con varios métodos, cada uno mapeado a una URL en struts.xml:
 * GET /favoritos (listar), POST /favorito-anadir (anadir), POST /favorito-quitar (quitar).
 */
public class FavoritoAction extends BaseAction {

    private final FavoritoServicio favoritoServicio;

    private String bastidor;
    private String volver;
    private List<Coche> coches;

    public FavoritoAction(UsuarioActual usuarioActual, FavoritoServicio favoritoServicio) {
        super(usuarioActual);
        this.favoritoServicio = favoritoServicio;
    }

    public String listar() {
        coches = favoritoServicio.listar(usuarioActual.id());
        return SUCCESS;
    }

    public String anadir() {
        try {
            favoritoServicio.anadir(usuarioActual.id(), bastidor);
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        addActionMessage("Añadido a favoritos");
        return destino();
    }

    public String quitar() {
        favoritoServicio.quitar(usuarioActual.id(), bastidor);
        addActionMessage("Quitado de favoritos");
        return destino();
    }

    /** Vuelve a la página desde la que se pulsó el botón (solo valores conocidos: nada de redirecciones abiertas). */
    private String destino() {
        return switch (volver == null ? "" : volver) {
            case "catalogo" -> "catalogo";
            case "favoritos" -> "favoritos";
            default -> "coche";
        };
    }

    @StrutsParameter
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }
    @StrutsParameter
    public void setVolver(String volver) { this.volver = volver; }

    public String getBastidor() { return bastidor; }
    public List<Coche> getCoches() { return coches; }
}
