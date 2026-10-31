package com.sebas.concesionario.config;

import jakarta.servlet.DispatcherType;
import org.apache.struts2.dispatcher.filter.StrutsPrepareAndExecuteFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

/**
 * Registra el filtro de Struts 2 (lo que en una aplicación clásica iría en web.xml).
 *
 * Va detrás de Spring Security, así que cuando una petición llega a una Action ya está
 * autenticada/autorizada. Las rutas que no son de Struts (/api, /fotos, /css...) se excluyen
 * en struts.xml (struts.action.excludePattern) y las atiende el DispatcherServlet de Spring MVC.
 */
@Configuration
public class StrutsConfig {

    @Bean
    public FilterRegistrationBean<StrutsPrepareAndExecuteFilter> strutsFilter() {
        FilterRegistrationBean<StrutsPrepareAndExecuteFilter> registro =
                new FilterRegistrationBean<>(new StrutsPrepareAndExecuteFilter());
        registro.setName("struts2");
        registro.addUrlPatterns("/*");
        registro.setDispatcherTypes(DispatcherType.REQUEST);
        registro.setOrder(Ordered.LOWEST_PRECEDENCE - 10);
        return registro;
    }
}
