package com.sebas.concesionario.web.api;

import com.sebas.concesionario.modelo.Coche;
import java.math.BigDecimal;
import java.time.LocalDate;

/** Lo que devuelve la API: un DTO inmutable, para no exponer directamente el modelo interno. */
public record CocheDto(String bastidor, String matricula, String marca, String modelo, LocalDate fechaFabricacion,
                       String motor, String color, BigDecimal cilindrada, int potenciaCv, int puertas, int plazas,
                       String cambio, BigDecimal precio, String estado, String foto) {

    public static CocheDto desde(Coche c) {
        return new CocheDto(c.getBastidor(), c.getMatricula(), c.getMarca(), c.getModelo(), c.getFechaFabricacion(),
                c.getMotor().name(), c.getColor(), c.getCilindrada(), c.getPotenciaCv(), c.getPuertas(),
                c.getPlazas(), c.getCambio().name(), c.getPrecio(), c.getEstado().name(),
                c.getImagen() == null ? null : "/fotos/" + c.getImagen());
    }
}
