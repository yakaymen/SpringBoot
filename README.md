# SpringBoot

**INSTALACIÓN DE ANGULAR**
Página Oficial de NVM 
-> https://github.com/nvm-sh/nvm

cd ~/Descargas

wget -qO- https://raw.githubusercontent.com/nvm-sh/nvm/v0.40.5/install.sh | bash

source ~/.bashrc   

nvm --version

# Instalar Node.js LTS

nvm install --lts

nvm use --lts

# Creamos un alias a la versión LTS

nvm alias default lts/*

# Para ver las versiones actuales de NodeJS y NPM

nvm ls

# Instalar globalmente

npm install -g @angular/cli@21

# Verificar

ng version

**PARA AQUELLOS QUE TENGAIS WINDOWS**
Lo único que hay que cambiar es el NVM
Descargar el exe:

https://github.com/coreybutler/nvm-windows/releases/download/1.1.12/nvm-setup.exe

O bien irse a los releases:

https://github.com/coreybutler/nvm-windows/releases

**CREAR PROYECTO ANGULAR**
cd ~/Documentos/SpringBoot_Angular

ng new frontendalumnos --routing --style=css

# Preguntará varias cosas, INTRO, INTRO, INTRO

cd frontendalumnos

npm install bootstrap


## Proceso de Construcción Proyecto

### 1. Backend

#### 1.1 Modelos

- Alumno.java -> Tabla alumnos (incluir anotaciones, @Entity, @Data,...)
- Asignatura.java -> Tabla asignaturas (incluir anotaciones)
  - En ambos casos NO incuir relaciones
- AlumnoAsignatura -> Tabla alumnos_asignaturas (incluir anotaciones)
  - Añadir los FKs en el @table
  
- Añadir las relaciones
  - Empezar por la relacionada (AlumnoAsignatura) @ManyToOne
  - Establecer los campos de la relación con OBJETOS!!
  - Seguir con las tablas principales
    - Poner el @OneToMany y hacer el List y el ArrayList