package ArriendaTuFinca.com.javeriana.services;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.SolicitudArriendoRepository;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;
import ArriendaTuFinca.com.javeriana.security.JwtService;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SolicitudArriendoService {

    @Autowired
    private SolicitudArriendoRepository solicitudArriendoRepository;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private JwtService jwtService;

    public SolicitudArriendoDTO crearSolicitud(SolicitudArriendoDTO solicitudArriendoDTO) {
        SolicitudArriendo solicitudArriendo = new SolicitudArriendo();
        solicitudArriendo.setPropiedadId(solicitudArriendoDTO.getPropiedadId());
        solicitudArriendo.setUsuarioId(solicitudArriendoDTO.getUsuarioId());
        solicitudArriendo.setFechaSolicitud(Date.valueOf(LocalDate.now()));
        solicitudArriendo.setEstado("PENDIENTE");

        solicitudArriendo = solicitudArriendoRepository.save(solicitudArriendo);
        return convertirASolicitudArriendoDTO(solicitudArriendo);
    }

    public SolicitudArriendoDTO obtenerSolicitudPorId(Long id) {
        SolicitudArriendo solicitudArriendo = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return convertirASolicitudArriendoDTO(solicitudArriendo);
    }

    public SolicitudArriendoDTO actualizarSolicitud(Long id, SolicitudArriendoDTO solicitudArriendoDTO) {
        SolicitudArriendo solicitudArriendo = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitudArriendo.setPropiedadId(solicitudArriendoDTO.getPropiedadId());
        solicitudArriendo.setUsuarioId(solicitudArriendoDTO.getUsuarioId());
        solicitudArriendo.setFechaSolicitud(solicitudArriendoDTO.getFechaSolicitud());
        solicitudArriendo.setEstado(solicitudArriendoDTO.getEstado());

        solicitudArriendo = solicitudArriendoRepository.save(solicitudArriendo);
        return convertirASolicitudArriendoDTO(solicitudArriendo);
    }

    public void eliminarSolicitud(Long id) {
        solicitudArriendoRepository.deleteById(id);
    }

    public List<SolicitudArriendoDTO> listarTodasLasSolicitudes() {
        return solicitudArriendoRepository.findAll().stream()
            .map(this::convertirASolicitudArriendoDTO)
            .collect(Collectors.toList());
    }

    public SolicitudArriendoDTO aceptarSolicitud(Long id) {
        SolicitudArriendo solicitud = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado("PENDIENTE_PAGO");
        solicitud = solicitudArriendoRepository.save(solicitud);
        return convertirASolicitudArriendoDTO(solicitud);
    }

    public SolicitudArriendoDTO rechazarSolicitud(Long id) {
        SolicitudArriendo solicitud = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        solicitud.setEstado("RECHAZADA");
        solicitud = solicitudArriendoRepository.save(solicitud);
        return convertirASolicitudArriendoDTO(solicitud);
    }

    public List<SolicitudArriendoDTO> listarSolicitudesRecibidas(Long idDueno) {
        System.out.println("🟢 ID del dueño recibido: " + idDueno);

        List<Long> idsPropiedades = propiedadService.obtenerIdsPropiedadesPorDueno(idDueno);
        System.out.println("🟦 IDs de propiedades del dueño: " + idsPropiedades);

        if (idsPropiedades == null || idsPropiedades.isEmpty()) {
            System.out.println("⚠️ El dueño no tiene propiedades registradas.");
            return List.of();
        }

        List<SolicitudArriendo> solicitudes = solicitudArriendoRepository.findByPropiedadIdIn(idsPropiedades);
        System.out.println("✅ Cantidad de solicitudes encontradas: " + solicitudes.size());

        return solicitudes.stream()
            .map(this::convertirASolicitudArriendoDTO)
            .collect(Collectors.toList());
    }

    public List<SolicitudArriendoDTO> listarSolicitudesDelUsuarioActual() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correo = auth.getName().trim().toLowerCase();

        System.out.println("🟡 Usuario autenticado (correo): " + correo);

        Usuario usuario = usuarioRepository.findByCorreo(correo);
        if (usuario == null) {
            System.out.println("🔴 No se encontró el usuario con correo: " + correo);
            throw new RuntimeException("Usuario no autenticado");
        }

        List<SolicitudArriendo> solicitudes = solicitudArriendoRepository.findByUsuarioId(usuario.getId());
        return solicitudes.stream()
            .map(this::convertirASolicitudArriendoDTO)
            .collect(Collectors.toList());
    }

    private SolicitudArriendoDTO convertirASolicitudArriendoDTO(SolicitudArriendo solicitudArriendo) {
        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(solicitudArriendo.getId());
        dto.setPropiedadId(solicitudArriendo.getPropiedadId());
        dto.setUsuarioId(solicitudArriendo.getUsuarioId());
        dto.setFechaSolicitud(solicitudArriendo.getFechaSolicitud());
        dto.setEstado(solicitudArriendo.getEstado());
        return dto;
    }
}
