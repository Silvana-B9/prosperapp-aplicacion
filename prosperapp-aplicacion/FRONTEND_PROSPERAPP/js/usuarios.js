/*
  usuarios.js
  Consume unicamente UsuarioController, incluido el login
  (POST /api/usuarios/login), que valida la contrasena en el backend
  con passwordEncoder.matches() y nunca expone el hash al frontend.
*/

document.addEventListener("DOMContentLoaded", function () {
  const formLogin = document.getElementById("formLogin");

  if (!formLogin) {
    return;
  }

  formLogin.addEventListener("submit", async function (evento) {
    evento.preventDefault();

    const correo = document.getElementById("usuario").value.trim();
    const clave = document.getElementById("clave").value.trim();
    const mensajeError = document.getElementById("mensajeError");
    const botonIngresar = formLogin.querySelector("button[type=submit]");

    if (!correo || !clave) {
      mensajeError.textContent = "Debes ingresar usuario y contrasena.";
      mensajeError.hidden = false;
      return;
    }

    mensajeError.hidden = true;
    botonIngresar.disabled = true;

    try {
      const usuario = await peticionApi("/usuarios/login", {
        method: "POST",
        body: JSON.stringify({ correo: correo, contrasena: clave })
      });

      guardarSesion({
        idUsuario: usuario.idUsuario,
        nombreUsuario: usuario.nombre,
        correo: usuario.correo
      });

      window.location.href = "pages/dashboard.html";
    } catch (error) {
      mensajeError.textContent = "Correo o contrasena incorrectos.";
      mensajeError.hidden = false;
    } finally {
      botonIngresar.disabled = false;
    }
  });
});
