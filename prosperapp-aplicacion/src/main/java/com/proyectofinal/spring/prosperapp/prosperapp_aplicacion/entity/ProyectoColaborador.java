package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "proyecto_colaborador")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProyectoColaborador {

    @EmbeddedId
    private ProyectoColaboradorId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idProyecto")
    @JoinColumn(name = "id_proyecto")
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idUsuario")
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(name = "rol", length = 20, nullable = false)
    @Builder.Default
    private RolColaborador rol = RolColaborador.colaborador;

    @Column(name = "fecha_ingreso", nullable = false)
    @Builder.Default
    private LocalDate fechaIngreso = LocalDate.now();
}
