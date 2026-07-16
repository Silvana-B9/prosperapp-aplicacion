package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadResponse;

import java.util.List;

public interface FuncionalidadService {

    FuncionalidadResponse crear(Integer idSeccion, FuncionalidadRequest request);

    FuncionalidadResponse obtenerPorId(Integer idFuncionalidad);

    List<FuncionalidadResponse> listarPorSeccion(Integer idSeccion);

    FuncionalidadResponse actualizar(Integer idFuncionalidad, FuncionalidadRequest request);

    void eliminar(Integer idFuncionalidad);
}
