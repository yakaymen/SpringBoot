package com.inserta.crudalumnos.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.inserta.crudalumnos.modelo.Asignatura;
import com.inserta.crudalumnos.repositorio.AsignaturaRepository;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;



// Para la API/Rest (en el controlador) se pone la anotación @RestController
// @RequestMapping define el endpoint (la URL que sale en swagger/postman)
@RestController
@RequestMapping("/api/asignaturas")
public class AsignaturaController {
    // Agregamos el repo
    private final AsignaturaRepository asignaturaRepo;

    public AsignaturaController(AsignaturaRepository asignaturaRepo) {
        this.asignaturaRepo = asignaturaRepo;
    }

    // Por cada endpoint se crea un método
    // endpoint -> http://localhost:8080/api/asignaturas/consultar
    @GetMapping("/consultar")
    @Operation(summary = "Lista asignaturas")
    public List<Asignatura> verAsignaturas(){
        return asignaturaRepo.findAll();
    }


}
