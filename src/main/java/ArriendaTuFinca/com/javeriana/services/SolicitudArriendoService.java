package ArriendaTuFinca.com.javeriana.services;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;
import ArriendaTuFinca.com.javeriana.repositories.SolicitudArriendoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.sql.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class SolicitudArriendoService {

    @Autowired
    private SolicitudArriendoRepository solicitudArriendoRepository;

  public SolicitudArriendoDTO crearSolicitud(SolicitudArriendoDTO solicitudArriendoDTO) {
    SolicitudArriendo solicitudArriendo = new SolicitudArriendo();
    solicitudArriendo.setPropiedadId(solicitudArriendoDTO.getPropiedadId());
    solicitudArriendo.setUsuarioId(solicitudArriendoDTO.getUsuarioId());

    // 🔥 Agrega la fecha de hoy automáticamente
    solicitudArriendo.setFechaSolicitud(Date.valueOf(LocalDate.now()));

    // 🔥 Estado inicial "PENDIENTE"
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

    private SolicitudArriendoDTO convertirASolicitudArriendoDTO(SolicitudArriendo solicitudArriendo) {
        SolicitudArriendoDTO solicitudArriendoDTO = new SolicitudArriendoDTO();
        solicitudArriendoDTO.setId(solicitudArriendo.getId());
        solicitudArriendoDTO.setPropiedadId(solicitudArriendo.getPropiedadId());
        solicitudArriendoDTO.setUsuarioId(solicitudArriendo.getUsuarioId());
        solicitudArriendoDTO.setFechaSolicitud(solicitudArriendo.getFechaSolicitud());
        solicitudArriendoDTO.setEstado(solicitudArriendo.getEstado());
        return solicitudArriendoDTO;
    }
    

    @Autowired
private PropiedadService propiedadService; // Asegúrate que tienes esto para acceder a las propiedades

public List<SolicitudArriendoDTO> listarSolicitudesRecibidas(Long idDueno) {
    // 1. Obtener las propiedades del dueño
    List<Long> idsPropiedades = propiedadService.obtenerIdsPropiedadesPorDueno(idDueno);

    // 2. Buscar las solicitudes asociadas a esas propiedades
    List<SolicitudArriendo> solicitudes = solicitudArriendoRepository.findByPropiedadIdIn(idsPropiedades);

    // 3. Convertir a DTOs
    return solicitudes.stream()
        .map(this::convertirASolicitudArriendoDTO)
        .collect(Collectors.toList());
}

public SolicitudArriendoDTO aceptarSolicitud(Long id) {
    SolicitudArriendo solicitud = solicitudArriendoRepository.findById(id)
        .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

    solicitud.setEstado("ACEPTADA");
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


}