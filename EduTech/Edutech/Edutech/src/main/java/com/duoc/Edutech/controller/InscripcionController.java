package com.duoc.Edutech.controller;

import com.duoc.Edutech.model.Inscripcion;
import com.duoc.Edutech.services.InscripcionServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/Inscripcion")
public class InscripcionController {
    @Autowired
    private InscripcionServices inscripcionServices;

    @PostMapping("/crear")
    public ResponseEntity<Inscripcion> CrearInscripcion(@RequestBody Inscripcion inscripcion) {
        inscripcion.setFechaInscripcion(LocalDate.now());
        return ResponseEntity.ok(inscripcionServices.save(inscripcion));
    }

    @PostMapping("/borrar/{idInscripcion}")
    public ResponseEntity<Inscripcion> deleteById(@PathVariable Integer idInscripcion) {
        Inscripcion inscripcion = new Inscripcion();
        inscripcion.setIdInscripcion(idInscripcion);
        return ResponseEntity.ok(inscripcionServices.deleteById(inscripcion));
    }
    @GetMapping("/buscarporrut/{rut}")
    public ResponseEntity<String> buscarporrut(@PathVariable String rut) {
        return ResponseEntity.ok(inscripcionServices.buscarporrut(rut));
    }
}