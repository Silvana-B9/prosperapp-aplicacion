package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Subtarea;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubtareaRepository extends JpaRepository<Subtarea, Integer> {

    List<Subtarea> findByFuncionalidad_IdFuncionalidad(Integer idFuncionalidad);
}
