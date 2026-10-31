<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Iniciar sesión" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="acceso">
    <div class="acceso-imagen">
        <span class="etiqueta" style="color: rgba(255,255,255,.7)">Car4U</span>
        <h2>Guarda favoritos y compra en dos pasos.</h2>
        <p>Tu cuenta guarda los coches que te interesan y el historial de tus compras.</p>
    </div>
    <div class="acceso-form">
        <div class="acceso-caja">
            <span class="etiqueta">Bienvenido de nuevo</span>
            <h1>Iniciar sesión</h1>
            <p>¿No tienes cuenta? <a class="fw-semibold" href="<c:url value='/registro'/>">Créala en un minuto</a></p>

            <c:if test="${param.error != null}">
                <div class="aviso aviso-error"><svg class="ico"><use href="#i-alerta"/></svg><span>Usuario o contraseña incorrectos.</span></div>
            </c:if>

            <%-- Formulario HTML normal: el POST /login lo procesa Spring Security, no Struts --%>
            <form method="post" action="<c:url value='/login'/>">
                <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                <div class="campo">
                    <label class="etiqueta" for="username">Usuario</label>
                    <input class="control" id="username" name="username" autocomplete="username" required autofocus>
                </div>
                <div class="campo">
                    <label class="etiqueta" for="password">Contraseña</label>
                    <input class="control" id="password" name="password" type="password" autocomplete="current-password" required>
                </div>
                <button type="submit" class="boton boton-tinta boton-bloque mt-2">Entrar <svg class="ico"><use href="#i-flecha"/></svg></button>
            </form>

            <div class="cuentas-demo">
                <strong class="d-block mb-1 text-body">Cuentas de demostración</strong>
                Cliente: <code>cliente</code> / <code>cliente1234</code><br>
                Administrador: <code>admin</code> / <code>admin1234</code>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
