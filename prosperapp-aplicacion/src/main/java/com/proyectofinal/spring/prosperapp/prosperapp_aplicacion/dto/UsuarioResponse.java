package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import java.time.LocalDate;

public record UsuarioResponse(
        Integer idUsuario,
        String nombre,
        String correo,
        LocalDate fechaRegistro
) {
}
