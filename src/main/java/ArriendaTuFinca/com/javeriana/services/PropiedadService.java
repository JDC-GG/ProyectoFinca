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

    @Autowired private PropiedadRepository propiedadRepository;
    @Autowired private UsuarioRepository   usuarioRepository;

    /* ───────────────────── CRUD ───────────────────── */

    public PropiedadDTO crearPropiedad(PropiedadDTO dto) {

        Optional<Propiedad> existente =
            propiedadRepository.findByNombreAndDepartamentoAndMunicipio(
                    dto.getNombre(), dto.getDepartamento(), dto.getMunicipio());

        if (existente.isPresent())
            throw new RuntimeException("La propiedad ya existe");

        // ――― Propietario autenticado ―――
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario usuario = usuarioRepository.findByCorreo(auth.getName());
        if (usuario == null)
            throw new RuntimeException("Usuario no encontrado");

        Propiedad p = new Propiedad();
        copiarDtoAEntidad(dto, p);
        p.setUsuario(usuario);

        return convertir(propiedadRepository.save(p));
    }

    public PropiedadDTO obtenerPropiedadPorId(Long id) {
        Propiedad p = propiedadRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        return convertir(p);
    }

    public PropiedadDTO actualizarPropiedad(Long id, PropiedadDTO dto) {
        Propiedad p = propiedadRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Propiedad no encontrada"));
        copiarDtoAEntidad(dto, p);
        return convertir(propiedadRepository.save(p));
    }

    public void eliminarPropiedad(Long id) {
        propiedadRepository.deleteById(id);
    }

    public List<PropiedadDTO> listarTodasLasPropiedades() {
        return propiedadRepository.findAll()
                                  .stream()
                                  .map(this::convertir)
                                  .collect(Collectors.toList());
    }

    /* ───────────── Helpers de mapeo ───────────── */

    private PropiedadDTO convertir(Propiedad p) {
        PropiedadDTO dto = new PropiedadDTO();
        dto.setId(p.getId());
        dto.setNombre(p.getNombre());
        dto.setDepartamento(p.getDepartamento());
        dto.setMunicipio(p.getMunicipio());
        dto.setDescripcion(p.getDescripcion());
        dto.setHabitaciones(p.getHabitaciones());
        dto.setBanos(p.getBanos());
        dto.setMascotas(p.isMascotas());
        dto.setPiscina(p.isPiscina());
        dto.setAsador(p.isAsador());
        dto.setValorNoche(p.getValorNoche());
        dto.setTipoIngreso(p.getTipoIngreso());
        dto.setStatus(p.getStatus());
        dto.setIdUsuario(p.getUsuario() != null ? p.getUsuario().getId() : null);
        return dto;
    }

    private void copiarDtoAEntidad(PropiedadDTO dto, Propiedad p) {
        p.setNombre(dto.getNombre());
        p.setDepartamento(dto.getDepartamento());
        p.setMunicipio(dto.getMunicipio());
        p.setDescripcion(dto.getDescripcion());
        p.setHabitaciones(dto.getHabitaciones());
        p.setBanos(dto.getBanos());
        p.setMascotas(dto.isMascotas());
        p.setPiscina(dto.isPiscina());
        p.setAsador(dto.isAsador());
        p.setValorNoche(dto.getValorNoche());
        p.setTipoIngreso(dto.getTipoIngreso());
        p.setStatus(dto.getStatus());
    }
}
