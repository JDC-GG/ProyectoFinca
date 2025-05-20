package ArriendaTuFinca.com.javeriana;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    void limpiarUsuarios() {
        // Limpiar los usuarios antes de cada prueba
        usuarioService.listarTodosLosUsuarios().forEach(usuario -> usuarioService.eliminarUsuario(usuario.getId()));
    }

    @Test
    void eliminarUsuario_DebeEliminarCorrectamente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan");
        usuarioDTO.setApellido("Pérez");
        usuarioDTO.setTelefono("1234567890");
        usuarioDTO.setCorreo("juan@gmail.com");
        usuarioDTO.setContrasena("12345678");
        usuarioDTO.setRol("ARRENDADOR");

        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);

        // Verificar creación
        List<UsuarioDTO> usuariosAntes = usuarioService.listarTodosLosUsuarios();
        assertEquals(1, usuariosAntes.size(), "Debe existir 1 usuario antes de eliminar");

        usuarioService.eliminarUsuario(usuarioCreado.getId());

        List<UsuarioDTO> usuariosDespues = usuarioService.listarTodosLosUsuarios();
        assertTrue(usuariosDespues.isEmpty(), "La lista debe estar vacía después de eliminar");

        try {
            usuarioService.obtenerUsuarioPorId(usuarioCreado.getId());
            fail("Debería haber lanzado una excepción");
        } catch (RuntimeException ex) {
            assertEquals("Usuario no encontrado", ex.getMessage());
        }
    }

    @Test
    void crearUsuario_DebeAsignarRolCorrectamente() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Carlos");
        usuarioDTO.setApellido("López");
        usuarioDTO.setTelefono("987654321");
        usuarioDTO.setCorreo("carlos@gmail.com");
        usuarioDTO.setContrasena("12345678");
        usuarioDTO.setRol("ARRENDADOR");

        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);

        assertEquals("ARRENDADOR", usuarioCreado.getRol());
    }

    @Test
    void actualizarUsuario_DebeCambiarEmailCorrectamente() {
        UsuarioDTO usuarioCreado = crearUsuarioDePrueba();

        usuarioCreado.setCorreo("David@gmail.com");
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(usuarioCreado.getId(), usuarioCreado);

        assertEquals("David@gmail.com", usuarioActualizado.getCorreo());
    }

    private UsuarioDTO crearUsuarioDePrueba() {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Usuario");
        usuarioDTO.setApellido("Prueba");
        usuarioDTO.setTelefono("111222333");
        usuarioDTO.setCorreo("prueba@gmail.com");
        usuarioDTO.setContrasena("12345678");
        usuarioDTO.setRol("ARRENDATARIO");
        return usuarioService.crearUsuario(usuarioDTO);
    }
}
