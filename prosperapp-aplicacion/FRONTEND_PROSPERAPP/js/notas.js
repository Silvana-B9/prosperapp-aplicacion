/*
  notas.js
  Consume unicamente NotaDisenoController. La pagina necesita saber a que
  funcionalidad pertenecen las notas, por eso siempre se entra con
  notas.html?idFuncionalidad=X (mas idSeccion e idProyecto, que solo se
  usan para armar el enlace "Volver").
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

function crearFilaNota(nota) {
  const fila = document.createElement("tr");

  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const parametrosActuales = new URLSearchParams(window.location.search);
  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaNotas(nota.idFuncionalidad, parametrosActuales.get("idSeccion"), parametrosActuales.get("idProyecto")) +
      "&id=" + nota.idNota
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarNota(nota.idNota, nota.idFuncionalidad);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(nota.idNota, "ID"),
    crearCeldaTexto(truncar(nota.contenido, 80), "Contenido"),
    crearCeldaTexto(nota.fechaCreacion, "Fecha de creacion"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaNotas(idFuncionalidad) {
  const cuerpoTabla = document.getElementById("cuerpoTablaNotas");

  try {
    const notas = await peticionApi("/funcionalidades/" + idFuncionalidad + "/notas");

    if (notas.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">Esta funcionalidad todavia no tiene notas de diseno.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    notas.forEach(function (nota) {
      cuerpoTabla.appendChild(crearFilaNota(nota));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">No se pudo cargar la lista de notas.</td></tr>";
  }
}

async function eliminarNota(idNota, idFuncionalidad) {
  const confirmar = window.confirm("Seguro que deseas eliminar esta nota de diseno?");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/notas/" + idNota, { method: "DELETE" });
    cargarTablaNotas(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo eliminar la nota.");
  }
}

function urlListaNotas(idFuncionalidad, idSeccion, idProyecto) {
  let url = "notas.html?idFuncionalidad=" + idFuncionalidad;
  if (idSeccion) {
    url += "&idSeccion=" + idSeccion;
  }
  if (idProyecto) {
    url += "&idProyecto=" + idProyecto;
  }
  return url;
}

async function inicializarFormularioNota(idFuncionalidad, idSeccion, idProyecto) {
  const formulario = document.getElementById("formNota");
  const idNota = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioNota");
  const botonGuardar = document.getElementById("botonGuardarNota");
  const enlaceCancelar = document.getElementById("enlaceCancelarNota");
  const mensajeError = document.getElementById("mensajeErrorNota");

  enlaceCancelar.href = urlListaNotas(idFuncionalidad, idSeccion, idProyecto);

  if (idNota) {
    tituloFormulario.textContent = "Editar nota";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const nota = await peticionApi("/notas/" + idNota);
      document.getElementById("idNota").value = nota.idNota;
      document.getElementById("contenidoNota").value = nota.contenido;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la nota a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      contenido: document.getElementById("contenidoNota").value.trim()
    };

    const idActual = document.getElementById("idNota").value;

    try {
      if (idActual) {
        await peticionApi("/notas/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/funcionalidades/" + idFuncionalidad + "/notas", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaNotas(idFuncionalidad, idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la nota. Revisa los datos.";
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
  const bloqueNotas = document.getElementById("bloqueNotas");
  const tituloNotas = document.getElementById("tituloNotas");
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

  bloqueNotas.hidden = false;
  tituloNotas.textContent = "Notas de diseno de la funcionalidad #" + idFuncionalidad;

  cargarTablaNotas(idFuncionalidad);
  inicializarFormularioNota(idFuncionalidad, idSeccion, idProyecto);
});
