package ArriendaTuFinca.com.javeriana.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;
import ArriendaTuFinca.com.javeriana.repositories.PropiedadRepository;
@Service
public class PropiedadService {
    
    @Autowired
    private PropiedadRepository propiedadRepository;

    public PropiedadDTO crearPropiedad(PropiedadDTO propiedadDTO) {
        // Validar si la propiedad ya existe
        Optional<Propiedad> propiedadExistente = propiedadRepository.findByNombreAndUbicacion(
            propiedadDTO.getNombre(), propiedadDTO.getUbicacion());
        
        if (propiedadExistente.isPresent()) {
            throw new RuntimeException("La propiedad ya existe");
        }

        // Crear la propiedad si no existe
        Propiedad propiedad = new Propiedad();
        propiedad.setNombre(propiedadDTO.getNombre());
        propiedad.setUbicacion(propiedadDTO.getUbicacion());
        propiedad.setPrecio(propiedadDTO.getPrecio());
        propiedad.setId_usuario(propiedadDTO.getId_usuario()); 
        propiedad = propiedadRepository.save(propiedad);
        return convertirAPropiedadDTO(propiedad);
    }

    // Obtener propiedad por ID
    public PropiedadDTO obtenerPropiedadPorId(Long id) {
        Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        return convertirAPropiedadDTO(propiedad);
    }

    // Actualizar propiedad
    public PropiedadDTO actualizarPropiedad(Long id, PropiedadDTO propiedadDTO) {
        Propiedad propiedad = propiedadRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        propiedad.setNombre(propiedadDTO.getNombre());
        propiedad.setUbicacion(propiedadDTO.getUbicacion());
        propiedad.setPrecio(propiedadDTO.getPrecio());
        propiedad = propiedadRepository.save(propiedad);
        return convertirAPropiedadDTO(propiedad);
    }

    // Eliminar propiedad
    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }

    // Listar todas las propiedades
    public List<PropiedadDTO> listarTodasLasPropiedades() {
        return propiedadRepository.findAll().stream()
            .map(this::convertirAPropiedadDTO)
            .collect(Collectors.toList());
    }

    // Convertir entidad a DTO
    private PropiedadDTO convertirAPropiedadDTO(Propiedad propiedad) {
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setId(propiedad.getId());
        propiedadDTO.setNombre(propiedad.getNombre());
        propiedadDTO.setUbicacion(propiedad.getUbicacion());
        propiedadDTO.setPrecio(propiedad.getPrecio());
        propiedadDTO.setId_usuario(propiedad.getId_usuario());
        return propiedadDTO;
    }
}