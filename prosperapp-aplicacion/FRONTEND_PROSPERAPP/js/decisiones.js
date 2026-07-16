/*
  decisiones.js
  Consume unicamente DecisionTecnicaController. La pagina necesita saber a
  que funcionalidad pertenecen las decisiones, por eso siempre se entra con
  decisiones.html?idFuncionalidad=X (mas idSeccion e idProyecto, que solo
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

function truncar(texto, limite) {
  if (texto.length <= limite) {
    return texto;
  }
  return texto.slice(0, limite) + "...";
}

function crearFilaDecision(decision) {
  const fila = document.createElement("tr");

  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const parametrosActuales = new URLSearchParams(window.location.search);
  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaDecisiones(decision.idFuncionalidad, parametrosActuales.get("idSeccion"), parametrosActuales.get("idProyecto")) +
      "&id=" + decision.idDecision
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarDecision(decision.idDecision, decision.idFuncionalidad);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(decision.idDecision, "ID"),
    crearCeldaTexto(decision.titulo, "Titulo"),
    crearCeldaTexto(truncar(decision.justificacion || "", 60), "Justificacion"),
    crearCeldaTexto(decision.fechaCreacion, "Fecha de creacion"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaDecisiones(idFuncionalidad) {
  const cuerpoTabla = document.getElementById("cuerpoTablaDecisiones");

  try {
    const decisiones = await peticionApi("/funcionalidades/" + idFuncionalidad + "/decisiones");

    if (decisiones.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">Esta funcionalidad todavia no tiene decisiones tecnicas.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    decisiones.forEach(function (decision) {
      cuerpoTabla.appendChild(crearFilaDecision(decision));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"5\">No se pudo cargar la lista de decisiones.</td></tr>";
  }
}

async function eliminarDecision(idDecision, idFuncionalidad) {
  const confirmar = window.confirm("Seguro que deseas eliminar esta decision tecnica?");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/decisiones/" + idDecision, { method: "DELETE" });
    cargarTablaDecisiones(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo eliminar la decision.");
  }
}

function urlListaDecisiones(idFuncionalidad, idSeccion, idProyecto) {
  let url = "decisiones.html?idFuncionalidad=" + idFuncionalidad;
  if (idSeccion) {
    url += "&idSeccion=" + idSeccion;
  }
  if (idProyecto) {
    url += "&idProyecto=" + idProyecto;
  }
  return url;
}

async function inicializarFormularioDecision(idFuncionalidad, idSeccion, idProyecto) {
  const formulario = document.getElementById("formDecision");
  const idDecision = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioDecision");
  const botonGuardar = document.getElementById("botonGuardarDecision");
  const enlaceCancelar = document.getElementById("enlaceCancelarDecision");
  const mensajeError = document.getElementById("mensajeErrorDecision");

  enlaceCancelar.href = urlListaDecisiones(idFuncionalidad, idSeccion, idProyecto);

  if (idDecision) {
    tituloFormulario.textContent = "Editar decision tecnica";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const decision = await peticionApi("/decisiones/" + idDecision);
      document.getElementById("idDecision").value = decision.idDecision;
      document.getElementById("tituloDecision").value = decision.titulo;
      document.getElementById("justificacionDecision").value = decision.justificacion || "";
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la decision a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      titulo: document.getElementById("tituloDecision").value.trim(),
      justificacion: document.getElementById("justificacionDecision").value.trim()
    };

    const idActual = document.getElementById("idDecision").value;

    try {
      if (idActual) {
        await peticionApi("/decisiones/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/funcionalidades/" + idFuncionalidad + "/decisiones", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaDecisiones(idFuncionalidad, idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la decision. Revisa los datos.";
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
  const bloqueDecisiones = document.getElementById("bloqueDecisiones");
  const tituloDecisiones = document.getElementById("tituloDecisiones");
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

  bloqueDecisiones.hidden = false;
  tituloDecisiones.textContent = "Decisiones tecnicas de la funcionalidad #" + idFuncionalidad;

  cargarTablaDecisiones(idFuncionalidad);
  inicializarFormularioDecision(idFuncionalidad, idSeccion, idProyecto);
});
