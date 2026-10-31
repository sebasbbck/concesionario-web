package com.sebas.concesionario.web.api;

import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.modelo.Marca;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import com.sebas.concesionario.servicio.CatalogoServicio;
import com.sebas.concesionario.servicio.CocheServicio;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * API REST de solo lectura del catálogo (Spring MVC). Reutiliza los mismos servicios que las
 * pantallas de Struts: la lógica está en una sola capa.
 *
 * GET /api/coches?marca=Seat&motor=HIBRIDO&precioMax=30000
 * GET /api/coches/{bastidor}
 * GET /api/marcas
 */
@RestController
@RequestMapping("/api")
public class CocheRestController {

    private final CocheServicio cocheServicio;
    private final CatalogoServicio catalogoServicio;

    public CocheRestController(CocheServicio cocheServicio, CatalogoServicio catalogoServicio) {
        this.cocheServicio = cocheServicio;
        this.catalogoServicio = catalogoServicio;
    }

    @GetMapping("/coches")
    public List<CocheDto> buscar(@RequestParam(required = false) String marca,
                                 @RequestParam(required = false) TipoMotor motor,
                                 @RequestParam(required = false) TipoCambio cambio,
                                 @RequestParam(required = false) BigDecimal precioMax,
                                 @RequestParam(required = false) Integer plazasMin,
                                 @RequestParam(defaultValue = "DISPONIBLE") EstadoCoche estado) {
        FiltroCoches filtro = new FiltroCoches(marca, null, motor, cambio, precioMax, plazasMin, estado);
        return cocheServicio.buscar(filtro).stream().map(CocheDto::desde).toList();
    }

    @GetMapping("/coches/{bastidor}")
    public CocheDto obtener(@PathVariable String bastidor) {
        return CocheDto.desde(cocheServicio.obtener(bastidor));
    }

    @GetMapping("/marcas")
    public List<Marca> marcas() {
        return catalogoServicio.marcas();
    }
}
