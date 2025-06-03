package com.duoc.Edutech.controller;
import java.time.LocalDate;
import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.repository.CuponRepository;
import com.duoc.Edutech.services.CuponServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Random;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/Cupon")
public class CuponController {

    @Autowired
    private CuponServices cuponServices;
    @Autowired
    private CuponRepository cuponRepository;

    @PostMapping("/crearCupon")
    public ResponseEntity<Cupon> crearCupon(@RequestBody Cupon cupon) {
        return ResponseEntity.ok(cuponServices.crearCupon(cupon));
    }

    @GetMapping("/buscarporid/{idCupon}")
    public ResponseEntity<?> buscarporid(@PathVariable Integer idCupon) {
        Map<String, Object> resultado = cuponServices.findByIdConpago(idCupon);
        if (resultado.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultado);
    }

    @DeleteMapping("/borrarCupon/{idCupon}")
    public ResponseEntity<?> deleteById(@PathVariable Integer idCupon) {
        return cuponServices.findById(idCupon).map(cupon -> {
            cuponServices.deleteById(idCupon);
            return ResponseEntity.ok("Cupon eliminado con éxito");
        }).orElseGet(() -> {
            return ResponseEntity.notFound().build();
        });
    }
}
