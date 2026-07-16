package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FuncionalidadRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 100, message = "El titulo no puede superar 100 caracteres")
        String titulo,

        @Size(max = 500, message = "La historia de usuario no puede superar 500 caracteres")
        String historiaUsuario,

        @Min(value = 1, message = "La prioridad minima es 1")
        @Max(value = 5, message = "La prioridad maxima es 5")
        Integer prioridad
) {
}
