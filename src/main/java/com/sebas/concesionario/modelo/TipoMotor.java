package com.sebas.concesionario.modelo;

public enum TipoMotor {
    GASOLINA("Gasolina"),
    DIESEL("Diésel"),
    HIBRIDO("Híbrido"),
    ELECTRICO("Eléctrico");

    private final String etiqueta;

    TipoMotor(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
