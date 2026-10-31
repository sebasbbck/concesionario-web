package com.sebas.concesionario.servicio;

/**
 * Error de negocio con un mensaje que se puede enseñar al usuario
 * (matrícula repetida, coche ya vendido...). Es unchecked: no obliga a try/catch en cada capa.
 */
public class NegocioException extends RuntimeException {

    public NegocioException(String mensaje) {
        super(mensaje);
    }
}
