package com.sebas.concesionario.web.struts;

import com.sebas.concesionario.modelo.Usuario;
import com.sebas.concesionario.seguridad.UsuarioActual;
import com.sebas.concesionario.servicio.NegocioException;
import com.sebas.concesionario.servicio.UsuarioServicio;
import java.util.regex.Pattern;
import org.apache.struts2.interceptor.parameter.StrutsParameter;

/**
 * GET /registro muestra el formulario (método "input") y POST /registrar lo procesa (execute).
 *
 * validate() se ejecuta antes de execute(): si añade errores, el interceptor "workflow"
 * corta la petición y devuelve el resultado "input" (vuelve a pintar el formulario con los errores).
 */
public class RegistroAction extends BaseAction {

    private static final Pattern CORREO = Pattern.compile("^[^@\\s]+@[^@\\s]+\\.[^@\\s]+$");
    private static final Pattern NOMBRE_USUARIO = Pattern.compile("^[a-zA-Z0-9._-]{3,30}$");

    private final UsuarioServicio usuarioServicio;

    private String nombreUsuario;
    private String nombre;
    private String apellido1;
    private String apellido2;
    private String correo;
    private String contrasena;
    private String contrasena2;

    public RegistroAction(UsuarioActual usuarioActual, UsuarioServicio usuarioServicio) {
        super(usuarioActual);
        this.usuarioServicio = usuarioServicio;
    }

    /** Struts no ejecuta validate() para el método "input" (excludeMethods del interceptor). */
    @Override
    public String input() {
        return INPUT;
    }

    @Override
    public void validate() {
        if (nombreUsuario == null || !NOMBRE_USUARIO.matcher(nombreUsuario.trim()).matches()) {
            addFieldError("nombreUsuario", "Entre 3 y 30 caracteres: letras, números, punto, guion o guion bajo");
        }
        if (vacioANull(nombre) == null) {
            addFieldError("nombre", "El nombre es obligatorio");
        }
        if (vacioANull(apellido1) == null) {
            addFieldError("apellido1", "El primer apellido es obligatorio");
        }
        if (correo == null || !CORREO.matcher(correo.trim()).matches()) {
            addFieldError("correo", "Introduce un correo válido");
        }
        if (contrasena == null || contrasena.length() < 8) {
            addFieldError("contrasena", "Mínimo 8 caracteres");
        } else if (!contrasena.equals(contrasena2)) {
            addFieldError("contrasena2", "Las contraseñas no coinciden");
        }
    }

    @Override
    public String execute() {
        Usuario u = new Usuario();
        u.setNombreUsuario(nombreUsuario);
        u.setNombre(nombre.trim());
        u.setApellido1(apellido1.trim());
        u.setApellido2(vacioANull(apellido2));
        u.setCorreo(correo);
        try {
            usuarioServicio.registrar(u, contrasena);
        } catch (NegocioException e) {
            addActionError(e.getMessage());
            return INPUT;
        }
        addActionMessage("Cuenta creada. Ya puedes iniciar sesión.");
        return SUCCESS;
    }

    @StrutsParameter
    public void setNombreUsuario(String nombreUsuario) { this.nombreUsuario = nombreUsuario; }
    @StrutsParameter
    public void setNombre(String nombre) { this.nombre = nombre; }
    @StrutsParameter
    public void setApellido1(String apellido1) { this.apellido1 = apellido1; }
    @StrutsParameter
    public void setApellido2(String apellido2) { this.apellido2 = apellido2; }
    @StrutsParameter
    public void setCorreo(String correo) { this.correo = correo; }
    @StrutsParameter
    public void setContrasena(String contrasena) { this.contrasena = contrasena; }
    @StrutsParameter
    public void setContrasena2(String contrasena2) { this.contrasena2 = contrasena2; }

    // Sin getters de las contraseñas: no se vuelven a pintar en el formulario
    public String getNombreUsuario() { return nombreUsuario; }
    public String getNombre() { return nombre; }
    public String getApellido1() { return apellido1; }
    public String getApellido2() { return apellido2; }
    public String getCorreo() { return correo; }
}
