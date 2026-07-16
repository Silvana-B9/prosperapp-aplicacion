package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.FragmentoCodigo;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FragmentoCodigoRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.FuncionalidadRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.FragmentoCodigoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class FragmentoCodigoServiceImpl implements FragmentoCodigoService {

    private final FragmentoCodigoRepository fragmentoCodigoRepository;
    private final FuncionalidadRepository funcionalidadRepository;

    @Override
    public FragmentoCodigoResponse crear(Integer idFuncionalidad, FragmentoCodigoRequest request) {
        Funcionalidad funcionalidad = funcionalidadRepository.findById(idFuncionalidad)
                .orElseThrow(() -> ResourceNotFoundException.of("Funcionalidad", idFuncionalidad));

        FragmentoCodigo fragmento = FragmentoCodigo.builder()
                .funcionalidad(funcionalidad)
                .lenguaje(request.lenguaje())
                .codigo(request.codigo())
                .build();

        return toResponse(fragmentoCodigoRepository.save(fragmento));
    }

    @Override
    @Transactional(readOnly = true)
    public FragmentoCodigoResponse obtenerPorId(Integer idFragmento) {
        return toResponse(buscarOrLanzar(idFragmento));
    }

    @Override
    @Transactional(readOnly = true)
    public List<FragmentoCodigoResponse> listarPorFuncionalidad(Integer idFuncionalidad) {
        return fragmentoCodigoRepository.findByFuncionalidad_IdFuncionalidad(idFuncionalidad).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public FragmentoCodigoResponse actualizar(Integer idFragmento, FragmentoCodigoRequest request) {
        FragmentoCodigo fragmento = buscarOrLanzar(idFragmento);
        fragmento.setLenguaje(request.lenguaje());
        fragmento.setCodigo(request.codigo());
        return toResponse(fragmentoCodigoRepository.save(fragmento));
    }

    @Override
    public void eliminar(Integer idFragmento) {
        fragmentoCodigoRepository.delete(buscarOrLanzar(idFragmento));
    }

    private FragmentoCodigo buscarOrLanzar(Integer idFragmento) {
        return fragmentoCodigoRepository.findById(idFragmento)
                .orElseThrow(() -> ResourceNotFoundException.of("FragmentoCodigo", idFragmento));
    }

    private FragmentoCodigoResponse toResponse(FragmentoCodigo fragmento) {
        return new FragmentoCodigoResponse(
                fragmento.getIdFragmento(),
                fragmento.getFuncionalidad().getIdFuncionalidad(),
                fragmento.getLenguaje(),
                fragmento.getCodigo()
        );
    }
}
