<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Acceso denegado" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="pagina-error">
    <div class="codigo">4<span>0</span>3</div>
    <h1 style="font-size: 28px" class="mt-3">No tienes permiso para ver esta página</h1>
    <p>Esta zona es solo para otro tipo de cuenta.</p>
    <a class="boton boton-tinta" href="<c:url value='/catalogo'/>">Volver al catálogo</a>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
