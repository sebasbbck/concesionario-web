<%@ page pageEncoding="UTF-8" %>
<%@ include file="/WEB-INF/jsp/comun/taglibs.jspf" %>
<c:set var="titulo" value="Gestión de stock" scope="request"/>
<c:set var="seccion" value="admin" scope="request"/>
<%@ include file="/WEB-INF/jsp/comun/cabecera.jspf" %>

<div class="contenedor pb-5">
    <header class="titulo-pagina d-flex flex-wrap justify-content-between align-items-end gap-3">
        <div>
            <span class="etiqueta">Administración</span>
            <h1>Gestión de stock</h1>
        </div>
        <a class="boton boton-acento" href="<c:url value='/admin/coche-nuevo'/>"><svg class="ico"><use href="#i-mas"/></svg>Nuevo coche</a>
    </header>

    <div class="kpis">
        <div class="kpi"><span class="etiqueta">Disponibles</span><strong><c:out value="${totalDisponibles}"/></strong></div>
        <div class="kpi"><span class="etiqueta">Vendidos</span><strong><c:out value="${totalVendidos}"/></strong></div>
        <div class="kpi"><span class="etiqueta">Valor del stock</span><strong><fmt:formatNumber value="${valorStock}" type="currency" currencyCode="EUR" maxFractionDigits="0"/></strong></div>
    </div>

    <div class="d-flex justify-content-between align-items-center mb-3 flex-wrap gap-2">
        <nav class="segmentado" aria-label="Filtrar por estado">
            <a class="${empty estado ? 'activo' : ''}" href="<c:url value='/admin/coches'/>">Todos</a>
            <a class="${estado == 'DISPONIBLE' ? 'activo' : ''}" href="<c:url value='/admin/coches?estado=DISPONIBLE'/>">Disponibles</a>
            <a class="${estado == 'COMPRADO' ? 'activo' : ''}" href="<c:url value='/admin/coches?estado=COMPRADO'/>">Vendidos</a>
        </nav>
        <span class="etiqueta"><c:out value="${fn:length(coches)}"/> resultados</span>
    </div>

    <div class="tabla-panel">
        <div class="table-responsive">
            <table class="tabla">
                <thead>
                <tr>
                    <th class="etiqueta">Coche</th>
                    <th class="etiqueta">Matrícula</th>
                    <th class="etiqueta">Año</th>
                    <th class="etiqueta">Motor</th>
                    <th class="etiqueta text-end">Precio</th>
                    <th class="etiqueta">Estado</th>
                    <th><span class="visually-hidden">Acciones</span></th>
                </tr>
                </thead>
                <tbody>
                <%-- s:iterator recorre la lista de la Action; dentro, cada coche está en la cima de la ValueStack --%>
                <s:iterator value="coches">
                    <tr>
                        <td>
                            <div class="d-flex align-items-center gap-3">
                                <img class="miniatura" src="<c:url value='/fotos/'/><s:property value='imagen'/>" alt="" loading="lazy">
                                <div>
                                    <a class="nombre" href="<s:url action='coche' namespace='/'><s:param name='bastidor' value='bastidor'/></s:url>"><s:property value="modelo"/></a>
                                    <div class="etiqueta" style="font-size: 11px"><s:property value="marca"/></div>
                                </div>
                            </div>
                        </td>
                        <td class="mono"><s:property value="matricula"/></td>
                        <td class="cifra"><s:property value="anio"/></td>
                        <td><s:property value="motor.etiqueta"/></td>
                        <td class="text-end cifra fw-semibold"><s:number name="precio" type="currency" currency="EUR" maximumFractionDigits="0"/></td>
                        <td>
                            <s:if test="disponible"><span class="estado">Disponible</span></s:if>
                            <s:else><span class="estado vendido">Vendido</span></s:else>
                        </td>
                        <td class="text-end text-nowrap">
                            <a class="boton boton-icono" href="<s:url action='coche-editar'><s:param name='bastidor' value='bastidor'/></s:url>"
                               aria-label="Editar" title="Editar"><svg class="ico ico-sm"><use href="#i-lapiz"/></svg></a>
                            <s:if test="disponible">
                                <form method="post" action="<c:url value='/admin/coche-borrar'/>" class="d-inline"
                                      onsubmit="return confirm('¿Borrar el coche ' + this.dataset.matricula + '?')"
                                      data-matricula="<s:property value='matricula'/>">
                                    <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}">
                                    <input type="hidden" name="bastidor" value="<s:property value='bastidor'/>">
                                    <button type="submit" class="boton boton-icono peligro" aria-label="Borrar" title="Borrar"><svg class="ico ico-sm"><use href="#i-papelera"/></svg></button>
                                </form>
                            </s:if>
                        </td>
                    </tr>
                </s:iterator>
                </tbody>
            </table>
        </div>
    </div>
</div>

<%@ include file="/WEB-INF/jsp/comun/pie.jspf" %>
