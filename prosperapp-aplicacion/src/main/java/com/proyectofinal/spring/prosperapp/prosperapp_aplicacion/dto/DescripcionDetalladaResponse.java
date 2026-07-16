package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import java.time.LocalDate;

public record DescripcionDetalladaResponse(
        Integer idDescripcion,
        Integer idFuncionalidad,
        String contenido,
        LocalDate fechaCreacion
) {
}
