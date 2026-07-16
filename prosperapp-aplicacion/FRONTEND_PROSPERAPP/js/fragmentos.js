/*
  fragmentos.js
  Consume unicamente FragmentoCodigoController. La pagina necesita saber a
  que funcionalidad pertenecen los fragmentos, por eso siempre se entra con
  fragmentos.html?idFuncionalidad=X (mas idSeccion e idProyecto, que solo
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

function crearFilaFragmento(fragmento) {
  const fila = document.createElement("tr");

  const celdaCodigo = crearCeldaTexto(truncar(fragmento.codigo, 60), "Codigo");
  celdaCodigo.classList.add("codigo");

  const celdaAcciones = crearCeldaTexto("", "Acciones");
  const parametrosActuales = new URLSearchParams(window.location.search);
  const enlaceEditar = crearEnlaceAccion(
    "Editar",
    urlListaFragmentos(fragmento.idFuncionalidad, parametrosActuales.get("idSeccion"), parametrosActuales.get("idProyecto")) +
      "&id=" + fragmento.idFragmento
  );

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarFragmento(fragmento.idFragmento, fragmento.idFuncionalidad);
  });

  celdaAcciones.append(enlaceEditar, document.createTextNode(" "), botonEliminar);

  fila.append(
    crearCeldaTexto(fragmento.idFragmento, "ID"),
    crearCeldaTexto(fragmento.lenguaje, "Lenguaje"),
    celdaCodigo,
    celdaAcciones
  );

  return fila;
}

async function cargarTablaFragmentos(idFuncionalidad) {
  const cuerpoTabla = document.getElementById("cuerpoTablaFragmentos");

  try {
    const fragmentos = await peticionApi("/funcionalidades/" + idFuncionalidad + "/fragmentos");

    if (fragmentos.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">Esta funcionalidad todavia no tiene fragmentos de codigo.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    fragmentos.forEach(function (fragmento) {
      cuerpoTabla.appendChild(crearFilaFragmento(fragmento));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">No se pudo cargar la lista de fragmentos.</td></tr>";
  }
}

async function eliminarFragmento(idFragmento, idFuncionalidad) {
  const confirmar = window.confirm("Seguro que deseas eliminar este fragmento de codigo?");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/fragmentos/" + idFragmento, { method: "DELETE" });
    cargarTablaFragmentos(idFuncionalidad);
  } catch (error) {
    window.alert("No se pudo eliminar el fragmento.");
  }
}

function urlListaFragmentos(idFuncionalidad, idSeccion, idProyecto) {
  let url = "fragmentos.html?idFuncionalidad=" + idFuncionalidad;
  if (idSeccion) {
    url += "&idSeccion=" + idSeccion;
  }
  if (idProyecto) {
    url += "&idProyecto=" + idProyecto;
  }
  return url;
}

async function inicializarFormularioFragmento(idFuncionalidad, idSeccion, idProyecto) {
  const formulario = document.getElementById("formFragmento");
  const idFragmento = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioFragmento");
  const botonGuardar = document.getElementById("botonGuardarFragmento");
  const enlaceCancelar = document.getElementById("enlaceCancelarFragmento");
  const mensajeError = document.getElementById("mensajeErrorFragmento");

  enlaceCancelar.href = urlListaFragmentos(idFuncionalidad, idSeccion, idProyecto);

  if (idFragmento) {
    tituloFormulario.textContent = "Editar fragmento";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const fragmento = await peticionApi("/fragmentos/" + idFragmento);
      document.getElementById("idFragmento").value = fragmento.idFragmento;
      document.getElementById("lenguajeFragmento").value = fragmento.lenguaje;
      document.getElementById("codigoFragmento").value = fragmento.codigo;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar el fragmento a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      lenguaje: document.getElementById("lenguajeFragmento").value.trim(),
      codigo: document.getElementById("codigoFragmento").value
    };

    const idActual = document.getElementById("idFragmento").value;

    try {
      if (idActual) {
        await peticionApi("/fragmentos/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/funcionalidades/" + idFuncionalidad + "/fragmentos", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = urlListaFragmentos(idFuncionalidad, idSeccion, idProyecto);
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar el fragmento. Revisa los datos.";
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
  const bloqueFragmentos = document.getElementById("bloqueFragmentos");
  const tituloFragmentos = document.getElementById("tituloFragmentos");
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

  bloqueFragmentos.hidden = false;
  tituloFragmentos.textContent = "Fragmentos de codigo de la funcionalidad #" + idFuncionalidad;

  cargarTablaFragmentos(idFuncionalidad);
  inicializarFormularioFragmento(idFuncionalidad, idSeccion, idProyecto);
});
