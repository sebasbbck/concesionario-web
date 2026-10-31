package com.sebas.concesionario.modelo;

public record Color(String id, String nombre, boolean metalizado) {

    public String getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public boolean isMetalizado() {
        return metalizado;
    }
}
