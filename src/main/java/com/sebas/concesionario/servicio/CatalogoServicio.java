package com.sebas.concesionario.servicio;

import com.sebas.concesionario.modelo.Color;
import com.sebas.concesionario.modelo.Marca;
import com.sebas.concesionario.modelo.Modelo;
import com.sebas.concesionario.repositorio.CatalogoRepositorio;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CatalogoServicio {

    private final CatalogoRepositorio repositorio;

    public CatalogoServicio(CatalogoRepositorio repositorio) {
        this.repositorio = repositorio;
    }

    public List<Marca> marcas() {
        return repositorio.marcas();
    }

    public List<Modelo> modelos() {
        return repositorio.modelos();
    }

    public List<Color> colores() {
        return repositorio.colores();
    }
}
