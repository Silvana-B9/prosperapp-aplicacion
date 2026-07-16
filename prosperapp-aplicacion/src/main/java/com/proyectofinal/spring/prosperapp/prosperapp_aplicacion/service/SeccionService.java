package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionResponse;

import java.util.List;

public interface SeccionService {

    SeccionResponse crear(Integer idProyecto, SeccionRequest request);

    SeccionResponse obtenerPorId(Integer idSeccion);

    List<SeccionResponse> listarPorProyecto(Integer idProyecto);

    SeccionResponse actualizar(Integer idSeccion, SeccionRequest request);

    void eliminar(Integer idSeccion);
}
