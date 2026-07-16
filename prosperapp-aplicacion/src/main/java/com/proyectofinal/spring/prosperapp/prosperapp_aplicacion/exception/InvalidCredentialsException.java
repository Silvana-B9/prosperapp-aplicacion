package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception;

public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String mensaje) {
        super(mensaje);
    }
}
