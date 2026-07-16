package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoResponse;

import java.util.List;

public interface FragmentoCodigoService {

    FragmentoCodigoResponse crear(Integer idFuncionalidad, FragmentoCodigoRequest request);

    FragmentoCodigoResponse obtenerPorId(Integer idFragmento);

    List<FragmentoCodigoResponse> listarPorFuncionalidad(Integer idFuncionalidad);

    FragmentoCodigoResponse actualizar(Integer idFragmento, FragmentoCodigoRequest request);

    void eliminar(Integer idFragmento);
}
