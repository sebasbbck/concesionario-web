package com.sebas.concesionario.servicio;

import com.sebas.concesionario.modelo.Coche;
import com.sebas.concesionario.repositorio.CocheRepositorio;
import com.sebas.concesionario.repositorio.FavoritoRepositorio;
import java.util.List;
import java.util.Set;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class FavoritoServicio {

    private final FavoritoRepositorio favoritos;
    private final CocheRepositorio coches;

    public FavoritoServicio(FavoritoRepositorio favoritos, CocheRepositorio coches) {
        this.favoritos = favoritos;
        this.coches = coches;
    }

    @Transactional(readOnly = true)
    public List<Coche> listar(int usuarioId) {
        return favoritos.listar(usuarioId);
    }

    @Transactional(readOnly = true)
    public Set<String> bastidores(int usuarioId) {
        return favoritos.bastidores(usuarioId);
    }

    /** Añade el coche a favoritos. Si ya estaba, no hace nada (idempotente). */
    public void anadir(int usuarioId, String bastidor) {
        if (coches.buscarPorBastidor(bastidor).isEmpty()) {
            throw new NoEncontradoException("No existe ningún coche con bastidor " + bastidor);
        }
        if (!favoritos.existe(usuarioId, bastidor)) {
            favoritos.anadir(usuarioId, bastidor);
        }
    }

    public void quitar(int usuarioId, String bastidor) {
        favoritos.quitar(usuarioId, bastidor);
    }
}
