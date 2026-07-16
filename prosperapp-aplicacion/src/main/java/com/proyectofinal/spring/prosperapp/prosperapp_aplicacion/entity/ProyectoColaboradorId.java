package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Embeddable
@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class ProyectoColaboradorId implements Serializable {

    @Column(name = "id_proyecto")
    private Integer idProyecto;

    @Column(name = "id_usuario")
    private Integer idUsuario;
}
