package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto;

import java.time.LocalDate;

public record DecisionTecnicaResponse(
        Integer idDecision,
        Integer idFuncionalidad,
        String titulo,
        String justificacion,
        LocalDate fechaCreacion
) {
}
