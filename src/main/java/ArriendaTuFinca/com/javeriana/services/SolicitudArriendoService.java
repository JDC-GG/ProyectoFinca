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

    public SolicitudArriendoService(SolicitudArriendoRepository solicitudRepo,UsuarioRepository usuarioRepo, PropiedadRepository propiedadRepo) {
        this.solicitudRepo = solicitudRepo;
        this.usuarioRepo = usuarioRepo;
        this.propiedadRepo = propiedadRepo;
    }

    /**
     * Crea una nueva solicitud de arriendo respetando las validaciones:
     * 1) fechaLlegada >= hoy
     * 2) fechaSalida > fechaLlegada
     * 3) cantidadPersonas <= capacidad (número de habitaciones)
     * Luego calcula el valor total (días * valorNoche) y guarda el estado "PENDIENTE_PAGO".
     *
     * @param dto           Objeto DTO que trae: propiedadId, fechaLlegada, fechaSalida, cantidadPersonas
     * @param correoUsuario El correo del usuario logueado (se extrae del token JWT)
     * @return Un DTO con todos los datos de la solicitud recién creada
     */
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
        entidad.setEstado(SolicitudArriendo.EstadoSolicitud.PENDIENTE_PAGO);

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

        return respuesta;
    }

    /**
     * Lista todas las solicitudes del usuario identificado por 'correoUsuario',
     * ordenadas de la más reciente a la más antigua (por fechaSolicitud DESC).
     *
     * @param correoUsuario Correo del arrendatario
     * @return Lista de DTOs, cada uno con nombrePropiedad, fechas, valor, estado, etc.
     */
    @Transactional(readOnly = true)
    public List<SolicitudArriendoDTO> listarSolicitudesPorUsuario(String correoUsuario) {
        Usuario usuario = usuarioRepo.findByCorreo(correoUsuario);
        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        List<SolicitudArriendo> listaEntidades =
            solicitudRepo.findAllByUsuarioIdOrderByFechaSolicitudDesc(usuario.getId());

        return listaEntidades.stream()
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
                return dto;
            })
            .collect(Collectors.toList());
    }
}
