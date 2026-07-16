package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Seccion;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.SeccionRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.FuncionalidadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FuncionalidadServiceImpl implements FuncionalidadService {

    private final FuncionalidadRepository funcionalidadRepository;
    private final SeccionRepository seccionRepository;

    @Override
    public FuncionalidadResponse crear(Integer idSeccion, FuncionalidadRequest request) {
        Seccion seccion = seccionRepository.findById(idSeccion)
                .orElseThrow(() -> ResourceNotFoundException.of("Seccion", idSeccion));

        Funcionalidad funcionalidad = Funcionalidad.builder()
                .seccion(seccion)
                .titulo(request.titulo())
                .historiaUsuario(request.historiaUsuario())
                .prioridad(request.prioridad() != null ? request.prioridad() : 3)
                .build();

        return toResponse(funcionalidadRepository.save(funcionalidad));
    }

    @Override
    @Transactional(readOnly = true)
    public FuncionalidadResponse obtenerPorId(Integer idFuncionalidad) {
        return toResponse(buscarOrLanzar(idFuncionalidad));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FuncionalidadResponse> listarPorSeccion(Integer idSeccion) {
        return funcionalidadRepository.findBySeccion_IdSeccion(idSeccion).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FuncionalidadResponse actualizar(Integer idFuncionalidad, FuncionalidadRequest request) {
        Funcionalidad funcionalidad = buscarOrLanzar(idFuncionalidad);

        funcionalidad.setTitulo(request.titulo());
        funcionalidad.setHistoriaUsuario(request.historiaUsuario());
        if (request.prioridad() != null) {
            funcionalidad.setPrioridad(request.prioridad());
        }

        return toResponse(funcionalidadRepository.save(funcionalidad));
    }

    @Override
    public void eliminar(Integer idFuncionalidad) {
        Funcionalidad funcionalidad = buscarOrLanzar(idFuncionalidad);
        funcionalidadRepository.delete(funcionalidad);
    }

    private Funcionalidad buscarOrLanzar(Integer idFuncionalidad) {
        return funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));
    }

    private FuncionalidadResponse toResponse(Funcionalidad funcionalidad) {
        return new FuncionalidadResponse(
                funcionalidad.getIdFuncionalidad(),
                funcionalidad.getSeccion().getIdSeccion(),
                funcionalidad.getTitulo(),
                funcionalidad.getHistoriaUsuario(),
                funcionalidad.getPrioridad(),
                funcionalidad.getFechaCreacion()
        );
    }
}
