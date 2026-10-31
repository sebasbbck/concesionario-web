<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Favoritos" scope="request"/>
<c:set var="seccion" value="favoritos" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor">
    <header class="titulo-pagina">
        <span class="etiqueta">Tu selección</span>
        <h1>Favoritos</h1>
        <p><c:out value="${fn:length(coches)}"/> coche<c:if test="${fn:length(coches) != 1}">s</c:if> guardado<c:if test="${fn:length(coches) != 1}">s</c:if></p>
    </header>

    <section class="seccion pt-4">
        <c:choose>
            <c:when test="${empty coches}">
                <div class="vacio">
                    <span class="icono-vacio"><svg class="ico ico-lg"><use href="#i-corazon"/></svg></span>
                    <h2>Aún no has guardado ningún coche</h2>
                    <p>Pulsa el corazón de cualquier coche del catálogo para tenerlo aquí a mano.</p>
                    <a class="boton boton-tinta" href="<c:url value='/catalogo'/>">Explorar el catálogo</a>
                </div>
            </c:when>
            <c:otherwise>
                <div class="rejilla-coches">
                    <c:set var="volver" value="favoritos"/>
                    <c:forEach var="coche" items="${coches}">
                        <%@ include file="/WEB-INF/jsp/comun/tarjeta-coche.jspf" %>
                    </c:forEach>
                </div>
            </c:otherwise>
        </c:choose>
    </section>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
