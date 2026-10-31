package com.sebas.concesionario.modelo;

public record Modelo(int id, String nombre, String marca, String imagen) {

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getMarca() {
        return marca;
    }

    public String getImagen() {
        return imagen;
    }
}
