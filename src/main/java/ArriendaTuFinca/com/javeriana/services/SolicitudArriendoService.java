package ArriendaTuFinca.com.javeriana.services;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.PropiedadRepository;
import ArriendaTuFinca.com.javeriana.repositories.SolicitudArriendoRepository;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SolicitudArriendoService {

    @Autowired
    private SolicitudArriendoRepository solicitudArriendoRepository;
    
    @Autowired
    private PropiedadRepository propiedadRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public SolicitudArriendoDTO crearSolicitud(SolicitudArriendoDTO solicitudDTO) {
        // Buscar la propiedad y usuario relacionados
        Propiedad propiedad = propiedadRepository.findById(solicitudDTO.getPropiedad().getId())
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        Usuario usuario = usuarioRepository.findById(solicitudDTO.getUsuario().getId())
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Crear la entidad SolicitudArriendo
        SolicitudArriendo solicitud = new SolicitudArriendo();
        solicitud.setPropiedad(propiedad);
        solicitud.setUsuario(usuario);
        solicitud.setFechaSolicitud(solicitudDTO.getFechaSolicitud());
        solicitud.setEstado(solicitudDTO.getEstado());
        
        // Guardar y retornar DTO
        solicitud = solicitudArriendoRepository.save(solicitud);
        return convertirASolicitudDTO(solicitud);
    }

    public SolicitudArriendoDTO obtenerSolicitudPorId(Long id) {
        SolicitudArriendo solicitud = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        return convertirASolicitudDTO(solicitud);
    }

    public SolicitudArriendoDTO actualizarSolicitud(Long id, SolicitudArriendoDTO solicitudDTO) {
        SolicitudArriendo solicitud = solicitudArriendoRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));
        
        // Actualizar relaciones si es necesario
        if (solicitudDTO.getPropiedad() != null && !solicitud.getPropiedad().getId().equals(solicitudDTO.getPropiedad().getId())) {
            Propiedad propiedad = propiedadRepository.findById(solicitudDTO.getPropiedad().getId())
                .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
            solicitud.setPropiedad(propiedad);
        }
        
        if (solicitudDTO.getUsuario() != null && !solicitud.getUsuario().getId().equals(solicitudDTO.getUsuario().getId())) {
            Usuario usuario = usuarioRepository.findById(solicitudDTO.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            solicitud.setUsuario(usuario);
        }
        
        solicitud.setFechaSolicitud(solicitudDTO.getFechaSolicitud());
        solicitud.setEstado(solicitudDTO.getEstado());
        
        solicitud = solicitudArriendoRepository.save(solicitud);
        return convertirASolicitudDTO(solicitud);
    }

    public void eliminarSolicitud(Long id) {
        solicitudArriendoRepository.deleteById(id);
    }

    public List<SolicitudArriendoDTO> listarTodasLasSolicitudes() {
        return solicitudArriendoRepository.findAll().stream()
            .map(this::convertirASolicitudDTO)
            .collect(Collectors.toList());
    }

    private SolicitudArriendoDTO convertirASolicitudDTO(SolicitudArriendo solicitud) {
        SolicitudArriendoDTO dto = new SolicitudArriendoDTO();
        dto.setId(solicitud.getId());
        
        // Convertir Propiedad a DTO básico
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setId(solicitud.getPropiedad().getId());
        propiedadDTO.setNombre(solicitud.getPropiedad().getNombre());
        dto.setPropiedad(propiedadDTO);
        
        // Convertir Usuario a DTO básico
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(solicitud.getUsuario().getId());
        usuarioDTO.setNombre(solicitud.getUsuario().getNombre());
        dto.setUsuario(usuarioDTO);
        
        dto.setFechaSolicitud(solicitud.getFechaSolicitud());
        dto.setEstado(solicitud.getEstado());
        
        return dto;
    }
}