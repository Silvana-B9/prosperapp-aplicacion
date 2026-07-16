package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.LoginRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.UsuarioService;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    public ResponseEntity<UsuarioResponse> crear(@Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(usuarioService.crear(request));
    }

    @PostMapping("/login")
    public ResponseEntity<UsuarioResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(usuarioService.login(request));
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponse>> listarTodos() {
        return ResponseEntity.ok(usuarioService.listarTodos());
    }

    @GetMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> obtenerPorId(@PathVariable Integer idUsuario) {
        return ResponseEntity.ok(usuarioService.obtenerPorId(idUsuario));
    }

    @PutMapping("/{idUsuario}")
    public ResponseEntity<UsuarioResponse> actualizar(@PathVariable Integer idUsuario,
                                                       @Valid @RequestBody UsuarioRequest request) {
        return ResponseEntity.ok(usuarioService.actualizar(idUsuario, request));
    }

    @DeleteMapping("/{idUsuario}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idUsuario) {
        usuarioService.eliminar(idUsuario);
        return ResponseEntity.noContent().build();
    }
}
