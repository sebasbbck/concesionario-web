package com.sebas.concesionario.web.struts;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.struts2.ActionInvocation;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.interceptor.AbstractInterceptor;

/**
 * Interceptor de Struts que solo deja pasar peticiones POST.
 *
 * Se aplica a las Actions que cambian datos (comprar, borrar, guardar...). Así no se pueden
 * lanzar con un enlace GET, y todas pasan por el token CSRF que comprueba Spring Security en los POST.
 */
public class SoloPostInterceptor extends AbstractInterceptor {

    @Override
    public String intercept(ActionInvocation invocation) throws Exception {
        HttpServletRequest request = ServletActionContext.getRequest();
        if (!"POST".equalsIgnoreCase(request.getMethod())) {
            ServletActionContext.getResponse().sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return null;
        }
        return invocation.invoke();
    }
}
