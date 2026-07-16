package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Proyecto;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Seccion;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.BusinessRuleException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.ProyectoRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.SeccionRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.SeccionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SeccionServiceImpl implements SeccionService {

    private static final int MAX_SECCIONES_POR_PROYECTO = 6;

    private final SeccionRepository seccionRepository;
    private final ProyectoRepository proyectoRepository;

    @Override
    public SeccionResponse crear(Integer idProyecto, SeccionRequest request) {
        Proyecto proyecto = proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> ResourceNotFoundException.of("Proyecto", idProyecto));

        if (seccionRepository.countByProyecto_IdProyecto(idProyecto) >= MAX_SECCIONES_POR_PROYECTO) {
            throw new BusinessRuleException(
                    "Un proyecto no puede tener mas de " + MAX_SECCIONES_POR_PROYECTO + " secciones");
        }

        Seccion seccion = Seccion.builder()
                .proyecto(proyecto)
                .nombre(request.nombre())
                .orden(request.orden())
                .build();

        return toResponse(seccionRepository.save(seccion));
    }

    @Override
    @Transactional(readOnly = true)
    public SeccionResponse obtenerPorId(Integer idSeccion) {
        return toResponse(buscarOrLanzar(idSeccion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SeccionResponse> listarPorProyecto(Integer idProyecto) {
        return seccionRepository.findByProyecto_IdProyectoOrderByOrdenAsc(idProyecto).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SeccionResponse actualizar(Integer idSeccion, SeccionRequest request) {
        Seccion seccion = buscarOrLanzar(idSeccion);

        seccion.setNombre(request.nombre());
        seccion.setOrden(request.orden());

        return toResponse(seccionRepository.save(seccion));
    }

    @Override
    public void eliminar(Integer idSeccion) {
        Seccion seccion = buscarOrLanzar(idSeccion);

        long totalSecciones = seccionRepository.countByProyecto_IdProyecto(seccion.getProyecto().getIdProyecto());
        if (totalSecciones <= 1) {
            throw new BusinessRuleException("Un proyecto debe tener al menos una seccion");
        }

        seccionRepository.delete(seccion);
    }

    private Seccion buscarOrLanzar(Integer idSeccion) {
        return seccionRepository.findById(idSeccion)
                .orElseThrow(() -> ResourceNotFoundException.of("Seccion", idSeccion));
    }

    private SeccionResponse toResponse(Seccion seccion) {
        return new SeccionResponse(
                seccion.getIdSeccion(),
                seccion.getProyecto().getIdProyecto(),
                seccion.getNombre(),
                seccion.getOrden()
        );
    }
}
