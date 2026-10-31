package com.sebas.concesionario.modelo;

public enum TipoCambio {
    MANUAL("Manual"),
    AUTOMATICO("Automático");

    private final String etiqueta;

    TipoCambio(String etiqueta) {
        this.etiqueta = etiqueta;
    }

    public String getEtiqueta() {
        return etiqueta;
    }
}
