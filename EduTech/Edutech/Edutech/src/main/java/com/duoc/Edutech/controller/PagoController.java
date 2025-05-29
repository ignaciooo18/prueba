package com.duoc.Edutech.controller;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Optional;
import java.util.Map;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.services.CuponServices;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.duoc.Edutech.model.Pago;
import com.duoc.Edutech.repository.InscripcionRepository;
import com.duoc.Edutech.services.PagoServices;

@RestController
@RequestMapping("/api/pagoEdutech")
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
                .map(pago -> {
                    System.out.println("Pago encontrado: " + pago);
                    return ResponseEntity.ok(pago);
                })
                .orElseGet(() -> {
                    System.out.println("No se encontró pago con ID: " + idpago);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/eliminarpago/{idpago}")
    public ResponseEntity<?> eliminarpago(@PathVariable Integer idpago) {
        return pagoServices.findById(idpago)
                .map(pago -> {
                    pagoServices.deleteById(idpago);
                    return ResponseEntity.ok("Pago eliminado con éxito");
                })
                .orElseGet(() -> {
                    return ResponseEntity.notFound().build();
                });
    }
    @PostMapping("/pagarconcupon/{idInscripcion}/{idcupon}")
    public ResponseEntity<?> pagarcurso(@PathVariable Integer idInscripcion,
                                        @PathVariable Integer idcupon,
                                        @RequestBody Pago pagoNuevo) {
        return inscripcionRepository.findById(idInscripcion)
                .map(inscripcion -> {
                    Optional<Cupon> cupon = cuponServices.findById(idcupon);
                    if (cupon.isEmpty()) {
                        return ResponseEntity.badRequest().body("El cupon no existe");
                    }

                    Cupon cup = cupon.get();

                    if (cup.getEstado().equals("usado") || cup.getEstado().equals("canjeado")) {
                        return ResponseEntity.badRequest().body("El cupon esta vencido/canjeado");
                    }

                    if (pagoNuevo.getMontoPagos() == null) {
                        return ResponseEntity.badRequest().body("El monto a pagar es necesario");
                    }
                    pagoNuevo.setFecha_pago(LocalDate.now());
                    pagoNuevo.setInscripcion(inscripcion);
                    if (pagoNuevo.getMontoPagos() >= pagoNuevo.getMonto() - (pagoNuevo.getMonto() * cup.getDescuento() / 100)
                    ) {
                        pagoNuevo.setEstado("aprobado");
                        pagoNuevo.getInscripcion().setEstadoInscripcion("pagado");
                        cup.setEstado("usado");
                        cuponServices.save(cup);
                    } else {
                        pagoNuevo.setEstado("rechazado");
                        pagoNuevo.getInscripcion().setEstadoInscripcion("no pagado");
                    }


                    Pago pagoGuardado = pagoServices.save(pagoNuevo);
                    LinkedHashMap<String, Object> response = new LinkedHashMap<>();
                    response.put("descuento Aplicado", cup.getDescuento());
                    response.put("monto a pagar", pagoNuevo.getMonto());
                    response.put("monto final a pagar", pagoNuevo.getMonto() - (pagoNuevo.getMonto() * cup.getDescuento() / 100));
                    response.put("pago", pagoGuardado);

                    return ResponseEntity.ok(response);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/pagarsincupon/{idInscripcion}")
    public ResponseEntity<?> pagarcurso(@PathVariable Integer idInscripcion,
                                        @RequestBody Pago pagoNuevo) {
        return inscripcionRepository.findById(idInscripcion)
                .map(inscripcion -> {
                    if (pagoNuevo.getMontoPagos() == null) {
                        return ResponseEntity.badRequest().body("El monto a pagar es necesario");
                    }
                    pagoNuevo.setFecha_pago(LocalDate.now());
                    pagoNuevo.setInscripcion(inscripcion);
                    if (pagoNuevo.getMontoPagos() >= pagoNuevo.getMonto()) {
                        pagoNuevo.setEstado("aprobado");
                        pagoNuevo.getInscripcion().setEstadoInscripcion("pagado");
                    } else {
                        pagoNuevo.setEstado("rechazado");
                        pagoNuevo.getInscripcion().setEstadoInscripcion("no pagado");
                    }

                    Pago pagoGuardado = pagoServices.save(pagoNuevo);
                    return ResponseEntity.ok(pagoGuardado);
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}