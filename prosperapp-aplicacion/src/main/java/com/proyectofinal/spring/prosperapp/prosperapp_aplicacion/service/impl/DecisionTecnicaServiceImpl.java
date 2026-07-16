package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.DecisionTecnica;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.DecisionTecnicaRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.DecisionTecnicaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DecisionTecnicaServiceImpl implements DecisionTecnicaService {

    private final DecisionTecnicaRepository decisionTecnicaRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    @Override
    public DecisionTecnicaResponse crear(Integer idFuncionalidad, DecisionTecnicaRequest request) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));

        DecisionTecnica decision = DecisionTecnica.builder()
                .funcionalidad(funcionalidad)
                .titulo(request.titulo())
                .justificacion(request.justificacion())
                .build();

        return toResponse(decisionTecnicaRepository.save(decision));
    }

    @Override
    @Transactional(readOnly = true)
    public DecisionTecnicaResponse obtenerPorId(Integer idDecision) {
        return toResponse(buscarOrLanzar(idDecision));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DecisionTecnicaResponse> listarPorFuncionalidad(Integer idFuncionalidad) {
        return decisionTecnicaRepository.findByFuncionalidad_IdFuncionalidad(idFuncionalidad).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DecisionTecnicaResponse actualizar(Integer idDecision, DecisionTecnicaRequest request) {
        DecisionTecnica decision = buscarOrLanzar(idDecision);
        decision.setTitulo(request.titulo());
        decision.setJustificacion(request.justificacion());
        return toResponse(decisionTecnicaRepository.save(decision));
    }

    @Override
    public void eliminar(Integer idDecision) {
        decisionTecnicaRepository.delete(buscarOrLanzar(idDecision));
    }

    private DecisionTecnica buscarOrLanzar(Integer idDecision) {
        return decisionTecnicaRepository.findById(idDecision)
                .orElseThrow(() -> ResourceNotFoundException.of("DecisionTecnica", idDecision));
    }

    private DecisionTecnicaResponse toResponse(DecisionTecnica decision) {
        return new DecisionTecnicaResponse(
                decision.getIdDecision(),
                decision.getFuncionalidad().getIdFuncionalidad(),
                decision.getTitulo(),
                decision.getJustificacion(),
                decision.getFechaCreacion()
        );
    }
}
