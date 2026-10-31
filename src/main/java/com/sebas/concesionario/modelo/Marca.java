package com.sebas.concesionario.modelo;

public record Marca(String nombre, String imagen) {

    // Getters estilo JavaBean para OGNL (Struts) y EL (JSP), que no entienden los accesores de los records
    public String getNombre() {
        return nombre;
    }

    public String getImagen() {
        return imagen;
    }
}
