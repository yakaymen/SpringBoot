package com.inserta.crudalumnos.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inserta.crudalumnos.modelo.Alumno;
import com.inserta.crudalumnos.repositorio.AlumnoRepository;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;



// Para la API/Rest (en el controlador) se pone la anotación @RestController
// @RequestMapping define el endpoint (la URL que sale en swagger/postman)
@RestController
@RequestMapping("/api/alumnos")
public class AlumnoController {
    // Agregamos el repo
    private final AlumnoRepository alumnoRepo;

    // Constructor del repo
    public AlumnoController(AlumnoRepository alumnoRepo) {
        this.alumnoRepo = alumnoRepo;
    }

    // Por cada endpoint se crea un método
    // endpoint -> http://localhost:8080/api/alumnos/consultar
    @GetMapping("/consultar")
    @Operation(summary = "Lista alumnos")
    public List<Alumno> verAlumnos(){
        return alumnoRepo.findAll();
    }
    
    // Añadimos Swagger CTRL + MAY + P
    // Spring Initializr: Add Starter > SpringDoc Open Api Web
    // Se añade el paquete [Proceed] y se añade al Path
    // En application.properties se añade la url.path de swagger
    // http://localhost:8080/swagger-ui/index.html

    // GET por parámetro PK (findById) -> Parámetros van con { }
    @GetMapping("/consultar/{nif}")
    @Operation(summary = "Ver alumno por NIF")
    public ResponseEntity<Alumno> verAlumnoPorNIF(@PathVariable String nif){
        return alumnoRepo.findById(nif)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
    }
    

}
