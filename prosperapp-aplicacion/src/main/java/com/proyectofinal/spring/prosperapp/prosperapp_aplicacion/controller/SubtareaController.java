package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.SubtareaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.SubtareaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class SubtareaController {

    private final SubtareaService subtareaService;

    @PostMapping("/api/funcionalidades/{idFuncionalidad}/subtareas")
    public ResponseEntity<SubtareaResponse> crear(@PathVariable Integer idFuncionalidad,
                                                   @Valid @RequestBody SubtareaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(subtareaService.crear(idFuncionalidad, request));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}/subtareas")
    public ResponseEntity<List<SubtareaResponse>> listarPorFuncionalidad(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(subtareaService.listarPorFuncionalidad(idFuncionalidad));
    }

    @GetMapping("/api/subtareas/{idSubtarea}")
    public ResponseEntity<SubtareaResponse> obtenerPorId(@PathVariable Integer idSubtarea) {
        return ResponseEntity.ok(subtareaService.obtenerPorId(idSubtarea));
    }

    @PutMapping("/api/subtareas/{idSubtarea}")
    public ResponseEntity<SubtareaResponse> actualizar(@PathVariable Integer idSubtarea,
                                                        @Valid @RequestBody SubtareaRequest request) {
        return ResponseEntity.ok(subtareaService.actualizar(idSubtarea, request));
    }

    @PatchMapping("/api/subtareas/{idSubtarea}/completar")
    public ResponseEntity<SubtareaResponse> marcarCompletada(@PathVariable Integer idSubtarea,
                                                              @RequestParam(defaultValue = "true") boolean completada) {
        return ResponseEntity.ok(subtareaService.marcarCompletada(idSubtarea, completada));
    }

    @DeleteMapping("/api/subtareas/{idSubtarea}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idSubtarea) {
        subtareaService.eliminar(idSubtarea);
        return ResponseEntity.noContent().build();
    }
}
