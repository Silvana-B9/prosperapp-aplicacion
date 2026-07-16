package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.RolColaborador;

import java.time.LocalDate;

public record ColaboradorResponse(
        Integer idProyecto,
        Integer idUsuario,
        String nombreUsuario,
        String correoUsuario,
        RolColaborador rol,
        LocalDate fechaIngreso
) {
}
