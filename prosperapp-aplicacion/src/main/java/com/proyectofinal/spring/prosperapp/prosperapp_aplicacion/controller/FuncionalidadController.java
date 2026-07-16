package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FuncionalidadResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.FuncionalidadService;
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
public class FuncionalidadController {

    private final FuncionalidadService funcionalidadService;

    @PostMapping("/api/secciones/{idSeccion}/funcionalidades")
    public ResponseEntity<FuncionalidadResponse> crear(@PathVariable Integer idSeccion,
                                                        @Valid @RequestBody FuncionalidadRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(funcionalidadService.crear(idSeccion, request));
    }

    @GetMapping("/api/secciones/{idSeccion}/funcionalidades")
    public ResponseEntity<List<FuncionalidadResponse>> listarPorSeccion(@PathVariable Integer idSeccion) {
        return ResponseEntity.ok(funcionalidadService.listarPorSeccion(idSeccion));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}")
    public ResponseEntity<FuncionalidadResponse> obtenerPorId(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(funcionalidadService.obtenerPorId(idFuncionalidad));
    }

    @PutMapping("/api/funcionalidades/{idFuncionalidad}")
    public ResponseEntity<FuncionalidadResponse> actualizar(@PathVariable Integer idFuncionalidad,
                                                             @Valid @RequestBody FuncionalidadRequest request) {
        return ResponseEntity.ok(funcionalidadService.actualizar(idFuncionalidad, request));
    }

    @DeleteMapping("/api/funcionalidades/{idFuncionalidad}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idFuncionalidad) {
        funcionalidadService.eliminar(idFuncionalidad);
        return ResponseEntity.noContent().build();
    }
}
