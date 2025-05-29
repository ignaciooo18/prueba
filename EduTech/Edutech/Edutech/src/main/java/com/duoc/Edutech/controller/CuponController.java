package com.duoc.Edutech.controller;
import java.time.LocalDate;
import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.repository.CuponRepository;
import com.duoc.Edutech.services.CuponServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Random;

@RestController
@RequestMapping("/api/Cupon")
public class CuponController {

    @Autowired
    private CuponServices cuponServices;
    @Autowired
    private CuponRepository cuponRepository;

    @PostMapping("/crearCupon")
public ResponseEntity<Cupon> crearCupon(@RequestBody Cupon cupon) {

    LocalDate fechaVencimiento = LocalDate.now().plusDays(5);
    cupon.setFecha_vencimiento(fechaVencimiento);

    String caracteres = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    StringBuilder codigoBuilder = new StringBuilder();
    Random random = new Random();
    
    for (int i = 0; i < 6; i++) {
        codigoBuilder.append(caracteres.charAt(random.nextInt(caracteres.length())));
    }
    cupon.setCodigo(codigoBuilder.toString());

    if (fechaVencimiento.isAfter(LocalDate.now())) {
        cupon.setEstado("vigente");
    } else {
        cupon.setEstado("vencido");
    }

    return ResponseEntity.ok(cuponServices.save(cupon));
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