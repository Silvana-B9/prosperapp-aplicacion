package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.DescripcionDetallada;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.DescripcionDetalladaRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.DescripcionDetalladaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DescripcionDetalladaServiceImpl implements DescripcionDetalladaService {

    private final DescripcionDetalladaRepository descripcionDetalladaRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    @Override
    public DescripcionDetalladaResponse crear(Integer idFuncionalidad, DescripcionDetalladaRequest request) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));

        DescripcionDetallada descripcion = DescripcionDetallada.builder()
                .funcionalidad(funcionalidad)
                .contenido(request.contenido())
                .build();

        return toResponse(descripcionDetalladaRepository.save(descripcion));
    }

    @Override
    @Transactional(readOnly = true)
    public DescripcionDetalladaResponse obtenerPorId(Integer idDescripcion) {
        return toResponse(buscarOrLanzar(idDescripcion));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescripcionDetalladaResponse> listarPorFuncionalidad(Integer idFuncionalidad) {
        return descripcionDetalladaRepository.findByFuncionalidad_IdFuncionalidad(idFuncionalidad).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public DescripcionDetalladaResponse actualizar(Integer idDescripcion, DescripcionDetalladaRequest request) {
        DescripcionDetallada descripcion = buscarOrLanzar(idDescripcion);
        descripcion.setContenido(request.contenido());
        return toResponse(descripcionDetalladaRepository.save(descripcion));
    }

    @Override
    public void eliminar(Integer idDescripcion) {
        descripcionDetalladaRepository.delete(buscarOrLanzar(idDescripcion));
    }

    private DescripcionDetallada buscarOrLanzar(Integer idDescripcion) {
        return descripcionDetalladaRepository.findById(idDescripcion)
                .orElseThrow(() -> ResourceNotFoundException.of("DescripcionDetallada", idDescripcion));
    }

    private DescripcionDetalladaResponse toResponse(DescripcionDetallada descripcion) {
        return new DescripcionDetalladaResponse(
                descripcion.getIdDescripcion(),
                descripcion.getFuncionalidad().getIdFuncionalidad(),
                descripcion.getContenido(),
                descripcion.getFechaCreacion()
        );
    }
}
