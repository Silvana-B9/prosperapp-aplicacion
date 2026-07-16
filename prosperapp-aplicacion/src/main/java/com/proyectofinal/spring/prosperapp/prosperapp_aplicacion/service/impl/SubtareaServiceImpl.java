package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Subtarea;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.SubtareaRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.SubtareaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubtareaServiceImpl implements SubtareaService {

    private final SubtareaRepository subtareaRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    @Override
    public SubtareaResponse crear(Integer idFuncionalidad, SubtareaRequest request) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));

        Subtarea subtarea = Subtarea.builder()
                .funcionalidad(funcionalidad)
                .descripcion(request.descripcion())
                .completada(request.completada() != null ? request.completada() : Boolean.FALSE)
                .build();

        return toResponse(subtareaRepository.save(subtarea));
    }

    @Override
    @Transactional(readOnly = true)
    public SubtareaResponse obtenerPorId(Integer idSubtarea) {
        return toResponse(buscarOrLanzar(idSubtarea));
    }

    @Override
    @Transactional(readOnly = true)
    public List<SubtareaResponse> listarPorFuncionalidad(Integer idFuncionalidad) {
        return subtareaRepository.findByFuncionalidad_IdFuncionalidad(idFuncionalidad).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SubtareaResponse actualizar(Integer idSubtarea, SubtareaRequest request) {
        Subtarea subtarea = buscarOrLanzar(idSubtarea);
        subtarea.setDescripcion(request.descripcion());
        if (request.completada() != null) {
            subtarea.setCompletada(request.completada());
        }
        return toResponse(subtareaRepository.save(subtarea));
    }

    @Override
    public SubtareaResponse marcarCompletada(Integer idSubtarea, boolean completada) {
        Subtarea subtarea = buscarOrLanzar(idSubtarea);
        subtarea.setCompletada(completada);
        return toResponse(subtareaRepository.save(subtarea));
    }

    @Override
    public void eliminar(Integer idSubtarea) {
        subtareaRepository.delete(buscarOrLanzar(idSubtarea));
    }

    private Subtarea buscarOrLanzar(Integer idSubtarea) {
        return subtareaRepository.findById(idSubtarea)
                .orElseThrow(() -> ResourceNotFoundException.of("Subtarea", idSubtarea));
    }

    private SubtareaResponse toResponse(Subtarea subtarea) {
        return new SubtareaResponse(
                subtarea.getIdSubtarea(),
                subtarea.getFuncionalidad().getIdFuncionalidad(),
                subtarea.getDescripcion(),
                subtarea.getCompletada()
        );
    }
}
