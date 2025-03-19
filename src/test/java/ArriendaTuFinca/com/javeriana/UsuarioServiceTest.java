package ArriendaTuFinca.com.javeriana;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void eliminarUsuario_DebeEliminarCorrectamente() {
        // 1. Crear usuario
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Juan Pérez");
        usuarioDTO.setEmail("juan@gmail.com");
        usuarioDTO.setRol("ARRENDADOR");
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);

        // 2. Verificar creación
        List<UsuarioDTO> usuariosAntes = usuarioService.listarTodosLosUsuarios();
        assertEquals(1, usuariosAntes.size(), "Debe existir 1 usuario antes de eliminar");

        // 3. Eliminar usuario
        usuarioService.eliminarUsuario(usuarioCreado.getId());

        // 4. Verificar lista vacía
        List<UsuarioDTO> usuariosDespues = usuarioService.listarTodosLosUsuarios();
        assertTrue(usuariosDespues.isEmpty(), "La lista debe estar vacía después de eliminar");

        // 5. Intentar obtener usuario eliminado
        try {
            usuarioService.obtenerUsuarioPorId(usuarioCreado.getId());
            fail("Debería haber lanzado una excepción");
        } catch (RuntimeException ex) {
            assertEquals("Usuario no encontrado", ex.getMessage());
        }
    }
}