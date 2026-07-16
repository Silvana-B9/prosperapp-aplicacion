/*
  proyectos.js
  Consume unicamente ProyectoController.

  Este archivo hace dos cosas segun la pagina donde se cargue:
  - Si existe #cuerpoTablaProyectos, dibuja la tabla de proyectos del
    usuario logueado (la usan tanto dashboard.html como proyectos.html).
  - Si ademas existe #formProyecto (solo en proyectos.html), habilita el
    formulario de crear/editar y los botones de Editar/Eliminar.
*/

function capitalizar(texto) {
  return texto.charAt(0).toUpperCase() + texto.slice(1);
}

function crearCeldaTexto(texto, etiqueta) {
  const celda = document.createElement("td");
  celda.textContent = texto;
  celda.setAttribute("data-etiqueta", etiqueta);
  return celda;
}

function crearFilaProyecto(proyecto, modoCrud) {
  const fila = document.createElement("tr");
  const celdaAcciones = crearCeldaTexto("", "Acciones");

  if (modoCrud) {
    const enlaceSecciones = document.createElement("a");
    enlaceSecciones.href = "secciones.html?idProyecto=" + proyecto.idProyecto;
    enlaceSecciones.textContent = "Secciones";

    const enlaceEditar = document.createElement("a");
    enlaceEditar.href = "proyectos.html?id=" + proyecto.idProyecto;
    enlaceEditar.textContent = "Editar";

    const botonEliminar = document.createElement("button");
    botonEliminar.type = "button";
    botonEliminar.textContent = "Eliminar";
    botonEliminar.addEventListener("click", function () {
      eliminarProyecto(proyecto.idProyecto);
    });

    celdaAcciones.appendChild(enlaceSecciones);
    celdaAcciones.appendChild(document.createTextNode(" "));
    celdaAcciones.appendChild(enlaceEditar);
    celdaAcciones.appendChild(document.createTextNode(" "));
    celdaAcciones.appendChild(botonEliminar);
  } else {
    const enlaceVer = document.createElement("a");
    enlaceVer.href = "proyectos.html?id=" + proyecto.idProyecto;
    enlaceVer.textContent = "Ver";
    celdaAcciones.appendChild(enlaceVer);
  }

  fila.append(
    crearCeldaTexto(proyecto.idProyecto, "ID"),
    crearCeldaTexto(proyecto.nombre, "Nombre"),
    crearCeldaTexto(capitalizar(proyecto.estado), "Estado"),
    crearCeldaTexto(proyecto.fechaCreacion, "Fecha de creacion"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaProyectos() {
  const cuerpoTabla = document.getElementById("cuerpoTablaProyectos");
  if (!cuerpoTabla) {
    return;
  }

  const usuario = requerirSesion();
  if (!usuario) {
    return;
  }

  const modoCrud = !!document.getElementById("formProyecto");

  try {
    const proyectos = await peticionApi("/usuarios/" + usuario.idUsuario + "/proyectos");

    if (proyectos.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">Todavia no tienes proyectos.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    proyectos.forEach(function (proyecto) {
      cuerpoTabla.appendChild(crearFilaProyecto(proyecto, modoCrud));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">No se pudo cargar la lista de proyectos.</td></tr>";
  }
}

async function eliminarProyecto(idProyecto) {
  const confirmar = window.confirm("Seguro que deseas eliminar este proyecto? Tambien se eliminan sus secciones.");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/proyectos/" + idProyecto, { method: "DELETE" });
    cargarTablaProyectos();
  } catch (error) {
    window.alert("No se pudo eliminar el proyecto.");
  }
}

async function inicializarFormularioProyecto() {
  const formulario = document.getElementById("formProyecto");
  if (!formulario) {
    return;
  }

  const idProyecto = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormulario");
  const botonGuardar = document.getElementById("botonGuardarProyecto");
  const enlaceCancelar = document.getElementById("enlaceCancelar");
  const mensajeError = document.getElementById("mensajeErrorProyecto");

  if (idProyecto) {
    tituloFormulario.textContent = "Editar proyecto";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const proyecto = await peticionApi("/proyectos/" + idProyecto);
      document.getElementById("idProyecto").value = proyecto.idProyecto;
      document.getElementById("nombreProyecto").value = proyecto.nombre;
      document.getElementById("descripcionProyecto").value = proyecto.descripcion || "";
      document.getElementById("estadoProyecto").value = proyecto.estado;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar el proyecto a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const usuario = requerirSesion();
    if (!usuario) {
      return;
    }

    const datos = {
      nombre: document.getElementById("nombreProyecto").value.trim(),
      descripcion: document.getElementById("descripcionProyecto").value.trim(),
      estado: document.getElementById("estadoProyecto").value
    };

    const idActual = document.getElementById("idProyecto").value;

    try {
      if (idActual) {
        await peticionApi("/proyectos/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/usuarios/" + usuario.idUsuario + "/proyectos", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = "proyectos.html";
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar el proyecto. Revisa los datos.";
      mensajeError.hidden = false;
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  cargarTablaProyectos();
  inicializarFormularioProyecto();
});
