<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Catálogo" scope="request"/>
<c:set var="seccion" value="catalogo" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<section class="portada">
    <div class="contenedor">
        <span class="etiqueta">Stock disponible ahora</span>
        <h1>Tu próximo coche, <em>sin rodeos.</em></h1>
        <p>Todos los datos de cada coche a la vista, precio cerrado y compra en dos pasos.</p>
        <div class="portada-cifras">
            <div><strong class="cifra"><c:out value="${totalStock}"/></strong><span>coches disponibles</span></div>
            <div><strong class="cifra"><c:out value="${fn:length(marcas)}"/></strong><span>marcas</span></div>
            <div><strong class="cifra"><fmt:formatNumber value="${precioMinimo}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></strong><span>precio desde</span></div>
        </div>
    </div>
</section>

<div class="contenedor">
    <%-- Buscador: s:form genera la URL de la Action; s:select y s:textfield se rellenan con los valores actuales --%>
    <s:form action="catalogo" method="get" cssClass="buscador" aria-label="Buscar coches">
        <div class="buscador-campos">
            <div class="campo">
                <label class="etiqueta" for="marca">Marca</label>
                <s:select id="marca" name="marca" list="marcas" listKey="nombre" listValue="nombre"
                          headerKey="" headerValue="Todas" cssClass="form-select control"/>
            </div>
            <div class="campo">
                <label class="etiqueta" for="modeloId">Modelo</label>
                <select id="modeloId" name="modeloId" class="form-select control">
                    <option value="">Todos</option>
                    <c:forEach var="m" items="${modelos}">
                        <option value="${m.id}" data-marca="<c:out value='${m.marca}'/>" ${m.id == modeloId ? 'selected' : ''}><c:out value="${m.nombre}"/></option>
                    </c:forEach>
                </select>
            </div>
            <div class="campo">
                <label class="etiqueta" for="motor">Motor</label>
                <select id="motor" name="motor" class="form-select control">
                    <option value="">Todos</option>
                    <c:forEach var="t" items="${motores}">
                        <option value="${t}" ${t == motor ? 'selected' : ''}>${t.etiqueta}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="campo">
                <label class="etiqueta" for="cambio">Cambio</label>
                <select id="cambio" name="cambio" class="form-select control">
                    <option value="">Todos</option>
                    <c:forEach var="t" items="${cambios}">
                        <option value="${t}" ${t == cambio ? 'selected' : ''}>${t.etiqueta}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="campo">
                <label class="etiqueta" for="precioMax">Precio máx. (€)</label>
                <s:textfield id="precioMax" name="precioMax" type="number" min="0" step="1000" placeholder="Sin límite" cssClass="control"/>
            </div>
            <div class="campo">
                <label class="etiqueta" for="plazasMin">Plazas mín.</label>
                <s:textfield id="plazasMin" name="plazasMin" type="number" min="1" max="9" placeholder="Cualquiera" cssClass="control"/>
            </div>
            <button type="submit" class="boton boton-acento"><svg class="ico"><use href="#i-buscar"/></svg>Buscar</button>
        </div>
    </s:form>
</div>

