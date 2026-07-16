package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import java.time.LocalDate;

public record NotaDisenoResponse(
        Integer idNota,
        Integer idFuncionalidad,
        String contenido,
        LocalDate fechaCreacion
) {
}
