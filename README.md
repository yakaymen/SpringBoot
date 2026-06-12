# SpringBoot

**INSTALACIÓN DE ANGULAR**
Página Oficial de NVM -> https://github.com/nvm-sh/nvm

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
