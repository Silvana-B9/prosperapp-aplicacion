package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

public record SeccionResponse(
        Integer idSeccion,
        Integer idProyecto,
        String nombre,
        Integer orden
) {
}
