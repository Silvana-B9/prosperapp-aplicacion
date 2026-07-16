package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SubtareaRequest(
        @NotBlank(message = "La descripcion es obligatoria")
        @Size(max = 255, message = "La descripcion no puede superar 255 caracteres")
        String descripcion,

        Boolean completada
) {
}
