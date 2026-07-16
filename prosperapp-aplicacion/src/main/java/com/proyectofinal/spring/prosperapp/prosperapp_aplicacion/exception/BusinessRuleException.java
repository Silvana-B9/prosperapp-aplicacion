package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String mensaje) {
        super(mensaje);
    }
}
