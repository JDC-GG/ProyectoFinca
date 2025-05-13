package ArriendaTuFinca.com.javeriana.services;

import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public UsuarioDTO crearUsuario(UsuarioDTO usuarioDTO) {
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setRol(usuarioDTO.getRol() != null ? usuarioDTO.getRol() : "USUARIO");
        usuario = usuarioRepository.save(usuario);
        return convertirAUsuarioDTO(usuario);
    }

    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return convertirAUsuarioDTO(usuario);
    }

    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO usuarioDTO) {
        Usuario usuario = usuarioRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setApellido(usuarioDTO.getApellido());
        usuario.setTelefono(usuarioDTO.getTelefono());
        usuario.setCorreo(usuarioDTO.getCorreo());
        usuario.setContrasena(usuarioDTO.getContrasena());
        usuario.setRol(usuarioDTO.getRol());

        usuario = usuarioRepository.save(usuario);
        return convertirAUsuarioDTO(usuario);
    }

    public void eliminarUsuario(Long id) {
        usuarioRepository.deleteById(id);
    }

    public List<UsuarioDTO> listarTodosLosUsuarios() {
        return usuarioRepository.findAll().stream()
            .map(this::convertirAUsuarioDTO)
            .toList();
    }

    private UsuarioDTO convertirAUsuarioDTO(Usuario usuario) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setContrasena(usuario.getContrasena());
        usuarioDTO.setRol(usuario.getRol());
        return usuarioDTO;
    }

    // Nuevo método para login
    public UsuarioDTO login(String correo, String contrasena) {
        Usuario usuario = usuarioRepository.findByCorreo(correo)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return convertirAUsuarioDTO(usuario);
    }
}
