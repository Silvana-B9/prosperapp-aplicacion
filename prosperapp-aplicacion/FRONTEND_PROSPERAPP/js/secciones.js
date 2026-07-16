/*
  secciones.js
  Consume unicamente SeccionController. La pagina necesita saber a que
  proyecto pertenecen las secciones, por eso siempre se entra con
  secciones.html?idProyecto=X (el enlace "Secciones" de proyectos.js
  ya arma esa URL).
*/

function crearCeldaTexto(texto, etiqueta) {
  const celda = document.createElement("td");
  celda.textContent = texto;
  celda.setAttribute("data-etiqueta", etiqueta);
  return celda;
}

function crearFilaSeccion(seccion) {
  const fila = document.createElement("tr");
  const celdaAcciones = crearCeldaTexto("", "Acciones");

  const enlaceFuncionalidades = document.createElement("a");
  enlaceFuncionalidades.href = "funcionalidades.html?idSeccion=" + seccion.idSeccion + "&idProyecto=" + seccion.idProyecto;
  enlaceFuncionalidades.textContent = "Funcionalidades";

  const enlaceEditar = document.createElement("a");
  enlaceEditar.href = "secciones.html?idProyecto=" + seccion.idProyecto + "&id=" + seccion.idSeccion;
  enlaceEditar.textContent = "Editar";

  const botonEliminar = document.createElement("button");
  botonEliminar.type = "button";
  botonEliminar.textContent = "Eliminar";
  botonEliminar.addEventListener("click", function () {
    eliminarSeccion(seccion.idSeccion, seccion.idProyecto);
  });

  celdaAcciones.append(
    enlaceFuncionalidades,
    document.createTextNode(" "),
    enlaceEditar,
    document.createTextNode(" "),
    botonEliminar
  );

  fila.append(
    crearCeldaTexto(seccion.idSeccion, "ID"),
    crearCeldaTexto(seccion.nombre, "Nombre"),
    crearCeldaTexto(seccion.orden, "Orden"),
    celdaAcciones
  );

  return fila;
}

async function cargarTablaSecciones(idProyecto) {
  const cuerpoTabla = document.getElementById("cuerpoTablaSecciones");

  try {
    const secciones = await peticionApi("/proyectos/" + idProyecto + "/secciones");

    if (secciones.length === 0) {
      cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">Este proyecto todavia no tiene secciones.</td></tr>";
      return;
    }

    cuerpoTabla.innerHTML = "";
    secciones.forEach(function (seccion) {
      cuerpoTabla.appendChild(crearFilaSeccion(seccion));
    });
  } catch (error) {
    cuerpoTabla.innerHTML = "<tr><td colspan=\"4\">No se pudo cargar la lista de secciones.</td></tr>";
  }
}

async function eliminarSeccion(idSeccion, idProyecto) {
  const confirmar = window.confirm("Seguro que deseas eliminar esta seccion? Tambien se eliminan sus funcionalidades.");
  if (!confirmar) {
    return;
  }

  try {
    await peticionApi("/secciones/" + idSeccion, { method: "DELETE" });
    cargarTablaSecciones(idProyecto);
  } catch (error) {
    window.alert("No se pudo eliminar la seccion.");
  }
}

async function inicializarFormularioSeccion(idProyecto) {
  const formulario = document.getElementById("formSeccion");
  const idSeccion = new URLSearchParams(window.location.search).get("id");
  const tituloFormulario = document.getElementById("tituloFormularioSeccion");
  const botonGuardar = document.getElementById("botonGuardarSeccion");
  const enlaceCancelar = document.getElementById("enlaceCancelarSeccion");
  const mensajeError = document.getElementById("mensajeErrorSeccion");

  enlaceCancelar.href = "secciones.html?idProyecto=" + idProyecto;

  if (idSeccion) {
    tituloFormulario.textContent = "Editar seccion";
    botonGuardar.textContent = "Guardar cambios";
    enlaceCancelar.hidden = false;

    try {
      const seccion = await peticionApi("/secciones/" + idSeccion);
      document.getElementById("idSeccion").value = seccion.idSeccion;
      document.getElementById("nombreSeccion").value = seccion.nombre;
      document.getElementById("ordenSeccion").value = seccion.orden;
    } catch (error) {
      mensajeError.textContent = "No se pudo cargar la seccion a editar.";
      mensajeError.hidden = false;
    }
  }

  formulario.addEventListener("submit", async function (evento) {
    evento.preventDefault();
    mensajeError.hidden = true;

    const datos = {
      nombre: document.getElementById("nombreSeccion").value.trim(),
      orden: Number(document.getElementById("ordenSeccion").value)
    };

    const idActual = document.getElementById("idSeccion").value;

    try {
      if (idActual) {
        await peticionApi("/secciones/" + idActual, {
          method: "PUT",
          body: JSON.stringify(datos)
        });
      } else {
        await peticionApi("/proyectos/" + idProyecto + "/secciones", {
          method: "POST",
          body: JSON.stringify(datos)
        });
      }
      window.location.href = "secciones.html?idProyecto=" + idProyecto;
    } catch (error) {
      mensajeError.textContent = "No se pudo guardar la seccion. Revisa los datos.";
      mensajeError.hidden = false;
    }
  });
}

document.addEventListener("DOMContentLoaded", function () {
  const usuario = requerirSesion();
  if (!usuario) {
    return;
  }

  const idProyecto = new URLSearchParams(window.location.search).get("idProyecto");
  const mensajeSinProyecto = document.getElementById("mensajeSinProyecto");
  const bloqueSecciones = document.getElementById("bloqueSecciones");
  const tituloSecciones = document.getElementById("tituloSecciones");

  if (!idProyecto) {
    mensajeSinProyecto.hidden = false;
    return;
  }

  bloqueSecciones.hidden = false;
  tituloSecciones.textContent = "Secciones del proyecto #" + idProyecto;

  cargarTablaSecciones(idProyecto);
  inicializarFormularioSeccion(idProyecto);
});
