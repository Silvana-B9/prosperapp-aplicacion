package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "funcionalidad")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Funcionalidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_funcionalidad")
    private Integer idFuncionalidad;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_seccion", nullable = false)
    private Seccion seccion;

    @Column(name = "titulo", length = 100, nullable = false)
    private String titulo;

    @Column(name = "historia_usuario", length = 500)
    private String historiaUsuario;

    @Column(name = "prioridad", nullable = false)
    @Builder.Default
    private Integer prioridad = 3;

    @Column(name = "fecha_creacion", nullable = false)
    @Builder.Default
    private LocalDate fechaCreacion = LocalDate.now();
}
