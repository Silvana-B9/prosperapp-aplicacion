package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.LoginRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.UsuarioResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Usuario;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.DuplicateResourceException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.InvalidCredentialsException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.UsuarioRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UsuarioResponse crear(UsuarioRequest request) {
        if (usuarioRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con el correo: " + request.correo());
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.nombre())
                .correo(request.correo())
                .contrasenaHash(passwordEncoder.encode(request.contrasena()))
                .build();

        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.correo())
                .orElseThrow(() -> new InvalidCredentialsException("Correo o contrasena incorrectos"));

        if (!passwordEncoder.matches(request.contrasena(), usuario.getContrasenaHash())) {
            throw new InvalidCredentialsException("Correo o contrasena incorrectos");
        }

        return toResponse(usuario);
    }

    @Override
    @Transactional(readOnly = true)
    public UsuarioResponse obtenerPorId(Integer idUsuario) {
        return toResponse(buscarOrLanzar(idUsuario));
    }

    @Override
    @Transactional(readOnly = true)
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public UsuarioResponse actualizar(Integer idUsuario, UsuarioRequest request) {
        Usuario usuario = buscarOrLanzar(idUsuario);

        if (!usuario.getCorreo().equals(request.correo()) && usuarioRepository.existsByCorreo(request.correo())) {
            throw new DuplicateResourceException("Ya existe un usuario registrado con el correo: " + request.correo());
        }

        usuario.setNombre(request.nombre());
        usuario.setCorreo(request.correo());
        usuario.setContrasenaHash(passwordEncoder.encode(request.contrasena()));

        return toResponse(usuarioRepository.save(usuario));
    }

    @Override
    public void eliminar(Integer idUsuario) {
        Usuario usuario = buscarOrLanzar(idUsuario);
        usuarioRepository.delete(usuario);
    }

    private Usuario buscarOrLanzar(Integer idUsuario) {
        return usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", idUsuario));
    }

    private UsuarioResponse toResponse(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getIdUsuario(),
                usuario.getNombre(),
                usuario.getCorreo(),
                usuario.getFechaRegistro()
        );
    }
}
