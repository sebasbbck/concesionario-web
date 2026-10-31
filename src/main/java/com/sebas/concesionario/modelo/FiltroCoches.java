package com.sebas.concesionario.modelo;

import java.math.BigDecimal;

/**
 * Criterios de búsqueda del catálogo. Todos opcionales: los que vienen a null no filtran.
 */
public record FiltroCoches(String marca, Integer modeloId, TipoMotor motor, TipoCambio cambio,
                           BigDecimal precioMax, Integer plazasMin, EstadoCoche estado) {

    public static FiltroCoches disponibles() {
        return new FiltroCoches(null, null, null, null, null, null, EstadoCoche.DISPONIBLE);
    }
}
