package com.inserta.crudalumnos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inserta.crudalumnos.modelo.Asignatura;

public interface AsignaturaRepository extends JpaRepository<Asignatura, Integer>{
    
}
