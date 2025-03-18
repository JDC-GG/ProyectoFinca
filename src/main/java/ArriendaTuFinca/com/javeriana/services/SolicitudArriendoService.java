package ArriendaTuFinca.com.javeriana.services;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;
import ArriendaTuFinca.com.javeriana.repositories.SolicitudArriendoRepository;

import java.util.List;
import java.util.stream.Collectors;

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
        solicitudArriendo.setFechaSolicitud(solicitudArriendoDTO.getFechaSolicitud());
        solicitudArriendo.setEstado(solicitudArriendoDTO.getEstado());
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
}