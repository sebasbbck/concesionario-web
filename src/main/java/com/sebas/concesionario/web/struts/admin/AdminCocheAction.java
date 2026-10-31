package com.sebas.concesionario.web.struts.admin;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.Color;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.modelo.Modelo;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.CatalogoServicio;
import com.sebas.concesionario.servicio.CocheServicio;
import com.sebas.concesionario.servicio.NegocioException;
import com.sebas.concesionario.servicio.NoEncontradoException;
import com.sebas.concesionario.web.struts.BaseAction;
import java.math.BigDecimal;
import java.util.List;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/**
 * CRUD de coches para el administrador (namespace /admin, protegido con hasRole("ADMIN")).
 *
 * GET  /admin/coches        listar
 * GET  /admin/coche-nuevo   nuevo     (formulario vacío)
 * GET  /admin/coche-editar  editar    (formulario con los datos del coche)
 * POST /admin/coche-guardar guardar   (alta o modificación; validateGuardar() se ejecuta antes)
 * POST /admin/coche-borrar  borrar
 */
public class AdminCocheAction extends BaseAction {

    private final CocheServicio cocheServicio;
    private final CatalogoServicio catalogoServicio;

    private String estado;
    private String bastidor;            // bastidor original al editar (vacío en un alta)
    private CocheForm coche = new CocheForm();
    private List<Coche> coches;
    private long totalDisponibles;
    private long totalVendidos;
    private BigDecimal valorStock = BigDecimal.ZERO;

    public AdminCocheAction(UsuarioActual usuarioActual, CocheServicio cocheServicio,
                            CatalogoServicio catalogoServicio) {
        super(usuarioActual);
        this.cocheServicio = cocheServicio;
        this.catalogoServicio = catalogoServicio;
    }

    public String listar() {
        EstadoCoche filtroEstado = aEnum(EstadoCoche.class, estado);
        List<Coche> todos = cocheServicio.buscar(new FiltroCoches(null, null, null, null, null, null, null));
        totalDisponibles = todos.stream().filter(Coche::isDisponible).count();
        totalVendidos = todos.size() - totalDisponibles;
        valorStock = todos.stream().filter(Coche::isDisponible).map(Coche::getPrecio)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        coches = filtroEstado == null ? todos
                : todos.stream().filter(c -> c.getEstado() == filtroEstado).toList();
        return SUCCESS;
    }

    public String nuevo() {
        bastidor = null;
        coche = new CocheForm();
        return INPUT;
    }

    public String editar() {
        try {
            coche = CocheForm.desde(cocheServicio.obtener(bastidor));
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        return INPUT;
    }

    /** Validación solo para guardar(): Struts llama a validate + nombre del método. */
    public void validateGuardar() {
        coche.validar().forEach((campo, mensaje) -> addFieldError("coche." + campo, mensaje));
    }

    public String guardar() {
        Coche datos = coche.aCoche();
        try {
            if (isEdicion()) {
                cocheServicio.actualizar(bastidor, datos);
                addActionMessage("Coche " + datos.getMatricula() + " actualizado");
            } else {
                cocheServicio.crear(datos, usuarioActual.id());
                addActionMessage("Coche " + datos.getMatricula() + " dado de alta");
            }
        } catch (NegocioException e) {
            addActionError(e.getMessage());
            return INPUT;
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        return SUCCESS;
    }

    public String borrar() {
        try {
            cocheServicio.borrar(bastidor);
            addActionMessage("Coche borrado");
        } catch (NegocioException e) {
            addActionError(e.getMessage());
        } catch (NoEncontradoException e) {
            return NO_ENCONTRADO;
        }
        return SUCCESS;
    }

    public boolean isEdicion() {
        return vacioANull(bastidor) != null;
    }

    public List<Modelo> getModelos() {
        return catalogoServicio.modelos();
    }

    public List<Color> getColores() {
        return catalogoServicio.colores();
    }

    public TipoMotor[] getMotores() {
        return TipoMotor.values();
    }

    public TipoCambio[] getCambios() {
        return TipoCambio.values();
    }

    @StrutsParameter
    public void setEstado(String estado) { this.estado = estado; }
    @StrutsParameter
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }

    /** depth = 1: permite rellenar coche.matricula, coche.precio... pero no rutas más profundas. */
    @StrutsParameter(depth = 1)
    public CocheForm getCoche() { return coche; }

    public String getEstado() { return estado; }
    public String getBastidor() { return bastidor; }
    public List<Coche> getCoches() { return coches; }
    public long getTotalDisponibles() { return totalDisponibles; }
    public long getTotalVendidos() { return totalVendidos; }
    public BigDecimal getValorStock() { return valorStock; }
}
