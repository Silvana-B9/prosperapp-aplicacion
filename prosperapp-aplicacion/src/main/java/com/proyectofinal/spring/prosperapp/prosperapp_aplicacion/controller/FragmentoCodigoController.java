package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.FragmentoCodigoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.FragmentoCodigoService;
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
public class FragmentoCodigoController {

    private final FragmentoCodigoService fragmentoCodigoService;

    @PostMapping("/api/funcionalidades/{idFuncionalidad}/fragmentos")
    public ResponseEntity<FragmentoCodigoResponse> crear(@PathVariable Integer idFuncionalidad,
                                                          @Valid @RequestBody FragmentoCodigoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(fragmentoCodigoService.crear(idFuncionalidad, request));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}/fragmentos")
    public ResponseEntity<List<FragmentoCodigoResponse>> listarPorFuncionalidad(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(fragmentoCodigoService.listarPorFuncionalidad(idFuncionalidad));
    }

    @GetMapping("/api/fragmentos/{idFragmento}")
    public ResponseEntity<FragmentoCodigoResponse> obtenerPorId(@PathVariable Integer idFragmento) {
        return ResponseEntity.ok(fragmentoCodigoService.obtenerPorId(idFragmento));
    }

    @PutMapping("/api/fragmentos/{idFragmento}")
    public ResponseEntity<FragmentoCodigoResponse> actualizar(@PathVariable Integer idFragmento,
                                                               @Valid @RequestBody FragmentoCodigoRequest request) {
        return ResponseEntity.ok(fragmentoCodigoService.actualizar(idFragmento, request));
    }

    @DeleteMapping("/api/fragmentos/{idFragmento}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idFragmento) {
        fragmentoCodigoService.eliminar(idFragmento);
        return ResponseEntity.noContent().build();
    }
}
