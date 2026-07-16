package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Seccion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeccionRepository extends JpaRepository<Seccion, Integer> {

    List<Seccion> findByProyecto_IdProyectoOrderByOrdenAsc(Integer idProyecto);

    long countByProyecto_IdProyecto(Integer idProyecto);
}
