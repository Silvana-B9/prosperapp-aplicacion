package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.DecisionTecnica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DecisionTecnicaRepository extends JpaRepository<DecisionTecnica, Integer> {

    List<DecisionTecnica> findByFuncionalidad_IdFuncionalidad(Integer idFuncionalidad);
}
