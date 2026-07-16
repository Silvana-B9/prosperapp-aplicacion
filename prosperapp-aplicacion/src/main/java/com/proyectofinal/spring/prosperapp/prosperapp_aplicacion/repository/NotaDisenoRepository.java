package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.NotaDiseno;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotaDisenoRepository extends JpaRepository<NotaDiseno, Integer> {

    List<NotaDiseno> findByFuncionalidad_IdFuncionalidad(Integer idFuncionalidad);
}
