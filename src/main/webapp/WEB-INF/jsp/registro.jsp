<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Crear cuenta" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="acceso">
    <div class="acceso-imagen">
        <span class="etiqueta" style="color: rgba(255,255,255,.7)">Car4U</span>
        <h2>Crea tu cuenta y encuentra tu coche.</h2>
        <p>Guarda los que más te gusten, compáralos con calma y cómpralo cuando estés listo.</p>
    </div>
    <div class="acceso-form">
        <div class="acceso-caja">
            <span class="etiqueta">Nuevo cliente</span>
            <h1>Crear cuenta</h1>
            <p>¿Ya tienes cuenta? <a class="fw-semibold" href="<c:url value='/login'/>">Inicia sesión</a></p>

            <%-- s:form + s:textfield: Struts rellena los campos con los valores de la Action
                 y s:fielderror muestra los errores de validate() en cada campo --%>
            <s:form action="registrar" method="post" novalidate="novalidate">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="campo">
                    <label class="etiqueta" for="nombreUsuario">Nombre de usuario</label>
                    <s:textfield id="nombreUsuario" name="nombreUsuario" autocomplete="username" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="nombreUsuario" cssClass="error-campo"/>
                </div>
                <div class="row g-3 mb-3">
                    <div class="col-sm-4 campo mb-0">
                        <label class="etiqueta" for="nombre">Nombre</label>
                        <s:textfield id="nombre" name="nombre" autocomplete="given-name" cssClass="control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="nombre" cssClass="error-campo"/>
                    </div>
                    <div class="col-sm-4 campo mb-0">
                        <label class="etiqueta" for="apellido1">Apellido</label>
                        <s:textfield id="apellido1" name="apellido1" autocomplete="family-name" cssClass="control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="apellido1" cssClass="error-campo"/>
                    </div>
                    <div class="col-sm-4 campo mb-0">
                        <label class="etiqueta" for="apellido2">2º apellido</label>
                        <s:textfield id="apellido2" name="apellido2" cssClass="control"/>
                    </div>
                </div>
                <div class="campo">
                    <label class="etiqueta" for="correo">Correo electrónico</label>
                    <s:textfield id="correo" name="correo" type="email" autocomplete="email" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="correo" cssClass="error-campo"/>
                </div>
                <div class="row g-3">
                    <div class="col-sm-6 campo mb-0">
                        <label class="etiqueta" for="contrasena">Contraseña</label>
                        <s:password id="contrasena" name="contrasena" autocomplete="new-password" cssClass="control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="contrasena" cssClass="error-campo"/>
                    </div>
                    <div class="col-sm-6 campo mb-0">
                        <label class="etiqueta" for="contrasena2">Repite la contraseña</label>
                        <s:password id="contrasena2" name="contrasena2" autocomplete="new-password" cssClass="control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="contrasena2" cssClass="error-campo"/>
                    </div>
                </div>
                <p class="ayuda">Mínimo 8 caracteres.</p>
                <button type="submit" class="boton boton-tinta boton-bloque mt-3">Crear cuenta <svg class="ico"><use href="#i-flecha"/></svg></button>
            </s:form>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
