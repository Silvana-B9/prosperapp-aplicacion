/*
  subtareas.js
  Consume unicamente SubtareaController. La pagina necesita saber a que
  funcionalidad pertenecen las subtareas, por eso siempre se entra con
  subtareas.html?idFuncionalidad=X (mas idSeccion e idProyecto, que solo
  se usan para armar el enlace "Volver").
*/

function crearCeldaTexto(texto, etiqueta) {
  const celda = document.createElement("td");
  celda.textContent = texto;
  celda.setAttribute("data-etiqueta", etiqueta);
  return celda;
}

function crearEnlaceAccion(texto, href) {
  const enlace = document.createElement("a");
  enlace.href = href;
  enlace.textContent = texto;
  return enlace;
}

function crearFilaSubtarea(subtarea) {
  const fila = document.createElement("tr");

  const celdaCompletada = document.createElement("td");
  celdaCompletada.setAttribute("data-etiqueta", "Hecha");
  const casilla = document.createElement("input");
  casilla.type = "checkbox";
  casilla.checked = !!subtarea.completada;
  casilla.addEventListener("change", function () {
    marcarCompletada(subtarea.idSubtarea, casilla.checked, subtarea.idFuncionalidad);
  });
  celdaCompletada.appendChild(casilla);

  const celdaDescripcion = crearCeldaTexto(subtarea.descripcion, "Descripcion");
  if (subtarea.completada) {
    celdaDescripcion.classList.add("completada");
  }

  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const parametrosActuales = new URLSearchParams(window.location.search);
  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaSubtareas(subtarea.idFuncionalidad, parametrosActuales.get("idSeccion"), parametrosActuales.get("idProyecto")) +
      "&id=" + subtarea.idSubtarea
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarSubtarea(subtarea.idSubtarea, subtarea.idFuncionalidad);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(subtarea.idSubtarea, "ID"),
    celdaCompletada,
    celdaDescripcion,
    celdaAcciones
  );

  return fila;
}

async function cargarTablaSubtareas(idFuncionalidad) {
  const cuerpoTabla = document.getElementById("cuerpoTablaSubtareas");

  try {
    const subtareas = await peticionApi("/funcionalidades/" + idFuncionalidad + "/subtareas");

    if (subtareas.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">Esta funcionalidad todavia no tiene subtareas.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    subtareas.forEach(function (subtarea) {
      cuerpoTabla.appendChild(crearFilaSubtarea(subtarea));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">No se pudo cargar la lista de subtareas.</td></tr>";
  }
}

async function marcarCompletada(idSubtarea, completada, idFuncionalidad) {
  try {
    await peticionApi("/subtareas/" + idSubtarea + "/completar?completada=" + completada, {
      method: "PATCH"
    });
    cargarTablaSubtareas(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo actualizar el estado de la subtarea.");
    cargarTablaSubtareas(idFuncionalidad);
  }
}

async function eliminarSubtarea(idSubtarea, idFuncionalidad) {
  const confirmar = window.confirm("Seguro que deseas eliminar esta subtarea?");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/subtareas/" + idSubtarea, { method: "DELETE" });
    cargarTablaSubtareas(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo eliminar la subtarea.");
  }
}

function urlListaSubtareas(idFuncionalidad, idSeccion, idProyecto) {
  let url = "subtareas.html?idFuncionalidad=" + idFuncionalidad;
  if (idSeccion) {
    url += "&idSeccion=" + idSeccion;
  }
  if (idProyecto) {
    url += "&idProyecto=" + idProyecto;
  }
  return url;
}

async function inicializarFormularioSubtarea(idFuncionalidad, idSeccion, idProyecto) {
  const formulario = document.getElementById("formSubtarea");
  const idSubtarea = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioSubtarea");
  const botonGuardar = document.getElementById("botonGuardarSubtarea");
  const enlaceCancelar = document.getElementById("enlaceCancelarSubtarea");
  const mensajeError = document.getElementById("mensajeErrorSubtarea");

  enlaceCancelar.href = urlListaSubtareas(idFuncionalidad, idSeccion, idProyecto);

  let completadaActual = false;

  if (idSubtarea) {
    tituloFormulario.textContent = "Editar subtarea";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const subtarea = await peticionApi("/subtareas/" + idSubtarea);
      document.getElementById("idSubtarea").value = subtarea.idSubtarea;
      document.getElementById("descripcionSubtarea").value = subtarea.descripcion;
      completadaActual = !!subtarea.completada;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la subtarea a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      descripcion: document.getElementById("descripcionSubtarea").value.trim(),
      completada: completadaActual
    };

    const idActual = document.getElementById("idSubtarea").value;

    try {
      if (idActual) {
        await peticionApi("/subtareas/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/funcionalidades/" + idFuncionalidad + "/subtareas", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaSubtareas(idFuncionalidad, idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la subtarea. Revisa los datos.";
      mensajeError.hidden = false;
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  const usuario = requerirSesion();
  if (!usuario) {
    return;
  }

  const parametros = new URLSearchParams(window.location.search);
  const idFuncionalidad = parametros.get("idFuncionalidad");
  const idSeccion = parametros.get("idSeccion");
  const idProyecto = parametros.get("idProyecto");
  const mensajeSinFuncionalidad = document.getElementById("mensajeSinFuncionalidad");
  const bloqueSubtareas = document.getElementById("bloqueSubtareas");
  const tituloSubtareas = document.getElementById("tituloSubtareas");
  const enlaceVolver = document.getElementById("enlaceVolver");

  if (!idFuncionalidad) {
    mensajeSinFuncionalidad.hidden = false;
    return;
  }

  if (idSeccion) {
    const sufijo = idProyecto ? "&idProyecto=" + idProyecto : "";
    enlaceVolver.innerHTML = "";
    enlaceVolver.appendChild(
      crearEnlaceAccion("« Volver a funcionalidades", "funcionalidades.html?idSeccion=" + idSeccion + sufijo)
    );
  }

  bloqueSubtareas.hidden = false;
  tituloSubtareas.textContent = "Subtareas de la funcionalidad #" + idFuncionalidad;

  cargarTablaSubtareas(idFuncionalidad);
  inicializarFormularioSubtarea(idFuncionalidad, idSeccion, idProyecto);
});
