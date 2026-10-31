package com.sebas.concesionario.modelo;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Coche tal como se muestra en las pantallas: incluye los datos de las tablas relacionadas
 * (marca, nombre del modelo, foto y color) que trae la consulta con JOIN.
 */
public class Coche {

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String bastidor;
    private String matricula;
    private int modeloId;
    private String modelo;
    private String marca;
    private String imagen;
    private LocalDate fechaFabricacion;
    private TipoMotor motor;
    private String colorId;
    private String color;
    private BigDecimal cilindrada;
    private int potenciaCv;
    private int puertas;
    private int plazas;
    private TipoCambio cambio;
    private BigDecimal precio;
    private EstadoCoche estado;

    public boolean isDisponible() {
        return estado == EstadoCoche.DISPONIBLE;
    }

    public String getNombreCompleto() {
        return marca + " " + modelo;
    }

    /** JSTL (fmt:formatDate) no sabe formatear LocalDate, así que se da ya formateada. */
    public String getFechaFabricacionTexto() {
        return fechaFabricacion == null ? "" : fechaFabricacion.format(FORMATO_FECHA);
    }

    public int getAnio() {
        return fechaFabricacion == null ? 0 : fechaFabricacion.getYear();
    }

    public String getBastidor() { return bastidor; }
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }

    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }

    public int getModeloId() { return modeloId; }
    public void setModeloId(int modeloId) { this.modeloId = modeloId; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }

    public LocalDate getFechaFabricacion() { return fechaFabricacion; }
    public void setFechaFabricacion(LocalDate fechaFabricacion) { this.fechaFabricacion = fechaFabricacion; }

    public TipoMotor getMotor() { return motor; }
    public void setMotor(TipoMotor motor) { this.motor = motor; }

    public String getColorId() { return colorId; }
    public void setColorId(String colorId) { this.colorId = colorId; }

    public String getColor() { return color; }
    public void setColor(String color) { this.color = color; }

    public BigDecimal getCilindrada() { return cilindrada; }
    public void setCilindrada(BigDecimal cilindrada) { this.cilindrada = cilindrada; }

    public int getPotenciaCv() { return potenciaCv; }
    public void setPotenciaCv(int potenciaCv) { this.potenciaCv = potenciaCv; }

    public int getPuertas() { return puertas; }
    public void setPuertas(int puertas) { this.puertas = puertas; }

    public int getPlazas() { return plazas; }
    public void setPlazas(int plazas) { this.plazas = plazas; }

    public TipoCambio getCambio() { return cambio; }
    public void setCambio(TipoCambio cambio) { this.cambio = cambio; }

    public BigDecimal getPrecio() { return precio; }
    public void setPrecio(BigDecimal precio) { this.precio = precio; }

    public EstadoCoche getEstado() { return estado; }
    public void setEstado(EstadoCoche estado) { this.estado = estado; }
}
