package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.controller;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.DecisionTecnicaResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.DecisionTecnicaService;
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


/*Este controlador se encarga de manejar las operaciones de la entidad
Decisión técnica. su función es recibir las peticiones que llegan desde
el frontend y enviarlas al servicio correspondientes para que allí se 
realice la logica del negocio, en general todos los controladores llevan el mismo flujo
es decir, peticiones (request) desde frontend recibidas y manejadas por springboot, que este
a su vez solicita o ingresa información a la base de datos*/

@RestController
@RequiredArgsConstructor
public class DecisionTecnicaController {

    private final DecisionTecnicaService decisionTecnicaService;

    @PostMapping("/api/funcionalidades/{idFuncionalidad}/decisiones")
    public ResponseEntity<DecisionTecnicaResponse> crear(@PathVariable Integer idFuncionalidad,
                                                          @Valid @RequestBody DecisionTecnicaRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(decisionTecnicaService.crear(idFuncionalidad, request));
    }

    @GetMapping("/api/funcionalidades/{idFuncionalidad}/decisiones")
    public ResponseEntity<List<DecisionTecnicaResponse>> listarPorFuncionalidad(@PathVariable Integer idFuncionalidad) {
        return ResponseEntity.ok(decisionTecnicaService.listarPorFuncionalidad(idFuncionalidad));
    }

    @GetMapping("/api/decisiones/{idDecision}")
    public ResponseEntity<DecisionTecnicaResponse> obtenerPorId(@PathVariable Integer idDecision) {
        return ResponseEntity.ok(decisionTecnicaService.obtenerPorId(idDecision));
    }

    @PutMapping("/api/decisiones/{idDecision}")
    public ResponseEntity<DecisionTecnicaResponse> actualizar(@PathVariable Integer idDecision,
                                                               @Valid @RequestBody DecisionTecnicaRequest request) {
        return ResponseEntity.ok(decisionTecnicaService.actualizar(idDecision, request));
    }

    @DeleteMapping("/api/decisiones/{idDecision}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer idDecision) {
        decisionTecnicaService.eliminar(idDecision);
        return ResponseEntity.noContent().build();
    }
}
