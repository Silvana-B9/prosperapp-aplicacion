/*
  funcionalidades.js
  Consume unicamente FuncionalidadController. La pagina necesita saber a
  que seccion pertenecen las funcionalidades, por eso siempre se entra con
  funcionalidades.html?idSeccion=X&idProyecto=Y (el enlace "Funcionalidades"
  de secciones.js ya arma esa URL).
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

function crearFilaFuncionalidad(funcionalidad) {
  const fila = document.createElement("tr");
  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const idFunc = funcionalidad.idFuncionalidad;
  const idProyectoActual = new URLSearchParams(window.location.search).get("idProyecto");

  const sufijoContexto =
    "&idSeccion=" + funcionalidad.idSeccion + (idProyectoActual ? "&idProyecto=" + idProyectoActual : "");

  const enlaces = [
    crearEnlaceAccion("Subtareas", "subtareas.html?idFuncionalidad=" + idFunc + sufijoContexto),
    crearEnlaceAccion("Descripcion", "descripciones.html?idFuncionalidad=" + idFunc + sufijoContexto),
    crearEnlaceAccion("Notas", "notas.html?idFuncionalidad=" + idFunc + sufijoContexto),
    crearEnlaceAccion("Decisiones", "decisiones.html?idFuncionalidad=" + idFunc + sufijoContexto),
    crearEnlaceAccion("Fragmentos", "fragmentos.html?idFuncionalidad=" + idFunc + sufijoContexto)
  ];

  enlaces.forEach(function (enlace) {
    celdaAcciones.appendChild(enlace);
    celdaAcciones.appendChild(document.createTextNode(" "));
  });

  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaFuncionalidades(funcionalidad.idSeccion, idProyectoActual) + "&id=" + idFunc
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarFuncionalidad(idFunc, funcionalidad.idSeccion);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(idFunc, "ID"),
    crearCeldaTexto(funcionalidad.titulo, "Titulo"),
    crearCeldaTexto(funcionalidad.prioridad != null ? funcionalidad.prioridad : "-", "Prioridad"),
    crearCeldaTexto(funcionalidad.fechaCreacion, "Fecha de creacion"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaFuncionalidades(idSeccion) {
  const cuerpoTabla = document.getElementById("cuerpoTablaFuncionalidades");

  try {
    const funcionalidades = await peticionApi("/secciones/" + idSeccion + "/funcionalidades");

    if (funcionalidades.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">Esta seccion todavia no tiene funcionalidades.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    funcionalidades.forEach(function (funcionalidad) {
      cuerpoTabla.appendChild(crearFilaFuncionalidad(funcionalidad));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">No se pudo cargar la lista de funcionalidades.</td></tr>";
  }
}

async function eliminarFuncionalidad(idFuncionalidad, idSeccion) {
  const confirmar = window.confirm(
    "Seguro que deseas eliminar esta funcionalidad? Tambien se eliminan sus subtareas, notas, decisiones, descripcion y fragmentos."
  );
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/funcionalidades/" + idFuncionalidad, { method: "DELETE" });
    cargarTablaFuncionalidades(idSeccion);
  } catch (error) {
    window.alert("No se pudo eliminar la funcionalidad.");
  }
}

function urlListaFuncionalidades(idSeccion, idProyecto) {
  return "funcionalidades.html?idSeccion=" + idSeccion + (idProyecto ? "&idProyecto=" + idProyecto : "");
}

async function inicializarFormularioFuncionalidad(idSeccion, idProyecto) {
  const formulario = document.getElementById("formFuncionalidad");
  const idFuncionalidad = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioFuncionalidad");
  const botonGuardar = document.getElementById("botonGuardarFuncionalidad");
  const enlaceCancelar = document.getElementById("enlaceCancelarFuncionalidad");
  const mensajeError = document.getElementById("mensajeErrorFuncionalidad");

  enlaceCancelar.href = urlListaFuncionalidades(idSeccion, idProyecto);

  if (idFuncionalidad) {
    tituloFormulario.textContent = "Editar funcionalidad";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const funcionalidad = await peticionApi("/funcionalidades/" + idFuncionalidad);
      document.getElementById("idFuncionalidad").value = funcionalidad.idFuncionalidad;
      document.getElementById("tituloFuncionalidad").value = funcionalidad.titulo;
      document.getElementById("historiaUsuarioFuncionalidad").value = funcionalidad.historiaUsuario || "";
      document.getElementById("prioridadFuncionalidad").value = funcionalidad.prioridad || 3;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la funcionalidad a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const prioridadTexto = document.getElementById("prioridadFuncionalidad").value;

    const datos = {
      titulo: document.getElementById("tituloFuncionalidad").value.trim(),
      historiaUsuario: document.getElementById("historiaUsuarioFuncionalidad").value.trim(),
      prioridad: prioridadTexto ? Number(prioridadTexto) : null
    };

    const idActual = document.getElementById("idFuncionalidad").value;

    try {
      if (idActual) {
        await peticionApi("/funcionalidades/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/secciones/" + idSeccion + "/funcionalidades", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaFuncionalidades(idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la funcionalidad. Revisa los datos.";
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
  const idSeccion = parametros.get("idSeccion");
  const idProyecto = parametros.get("idProyecto");
  const mensajeSinSeccion = document.getElementById("mensajeSinSeccion");
  const bloqueFuncionalidades = document.getElementById("bloqueFuncionalidades");
  const tituloFuncionalidades = document.getElementById("tituloFuncionalidades");
  const enlaceVolver = document.getElementById("enlaceVolver");

  if (!idSeccion) {
    mensajeSinSeccion.hidden = false;
    return;
  }

  if (idProyecto) {
    enlaceVolver.innerHTML = "";
    enlaceVolver.appendChild(crearEnlaceAccion("« Volver a secciones", "secciones.html?idProyecto=" + idProyecto));
  }

  bloqueFuncionalidades.hidden = false;
  tituloFuncionalidades.textContent = "Funcionalidades de la seccion #" + idSeccion;

  cargarTablaFuncionalidades(idSeccion);
  inicializarFormularioFuncionalidad(idSeccion, idProyecto);
});
