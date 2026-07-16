package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DescripcionDetalladaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.DescripcionDetalladaService;
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
public class DescripcionDetalladaController {

    private final DescripcionDetalladaService descripcionDetalladaService;

    @PostMapping("/api/funcionalidades/{idFuncionalidad}/descripciones")
    public ResponseEntity<DescripcionDetalladaResponse> crear(@PathVariable Integer idFuncionalidad,
                                                                @Valid @RequestBody DescripcionDetalladaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(descripcionDetalladaService.crear(idFuncionalidad, request));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}/descripciones")
    public ResponseEntity<List<DescripcionDetalladaResponse>> listarPorFuncionalidad(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(descripcionDetalladaService.listarPorFuncionalidad(idFuncionalidad));
    }

    @GetMapping("/api/descripciones/{idDescripcion}")
    public ResponseEntity<DescripcionDetalladaResponse> obtenerPorId(@PathVariable Integer idDescripcion) {
        return ResponseEntity.ok(descripcionDetalladaService.obtenerPorId(idDescripcion));
    }

    @PutMapping("/api/descripciones/{idDescripcion}")
    public ResponseEntity<DescripcionDetalladaResponse> actualizar(@PathVariable Integer idDescripcion,
                                                                     @Valid @RequestBody DescripcionDetalladaRequest request) {
        return ResponseEntity.ok(descripcionDetalladaService.actualizar(idDescripcion, request));
    }

    @DeleteMapping("/api/descripciones/{idDescripcion}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idDescripcion) {
        descripcionDetalladaService.eliminar(idDescripcion);
        return ResponseEntity.noContent().build();
    }
}
