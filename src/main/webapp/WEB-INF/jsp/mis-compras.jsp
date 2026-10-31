<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Mis compras" scope="request"/>
<c:set var="seccion" value="compras" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor" style="max-width: 980px">
    <header class="titulo-pagina">
        <span class="etiqueta">Tu garaje</span>
        <h1>Mis compras</h1>
    </header>

    <c:choose>
        <c:when test="${empty compras}">
            <div class="vacio my-5">
                <span class="icono-vacio"><svg class="ico ico-lg"><use href="#i-bolsa"/></svg></span>
                <h2>Todavía no has comprado ningún coche</h2>
                <p>Cuando compres uno, aquí tendrás todos los detalles.</p>
                <a class="boton boton-tinta" href="<c:url value='/catalogo'/>">Ver el catálogo</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="kpis">
                <div class="kpi"><span class="etiqueta">Coches comprados</span><strong><c:out value="${fn:length(compras)}"/></strong></div>
                <div class="kpi"><span class="etiqueta">Importe total</span><strong><fmt:formatNumber value="${totalGastado}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></strong></div>
                <div class="kpi"><span class="etiqueta">Última compra</span><strong style="font-size: 20px; padding-top: 6px"><c:out value="${compras[0].fechaTexto}"/></strong></div>
            </div>

            <div class="mb-5">
                <c:forEach var="compra" items="${compras}">
                    <c:url var="urlFicha" value="/coche"><c:param name="bastidor" value="${compra.bastidor}"/></c:url>
                    <article class="fila-compra">
                        <a href="${urlFicha}"><img src="<c:url value='/fotos/${compra.imagen}'/>" alt=""></a>
                        <div>
                            <span class="etiqueta"><c:out value="${compra.fechaTexto}"/></span>
                            <h3><a href="${urlFicha}"><c:out value="${compra.coche}"/></a></h3>
                            <span class="matricula"><span><c:out value="${compra.matricula}"/></span></span>
                        </div>
                        <div class="text-end">
                            <div class="precio"><fmt:formatNumber value="${compra.precio}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></div>
                            <a class="ir" href="${urlFicha}">Ver ficha <svg class="ico ico-sm"><use href="#i-flecha"/></svg></a>
                        </div>
                    </article>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
