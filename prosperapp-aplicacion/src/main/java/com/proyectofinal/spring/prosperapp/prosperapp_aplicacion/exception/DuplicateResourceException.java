package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception;

public class DuplicateResourceException extends RuntimeException {

    public DuplicateResourceException(String mensaje) {
        super(mensaje);
    }
}
