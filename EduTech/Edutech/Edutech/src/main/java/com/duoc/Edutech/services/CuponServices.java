package com.duoc.Edutech.services;

import com.duoc.Edutech.model.Cupon;
import com.duoc.Edutech.repository.CuponRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class CuponServices {

    @Autowired
    CuponRepository cuponRepository;

    public Cupon save (Cupon cupon) {
        return cuponRepository.save(cupon);
    }

    public void deleteById (Integer idcupon) {
         cuponRepository.deleteById(idcupon);
    }
    public Optional<Cupon> findById(Integer idcupon) {
        return cuponRepository.findById(idcupon);
    }
}
