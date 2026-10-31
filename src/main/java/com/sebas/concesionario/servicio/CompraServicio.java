package com.sebas.concesionario.servicio;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.Compra;
import com.sebas.concesionario.repositorio.CocheRepositorio;
import com.sebas.concesionario.repositorio.CompraRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CompraServicio {

    private final CocheRepositorio coches;
    private final CompraRepositorio compras;

    public CompraServicio(CocheRepositorio coches, CompraRepositorio compras) {
        this.coches = coches;
        this.compras = compras;
    }

    /**
     * Compra un coche: lo marca como COMPRADO y registra la compra.
     *
     * Las dos operaciones van en la misma transacción (@Transactional): si falla el INSERT
     * de la compra, también se deshace el cambio de estado (rollback). Es el equivalente a
     * setAutoCommit(false) + commit()/rollback() con JDBC a mano.
     */
    @Transactional
    public Coche comprar(String bastidor, int usuarioId) {
        Coche coche = coches.buscarPorBastidor(bastidor)
                .orElseThrow(() -> new NoEncontradoException("No existe ningún coche con bastidor " + bastidor));
        if (!coche.isDisponible()) {
            throw new NegocioException("Este coche ya está vendido");
        }
        // El UPDATE solo afecta si el coche sigue DISPONIBLE: evita vender dos veces el mismo
        // coche si dos clientes pulsan "Comprar" a la vez
        if (coches.marcarComprado(bastidor) == 0) {
            throw new NegocioException("Este coche se acaba de vender");
        }
        compras.insertar(bastidor, usuarioId, coche.getPrecio());
        return coche;
    }

    @Transactional(readOnly = true)
    public List<Compra> deUsuario(int usuarioId) {
        return compras.listarPorUsuario(usuarioId);
    }
}
