package ArriendaTuFinca.com.javeriana;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    private UsuarioDTO crearUsuarioValido() {
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre("Test");
        usuario.setApellido("Usuario");
        usuario.setTelefono("1234567890");
        usuario.setCorreo("test" + System.currentTimeMillis() + "@example.com");
        usuario.setContrasena("password123");
        usuario.setRol("ARRENDATARIO");
        return usuario;
    }

    @Test
    void crearUsuario_ConDatosValidos_RetornaUsuarioConId() {
        UsuarioDTO usuario = crearUsuarioValido();
        
        UsuarioDTO resultado = usuarioService.crearUsuario(usuario);
        
        assertNotNull(resultado.getId());
        assertEquals(usuario.getNombre(), resultado.getNombre());
    }

    @Test
    void obtenerUsuarioPorId_ConIdExistente_RetornaUsuarioCorrecto() {
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(crearUsuarioValido());
        
        UsuarioDTO usuarioObtenido = usuarioService.obtenerUsuarioPorId(usuarioCreado.getId());
        
        assertEquals(usuarioCreado.getId(), usuarioObtenido.getId());
    }

    @Test
    void actualizarUsuario_ConNuevosDatos_ActualizaCorrectamente() {
        UsuarioDTO usuarioOriginal = usuarioService.crearUsuario(crearUsuarioValido());
        String nuevoNombre = "Nombre Actualizado";
        
        usuarioOriginal.setNombre(nuevoNombre);
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(usuarioOriginal.getId(), usuarioOriginal);
        
        assertEquals(nuevoNombre, usuarioActualizado.getNombre());
    }

    @Test
    void eliminarUsuario_ConIdExistente_ElUsuarioNoDebeExistir() {
        UsuarioDTO usuario = usuarioService.crearUsuario(crearUsuarioValido());
        
        usuarioService.eliminarUsuario(usuario.getId());
        
        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerUsuarioPorId(usuario.getId());
        });
    }

    @Test
    void listarUsuarios_ConUsuariosExistentes_RetornaListaNoVacia() {
        usuarioService.crearUsuario(crearUsuarioValido());
        usuarioService.crearUsuario(crearUsuarioValido());
        
        var usuarios = usuarioService.listarTodosLosUsuarios();
        
        assertFalse(usuarios.isEmpty());
        assertTrue(usuarios.size() >= 2);
    }

    @Test
    void crearUsuario_SinApellido_LanzaExcepcion() {
        UsuarioDTO usuarioInvalido = crearUsuarioValido();
        usuarioInvalido.setApellido(null);
        
        assertThrows(DataIntegrityViolationException.class, () -> {
            usuarioService.crearUsuario(usuarioInvalido);
        });
    }
}