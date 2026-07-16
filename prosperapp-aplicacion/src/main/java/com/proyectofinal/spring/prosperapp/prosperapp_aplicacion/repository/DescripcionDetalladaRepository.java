package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.DescripcionDetallada;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DescripcionDetalladaRepository extends JpaRepository<DescripcionDetallada, Integer> {

    List<DescripcionDetallada> findByFuncionalidad_IdFuncionalidad(Integer idFuncionalidad);
}
