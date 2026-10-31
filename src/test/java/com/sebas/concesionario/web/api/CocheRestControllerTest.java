package com.sebas.concesionario.web.api;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.sebas.concesionario.config.SecurityConfig;
import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import com.sebas.concesionario.servicio.CatalogoServicio;
import com.sebas.concesionario.servicio.CocheServicio;
import com.sebas.concesionario.servicio.NoEncontradoException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/** Test de la capa web de la API: levanta solo Spring MVC + seguridad, con los servicios simulados. */
@WebMvcTest(CocheRestController.class)
@Import(SecurityConfig.class)
class CocheRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private CocheServicio cocheServicio;

    @MockitoBean
    private CatalogoServicio catalogoServicio;

    @Test
    void listaCochesEnJsonSinNecesidadDeLogin() throws Exception {
        when(cocheServicio.buscar(any())).thenReturn(List.of(coche()));

        mvc.perform(get("/api/coches").param("marca", "Seat"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].matricula").value("1234ABC"))
                .andExpect(jsonPath("$[0].motor").value("HIBRIDO"))
                .andExpect(jsonPath("$[0].foto").value("/fotos/coches/seat-leon.jpg"));
    }

    @Test
    void devuelve404ConProblemDetail() throws Exception {
        when(cocheServicio.obtener("NOEXISTE")).thenThrow(new NoEncontradoException("No existe"));

        mvc.perform(get("/api/coches/NOEXISTE"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.detail").value("No existe"));
    }

    @Test
    void parametroNoValidoDevuelve400() throws Exception {
        mvc.perform(get("/api/coches").param("motor", "VAPOR"))
                .andExpect(status().isBadRequest());
    }

    private static Coche coche() {
        Coche c = new Coche();
        c.setBastidor("VSSZZZKJZPR000001");
        c.setMatricula("1234ABC");
        c.setMarca("Seat");
        c.setModelo("León");
        c.setImagen("coches/seat-leon.jpg");
        c.setFechaFabricacion(LocalDate.of(2023, 5, 1));
        c.setMotor(TipoMotor.HIBRIDO);
        c.setColor("Blanco Normal");
        c.setCilindrada(new BigDecimal("1.4"));
        c.setPotenciaCv(204);
        c.setPuertas(5);
        c.setPlazas(5);
        c.setCambio(TipoCambio.AUTOMATICO);
        c.setPrecio(new BigDecimal("29900"));
        c.setEstado(EstadoCoche.DISPONIBLE);
        return c;
    }
}
