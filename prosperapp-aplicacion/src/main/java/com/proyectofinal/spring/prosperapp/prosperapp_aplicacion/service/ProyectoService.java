package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoResponse;

import java.util.List;

public interface ProyectoService {

    ProyectoResponse crear(Integer idUsuario, ProyectoRequest request);

    ProyectoResponse obtenerPorId(Integer idProyecto);

    List<ProyectoResponse> listarPorUsuario(Integer idUsuario);

    List<ProyectoResponse> listarTodos();

    ProyectoResponse actualizar(Integer idProyecto, ProyectoRequest request);

    void eliminar(Integer idProyecto);

    ColaboradorResponse agregarColaborador(Integer idProyecto, ColaboradorRequest request);

    List<ColaboradorResponse> listarColaboradores(Integer idProyecto);

    ColaboradorResponse actualizarRolColaborador(Integer idProyecto, Integer idUsuario, ColaboradorRequest request);

    void eliminarColaborador(Integer idProyecto, Integer idUsuario);
}
