package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoResponse;

import java.util.List;

public interface NotaDisenoService {

    NotaDisenoResponse crear(Integer idFuncionalidad, NotaDisenoRequest request);

    NotaDisenoResponse obtenerPorId(Integer idNota);

    List<NotaDisenoResponse> listarPorFuncionalidad(Integer idFuncionalidad);

    NotaDisenoResponse actualizar(Integer idNota, NotaDisenoRequest request);

    void eliminar(Integer idNota);
}
