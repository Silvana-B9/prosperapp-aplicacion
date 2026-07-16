package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.ProyectoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProyectoController {

    private final ProyectoService proyectoService;

    @PostMapping("/api/usuarios/{idUsuario}/proyectos")
    public ResponseEntity<ProyectoResponse> crear(@PathVariable Integer idUsuario,
                                                   @Valid @RequestBody ProyectoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.crear(idUsuario, request));
    }

    @GetMapping("/api/usuarios/{idUsuario}/proyectos")
    public ResponseEntity<List<ProyectoResponse>> listarPorUsuario(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(proyectoService.listarPorUsuario(idUsuario));
    }

    @GetMapping("/api/proyectos")
    public ResponseEntity<List<ProyectoResponse>> listarTodos() {
        return ResponseEntity.ok(proyectoService.listarTodos());
    }

    @GetMapping("/api/proyectos/{idProyecto}")
    public ResponseEntity<ProyectoResponse> obtenerPorId(@PathVariable Integer idProyecto) {
        return ResponseEntity.ok(proyectoService.obtenerPorId(idProyecto));
    }

    @PutMapping("/api/proyectos/{idProyecto}")
    public ResponseEntity<ProyectoResponse> actualizar(@PathVariable Integer idProyecto,
                                                        @Valid @RequestBody ProyectoRequest request) {
        return ResponseEntity.ok(proyectoService.actualizar(idProyecto, request));
    }

    @DeleteMapping("/api/proyectos/{idProyecto}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idProyecto) {
        proyectoService.eliminar(idProyecto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/api/proyectos/{idProyecto}/colaboradores")
    public ResponseEntity<ColaboradorResponse> agregarColaborador(@PathVariable Integer idProyecto,
                                                                   @Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(proyectoService.agregarColaborador(idProyecto, request));
    }

    @GetMapping("/api/proyectos/{idProyecto}/colaboradores")
    public ResponseEntity<List<ColaboradorResponse>> listarColaboradores(@PathVariable Integer idProyecto) {
        return ResponseEntity.ok(proyectoService.listarColaboradores(idProyecto));
    }

    @PutMapping("/api/proyectos/{idProyecto}/colaboradores/{idUsuario}")
    public ResponseEntity<ColaboradorResponse> actualizarRolColaborador(@PathVariable Integer idProyecto,
                                                                         @PathVariable Integer idUsuario,
                                                                         @Valid @RequestBody ColaboradorRequest request) {
        return ResponseEntity.ok(proyectoService.actualizarRolColaborador(idProyecto, idUsuario, request));
    }

    @DeleteMapping("/api/proyectos/{idProyecto}/colaboradores/{idUsuario}")
    public ResponseEntity<Void> eliminarColaborador(@PathVariable Integer idProyecto, @PathVariable Integer idUsuario) {
        proyectoService.eliminarColaborador(idProyecto, idUsuario);
        return ResponseEntity.noContent().build();
    }
}
