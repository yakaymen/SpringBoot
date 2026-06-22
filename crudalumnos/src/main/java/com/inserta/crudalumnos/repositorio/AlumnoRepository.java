package com.inserta.crudalumnos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inserta.crudalumnos.modelo.Alumno;
/**
 * JpaRepository<Alumno,String> 
 * Alumno es el modelo y el String es el tipo de la PK (nif)
 */

public interface AlumnoRepository extends JpaRepository<Alumno,String> {
    // GET R07 → /api/alumnos/consulta/nombre
    // Que nombre esté como parte del dato ignorando MAY/MIN
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);
    
    // Esto sería para buscar EXACTAMENTE un nombre
    // List<Alumno> findByNombre(String nombre);
}
