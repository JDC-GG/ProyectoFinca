package ArriendaTuFinca.com.javeriana.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.PropiedadRepository;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class PropiedadService {

    @Autowired
    private PropiedadRepository propiedadRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public PropiedadDTO crearPropiedad(PropiedadDTO propiedadDTO) {
        Optional<Propiedad> propiedadExistente = propiedadRepository.findByNombreAndUbicacion(
            propiedadDTO.getNombre(), propiedadDTO.getUbicacion());

        if (propiedadExistente.isPresent()) {
            throw new RuntimeException("La propiedad ya existe");
        }

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String correo = authentication.getName();
        Usuario usuario = usuarioRepository.findByCorreo(correo);

        if (usuario == null) {
            throw new RuntimeException("Usuario no encontrado");
        }

        Propiedad propiedad = new Propiedad();
        propiedad.setNombre(propiedadDTO.getNombre());
        propiedad.setUbicacion(propiedadDTO.getUbicacion());
        propiedad.setPrecio(propiedadDTO.getPrecio());
        propiedad.setUsuario(usuario);

        propiedad = propiedadRepository.save(propiedad);
        return convertirAPropiedadDTO(propiedad);
    }

    public PropiedadDTO obtenerPropiedadPorId(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        return convertirAPropiedadDTO(propiedad);
    }

    public PropiedadDTO actualizarPropiedad(Long id, PropiedadDTO propiedadDTO) {
        Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));

        propiedad.setNombre(propiedadDTO.getNombre());
        propiedad.setUbicacion(propiedadDTO.getUbicacion());
        propiedad.setPrecio(propiedadDTO.getPrecio());

        propiedad = propiedadRepository.save(propiedad);
        return convertirAPropiedadDTO(propiedad);
    }

    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }

    public List<PropiedadDTO> listarTodasLasPropiedades() {
        return propiedadRepository.findAll().stream()
            .map(this::convertirAPropiedadDTO)
            .collect(Collectors.toList());
    }

    private PropiedadDTO convertirAPropiedadDTO(Propiedad propiedad) {
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setId(propiedad.getId());
        propiedadDTO.setNombre(propiedad.getNombre());
        propiedadDTO.setUbicacion(propiedad.getUbicacion());
        propiedadDTO.setPrecio(propiedad.getPrecio());
        propiedadDTO.setId_usuario(
            propiedad.getUsuario() != null ? propiedad.getUsuario().getId() : null
        );
        return propiedadDTO;
    }
}
