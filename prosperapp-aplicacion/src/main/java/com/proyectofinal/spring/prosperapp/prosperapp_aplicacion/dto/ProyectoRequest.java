package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.EstadoProyecto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProyectoRequest(
        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripcion no puede superar 500 caracteres")
        String descripcion,

        EstadoProyecto estado
) {
}
