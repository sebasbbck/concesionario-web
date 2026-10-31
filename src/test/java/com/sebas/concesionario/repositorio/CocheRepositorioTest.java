package com.sebas.concesionario.repositorio;

import static org.assertj.core.api.Assertions.assertThat;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.modelo.TipoCambio;
import com.sebas.concesionario.modelo.TipoMotor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

/**
 * Prueba el repositorio contra una BD real (H2 en memoria) con el esquema y los datos de Flyway.
 * Cada test va en una transacción que se deshace al acabar (@JdbcTest es @Transactional).
 */
@JdbcTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Import(CocheRepositorio.class)
class CocheRepositorioTest {

    private static final String LEON = "KL1TD62684B456789";

    @Autowired
    private CocheRepositorio repositorio;

    @Test
    void filtraPorMarcaYMotor() {
        FiltroCoches filtro = new FiltroCoches("Seat", null, TipoMotor.DIESEL, null, null, null, EstadoCoche.DISPONIBLE);

        List<Coche> coches = repositorio.buscar(filtro);

        assertThat(coches).isNotEmpty()
                .allSatisfy(c -> {
                    assertThat(c.getMarca()).isEqualTo("Seat");
                    assertThat(c.getMotor()).isEqualTo(TipoMotor.DIESEL);
                });
    }

    @Test
    void filtraPorPrecioMaximoYDevuelveSoloDisponibles() {
        List<Coche> coches = repositorio.buscar(new FiltroCoches(null, null, null, null,
                new BigDecimal("20000"), null, EstadoCoche.DISPONIBLE));

        assertThat(coches).isNotEmpty()
                .allMatch(c -> c.getPrecio().compareTo(new BigDecimal("20000")) <= 0)
                .allMatch(Coche::isDisponible);
    }

    @Test
    void unValorConComillasNoRompeLaConsulta() {
        // Con concatenación sería inyección SQL; con parámetros es solo un texto que no coincide
        List<Coche> coches = repositorio.buscar(new FiltroCoches("Seat' OR '1'='1", null, null, null, null, null, null));

        assertThat(coches).isEmpty();
    }

    @Test
    void marcarCompradoSoloFuncionaUnaVez() {
        assertThat(repositorio.marcarComprado(LEON)).isEqualTo(1);
        assertThat(repositorio.marcarComprado(LEON)).isZero();
        assertThat(repositorio.buscarPorBastidor(LEON)).get()
                .extracting(Coche::getEstado).isEqualTo(EstadoCoche.COMPRADO);
    }

    @Test
    void insertaYDetectaMatriculaRepetida() {
        Coche nuevo = new Coche();
        nuevo.setBastidor("VSSZZZKJZPR000001");
        nuevo.setMatricula("0000TST");
        nuevo.setModeloId(1);
        nuevo.setFechaFabricacion(LocalDate.of(2024, 1, 15));
        nuevo.setMotor(TipoMotor.GASOLINA);
        nuevo.setColorId("BLA00");
        nuevo.setCilindrada(new BigDecimal("1.5"));
        nuevo.setPotenciaCv(150);
        nuevo.setPuertas(5);
        nuevo.setPlazas(5);
        nuevo.setCambio(TipoCambio.MANUAL);
        nuevo.setPrecio(new BigDecimal("25000"));

        repositorio.insertar(nuevo, 1);

        assertThat(repositorio.existeBastidor("VSSZZZKJZPR000001")).isTrue();
        assertThat(repositorio.existeMatricula("0000TST", null)).isTrue();
        assertThat(repositorio.existeMatricula("0000TST", "VSSZZZKJZPR000001")).isFalse();
    }
}
