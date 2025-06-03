package ArriendaTuFinca.com.javeriana.services;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.PropiedadRepository;
import ArriendaTuFinca.com.javeriana.repositories.SolicitudArriendoRepository;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;

@Service
public class SolicitudArriendoService {

    private final SolicitudArriendoRepository solicitudRepo;
    private final UsuarioRepository usuarioRepo;
    private final PropiedadRepository propiedadRepo;

    public SolicitudArriendoService(
            SolicitudArriendoRepository solicitudRepo,
            UsuarioRepository usuarioRepo,
            PropiedadRepository propiedadRepo
    ) {
        this.solicitudRepo = solicitudRepo;
        this.usuarioRepo = usuarioRepo;
        this.propiedadRepo = propiedadRepo;
    }

    @Transactional
    public SolicitudArriendoDTO crearSolicitud(SolicitudArriendoDTO dto, String correoUsuario) {
        Usuario usuario = usuarioRepo.findByCorreo(correoUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Propiedad propiedad = propiedadRepo.findById(dto.getPropiedadId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        LocalDate hoy = LocalDate.now();
        LocalDate llegada = dto.getFechaLlegada();
        LocalDate salida = dto.getFechaSalida();

        if (llegada.isBefore(hoy)) {
            throw new RuntimeException("La fecha de llegada no puede ser anterior a hoy");
        }
        if (!salida.isAfter(llegada)) {
            throw new RuntimeException("La fecha de salida debe ser al menos un día posterior a la llegada");
        }
        if (dto.getCantidadPersonas() > propiedad.getHabitaciones()) {
            throw new RuntimeException("La cantidad de personas excede la capacidad de la propiedad");
        }

        long dias = ChronoUnit.DAYS.between(llegada, salida);
        double valorTotal = dias * propiedad.getValorNoche();

        SolicitudArriendo entidad = new SolicitudArriendo();
        entidad.setUsuario(usuario);
        entidad.setPropiedad(propiedad);
        entidad.setFechaSolicitud(LocalDateTime.now());
        entidad.setFechaLlegada(llegada);
        entidad.setFechaSalida(salida);
        entidad.setCantidadPersonas(dto.getCantidadPersonas());
        entidad.setValorTotal(valorTotal);
        entidad.setEstado(SolicitudArriendo.EstadoSolicitud.POR_ACEPTAR);

        SolicitudArriendo guardada = solicitudRepo.save(entidad);

        SolicitudArriendoDTO respuesta = new SolicitudArriendoDTO();
        respuesta.setId(guardada.getId());
        respuesta.setNombrePropiedad(propiedad.getNombre());
        respuesta.setFechaSolicitud(guardada.getFechaSolicitud());
        respuesta.setFechaLlegada(guardada.getFechaLlegada());
        respuesta.setFechaSalida(guardada.getFechaSalida());
        respuesta.setCantidadPersonas(guardada.getCantidadPersonas());
        respuesta.setValorTotal(guardada.getValorTotal());
        respuesta.setEstado(guardada.getEstado().name());
        respuesta.setUsuarioId(usuario.getId());
        respuesta.setPropiedadId(propiedad.getId());
        respuesta.setNombreUsuario(usuario.getNombre());

        return respuesta;
    }

    @Transactional(readOnly = true)
    public List<SolicitudArriendoDTO> listarSolicitudesPorUsuario(String correoUsuario) {
        Usuario usuario = usuarioRepo.findByCorreo(correoUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        List<SolicitudArriendo> lista =
            solicitudRepo.findAllByUsuarioIdOrderByFechaSolicitudDesc(usuario.getId());

        return lista.stream()
                .map(sol -> {
                    SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
                    dto.setId(sol.getId());
                    dto.setNombrePropiedad(sol.getPropiedad().getNombre());
                    dto.setFechaSolicitud(sol.getFechaSolicitud());
                    dto.setFechaLlegada(sol.getFechaLlegada());
                    dto.setFechaSalida(sol.getFechaSalida());
                    dto.setCantidadPersonas(sol.getCantidadPersonas());
                    dto.setValorTotal(sol.getValorTotal());
                    dto.setEstado(sol.getEstado().name());
                    dto.setUsuarioId(usuario.getId());
                    dto.setPropiedadId(sol.getPropiedad().getId());
                    dto.setNombreUsuario(usuario.getNombre());

                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Transactional
    public SolicitudArriendoDTO aceptarSolicitud(Long solicitudId) {
        SolicitudArriendo sol = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Sólo se puede aceptar si estaba POR_ACEPTAR
        if (sol.getEstado() != SolicitudArriendo.EstadoSolicitud.POR_ACEPTAR) {
            throw new RuntimeException("Solo se puede aceptar una solicitud en estado POR_ACEPTAR");
        }

        sol.setEstado(SolicitudArriendo.EstadoSolicitud.PENDIENTE_PAGO);
        SolicitudArriendo actualizar = solicitudRepo.save(sol);

        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(actualizar.getId());
        dto.setNombrePropiedad(actualizar.getPropiedad().getNombre());
        dto.setFechaSolicitud(actualizar.getFechaSolicitud());
        dto.setFechaLlegada(actualizar.getFechaLlegada());
        dto.setFechaSalida(actualizar.getFechaSalida());
        dto.setCantidadPersonas(actualizar.getCantidadPersonas());
        dto.setValorTotal(actualizar.getValorTotal());
        dto.setEstado(actualizar.getEstado().name());
        dto.setUsuarioId(actualizar.getUsuario().getId());
        dto.setPropiedadId(actualizar.getPropiedad().getId());
        dto.setNombreUsuario(actualizar.getUsuario().getNombre());

        return dto;
    }

    @Transactional
    public SolicitudArriendoDTO rechazarSolicitud(Long solicitudId) {
        SolicitudArriendo sol = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Sólo se puede rechazar si estaba POR_ACEPTAR
        if (sol.getEstado() != SolicitudArriendo.EstadoSolicitud.POR_ACEPTAR) {
            throw new RuntimeException("Solo se puede rechazar una solicitud en estado POR_ACEPTAR");
        }

        sol.setEstado(SolicitudArriendo.EstadoSolicitud.RECHAZADA);
        SolicitudArriendo actualizar = solicitudRepo.save(sol);

        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(actualizar.getId());
        dto.setNombrePropiedad(actualizar.getPropiedad().getNombre());
        dto.setFechaSolicitud(actualizar.getFechaSolicitud());
        dto.setFechaLlegada(actualizar.getFechaLlegada());
        dto.setFechaSalida(actualizar.getFechaSalida());
        dto.setCantidadPersonas(actualizar.getCantidadPersonas());
        dto.setValorTotal(actualizar.getValorTotal());
        dto.setEstado(actualizar.getEstado().name());
        dto.setUsuarioId(actualizar.getUsuario().getId());
        dto.setPropiedadId(actualizar.getPropiedad().getId());
        dto.setNombreUsuario(actualizar.getUsuario().getNombre());

        return dto;
    }

    @Transactional
    public SolicitudArriendoDTO marcarComoPagada(Long solicitudId) {
        SolicitudArriendo sol = solicitudRepo.findById(solicitudId)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Solo puede marcarse pagada si estaba PENDIENTE_PAGO
        if (sol.getEstado() != SolicitudArriendo.EstadoSolicitud.PENDIENTE_PAGO) {
            throw new RuntimeException("Solo se puede marcar como pagada una solicitud en estado PENDIENTE_PAGO");
        }

        sol.setEstado(SolicitudArriendo.EstadoSolicitud.ACEPTADA);
        SolicitudArriendo actualizar = solicitudRepo.save(sol);

        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(actualizar.getId());
        dto.setNombrePropiedad(actualizar.getPropiedad().getNombre());
        dto.setFechaSolicitud(actualizar.getFechaSolicitud());
        dto.setFechaLlegada(actualizar.getFechaLlegada());
        dto.setFechaSalida(actualizar.getFechaSalida());
        dto.setCantidadPersonas(actualizar.getCantidadPersonas());
        dto.setValorTotal(actualizar.getValorTotal());
        dto.setEstado(actualizar.getEstado().name());
        dto.setUsuarioId(actualizar.getUsuario().getId());
        dto.setPropiedadId(actualizar.getPropiedad().getId());
        dto.setNombreUsuario(actualizar.getUsuario().getNombre());

        return dto;
    }

    /**
     * Lista todas las solicitudes de arriendo cuyas propiedades
     * pertenecen al dueño con id = usuarioId (arrendador).
     * Las ordena de la más reciente a la más antigua por fechaSolicitud.
     */
    @Transactional(readOnly = true)
    public List<SolicitudArriendoDTO> listarSolicitudesPorPropietario(Long usuarioId) {
        List<SolicitudArriendo> listaEntidades =
            solicitudRepo.findAllByPropiedadUsuarioIdOrderByFechaSolicitudDesc(usuarioId);

        return listaEntidades.stream().map(sol -> {
            SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
            dto.setId(sol.getId());
            dto.setPropiedadId(sol.getPropiedad().getId());
            dto.setNombrePropiedad(sol.getPropiedad().getNombre());
            dto.setFechaSolicitud(sol.getFechaSolicitud());
            dto.setFechaLlegada(sol.getFechaLlegada());
            dto.setFechaSalida(sol.getFechaSalida());
            dto.setCantidadPersonas(sol.getCantidadPersonas());
            dto.setValorTotal(sol.getValorTotal());
            dto.setEstado(sol.getEstado().name());
            dto.setUsuarioId(sol.getUsuario().getId());

            // <<< Poblamos el nombre del solicitante >>>
            dto.setNombreUsuario(sol.getUsuario().getNombre());

            return dto;
        }).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SolicitudArriendoDTO obtenerSolicitudPorId(Long solicitudId) {
        SolicitudArriendo sol = solicitudRepo.findById(solicitudId)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        // Mapeo a DTO, exactamente igual que haces en aceptar/rechazar/marcarComoPagada
        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(sol.getId());
        dto.setNombrePropiedad(sol.getPropiedad().getNombre());
        dto.setFechaSolicitud(sol.getFechaSolicitud());
        dto.setFechaLlegada(sol.getFechaLlegada());
        dto.setFechaSalida(sol.getFechaSalida());
        dto.setCantidadPersonas(sol.getCantidadPersonas());
        dto.setValorTotal(sol.getValorTotal());
        dto.setEstado(sol.getEstado().name());
        dto.setUsuarioId(sol.getUsuario().getId());
        dto.setPropiedadId(sol.getPropiedad().getId());
        dto.setNombreUsuario(sol.getUsuario().getNombre()); 

        return dto;
    }
}
