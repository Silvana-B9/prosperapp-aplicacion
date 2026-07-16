package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Funcionalidad;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FuncionalidadRepository extends JpaRepository<Funcionalidad, Integer> {

    List<Funcionalidad> findBySeccion_IdSeccion(Integer idSeccion);
}
