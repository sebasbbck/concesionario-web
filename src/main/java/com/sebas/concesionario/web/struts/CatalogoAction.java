package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.modelo.Marca;
import com.sebas.concesionario.modelo.Modelo;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.CatalogoServicio;
import com.sebas.concesionario.servicio.CocheServicio;
import com.sebas.concesionario.servicio.FavoritoServicio;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/** GET /catalogo — listado de coches disponibles con filtros. */
public class CatalogoAction extends BaseAction {

    private final CocheServicio cocheServicio;
    private final CatalogoServicio catalogoServicio;
    private final FavoritoServicio favoritoServicio;

    // Parámetros del formulario de filtros (llegan como texto y se validan al convertirlos)
    private String marca;
    private String modeloId;
    private String motor;
    private String cambio;
    private String precioMax;
    private String plazasMin;

    // Datos para la JSP
    private List<Coche> coches;
    private List<Marca> marcas;
    private List<Modelo> modelos;
    private Set<String> favoritos = Set.of();
    private int totalStock;
    private BigDecimal precioMinimo;

    public CatalogoAction(UsuarioActual usuarioActual, CocheServicio cocheServicio,
                          CatalogoServicio catalogoServicio, FavoritoServicio favoritoServicio) {
        super(usuarioActual);
        this.cocheServicio = cocheServicio;
        this.catalogoServicio = catalogoServicio;
        this.favoritoServicio = favoritoServicio;
    }

    @Override
    public String execute() {
        Integer precio = aEntero(precioMax);
        FiltroCoches filtro = new FiltroCoches(
                vacioANull(marca),
                aEntero(modeloId),
                aEnum(TipoMotor.class, motor),
                aEnum(TipoCambio.class, cambio),
                precio == null ? null : BigDecimal.valueOf(precio),
                aEntero(plazasMin),
                EstadoCoche.DISPONIBLE);
        coches = cocheServicio.buscar(filtro);
        // Cifras de la portada: siempre sobre todo el stock disponible, no sobre el resultado filtrado
        List<Coche> stock = isHayFiltros() ? cocheServicio.buscar(FiltroCoches.disponibles()) : coches;
        totalStock = stock.size();
        precioMinimo = stock.stream().map(Coche::getPrecio).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO);
        marcas = catalogoServicio.marcas();
        modelos = catalogoServicio.modelos();
        usuarioActual.obtener().ifPresent(u -> favoritos = favoritoServicio.bastidores(u.getId()));
        return SUCCESS;
    }

    public TipoMotor[] getMotores() {
        return TipoMotor.values();
    }

    public TipoCambio[] getCambios() {
        return TipoCambio.values();
    }

    public boolean isHayFiltros() {
        return vacioANull(marca) != null || vacioANull(modeloId) != null || vacioANull(motor) != null
                || vacioANull(cambio) != null || vacioANull(precioMax) != null || vacioANull(plazasMin) != null;
    }

    // Con Struts 7 solo se rellenan desde la petición los setters anotados con @StrutsParameter
    @StrutsParameter
    public void setMarca(String marca) { this.marca = marca; }
    @StrutsParameter
    public void setModeloId(String modeloId) { this.modeloId = modeloId; }
    @StrutsParameter
    public void setMotor(String motor) { this.motor = motor; }
    @StrutsParameter
    public void setCambio(String cambio) { this.cambio = cambio; }
    @StrutsParameter
    public void setPrecioMax(String precioMax) { this.precioMax = precioMax; }
    @StrutsParameter
    public void setPlazasMin(String plazasMin) { this.plazasMin = plazasMin; }

    public String getMarca() { return marca; }
    public String getModeloId() { return modeloId; }
    public String getMotor() { return motor; }
    public String getCambio() { return cambio; }
    public String getPrecioMax() { return precioMax; }
    public String getPlazasMin() { return plazasMin; }
    public List<Coche> getCoches() { return coches; }
    public List<Marca> getMarcas() { return marcas; }
    public List<Modelo> getModelos() { return modelos; }
    public Set<String> getFavoritos() { return favoritos; }
    public int getTotalStock() { return totalStock; }
    public BigDecimal getPrecioMinimo() { return precioMinimo; }
}
