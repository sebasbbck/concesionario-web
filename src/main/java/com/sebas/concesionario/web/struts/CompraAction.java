package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.Compra;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.CocheServicio;
import com.sebas.concesionario.servicio.CompraServicio;
import com.sebas.concesionario.servicio.NegocioException;
import com.sebas.concesionario.servicio.NoEncontradoException;
import java.math.BigDecimal;
import java.util.List;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/**
 * Compra de coches:
 * GET /comprar (confirmar) pide confirmación, POST /comprar-confirmar (comprar) la realiza
 * y GET /mis-compras (misCompras) lista las compras del cliente.
 */
public class CompraAction extends BaseAction {

    private final CocheServicio cocheServicio;
    private final CompraServicio compraServicio;

    private String bastidor;
    private Coche coche;
    private List<Compra> compras;

    public CompraAction(UsuarioActual usuarioActual, CocheServicio cocheServicio, CompraServicio compraServicio) {
        super(usuarioActual);
        this.cocheServicio = cocheServicio;
        this.compraServicio = compraServicio;
    }

    public String confirmar() {
        try {
            coche = cocheServicio.obtener(bastidor);
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        if (!coche.isDisponible()) {
            addActionError("Este coche ya está vendido");
            return "coche";
        }
        return INPUT;
    }

    public String comprar() {
        try {
            coche = compraServicio.comprar(bastidor, usuarioActual.id());
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        } catch (NegocioException e) {
            addActionError(e.getMessage());
            return "coche";
        }
        addActionMessage("¡Enhorabuena! Has comprado el " + coche.getNombreCompleto());
        return SUCCESS;
    }

    public String misCompras() {
        compras = compraServicio.deUsuario(usuarioActual.id());
        return SUCCESS;
    }

    @StrutsParameter
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }

    public String getBastidor() { return bastidor; }
    public Coche getCoche() { return coche; }
    public List<Compra> getCompras() { return compras; }

    public BigDecimal getTotalGastado() {
        return compras == null ? BigDecimal.ZERO
                : compras.stream().map(Compra::getPrecio).reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
