package com.sebas.concesionario;

import static org.assertj.core.api.Assertions.assertThat;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba de integración: arranca la aplicación completa en un Tomcat embebido (puerto aleatorio)
 * con Spring Security + Struts 2 + JSP + H2, y hace peticiones HTTP reales.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class AplicacionIntegracionTest {

    @LocalServerPort
    private int puerto;

    private final HttpClient http = HttpClient.newBuilder()
            .followRedirects(HttpClient.Redirect.NEVER)
            .build();

    @Test
    void elCatalogoSePintaConStrutsYJsp() throws Exception {
        HttpResponse<String> r = get("/catalogo?marca=Seat");

        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.body()).contains("coches disponibles").contains("tarjeta-marca\">Seat<").doesNotContain("tarjeta-marca\">Toyota<");
    }

    @Test
    void laFichaDeUnCocheInexistenteDa404() throws Exception {
        assertThat(get("/coche?bastidor=NOEXISTE").statusCode()).isEqualTo(404);
    }

    @Test
    void laAdministracionPideLogin() throws Exception {
        HttpResponse<String> r = get("/admin/coches");

        assertThat(r.statusCode()).isEqualTo(302);
        assertThat(r.headers().firstValue("Location")).hasValueSatisfying(l -> assertThat(l).endsWith("/login"));
    }

    @Test
    void unPostSinTokenCsrfSeRechaza() throws Exception {
        HttpRequest req = HttpRequest.newBuilder(uri("/registrar"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString("nombreUsuario=hacker"))
                .build();

        assertThat(http.send(req, HttpResponse.BodyHandlers.ofString()).statusCode()).isEqualTo(403);
    }

    @Test
    void laApiDevuelveJson() throws Exception {
        HttpResponse<String> r = get("/api/coches?marca=Seat");

        assertThat(r.statusCode()).isEqualTo(200);
        assertThat(r.headers().firstValue("Content-Type")).hasValueSatisfying(t -> assertThat(t).contains("json"));
        assertThat(r.body()).contains("\"marca\":\"Seat\"");
    }

    private HttpResponse<String> get(String ruta) throws Exception {
        return http.send(HttpRequest.newBuilder(uri(ruta)).GET().build(), HttpResponse.BodyHandlers.ofString());
    }

    private URI uri(String ruta) {
        return URI.create("http://localhost:" + puerto + ruta);
    }
}
