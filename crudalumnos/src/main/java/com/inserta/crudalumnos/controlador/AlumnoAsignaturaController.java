package com.inserta.crudalumnos.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inserta.crudalumnos.repositorio.AlumnoAsignaturaRepository;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.transaction.Transactional;

@RestController
@RequestMapping("/api/alumnos-asignaturas")
public class AlumnoAsignaturaController {
    // Agregamos el repo
    private final AlumnoAsignaturaRepository alumnoAsignaturaRepo;

    // Constructor del repo
    public AlumnoAsignaturaController(
            AlumnoAsignaturaRepository alumnoAsignaturaRepo) {
        this.alumnoAsignaturaRepo = alumnoAsignaturaRepo;
    }

    // Paso1. Vamos a borrar todas las asignaturas de un alumno
    // Paso2. Ya borradas, podremos borrar el alumno

    // Paso2b. Usamos el método abstracto y definimos el endpoint
    // Ej: http://localhost:8080/api/alumnos-asignaturas/borrar/{nif}
    @DeleteMapping("/borrar/{nif}")
    @Operation(summary = "Borrar asignaturas a partir del NIF de un alumno")
    @Transactional
    public ResponseEntity<String> borrarAsignturasNIF(@PathVariable String nif) {
        // Vemos las asignaturas por alumno
        long totalAsignaturas = alumnoAsignaturaRepo.countByAlumnoNif(nif);

        if (totalAsignaturas == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("El alumno/a no tiene asignaturas matriculadas");
        }
        // Si tiene mas de 1 asignatura
        alumnoAsignaturaRepo.deleteByAlumnoNif(nif);
        return ResponseEntity.ok("Asignaturas borradas!");
    }

}
