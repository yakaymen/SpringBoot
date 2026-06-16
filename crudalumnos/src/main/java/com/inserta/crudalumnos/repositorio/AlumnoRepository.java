package com.inserta.crudalumnos.repositorio;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inserta.crudalumnos.modelo.Alumno;
/**
 * JpaRepository<Alumno,String> 
 * Alumno es el modelo y el String es el tipo de la PK (nif)
 */

public interface AlumnoRepository extends JpaRepository<Alumno,String> {
    
}
