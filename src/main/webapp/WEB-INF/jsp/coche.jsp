<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="${coche.nombreCompleto}" scope="request"/>
<c:set var="seccion" value="catalogo" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor">
    <nav class="miga" aria-label="Ruta">
        <a href="<c:url value='/catalogo'/>">Catálogo</a><span>/</span>
        <a href="<c:url value='/catalogo'><c:param name='marca' value='${coche.marca}'/></c:url>"><c:out value="${coche.marca}"/></a><span>/</span>
        <span class="text-body"><c:out value="${coche.modelo}"/></span>
    </nav>

    <div class="ficha">
        <div>
            <div class="ficha-foto">
                <img src="<c:url value='/fotos/${coche.imagen}'/>" alt="<c:out value='${coche.nombreCompleto}'/>">
            </div>

            <div class="specs">
                <div class="spec"><svg class="ico"><use href="#i-calendario"/></svg><span class="etiqueta d-block">Año</span><strong class="cifra"><c:out value="${coche.anio}"/></strong></div>
                <div class="spec"><svg class="ico"><use href="${coche.motor == 'ELECTRICO' ? '#i-rayo' : '#i-motor'}"/></svg><span class="etiqueta d-block">Motor</span><strong><c:out value="${coche.motor.etiqueta}"/></strong></div>
                <div class="spec"><svg class="ico"><use href="#i-potencia"/></svg><span class="etiqueta d-block">Potencia</span><strong class="cifra"><c:out value="${coche.potenciaCv}"/> CV</strong></div>
                <div class="spec"><svg class="ico"><use href="#i-cambio"/></svg><span class="etiqueta d-block">Cambio</span><strong><c:out value="${coche.cambio.etiqueta}"/></strong></div>
                <div class="spec"><svg class="ico"><use href="#i-cilindro"/></svg><span class="etiqueta d-block">Cilindrada</span>
                    <strong class="cifra"><c:choose><c:when test="${coche.cilindrada > 0}"><fmt:formatNumber value="${coche.cilindrada}" minFractionDigits="1"/> L</c:when><c:otherwise>—</c:otherwise></c:choose></strong></div>
                <div class="spec"><svg class="ico"><use href="#i-plazas"/></svg><span class="etiqueta d-block">Plazas</span><strong class="cifra"><c:out value="${coche.plazas}"/></strong></div>
                <div class="spec"><svg class="ico"><use href="#i-puerta"/></svg><span class="etiqueta d-block">Puertas</span><strong class="cifra"><c:out value="${coche.puertas}"/></strong></div>
                <div class="spec"><svg class="ico"><use href="#i-color"/></svg><span class="etiqueta d-block">Color</span><strong><c:out value="${coche.color}"/></strong></div>
            </div>

            <h2 class="mt-5 mb-0" style="font-size: 22px">Ficha técnica</h2>
            <table class="tabla-datos">
                <tbody>
                <tr><th scope="row">Marca y modelo</th><td><c:out value="${coche.nombreCompleto}"/></td></tr>
                <tr><th scope="row">Fecha de fabricación</th><td class="cifra"><c:out value="${coche.fechaFabricacionTexto}"/></td></tr>
                <tr><th scope="row">Número de bastidor (VIN)</th><td class="mono"><c:out value="${coche.bastidor}"/></td></tr>
                <tr><th scope="row">Matrícula</th><td class="mono"><c:out value="${coche.matricula}"/></td></tr>
                <tr><th scope="row">Combustible</th><td><c:out value="${coche.motor.etiqueta}"/></td></tr>
                <tr><th scope="row">Transmisión</th><td><c:out value="${coche.cambio.etiqueta}"/></td></tr>
                </tbody>
            </table>
        </div>

        <aside class="panel panel-compra">
            <span class="etiqueta"><c:out value="${coche.marca}"/></span>
            <h1><c:out value="${coche.modelo}"/></h1>
            <span class="matricula"><span><c:out value="${coche.matricula}"/></span></span>

            <div class="precio precio-grande"><fmt:formatNumber value="${coche.precio}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></div>
            <span class="ayuda">Precio final, IVA incluido</span>

            <div class="d-grid gap-2 mt-4">
                <c:choose>
                    <c:when test="${not coche.disponible}">
                        <div class="aviso aviso-error mb-0"><svg class="ico"><use href="#i-alerta"/></svg><span>Este coche ya está vendido</span></div>
                    </c:when>
                    <c:otherwise>
                        <sec:authorize access="isAnonymous()">
                            <a class="boton boton-acento boton-bloque" href="<c:url value='/login'/>">Inicia sesión para comprarlo</a>
                        </sec:authorize>
                        <sec:authorize access="hasRole('CLIENTE')">
                            <a class="boton boton-acento boton-bloque" href="<c:url value='/comprar'><c:param name='bastidor' value='${coche.bastidor}'/></c:url>">
                                <svg class="ico"><use href="#i-bolsa"/></svg>Comprar este coche
                            </a>
                        </sec:authorize>
                    </c:otherwise>
                </c:choose>
                <sec:authorize access="hasRole('CLIENTE')">
                    <form method="post" action="<c:url value='${favorito ? "/favorito-quitar" : "/favorito-anadir"}'/>" class="d-grid m-0">
                        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                        <input type="hidden" name="bastidor" value="<c:out value='${coche.bastidor}'/>">
                        <input type="hidden" name="volver" value="coche">
                        <button type="submit" class="boton boton-borde boton-bloque">
                            <svg class="ico" style="${favorito ? 'fill: var(--acento); color: var(--acento)' : ''}"><use href="#i-corazon"/></svg>
                            ${favorito ? 'Guardado en favoritos' : 'Guardar en favoritos'}
                        </button>
                    </form>
                </sec:authorize>
                <sec:authorize access="hasRole('ADMIN')">
                    <a class="boton boton-borde boton-bloque" href="<c:url value='/admin/coche-editar'><c:param name='bastidor' value='${coche.bastidor}'/></c:url>">
                        <svg class="ico"><use href="#i-lapiz"/></svg>Editar coche
                    </a>
                </sec:authorize>
            </div>

            <ul class="lista-check">
                <li><svg class="ico"><use href="#i-escudo"/></svg>Bastidor y matrícula verificados</li>
                <li><svg class="ico"><use href="#i-check"/></svg>Precio cerrado, sin costes ocultos</li>
                <li><svg class="ico"><use href="#i-check"/></svg>Compra online en dos pasos</li>
            </ul>
        </aside>
    </div>

    <c:if test="${not empty relacionados}">
        <section class="pb-5">
            <div class="cabecera-seccion">
                <div>
                    <span class="etiqueta">También te puede interesar</span>
                    <h2>Más de <c:out value="${coche.marca}"/></h2>
                </div>
                <a class="ir" href="<c:url value='/catalogo'><c:param name='marca' value='${coche.marca}'/></c:url>">Ver todos <svg class="ico ico-sm"><use href="#i-flecha"/></svg></a>
            </div>
            <div class="rejilla-coches">
                <c:set var="volver" value="catalogo"/>
                <c:forEach var="coche" items="${relacionados}">
                    <%@ include file="/WEB-INF/jsp/comun/tarjeta-coche.jspf" %>
                </c:forEach>
            </div>
        </section>
    </c:if>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
