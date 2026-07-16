package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.RolColaborador;
import jakarta.validation.constraints.NotNull;

public record ColaboradorRequest(
        @NotNull(message = "El id del usuario es obligatorio")
        Integer idUsuario,

        @NotNull(message = "El rol es obligatorio")
        RolColaborador rol
) {
}
