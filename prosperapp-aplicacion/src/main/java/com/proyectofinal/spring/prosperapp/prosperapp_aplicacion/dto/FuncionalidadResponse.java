package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import java.time.LocalDate;

public record FuncionalidadResponse(
        Integer idFuncionalidad,
        Integer idSeccion,
        String titulo,
        String historiaUsuario,
        Integer prioridad,
        LocalDate fechaCreacion
) {
}
