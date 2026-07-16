package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.ProyectoColaborador;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.ProyectoColaboradorId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProyectoColaboradorRepository extends JpaRepository<ProyectoColaborador, ProyectoColaboradorId> {

    List<ProyectoColaborador> findByProyecto_IdProyecto(Integer idProyecto);

    List<ProyectoColaborador> findByUsuario_IdUsuario(Integer idUsuario);

    boolean existsByProyecto_IdProyectoAndUsuario_IdUsuario(Integer idProyecto, Integer idUsuario);
}
