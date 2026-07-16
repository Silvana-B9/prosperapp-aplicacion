# ProsperApp

Proyecto final del curso de Bases de Datos (Universidad del Valle). Gestor de
proyectos personales: proyectos -> secciones -> funcionalidades -> (subtareas,
notas de diseno, decisiones tecnicas, descripciones detalladas, fragmentos de
codigo).

- **Backend**: Spring Boot 4 + PostgreSQL (`src/`).
- **Frontend**: HTML + CSS + JavaScript puro, sin frameworks (`FRONTEND_PROSPERAPP/`).

## Requisitos

- Java 17 o superior (el proyecto trae `mvnw`, no hace falta instalar Maven).
- Docker Desktop (recomendado, ver opcion A) **o** PostgreSQL instalado
  localmente (ver opcion B).

## 1. Levantar la base de datos

### Opcion A: Docker (recomendado, un solo comando)

Desde la carpeta raiz del proyecto:

```
docker compose up -d
```

Esto crea un contenedor de PostgreSQL 16 con la base `prosperapp_db` (usuario
`postgres`, contrasena `rodrigo123`, puerto `5433`) y ejecuta automaticamente
el script `src/main/resources/db/tablastrabajofinal.sql`, que crea todas las
tablas. Solo pasa la primera vez que se levanta el contenedor.

### Opcion B: PostgreSQL local (sin Docker)

1. Crear una base de datos llamada `prosperapp_db`.
2. Ejecutar el script `src/main/resources/db/tablastrabajofinal.sql` sobre esa
   base (crea las tablas, indices y el trigger de negocio).
3. Si el usuario/contrasena/puerto de tu PostgreSQL no son
   `postgres` / `rodrigo123` / `5433`, edita
   `src/main/resources/application.properties` con tus propios datos:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/prosperapp_db
spring.datasource.username=postgres
spring.datasource.password=rodrigo123
```

## 2. Levantar el backend

Desde la raiz del proyecto:

```
./mvnw spring-boot:run
```

(en Windows sin Git Bash: `mvnw.cmd spring-boot:run`)

Espera a ver `Started ProsperappAplicacionApplication` en la consola. El
backend queda escuchando en `http://localhost:8080`.

## 3. Crear un usuario (no hay pantalla de registro)

El frontend solo tiene pantalla de **login**, no de registro. Para crear el
primer usuario, con el backend ya corriendo, ejecuta en otra terminal:

```
curl -X POST http://localhost:8080/api/usuarios ^
  -H "Content-Type: application/json" ^
  -d "{\"nombre\":\"Tu Nombre\",\"correo\":\"tu_correo@ejemplo.com\",\"contrasena\":\"UnaClave123\"}"
```

(en Git Bash / Mac / Linux, cambia los `^` de fin de linea por `\` y usa
comillas simples en el `-d`). Tambien puedes usar Postman/Insomnia contra el
mismo endpoint.

Con ese correo y contrasena ya puedes iniciar sesion en el frontend.

## 4. Abrir el frontend

Abre `FRONTEND_PROSPERAPP/index.html` directamente en el navegador (doble
clic, o "Abrir con" tu navegador). No necesita servidor propio: todas las
peticiones van por `fetch()` hacia `http://localhost:8080`.

## Apagar todo

- Backend: `Ctrl+C` en la terminal donde corre `spring-boot:run`.
- Base de datos (si usaste Docker): `docker compose down` (o
  `docker compose down -v` si tambien quieres borrar los datos guardados).

## Notas

- `ProyectoColaborador` no tiene pantalla propia en el frontend: al crear un
  proyecto, el backend agrega automaticamente al creador como propietario, y
  tambien crea una seccion inicial llamada "General".
- Un proyecto no puede tener mas de 6 secciones (regla de negocio aplicada
  con un trigger en PostgreSQL); si se intenta crear una septima, el backend
  responde con error y el frontend muestra un mensaje generico.
