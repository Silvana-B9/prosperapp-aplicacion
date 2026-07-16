package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaResponse;

import java.util.List;

public interface DescripcionDetalladaService {

    DescripcionDetalladaResponse crear(Integer idFuncionalidad, DescripcionDetalladaRequest request);

    DescripcionDetalladaResponse obtenerPorId(Integer idDescripcion);

    List<DescripcionDetalladaResponse> listarPorFuncionalidad(Integer idFuncionalidad);

    DescripcionDetalladaResponse actualizar(Integer idDescripcion, DescripcionDetalladaRequest request);

    void eliminar(Integer idDescripcion);
}
