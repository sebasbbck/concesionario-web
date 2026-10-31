package com.sebas.concesionario.modelo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record Compra(int id, String bastidor, String coche, String matricula, String imagen,
                     BigDecimal precio, LocalDateTime fecha) {

    public int getId() { return id; }
    public String getBastidor() { return bastidor; }
    public String getCoche() { return coche; }
    public String getMatricula() { return matricula; }
    public String getImagen() { return imagen; }
    public BigDecimal getPrecio() { return precio; }
    public LocalDateTime getFecha() { return fecha; }

    public String getFechaTexto() {
        return fecha == null ? "" : fecha.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }
}
