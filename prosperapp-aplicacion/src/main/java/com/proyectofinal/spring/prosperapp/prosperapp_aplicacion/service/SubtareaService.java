package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaResponse;

import java.util.List;

public interface SubtareaService {

    SubtareaResponse crear(Integer idFuncionalidad, SubtareaRequest request);

    SubtareaResponse obtenerPorId(Integer idSubtarea);

    List<SubtareaResponse> listarPorFuncionalidad(Integer idFuncionalidad);

    SubtareaResponse actualizar(Integer idSubtarea, SubtareaRequest request);

    SubtareaResponse marcarCompletada(Integer idSubtarea, boolean completada);

    void eliminar(Integer idSubtarea);
}
