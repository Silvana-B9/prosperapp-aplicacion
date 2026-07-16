package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.LoginRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse crear(UsuarioRequest request);

    UsuarioResponse login(LoginRequest request);

    UsuarioResponse obtenerPorId(Integer idUsuario);

    List<UsuarioResponse> listarTodos();

    UsuarioResponse actualizar(Integer idUsuario, UsuarioRequest request);

    void eliminar(Integer idUsuario);
}
