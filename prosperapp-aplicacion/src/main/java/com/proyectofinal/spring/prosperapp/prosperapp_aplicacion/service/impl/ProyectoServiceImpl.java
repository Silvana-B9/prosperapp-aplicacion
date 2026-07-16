package com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.impl;

import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ColaboradorResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoRequest;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.dto.ProyectoResponse;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.EstadoProyecto;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Proyecto;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.ProyectoColaborador;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.ProyectoColaboradorId;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.RolColaborador;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Seccion;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.entity.Usuario;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.DuplicateResourceException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.exception.ResourceNotFoundException;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.ProyectoColaboradorRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.ProyectoRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.SeccionRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.repository.UsuarioRepository;
import com.proyectofinal.spring.prosperapp.prosperapp_aplicacion.service.ProyectoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ProyectoServiceImpl implements ProyectoService {

    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;
    private final SeccionRepository seccionRepository;
    private final ProyectoColaboradorRepository proyectoColaboradorRepository;

    @Override
    public ProyectoResponse crear(Integer idUsuario, ProyectoRequest request) {
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", idUsuario));

        Proyecto proyecto = Proyecto.builder()
                .usuario(usuario)
                .nombre(request.nombre())
                .descripcion(request.descripcion())
                .estado(request.estado() != null ? request.estado() : EstadoProyecto.activo)
                .build();
        proyecto = proyectoRepository.save(proyecto);

        Seccion seccionInicial = Seccion.builder()
                .proyecto(proyecto)
                .nombre("General")
                .orden(1)
                .build();
        seccionRepository.save(seccionInicial);

        ProyectoColaborador propietario = ProyectoColaborador.builder()
                .id(new ProyectoColaboradorId(proyecto.getIdProyecto(), usuario.getIdUsuario()))
                .proyecto(proyecto)
                .usuario(usuario)
                .rol(RolColaborador.propietario)
                .build();
        proyectoColaboradorRepository.save(propietario);

        return toResponse(proyecto);
    }

    @Override
    @Transactional(readOnly = true)
    public ProyectoResponse obtenerPorId(Integer idProyecto) {
        return toResponse(buscarOrLanzar(idProyecto));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarPorUsuario(Integer idUsuario) {
        return proyectoRepository.findByUsuario_IdUsuario(idUsuario).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarTodos() {
        return proyectoRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ProyectoResponse actualizar(Integer idProyecto, ProyectoRequest request) {
        Proyecto proyecto = buscarOrLanzar(idProyecto);

        proyecto.setNombre(request.nombre());
        proyecto.setDescripcion(request.descripcion());
        if (request.estado() != null) {
            proyecto.setEstado(request.estado());
        }

        return toResponse(proyectoRepository.save(proyecto));
    }

    @Override
    public void eliminar(Integer idProyecto) {
        Proyecto proyecto = buscarOrLanzar(idProyecto);
        proyectoRepository.delete(proyecto);
    }

    @Override
    public ColaboradorResponse agregarColaborador(Integer idProyecto, ColaboradorRequest request) {
        Proyecto proyecto = buscarOrLanzar(idProyecto);
        Usuario usuario = usuarioRepository.findById(request.idUsuario())
                .orElseThrow(() -> ResourceNotFoundException.of("Usuario", request.idUsuario()));

        if (proyectoColaboradorRepository.existsByProyecto_IdProyectoAndUsuario_IdUsuario(idProyecto, request.idUsuario())) {
            throw new DuplicateResourceException(
                    "El usuario " + request.idUsuario() + " ya es colaborador del proyecto " + idProyecto);
        }

        ProyectoColaborador colaborador = ProyectoColaborador.builder()
                .id(new ProyectoColaboradorId(idProyecto, request.idUsuario()))
                .proyecto(proyecto)
                .usuario(usuario)
                .rol(request.rol())
                .build();

        return toResponse(proyectoColaboradorRepository.save(colaborador));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ColaboradorResponse> listarColaboradores(Integer idProyecto) {
        return proyectoColaboradorRepository.findByProyecto_IdProyecto(idProyecto).stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public ColaboradorResponse actualizarRolColaborador(Integer idProyecto, Integer idUsuario, ColaboradorRequest request) {
        ProyectoColaborador colaborador = proyectoColaboradorRepository
                .findById(new ProyectoColaboradorId(idProyecto, idUsuario))
                .orElseThrow(() -> ResourceNotFoundException.of("Colaborador", idProyecto + "/" + idUsuario));

        colaborador.setRol(request.rol());

        return toResponse(proyectoColaboradorRepository.save(colaborador));
    }

    @Override
    public void eliminarColaborador(Integer idProyecto, Integer idUsuario) {
        ProyectoColaboradorId id = new ProyectoColaboradorId(idProyecto, idUsuario);
        if (!proyectoColaboradorRepository.existsById(id)) {
            throw ResourceNotFoundException.of("Colaborador", idProyecto + "/" + idUsuario);
        }
        proyectoColaboradorRepository.deleteById(id);
    }

    private Proyecto buscarOrLanzar(Integer idProyecto) {
        return proyectoRepository.findById(idProyecto)
                .orElseThrow(() -> ResourceNotFoundException.of("Proyecto", idProyecto));
    }

    private ProyectoResponse toResponse(Proyecto proyecto) {
        return new ProyectoResponse(
                proyecto.getIdProyecto(),
                proyecto.getUsuario().getIdUsuario(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getFechaCreacion(),
                proyecto.getEstado()
        );
    }

    private ColaboradorResponse toResponse(ProyectoColaborador colaborador) {
        return new ColaboradorResponse(
                colaborador.getProyecto().getIdProyecto(),
                colaborador.getUsuario().getIdUsuario(),
                colaborador.getUsuario().getNombre(),
                colaborador.getUsuario().getCorreo(),
                colaborador.getRol(),
                colaborador.getFechaIngreso()
        );
    }
}
