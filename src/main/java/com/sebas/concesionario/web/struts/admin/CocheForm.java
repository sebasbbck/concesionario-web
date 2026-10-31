package com.sebas.concesionario.web.struts.admin;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Datos del formulario de alta/edición de coches. Todo llega como texto desde el navegador:
 * validar() comprueba cada campo y aCoche() lo convierte a los tipos del modelo.
 */
public class CocheForm {

    private static final Pattern BASTIDOR = Pattern.compile("^[A-HJ-NPR-Z0-9]{11,17}$");
    private static final Pattern MATRICULA = Pattern.compile("^[A-Z0-9]{4,10}$");

    private String bastidor;
    private String matricula;
    private String modeloId;
    private String fechaFabricacion;
    private String motor;
    private String colorId;
    private String cilindrada;
    private String potenciaCv;
    private String puertas;
    private String plazas;
    private String cambio;
    private String precio;

    public static CocheForm desde(Coche c) {
        CocheForm f = new CocheForm();
        f.bastidor = c.getBastidor();
        f.matricula = c.getMatricula();
        f.modeloId = String.valueOf(c.getModeloId());
        f.fechaFabricacion = c.getFechaFabricacion().toString();
        f.motor = c.getMotor().name();
        f.colorId = c.getColorId();
        f.cilindrada = c.getCilindrada().toPlainString();
        f.potenciaCv = String.valueOf(c.getPotenciaCv());
        f.puertas = String.valueOf(c.getPuertas());
        f.plazas = String.valueOf(c.getPlazas());
        f.cambio = c.getCambio().name();
        f.precio = c.getPrecio().stripTrailingZeros().toPlainString();
        return f;
    }

    /** Devuelve los errores por campo (vacío si todo es correcto). */
    public Map<String, String> validar() {
        Map<String, String> errores = new LinkedHashMap<>();
        if (bastidor == null || !BASTIDOR.matcher(limpio(bastidor)).matches()) {
            errores.put("bastidor", "Entre 11 y 17 letras o números (sin I, O ni Q)");
        }
        if (matricula == null || !MATRICULA.matcher(limpio(matricula).replace("-", "")).matches()) {
            errores.put("matricula", "Matrícula no válida");
        }
        if (entero(modeloId) == null) {
            errores.put("modeloId", "Elige un modelo");
        }
        LocalDate fecha = fecha(fechaFabricacion);
        if (fecha == null || fecha.isAfter(LocalDate.now()) || fecha.getYear() < 1950) {
            errores.put("fechaFabricacion", "Fecha no válida");
        }
        if (enumValor(TipoMotor.class, motor) == null) {
            errores.put("motor", "Elige un tipo de motor");
        }
        if (colorId == null || colorId.isBlank()) {
            errores.put("colorId", "Elige un color");
        }
        BigDecimal cil = decimal(cilindrada);
        if (cil == null || cil.signum() < 0 || cil.compareTo(BigDecimal.valueOf(10)) > 0) {
            errores.put("cilindrada", "Litros entre 0 y 10 (0 para eléctricos)");
        }
        rango(errores, "potenciaCv", potenciaCv, 1, 2000, "Potencia entre 1 y 2000 CV");
        rango(errores, "puertas", puertas, 2, 5, "Entre 2 y 5 puertas");
        rango(errores, "plazas", plazas, 1, 9, "Entre 1 y 9 plazas");
        if (enumValor(TipoCambio.class, cambio) == null) {
            errores.put("cambio", "Elige el tipo de cambio");
        }
        BigDecimal pr = decimal(precio);
        if (pr == null || pr.signum() <= 0) {
            errores.put("precio", "Precio no válido");
        }
        return errores;
    }

    /** Convierte el formulario en un Coche. Llamar solo si validar() no ha devuelto errores. */
    public Coche aCoche() {
        Coche c = new Coche();
        c.setBastidor(limpio(bastidor));
        c.setMatricula(limpio(matricula));
        c.setModeloId(entero(modeloId));
        c.setFechaFabricacion(fecha(fechaFabricacion));
        c.setMotor(enumValor(TipoMotor.class, motor));
        c.setColorId(colorId.trim());
        c.setCilindrada(decimal(cilindrada));
        c.setPotenciaCv(entero(potenciaCv));
        c.setPuertas(entero(puertas));
        c.setPlazas(entero(plazas));
        c.setCambio(enumValor(TipoCambio.class, cambio));
        c.setPrecio(decimal(precio));
        return c;
    }

    private static void rango(Map<String, String> errores, String campo, String valor, int min, int max, String msg) {
        Integer n = entero(valor);
        if (n == null || n < min || n > max) {
            errores.put(campo, msg);
        }
    }

    private static String limpio(String s) {
        return s.trim().toUpperCase().replace(" ", "");
    }

    private static Integer entero(String s) {
        try {
            return s == null ? null : Integer.valueOf(s.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static BigDecimal decimal(String s) {
        try {
            return s == null || s.isBlank() ? null : new BigDecimal(s.trim().replace(",", "."));
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private static LocalDate fecha(String s) {
        try {
            return s == null || s.isBlank() ? null : LocalDate.parse(s.trim());
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    private static <E extends Enum<E>> E enumValor(Class<E> tipo, String s) {
        try {
            return s == null || s.isBlank() ? null : Enum.valueOf(tipo, s.trim());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public String getBastidor() { return bastidor; }
    public void setBastidor(String bastidor) { this.bastidor = bastidor; }
    public String getMatricula() { return matricula; }
    public void setMatricula(String matricula) { this.matricula = matricula; }
    public String getModeloId() { return modeloId; }
    public void setModeloId(String modeloId) { this.modeloId = modeloId; }
    public String getFechaFabricacion() { return fechaFabricacion; }
    public void setFechaFabricacion(String fechaFabricacion) { this.fechaFabricacion = fechaFabricacion; }
    public String getMotor() { return motor; }
    public void setMotor(String motor) { this.motor = motor; }
    public String getColorId() { return colorId; }
    public void setColorId(String colorId) { this.colorId = colorId; }
    public String getCilindrada() { return cilindrada; }
    public void setCilindrada(String cilindrada) { this.cilindrada = cilindrada; }
    public String getPotenciaCv() { return potenciaCv; }
    public void setPotenciaCv(String potenciaCv) { this.potenciaCv = potenciaCv; }
    public String getPuertas() { return puertas; }
    public void setPuertas(String puertas) { this.puertas = puertas; }
    public String getPlazas() { return plazas; }
    public void setPlazas(String plazas) { this.plazas = plazas; }
    public String getCambio() { return cambio; }
    public void setCambio(String cambio) { this.cambio = cambio; }
    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }
}
