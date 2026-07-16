/*
  app.js
  Funciones comunes que usaran TODAS las paginas de ProsperApp:
  - URL base del backend
  - ayudante generico para llamar la API con fetch()
  - manejo de la sesion del usuario logueado (localStorage)
  - resaltar el enlace activo del menu lateral y mostrar el usuario en la cabecera
*/

// URL base del backend Spring Boot. Cambiar aqui si el backend corre en otro puerto.
const API_BASE = "http://localhost:8080/api";

// ---------- Sesion del usuario ----------
const CLAVE_SESION = "prosperapp_usuario";

function guardarSesion(usuario) {
  localStorage.setItem(CLAVE_SESION, JSON.stringify(usuario));
}

function obtenerSesion() {
  const datos = localStorage.getItem(CLAVE_SESION);
  return datos ? JSON.parse(datos) : null;
}

// Las paginas internas viven dentro de /pages/, por eso el login
// puede estar un nivel arriba o en la raiz segun desde donde se llame.
function rutaRaiz() {
  return window.location.pathname.includes("/pages/") ? "../" : "";
}

function cerrarSesion() {
  localStorage.removeItem(CLAVE_SESION);
  window.location.href = rutaRaiz() + "index.html";
}

// Protege paginas internas: si no hay sesion activa, regresa al login.
function requerirSesion() {
  const usuario = obtenerSesion();
  if (!usuario) {
    window.location.href = rutaRaiz() + "index.html";
  }
  return usuario;
}

// ---------- Ayudante generico para consumir la API ----------
async function peticionApi(ruta, opciones = {}) {
  const respuesta = await fetch(API_BASE + ruta, {
    headers: { "Content-Type": "application/json" },
    ...opciones
  });

  if (!respuesta.ok) {
    throw new Error("Error " + respuesta.status + " al llamar " + ruta);
  }

  if (respuesta.status === 204) {
    return null;
  }

  return respuesta.json();
}

// ---------- Menu lateral: marcar la pagina activa ----------
function marcarEnlaceActivo() {
  const enlaces = document.querySelectorAll(".menu-lateral a");
  const paginaActual = window.location.pathname.split("/").pop();

  enlaces.forEach(function (enlace) {
    const destino = enlace.getAttribute("href").split("/").pop();
    if (destino === paginaActual) {
      enlace.classList.add("activo");
    }
  });
}

// ---------- Mostrar el nombre del usuario logueado en la cabecera ----------
function mostrarUsuarioEnCabecera() {
  const usuario = obtenerSesion();
  const contenedor = document.getElementById("nombreUsuario");
  if (usuario && contenedor) {
    contenedor.textContent = "Hola, " + usuario.nombreUsuario;
  }
}

// ---------- Se ejecuta en cuanto carga cualquier pagina ----------
document.addEventListener("DOMContentLoaded", function () {
  const botonSalir = document.getElementById("cerrarSesion");
  if (botonSalir) {
    botonSalir.addEventListener("click", function (evento) {
      evento.preventDefault();
      cerrarSesion();
    });
  }

  marcarEnlaceActivo();
  mostrarUsuarioEnCabecera();
});
