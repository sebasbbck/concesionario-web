package com.sebas.concesionario.servicio;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.modelo.FiltroCoches;
import com.sebas.concesionario.repositorio.CatalogoRepositorio;
import com.sebas.concesionario.repositorio.CocheRepositorio;
import com.sebas.concesionario.repositorio.CompraRepositorio;
import java.util.List;
import java.util.Locale;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CocheServicio {

    private final CocheRepositorio coches;
    private final CatalogoRepositorio catalogo;
    private final CompraRepositorio compras;

    public CocheServicio(CocheRepositorio coches, CatalogoRepositorio catalogo, CompraRepositorio compras) {
        this.coches = coches;
        this.catalogo = catalogo;
        this.compras = compras;
    }

    public List<Coche> buscar(FiltroCoches filtro) {
        return coches.buscar(filtro);
    }

    public Coche obtener(String bastidor) {
        return coches.buscarPorBastidor(bastidor)
                .orElseThrow(() -> new NoEncontradoException("No existe ningún coche con bastidor " + bastidor));
    }

    @Transactional
    public void crear(Coche coche, int adminId) {
        normalizar(coche);
        if (coches.existeBastidor(coche.getBastidor())) {
            throw new NegocioException("Ya hay un coche con el bastidor " + coche.getBastidor());
        }
        validarComun(coche, null);
        coches.insertar(coche, adminId);
    }

    @Transactional
    public void actualizar(String bastidorOriginal, Coche coche) {
        normalizar(coche);
        obtener(bastidorOriginal);
        if (!bastidorOriginal.equals(coche.getBastidor()) && coches.existeBastidor(coche.getBastidor())) {
            throw new NegocioException("Ya hay un coche con el bastidor " + coche.getBastidor());
        }
        validarComun(coche, bastidorOriginal);
        coches.actualizar(bastidorOriginal, coche);
    }

    @Transactional
    public void borrar(String bastidor) {
        obtener(bastidor);
        if (compras.existe(bastidor)) {
            throw new NegocioException("No se puede borrar un coche vendido: tiene una compra asociada");
        }
        coches.borrar(bastidor);
    }

    private void validarComun(Coche coche, String bastidorOriginal) {
        if (coches.existeMatricula(coche.getMatricula(), bastidorOriginal)) {
            throw new NegocioException("La matrícula " + coche.getMatricula() + " ya está registrada");
        }
        if (!catalogo.existeModelo(coche.getModeloId())) {
            throw new NegocioException("El modelo seleccionado no existe");
        }
        if (!catalogo.existeColor(coche.getColorId())) {
            throw new NegocioException("El color seleccionado no existe");
        }
    }

    private static void normalizar(Coche coche) {
        coche.setBastidor(coche.getBastidor().trim().toUpperCase(Locale.ROOT));
        coche.setMatricula(coche.getMatricula().replace(" ", "").replace("-", "").toUpperCase(Locale.ROOT));
    }
}
