package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String mensaje) {
        super(mensaje);
    }

    public static ResourceNotFoundException of(String entidad, Object id) {
        return new ResourceNotFoundException(entidad + " no encontrado(a) con id: " + id);
    }
}
