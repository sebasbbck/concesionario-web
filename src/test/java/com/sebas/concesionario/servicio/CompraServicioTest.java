package com.sebas.concesionario.servicio;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.EstadoCoche;
import com.sebas.concesionario.repositorio.CocheRepositorio;
import com.sebas.concesionario.repositorio.CompraRepositorio;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

/** Test unitario: los repositorios son mocks, así que solo se prueba la lógica del servicio. */
@ExtendWith(MockitoExtension.class)
class CompraServicioTest {

    @Mock
    private CocheRepositorio coches;

    @Mock
    private CompraRepositorio compras;

    @InjectMocks
    private CompraServicio servicio;

    @Test
    void compraUnCocheDisponible() {
        when(coches.buscarPorBastidor("B1")).thenReturn(Optional.of(coche(EstadoCoche.DISPONIBLE)));
        when(coches.marcarComprado("B1")).thenReturn(1);

        Coche comprado = servicio.comprar("B1", 7);

        assertThat(comprado.getBastidor()).isEqualTo("B1");
        verify(compras).insertar("B1", 7, new BigDecimal("20000"));
    }

    @Test
    void noDejaComprarUnCocheVendido() {
        when(coches.buscarPorBastidor("B1")).thenReturn(Optional.of(coche(EstadoCoche.COMPRADO)));

        assertThatThrownBy(() -> servicio.comprar("B1", 7))
                .isInstanceOf(NegocioException.class)
                .hasMessageContaining("vendido");
        verify(coches, never()).marcarComprado(anyString());
        verify(compras, never()).insertar(anyString(), anyInt(), any());
    }

    @Test
    void siOtroClienteSeAdelantaNoRegistraLaCompra() {
        when(coches.buscarPorBastidor("B1")).thenReturn(Optional.of(coche(EstadoCoche.DISPONIBLE)));
        when(coches.marcarComprado("B1")).thenReturn(0);   // el UPDATE no afectó a ninguna fila

        assertThatThrownBy(() -> servicio.comprar("B1", 7)).isInstanceOf(NegocioException.class);
        verify(compras, never()).insertar(anyString(), anyInt(), any());
    }

    @Test
    void cocheInexistente() {
        when(coches.buscarPorBastidor("NO")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> servicio.comprar("NO", 7)).isInstanceOf(NoEncontradoException.class);
    }

    private static Coche coche(EstadoCoche estado) {
        Coche c = new Coche();
        c.setBastidor("B1");
        c.setMarca("Seat");
        c.setModelo("León");
        c.setPrecio(new BigDecimal("20000"));
        c.setEstado(estado);
        return c;
    }
}
