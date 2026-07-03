package com.inserta.crudalumnos.controlador;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inserta.crudalumnos.modelo.Alumno;
import com.inserta.crudalumnos.modelo.AlumnoAsignatura;
import com.inserta.crudalumnos.modelo.Asignatura;
import com.inserta.crudalumnos.repositorio.AlumnoAsignaturaRepository;
import com.inserta.crudalumnos.repositorio.AlumnoRepository;
import com.inserta.crudalumnos.repositorio.AsignaturaRepository;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

@RestController
@RequestMapping("/api/matriculas")
public class MatriculaController {
    // Declaramos los repos y el contructor asociado
    private final AlumnoRepository alumnoRepo;
    private final AsignaturaRepository asignaturaRepo;
    private final AlumnoAsignaturaRepository alumnoAsignaturaRepo;

    public MatriculaController(
            AlumnoRepository alumnoRepo,
            AsignaturaRepository asignaturaRepo,
            AlumnoAsignaturaRepository alumnoAsignaturaRepo) {
        this.alumnoRepo = alumnoRepo;
        this.asignaturaRepo = asignaturaRepo;
        this.alumnoAsignaturaRepo = alumnoAsignaturaRepo;
    }

    // POST - C01 Matricula un alumno en una asignatura
    // Ej: http://localhost:8080/api/matriculas/matricular/33C/1
    // ResponseEntity<?> puede ser de varios tipos -> ? significa genérico
    // Permite:
    // a) ResponseEntity.notFound() -> No se ha encontrado Alumno y/o Asignatura
    // b) ResponseEntity.badRequest() -> Matricula ya existe (ERROR!)
    // c) ResponseEntity.ok() -> Todo perfe
    @PostMapping("/matricular/{nif}/{idAsignatura}")
    @Operation(summary = "Matricula un alumno en una asignatura")
    public ResponseEntity<?> matricular(
            @PathVariable String nif, @PathVariable Integer idAsignatura) {

        // 1. Comprobamos que existe el alumno y la asignatura
        final Alumno alumno = alumnoRepo.findById(nif).orElse(null);
        if (alumno == null) {
            // return ResponseEntity.notFound().build(); // Error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Alumno no encontrado");
        }

        final Asignatura asignatura = asignaturaRepo.findById(idAsignatura).orElse(null);
        if (asignatura == null) {
            // return ResponseEntity.notFound().build(); // Error 404
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Asignatura no encontrada");
        }

        // 2. Comprobamos que YA no está matriculado en esa asignatura
        boolean yaEstaMatriculado = alumnoAsignaturaRepo.existeMatricula(nif, idAsignatura);
        if (yaEstaMatriculado == true) {
            return ResponseEntity.badRequest().body("Ya matriculado!");
        }

        // 3. Si está todo correcto, entonces MATRICULO!
        // La salida si todo es correcto es un JSON con la nueva ID de matricula
        AlumnoAsignatura matricula = new AlumnoAsignatura(null, alumno, asignatura);
        return ResponseEntity.ok(alumnoAsignaturaRepo.save(matricula));
    }

    // DELETE - D01 -> Desmatricular una asigntura a un alumno
    @DeleteMapping("/desmatricular/{nif}/{idAsignatura}")
    @Operation(summary = "Desmatriculo un alumno en una asignatura")
    public ResponseEntity<?> desmatricular(
            @PathVariable String nif, @PathVariable Integer idAsignatura) {

        // 1. Comprobamos que YA está matriculado en esa asignatura
        if (!alumnoAsignaturaRepo.existeMatricula(nif, idAsignatura)) {
            return ResponseEntity.notFound().build(); // Error 404
        }

        // 2. Si existe la matricula, borramos
        alumnoAsignaturaRepo.borrarMatricula(nif, idAsignatura);
        return ResponseEntity.noContent().build(); // Salida 204 OK sin nada
    }

    // GET - R01 - Listado de asignaturas por alumno
    @GetMapping("/verAsignaturas/{nif}")
    public ResponseEntity<List<String>> verAsignaturasNIF(@PathVariable String nif) {
        List<String> denominaciones = alumnoAsignaturaRepo.asignaturasPorAlumno(nif);
        return ResponseEntity.ok(denominaciones);
    }

    // GET - R02 - Listado de asignaturas completas por alumno
    // Devuelve una lista de Asignaturas convertidas a JSON (ResponseEntity)
    @GetMapping("/verAsignaturasCompletas/{nif}")
    public ResponseEntity<List<Asignatura>> verAsignaturasCompletasNIF(@PathVariable String nif) {
        List<Asignatura> asignaturas = alumnoAsignaturaRepo.asignaturasCompletaPorAlumno(nif);
        return ResponseEntity.ok(asignaturas);
    }
}
