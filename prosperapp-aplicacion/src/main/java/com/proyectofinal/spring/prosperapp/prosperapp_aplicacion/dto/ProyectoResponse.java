package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.EstadoProyecto;

import java.time.LocalDate;

public record ProyectoResponse(
        Integer idProyecto,
        Integer idUsuario,
        String nombre,
        String descripcion,
        LocalDate fechaCreacion,
        EstadoProyecto estado
) {
}
