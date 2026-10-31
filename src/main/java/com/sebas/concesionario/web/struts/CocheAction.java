package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import java.util.List;
import java.util.Set;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.CocheServicio;
import com.sebas.concesionario.servicio.FavoritoServicio;
import com.sebas.concesionario.servicio.NoEncontradoException;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/** GET /coche?bastidor=... — ficha de un coche. */
public class CocheAction extends BaseAction {

    private final CocheServicio cocheServicio;
    private final FavoritoServicio favoritoServicio;

    private String bastidor;
    private Coche coche;
    private boolean favorito;
    private Set<String> favoritos = Set.of();
    private List<Coche> relacionados = List.of();

    public CocheAction(UsuarioActual usuarioActual, CocheServicio cocheServicio, FavoritoServicio favoritoServicio) {
        super(usuarioActual);
        this.cocheServicio = cocheServicio;
        this.favoritoServicio = favoritoServicio;
    }

    @Override
    public String execute() {
        if (vacioANull(bastidor) == null) {
            return NO_ENCONTRADO;
        }
        try {
            coche = cocheServicio.obtener(bastidor);
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        usuarioActual.obtener().ifPresent(u -> {
            favoritos = favoritoServicio.bastidores(u.getId());
            favorito = favoritos.contains(coche.getBastidor());
        });
        relacionados = cocheServicio.buscar(new FiltroCoches(coche.getMarca(), null, null, null, null, null,
                        EstadoCoche.DISPONIBLE)).stream()
                .filter(c -> !c.getBastidor().equals(coche.getBastidor()))
                .limit(3)
                .toList();
        return SUCCESS;
    }

    @StrutsParameter
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }

    public String getBastidor() { return bastidor; }
    public Coche getCoche() { return coche; }
    public boolean isFavorito() { return favorito; }
    public Set<String> getFavoritos() { return favoritos; }
    public List<Coche> getRelacionados() { return relacionados; }
}
