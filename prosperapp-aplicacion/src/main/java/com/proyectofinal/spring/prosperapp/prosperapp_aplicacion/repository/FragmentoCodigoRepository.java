package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.FragmentoCodigo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FragmentoCodigoRepository extends JpaRepository<FragmentoCodigo, Integer> {

    List<FragmentoCodigo> findByFuncionalidad_IdFuncionalidad(Integer idFuncionalidad);
}
