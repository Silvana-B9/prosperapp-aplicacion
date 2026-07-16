package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.NotaDisenoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.NotaDisenoService;
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
public class NotaDisenoController {

    private final NotaDisenoService notaDisenoService;

    @PostMapping("/api/funcionalidades/{idFuncionalidad}/notas")
    public ResponseEntity<NotaDisenoResponse> crear(@PathVariable Integer idFuncionalidad,
                                                     @Valid @RequestBody NotaDisenoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(notaDisenoService.crear(idFuncionalidad, request));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}/notas")
    public ResponseEntity<List<NotaDisenoResponse>> listarPorFuncionalidad(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(notaDisenoService.listarPorFuncionalidad(idFuncionalidad));
    }

    @GetMapping("/api/notas/{idNota}")
    public ResponseEntity<NotaDisenoResponse> obtenerPorId(@PathVariable Integer idNota) {
        return ResponseEntity.ok(notaDisenoService.obtenerPorId(idNota));
    }

    @PutMapping("/api/notas/{idNota}")
    public ResponseEntity<NotaDisenoResponse> actualizar(@PathVariable Integer idNota,
                                                          @Valid @RequestBody NotaDisenoRequest request) {
        return ResponseEntity.ok(notaDisenoService.actualizar(idNota, request));
    }

    @DeleteMapping("/api/notas/{idNota}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idNota) {
        notaDisenoService.eliminar(idNota);
        return ResponseEntity.noContent().build();
    }
}
