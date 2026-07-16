/*
  descripciones.js
  Consume unicamente DescripcionDetalladaController. La pagina necesita
  saber a que funcionalidad pertenecen las descripciones, por eso siempre
  se entra con descripciones.html?idFuncionalidad=X (mas idSeccion e
  idProyecto, que solo se usan para armar el enlace "Volver").
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

function crearFilaDescripcion(descripcion) {
  const fila = document.createElement("tr");

  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const parametrosActuales = new URLSearchParams(window.location.search);
  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaDescripciones(descripcion.idFuncionalidad, parametrosActuales.get("idSeccion"), parametrosActuales.get("idProyecto")) +
      "&id=" + descripcion.idDescripcion
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarDescripcion(descripcion.idDescripcion, descripcion.idFuncionalidad);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(descripcion.idDescripcion, "ID"),
    crearCeldaTexto(truncar(descripcion.contenido, 80), "Contenido"),
    crearCeldaTexto(descripcion.fechaCreacion, "Fecha de creacion"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaDescripciones(idFuncionalidad) {
  const cuerpoTabla = document.getElementById("cuerpoTablaDescripciones");

  try {
    const descripciones = await peticionApi("/funcionalidades/" + idFuncionalidad + "/descripciones");

    if (descripciones.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">Esta funcionalidad todavia no tiene descripciones detalladas.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    descripciones.forEach(function (descripcion) {
      cuerpoTabla.appendChild(crearFilaDescripcion(descripcion));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">No se pudo cargar la lista de descripciones.</td></tr>";
  }
}

async function eliminarDescripcion(idDescripcion, idFuncionalidad) {
  const confirmar = window.confirm("Seguro que deseas eliminar esta descripcion detallada?");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/descripciones/" + idDescripcion, { method: "DELETE" });
    cargarTablaDescripciones(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo eliminar la descripcion.");
  }
}

function urlListaDescripciones(idFuncionalidad, idSeccion, idProyecto) {
  let url = "descripciones.html?idFuncionalidad=" + idFuncionalidad;
  if (idSeccion) {
    url += "&idSeccion=" + idSeccion;
  }
  if (idProyecto) {
    url += "&idProyecto=" + idProyecto;
  }
  return url;
}

async function inicializarFormularioDescripcion(idFuncionalidad, idSeccion, idProyecto) {
  const formulario = document.getElementById("formDescripcion");
  const idDescripcion = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioDescripcion");
  const botonGuardar = document.getElementById("botonGuardarDescripcion");
  const enlaceCancelar = document.getElementById("enlaceCancelarDescripcion");
  const mensajeError = document.getElementById("mensajeErrorDescripcion");

  enlaceCancelar.href = urlListaDescripciones(idFuncionalidad, idSeccion, idProyecto);

  if (idDescripcion) {
    tituloFormulario.textContent = "Editar descripcion";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const descripcion = await peticionApi("/descripciones/" + idDescripcion);
      document.getElementById("idDescripcion").value = descripcion.idDescripcion;
      document.getElementById("contenidoDescripcion").value = descripcion.contenido;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la descripcion a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      contenido: document.getElementById("contenidoDescripcion").value.trim()
    };

    const idActual = document.getElementById("idDescripcion").value;

    try {
      if (idActual) {
        await peticionApi("/descripciones/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/funcionalidades/" + idFuncionalidad + "/descripciones", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaDescripciones(idFuncionalidad, idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la descripcion. Revisa los datos.";
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
  const bloqueDescripciones = document.getElementById("bloqueDescripciones");
  const tituloDescripciones = document.getElementById("tituloDescripciones");
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

  bloqueDescripciones.hidden = false;
  tituloDescripciones.textContent = "Descripciones detalladas de la funcionalidad #" + idFuncionalidad;

  cargarTablaDescripciones(idFuncionalidad);
  inicializarFormularioDescripcion(idFuncionalidad, idSeccion, idProyecto);
});
