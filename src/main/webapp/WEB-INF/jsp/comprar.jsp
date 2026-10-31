<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Confirmar compra" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor" style="max-width: 980px">
    <nav class="miga" aria-label="Ruta">
        <a href="<c:url value='/coche'><c:param name='bastidor' value='${coche.bastidor}'/></c:url>"><svg class="ico ico-sm"><use href="#i-flecha-izq"/></svg> Volver a la ficha</a>
    </nav>
    <header class="titulo-pagina">
        <span class="etiqueta">Paso 2 de 2</span>
        <h1>Confirma tu compra</h1>
        <p>Revisa los datos antes de confirmar. El coche quedará reservado a tu nombre.</p>
    </header>

    <div class="row g-4 py-4 mb-5">
        <div class="col-md-7">
            <div class="panel p-0 overflow-hidden">
                <div class="ficha-foto" style="border-radius: 0"><img src="<c:url value='/fotos/${coche.imagen}'/>" alt="<c:out value='${coche.nombreCompleto}'/>"></div>
                <div class="p-4">
                    <span class="etiqueta"><c:out value="${coche.marca}"/></span>
                    <h2 style="font-size: 26px" class="mt-1 mb-3"><c:out value="${coche.modelo}"/></h2>
                    <div class="datos mb-0">
                        <span><svg class="ico ico-sm"><use href="#i-calendario"/></svg><c:out value="${coche.anio}"/></span>
                        <span><svg class="ico ico-sm"><use href="#i-motor"/></svg><c:out value="${coche.motor.etiqueta}"/></span>
                        <span><svg class="ico ico-sm"><use href="#i-cambio"/></svg><c:out value="${coche.cambio.etiqueta}"/></span>
                        <span><svg class="ico ico-sm"><use href="#i-potencia"/></svg><c:out value="${coche.potenciaCv}"/> CV</span>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-5">
            <div class="panel">
                <span class="etiqueta">Resumen</span>
                <table class="tabla-datos mt-2">
                    <tbody>
                    <tr><th scope="row">Vehículo</th><td><c:out value="${coche.nombreCompleto}"/></td></tr>
                    <tr><th scope="row">Matrícula</th><td class="mono"><c:out value="${coche.matricula}"/></td></tr>
                    <tr><th scope="row">Gastos de gestión</th><td class="cifra">0 €</td></tr>
                    </tbody>
                </table>
                <div class="d-flex justify-content-between align-items-baseline mt-4">
                    <span class="fw-semibold">Total</span>
                    <span class="precio" style="font-size: 32px"><fmt:formatNumber value="${coche.precio}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></span>
                </div>
                <form method="post" action="<c:url value='/comprar-confirmar'/>" class="d-grid gap-2 mt-4">
                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                    <input type="hidden" name="bastidor" value="<c:out value='${coche.bastidor}'/>">
                    <button type="submit" class="boton boton-acento boton-bloque"><svg class="ico"><use href="#i-check"/></svg>Confirmar compra</button>
                    <a class="boton boton-fantasma" href="<c:url value='/coche'><c:param name='bastidor' value='${coche.bastidor}'/></c:url>">Cancelar</a>
                </form>
            </div>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
