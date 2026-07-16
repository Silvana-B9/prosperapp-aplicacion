package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record NotaDisenoRequest(
        @NotBlank(message = "El contenido es obligatorio")
        @Size(max = 1000, message = "El contenido no puede superar 1000 caracteres")
        String contenido
) {
}
