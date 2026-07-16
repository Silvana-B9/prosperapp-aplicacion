package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record DecisionTecnicaRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 100, message = "El titulo no puede superar 100 caracteres")
        String titulo,

        @Size(max = 1000, message = "La justificacion no puede superar 1000 caracteres")
        String justificacion
) {
}
