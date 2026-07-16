package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.NotaDiseno;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.NotaDisenoRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.NotaDisenoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class NotaDisenoServiceImpl implements NotaDisenoService {

    private final NotaDisenoRepository notaDisenoRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    @Override
    public NotaDisenoResponse crear(Integer idFuncionalidad, NotaDisenoRequest request) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));

        NotaDiseno nota = NotaDiseno.builder()
                .funcionalidad(funcionalidad)
                .contenido(request.contenido())
                .build();

        return toResponse(notaDisenoRepository.save(nota));
    }

    @Override
    @Transactional(readOnly = true)
    public NotaDisenoResponse obtenerPorId(Integer idNota) {
        return toResponse(buscarOrLanzar(idNota));
    }

    @Override
    @Transactional(readOnly = true)
    public List<NotaDisenoResponse> listarPorFuncionalidad(Integer idFuncionalidad) {
        return notaDisenoRepository.findByFuncionalidad_IdFuncionalidad(idFuncionalidad).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public NotaDisenoResponse actualizar(Integer idNota, NotaDisenoRequest request) {
        NotaDiseno nota = buscarOrLanzar(idNota);
        nota.setContenido(request.contenido());
        return toResponse(notaDisenoRepository.save(nota));
    }

    @Override
    public void eliminar(Integer idNota) {
        notaDisenoRepository.delete(buscarOrLanzar(idNota));
    }

    private NotaDiseno buscarOrLanzar(Integer idNota) {
        return notaDisenoRepository.findById(idNota)
                .orElseThrow(() -> ResourceNotFoundException.of("NotaDiseno", idNota));
    }

    private NotaDisenoResponse toResponse(NotaDiseno nota) {
        return new NotaDisenoResponse(
                nota.getIdNota(),
                nota.getFuncionalidad().getIdFuncionalidad(),
                nota.getContenido(),
                nota.getFechaCreacion()
        );
    }
}
