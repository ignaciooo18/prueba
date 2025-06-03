package com.duoc.Edutech.controller;

import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.InscripcionRepository;
import com.duoc.Edutech.services.CuponServices;
import com.duoc.Edutech.services.PagoServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/pagoEdutech")
public class PagoController {

    @Autowired
    private PagoServices pagoServices;
    
    @Autowired
    private InscripcionRepository inscripcionRepository;
    
    @Autowired
    private CuponServices cuponServices;

    @GetMapping("/consultarpago/{idpago}")
    public ResponseEntity<?> consultarpago(@PathVariable Integer idpago) {
        return pagoServices.findById(idpago)
                .map(pago -> ResponseEntity.ok(pago))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/eliminarpago/{idpago}")
    public ResponseEntity<?> eliminarpago(@PathVariable Integer idpago) {
        String resultado = pagoServices.eliminarPago(idpago);
        return resultado != null ? 
            ResponseEntity.ok(resultado) : 
            ResponseEntity.notFound().build();
    }

    @PostMapping("/pagarconcupon/{idInscripcion}/{idCupon}")
    public ResponseEntity<?> pagarConCupon(
            @PathVariable Integer idInscripcion,
            @PathVariable Integer idCupon,
            @RequestBody Pago pagoNuevo) {
        
        Map<String, Object> resultado = pagoServices.pagarConCupon(idInscripcion, idCupon, pagoNuevo);
        
        if (resultado.containsKey("error")) {
            return ResponseEntity.badRequest().body(resultado);
        }
        
        return ResponseEntity.ok(resultado);
    }

    @PostMapping("/pagarsincupon/{idInscripcion}")
    public ResponseEntity<?> pagarcurso(
            @PathVariable Integer idInscripcion,
            @RequestBody Pago pagoNuevo) {
        
        Pago pagoRealizado = pagoServices.pagarSinCupon(idInscripcion, pagoNuevo);
        
        if (pagoRealizado == null) {
            return ResponseEntity.badRequest().body("Error al procesar el pago. Verifique los datos.");
        }
        
        return ResponseEntity.ok(pagoRealizado);
    }
}