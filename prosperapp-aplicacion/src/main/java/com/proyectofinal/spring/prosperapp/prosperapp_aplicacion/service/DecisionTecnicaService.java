package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaResponse;

import java.util.List;

public interface DecisionTecnicaService {

    DecisionTecnicaResponse crear(Integer idFuncionalidad, DecisionTecnicaRequest request);

    DecisionTecnicaResponse obtenerPorId(Integer idDecision);

    List<DecisionTecnicaResponse> listarPorFuncionalidad(Integer idFuncionalidad);

    DecisionTecnicaResponse actualizar(Integer idDecision, DecisionTecnicaRequest request);

    void eliminar(Integer idDecision);
}
