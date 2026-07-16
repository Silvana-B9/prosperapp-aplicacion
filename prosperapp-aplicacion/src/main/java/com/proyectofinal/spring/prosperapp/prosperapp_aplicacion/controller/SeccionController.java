package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SeccionResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.SeccionService;
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
public class SeccionController {

    private final SeccionService seccionService;

    @PostMapping("/api/proyectos/{idProyecto}/secciones")
    public ResponseEntity<SeccionResponse> crear(@PathVariable Integer idProyecto,
                                                  @Valid @RequestBody SeccionRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(seccionService.crear(idProyecto, request));
    }

    @GetMapping("/api/proyectos/{idProyecto}/secciones")
    public ResponseEntity<List<SeccionResponse>> listarPorProyecto(@PathVariable Integer idProyecto) {
        return ResponseEntity.ok(seccionService.listarPorProyecto(idProyecto));
    }

    @GetMapping("/api/secciones/{idSeccion}")
    public ResponseEntity<SeccionResponse> obtenerPorId(@PathVariable Integer idSeccion) {
        return ResponseEntity.ok(seccionService.obtenerPorId(idSeccion));
    }

    @PutMapping("/api/secciones/{idSeccion}")
    public ResponseEntity<SeccionResponse> actualizar(@PathVariable Integer idSeccion,
                                                       @Valid @RequestBody SeccionRequest request) {
        return ResponseEntity.ok(seccionService.actualizar(idSeccion, request));
    }

    @DeleteMapping("/api/secciones/{idSeccion}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idSeccion) {
        seccionService.eliminar(idSeccion);
        return ResponseEntity.noContent().build();
    }
}
