package com.inserta.crudalumnos.controlador;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.inserta.crudalumnos.modelo.Alumno;
import com.inserta.crudalumnos.repositorio.AlumnoRepository;

import io.swagger.v3.oas.annotations.Operation;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


/**
 * Endpoints para Alumnos:
 * http://localhost:8080/api/alumnos
 * - GET R01 → /api/alumnos/consultar           → Consulta todos los alumnos
 * - GET R02 → /api/alumnos/consultar/{nif}     → Consulta por PF (nif)
 * - GET R03 → /api/alumnos/consultar/ordenado  → Consulta ordenada por campo=edad
 * - GET R04 → /api/alumnos/consultar/pagina    → Consulta paginada (LIMIT 0,5)
 */


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
    // Ej: http://localhost:8080/api/alumnos/consultar
    // GET R01 → /api/alumnos/consultar
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
    // Ej: http://localhost:8080/api/alumnos/consultar/22B
    // GET R02 → /api/alumnos/consultar/{nif}
    @GetMapping("/consultar/{nif}")
    @Operation(summary = "Ver alumno por NIF")
    public ResponseEntity<Alumno> verAlumnoPorNIF(@PathVariable String nif){
        return alumnoRepo.findById(nif)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    // Ej: http://localhost:8080/api/alumnos/consultar/ordenado?campo=edad
    // GET R03 → /api/alumnos/consultar/ordenado
    @GetMapping("/consultar/ordenado")
    @Operation(summary = "Consultar alumnos ordenado por nombre descendente")
    public List<Alumno> verAlumnosOrdenados
        (@RequestParam (defaultValue = "nombre") String campo) {
        return alumnoRepo.findAll(Sort.by(campo).descending());
    }
    
    // Ej: http://localhost:8080/api/alumnos/consultar/pagina?pagina=0&tam=5
    // GET R04 → /api/alumnos/consultar/pagina
    @GetMapping("/consultar/pagina")
    @Operation(summary = "Consultar alumnos paginado por intervalos")
    public Page<Alumno> verAlumnosPaginados 
        (@RequestParam (defaultValue = "0") int pagina,
        @RequestParam (defaultValue = "5") int tam) {
        return alumnoRepo.findAll(PageRequest.of(pagina, tam));
    }
    
    

}
