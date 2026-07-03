package com.inserta.crudalumnos.repositorio;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inserta.crudalumnos.modelo.Alumno;
/**
 * JpaRepository<Alumno,String> 
 * Alumno es el modelo y el String es el tipo de la PK (nif)
 */

public interface AlumnoRepository extends JpaRepository<Alumno,String> {
    // GET R07 → /api/alumnos/consultaR/nombre
    // Que nombre esté como parte del dato ignorando MAY/MIN
    List<Alumno> findByNombreContainingIgnoreCase(String nombre);

    // Esto sería para buscar EXACTAMENTE un nombre
    // List<Alumno> findByNombre(String nombre);

    // GET R08 y R09 → /api/alumnos/consultar/genero
    List<Alumno> findByGenero(boolean genero);

    // GET R10 y R11 → /api/alumnos/consultar/contar/
    // countBy<campo> -> Saca el entero(largo) de contar registros de un campo
    long countByGenero(boolean genero); 
}
