package com.inserta.crudalumnos.carga;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.inserta.crudalumnos.modelo.Alumno;
import com.inserta.crudalumnos.modelo.AlumnoAsignatura;
import com.inserta.crudalumnos.modelo.Asignatura;
import com.inserta.crudalumnos.repositorio.AlumnoAsignaturaRepository;
import com.inserta.crudalumnos.repositorio.AlumnoRepository;
import com.inserta.crudalumnos.repositorio.AsignaturaRepository;

@Component
public class DatosIniciales implements CommandLineRunner{

    // 1. Añadimos los repositorios!
    private final AlumnoRepository alumnoRepo;
    private final AsignaturaRepository asignaturaRepo;
    private final AlumnoAsignaturaRepository alumnoAsignaturaRepo;

    
    // 2. Creamos el constructor completo
    public DatosIniciales(AlumnoRepository alumnoRepo, AsignaturaRepository asignaturaRepo,
            AlumnoAsignaturaRepository alumnoAsignaturaRepo) {
        this.alumnoRepo = alumnoRepo;
        this.asignaturaRepo = asignaturaRepo;
        this.alumnoAsignaturaRepo = alumnoAsignaturaRepo;
    }

    // 3. Metemos los datos en el run
    @Override
    public void run(String... args) throws Exception {
        // Comprobamos que las tablas están vacias
        if (alumnoRepo.count() == 0 && asignaturaRepo.count() == 0) {
            
            // 3.1 Crear asignaturas
            Asignatura bbdd = new Asignatura(null, "Bases de datos", 1, 
                List.of(1.1f, 2.2f), new ArrayList<>());
            Asignatura prog = new Asignatura(null, "Programación", 1, 
                List.of(2.2f, 2.3f), new ArrayList<>());
            Asignatura fol = new Asignatura(null, "Form.Orientacion Lab", 1, 
                List.of(2.2f, 2.3f), new ArrayList<>());
            asignaturaRepo.saveAll(List.of(bbdd, prog, fol));

            // 3.2 Crear alumnos
            Alumno parraga = new Alumno("11A", "Alex Párraga", 23, false, new ArrayList<>());
            Alumno rider = new Alumno("22B", "Miguel Rider", 39, false, new ArrayList<>());
            Alumno aleon = new Alumno("33C", "Alex León", 30, false, new ArrayList<>());
            Alumno jaime = new Alumno("44D", "Jaime Peña", 30, false, new ArrayList<>());
            Alumno angela = new Alumno("55E", "Angela Crespo", 20, true, new ArrayList<>());
            alumnoRepo.saveAll(List.of(parraga, rider, aleon, jaime, angela));

            // 3.3 Relacionamos alumnos con asignaturas
            List<AlumnoAsignatura> relaciones = List.of(
                new AlumnoAsignatura(null, jaime, fol),
                new AlumnoAsignatura(null, jaime, bbdd),
                new AlumnoAsignatura(null, jaime, prog),
                new AlumnoAsignatura(null, aleon, prog),
                new AlumnoAsignatura(null, rider, bbdd),
                new AlumnoAsignatura(null, rider, prog),
                new AlumnoAsignatura(null, parraga, fol),
                new AlumnoAsignatura(null, parraga, bbdd),
                new AlumnoAsignatura(null, angela, bbdd),
                new AlumnoAsignatura(null, angela, prog)
            );

            alumnoAsignaturaRepo.saveAll(relaciones);
        }
        
    }

}
