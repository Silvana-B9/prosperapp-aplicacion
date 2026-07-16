package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> detalles
) {
    public ApiError(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, List.of());
    }

    public ApiError(int status, String error, String message, String path, List<String> detalles) {
        this(LocalDateTime.now(), status, error, message, path, detalles);
    }
}
