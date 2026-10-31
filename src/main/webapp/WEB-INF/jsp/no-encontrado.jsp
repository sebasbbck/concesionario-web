<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<% response.setStatus(404); %>
<c:set var="titulo" value="No encontrado" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="pagina-error">
    <div class="codigo">4<span>0</span>4</div>
    <h1 style="font-size: 28px" class="mt-3">Este coche ya no está aquí</h1>
    <p>Puede que se haya vendido o que el enlace no sea correcto.</p>
    <a class="boton boton-tinta" href="<c:url value='/catalogo'/>">Volver al catálogo</a>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
