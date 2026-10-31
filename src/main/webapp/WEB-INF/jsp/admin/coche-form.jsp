<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="${edicion ? 'Editar coche' : 'Nuevo coche'}" scope="request"/>
<c:set var="seccion" value="admin" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor pb-5" style="max-width: 1060px">
    <nav class="miga" aria-label="Ruta">
        <a href="<c:url value='/admin/coches'/>"><svg class="ico ico-sm"><use href="#i-flecha-izq"/></svg> Gestión de stock</a>
    </nav>
    <header class="titulo-pagina">
        <span class="etiqueta">${edicion ? 'Edición' : 'Alta'}</span>
        <h1>${edicion ? 'Editar coche' : 'Nuevo coche'}</h1>
        <p>${edicion ? 'Los cambios se verán al momento en el catálogo.' : 'El coche aparecerá en el catálogo como disponible.'}</p>
    </header>

    <s:form action="coche-guardar" namespace="/admin" method="post" cssClass="panel mt-4" novalidate="novalidate">
        <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
        <s:hidden name="bastidor"/>

        <div class="form-seccion pt-0">
            <div><h2>Identificación</h2><p>Bastidor (VIN) y matrícula. Se guardan en mayúsculas y sin espacios.</p></div>
            <div class="row g-3">
                <div class="col-md-7 campo">
                    <label class="etiqueta" for="f-bastidor">Bastidor (VIN)</label>
                    <s:textfield id="f-bastidor" name="coche.bastidor" maxlength="17" cssClass="control mono text-uppercase" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.bastidor" cssClass="error-campo"/>
                </div>
                <div class="col-md-5 campo">
                    <label class="etiqueta" for="f-matricula">Matrícula</label>
                    <s:textfield id="f-matricula" name="coche.matricula" maxlength="10" cssClass="control mono text-uppercase" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.matricula" cssClass="error-campo"/>
                </div>
            </div>
        </div>

        <div class="form-seccion">
            <div><h2>Modelo y aspecto</h2><p>La foto del catálogo es la del modelo elegido.</p></div>
            <div class="row g-3">
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-modelo">Modelo</label>
                    <select id="f-modelo" name="coche.modeloId" class="form-select control ${not empty fieldErrors['coche.modeloId'] ? 'is-invalid' : ''}">
                        <option value="">Elige un modelo</option>
                        <c:set var="marcaActual" value=""/>
                        <c:forEach var="m" items="${modelos}">
                            <c:if test="${m.marca != marcaActual}">
                                <c:if test="${not empty marcaActual}"></optgroup></c:if>
                                <optgroup label="<c:out value='${m.marca}'/>">
                                <c:set var="marcaActual" value="${m.marca}"/>
                            </c:if>
                            <option value="${m.id}" data-imagen="<c:url value='/fotos/${m.imagen}'/>" ${coche.modeloId == m.id ? 'selected' : ''}><c:out value="${m.nombre}"/></option>
                        </c:forEach>
                        <c:if test="${not empty marcaActual}"></optgroup></c:if>
                    </select>
                    <s:fielderror fieldName="coche.modeloId" cssClass="error-campo"/>
                    <div class="vista-previa" id="vista-previa"><svg class="ico ico-lg"><use href="#i-coche"/></svg></div>
                </div>
                <div class="col-md-6">
                    <div class="campo mb-3">
                        <label class="etiqueta" for="f-color">Color</label>
                        <s:select id="f-color" name="coche.colorId" list="colores" listKey="id" listValue="nombre"
                                  headerKey="" headerValue="Elige un color" cssClass="form-select control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="coche.colorId" cssClass="error-campo"/>
                    </div>
                    <div class="campo">
                        <label class="etiqueta" for="f-fecha">Fecha de fabricación</label>
                        <s:textfield id="f-fecha" name="coche.fechaFabricacion" type="date" cssClass="control" cssErrorClass="is-invalid"/>
                        <s:fielderror fieldName="coche.fechaFabricacion" cssClass="error-campo"/>
                    </div>
                </div>
            </div>
        </div>

        <div class="form-seccion">
            <div><h2>Mecánica</h2><p>Cilindrada en litros; 0 para los eléctricos.</p></div>
            <div class="row g-3">
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-motor">Motor</label>
                    <select id="f-motor" name="coche.motor" class="form-select control ${not empty fieldErrors['coche.motor'] ? 'is-invalid' : ''}">
                        <option value="">Elige</option>
                        <c:forEach var="t" items="${motores}">
                            <option value="${t}" ${coche.motor == t ? 'selected' : ''}>${t.etiqueta}</option>
                        </c:forEach>
                    </select>
                    <s:fielderror fieldName="coche.motor" cssClass="error-campo"/>
                </div>
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-cambio">Cambio</label>
                    <select id="f-cambio" name="coche.cambio" class="form-select control ${not empty fieldErrors['coche.cambio'] ? 'is-invalid' : ''}">
                        <option value="">Elige</option>
                        <c:forEach var="t" items="${cambios}">
                            <option value="${t}" ${coche.cambio == t ? 'selected' : ''}>${t.etiqueta}</option>
                        </c:forEach>
                    </select>
                    <s:fielderror fieldName="coche.cambio" cssClass="error-campo"/>
                </div>
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-cilindrada">Cilindrada (L)</label>
                    <s:textfield id="f-cilindrada" name="coche.cilindrada" type="number" step="0.1" min="0" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.cilindrada" cssClass="error-campo"/>
                </div>
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-potencia">Potencia (CV)</label>
                    <s:textfield id="f-potencia" name="coche.potenciaCv" type="number" min="1" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.potenciaCv" cssClass="error-campo"/>
                </div>
            </div>
        </div>

        <div class="form-seccion">
            <div><h2>Habitáculo y precio</h2><p>Precio final de venta, IVA incluido.</p></div>
            <div class="row g-3">
                <div class="col-md-3 col-6 campo">
                    <label class="etiqueta" for="f-puertas">Puertas</label>
                    <s:textfield id="f-puertas" name="coche.puertas" type="number" min="2" max="5" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.puertas" cssClass="error-campo"/>
                </div>
                <div class="col-md-3 col-6 campo">
                    <label class="etiqueta" for="f-plazas">Plazas</label>
                    <s:textfield id="f-plazas" name="coche.plazas" type="number" min="1" max="9" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.plazas" cssClass="error-campo"/>
                </div>
                <div class="col-md-6 campo">
                    <label class="etiqueta" for="f-precio">Precio (€)</label>
                    <s:textfield id="f-precio" name="coche.precio" type="number" min="0" step="100" cssClass="control" cssErrorClass="is-invalid"/>
                    <s:fielderror fieldName="coche.precio" cssClass="error-campo"/>
                </div>
            </div>
        </div>

        <div class="barra-acciones">
            <a class="boton boton-borde" href="<c:url value='/admin/coches'/>">Cancelar</a>
            <button type="submit" class="boton boton-acento"><svg class="ico"><use href="#i-check"/></svg>${edicion ? 'Guardar cambios' : 'Dar de alta'}</button>
        </div>
    </s:form>
</div>

<script>
    // Vista previa de la foto del modelo elegido
    (function () {
        const select = document.getElementById('f-modelo');
        const vista = document.getElementById('vista-previa');
        const vacia = vista.innerHTML;
        function pintar() {
            const op = select.selectedOptions[0];
            vista.innerHTML = op && op.dataset.imagen ? '<img src="' + op.dataset.imagen + '" alt="">' : vacia;
        }
        select.addEventListener('change', pintar);
        pintar();
    })();
</script>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
