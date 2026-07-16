package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

public record SubtareaResponse(
        Integer idSubtarea,
        Integer idFuncionalidad,
        String descripcion,
        Boolean completada
) {
}
