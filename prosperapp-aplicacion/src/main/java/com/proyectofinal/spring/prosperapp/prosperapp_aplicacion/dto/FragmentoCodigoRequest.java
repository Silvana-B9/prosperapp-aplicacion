package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record FragmentoCodigoRequest(
        @NotBlank(message = "El lenguaje es obligatorio")
        @Size(max = 30, message = "El lenguaje no puede superar 30 caracteres")
        String lenguaje,

        @NotBlank(message = "El codigo es obligatorio")
        @Size(max = 2000, message = "El codigo no puede superar 2000 caracteres")
        String codigo
) {
}