<section class="contenedor seccion">
    <span class="etiqueta d-block mb-2">Marcas</span>
    <%-- Carrusel: la franja se desplaza en horizontal; las flechas solo aparecen si hay más marcas en esa dirección --%>
    <div class="carrusel mb-4" id="carrusel-marcas">
        <button type="button" class="carrusel-flecha izq" aria-label="Ver marcas anteriores" hidden>
            <svg class="ico"><use href="#i-flecha-izq"/></svg>
        </button>
        <nav class="marcas" aria-label="Filtrar por marca">
            <a class="marca ${empty marca ? 'activa' : ''}" href="<c:url value='/catalogo'/>">
                <svg class="ico ico-lg" style="height: 36px"><use href="#i-coche"/></svg>Todas
            </a>
            <c:forEach var="m" items="${marcas}">
                <a class="marca ${m.nombre == marca ? 'activa' : ''}" href="<c:url value='/catalogo'><c:param name='marca' value='${m.nombre}'/></c:url>">
                    <img src="<c:url value='/fotos/${m.imagen}'/>" alt="" loading="lazy"><c:out value="${m.nombre}"/>
                </a>
            </c:forEach>
        </nav>
        <button type="button" class="carrusel-flecha der" aria-label="Ver más marcas" hidden>
            <svg class="ico"><use href="#i-flecha"/></svg>
        </button>
    </div>

    <div class="cabecera-seccion">
        <div>
            <span class="etiqueta">Resultados</span>
            <h2 class="cifra"><c:out value="${fn:length(coches)}"/> coche<c:if test="${fn:length(coches) != 1}">s</c:if></h2>
        </div>
        <c:if test="${hayFiltros}">
            <div class="chips">
                <c:if test="${not empty marca}"><span class="chip"><c:out value="${marca}"/></span></c:if>
                <c:forEach var="m" items="${modelos}"><c:if test="${m.id == modeloId}"><span class="chip"><c:out value="${m.nombre}"/></span></c:if></c:forEach>
                <c:forEach var="t" items="${motores}"><c:if test="${t == motor}"><span class="chip">${t.etiqueta}</span></c:if></c:forEach>
                <c:forEach var="t" items="${cambios}"><c:if test="${t == cambio}"><span class="chip">${t.etiqueta}</span></c:if></c:forEach>
                <c:if test="${not empty precioMax}"><span class="chip">Hasta <c:out value="${precioMax}"/> €</span></c:if>
                <c:if test="${not empty plazasMin}"><span class="chip"><c:out value="${plazasMin}"/>+ plazas</span></c:if>
                <a class="chip chip-oscuro" href="<c:url value='/catalogo'/>"><svg class="ico ico-sm"><use href="#i-cruz"/></svg>Limpiar</a>
            </div>
        </c:if>
    </div>

    <c:choose>
        <c:when test="${empty coches}">
            <div class="vacio">
                <span class="icono-vacio"><svg class="ico ico-lg"><use href="#i-buscar"/></svg></span>
                <h2>No hay coches con esos filtros</h2>
                <p>Prueba a quitar alguno o amplía el precio máximo.</p>
                <a class="boton boton-tinta" href="<c:url value='/catalogo'/>">Ver todo el catálogo</a>
            </div>
        </c:when>
        <c:otherwise>
            <div class="rejilla-coches">
                <c:set var="volver" value="catalogo"/>
                <c:forEach var="coche" items="${coches}">
                    <%@ include file="/WEB-INF/jsp/comun/tarjeta-coche.jspf" %>
                </c:forEach>
            </div>
        </c:otherwise>
    </c:choose>
</section>

<script>
    // Al cambiar de marca, deja solo los modelos de esa marca
    (function () {
        const marca = document.getElementById('marca');
        const modelo = document.getElementById('modeloId');
        function filtrar() {
            for (const op of modelo.options) {
                if (!op.value) continue;
                const visible = !marca.value || op.dataset.marca === marca.value;
                op.hidden = !visible;
                if (!visible && op.selected) modelo.value = '';
            }
        }
        marca.addEventListener('change', filtrar);
        filtrar();
    })();

    // Carrusel de marcas: flechas y difuminados según la posición del scroll
    (function () {
        const carrusel = document.getElementById('carrusel-marcas');
        const pista = carrusel.querySelector('.marcas');
        const izq = carrusel.querySelector('.izq');
        const der = carrusel.querySelector('.der');

        function actualizar() {
            const max = pista.scrollWidth - pista.clientWidth;
            const hayIzq = pista.scrollLeft > 2;
            const hayDer = pista.scrollLeft < max - 2;
            izq.hidden = !hayIzq;
            der.hidden = !hayDer;
            carrusel.classList.toggle('hay-izq', hayIzq);
            carrusel.classList.toggle('hay-der', hayDer);
        }

        function mover(sentido) {
            pista.scrollBy({ left: sentido * pista.clientWidth * 0.8, behavior: 'smooth' });
        }

        izq.addEventListener('click', () => mover(-1));
        der.addEventListener('click', () => mover(1));
        pista.addEventListener('scroll', actualizar, { passive: true });
        window.addEventListener('resize', actualizar);

        // Si hay una marca filtrada, centrarla para que se vea cuál está activa
        const activa = pista.querySelector('.marca.activa');
        if (activa && activa !== pista.firstElementChild) {
            pista.scrollLeft = activa.offsetLeft - (pista.clientWidth - activa.offsetWidth) / 2;
        }
        actualizar();
    })();
</script>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
