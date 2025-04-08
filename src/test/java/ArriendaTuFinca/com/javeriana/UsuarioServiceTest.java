package ArriendaTuFinca.com.javeriana;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@ActiveProfiles("test") // Usa configuración específica para tests
@Transactional // Cada test se ejecuta en una transacción que se revierte al final
class UsuarioServiceTest {

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    void limpiarBaseDeDatos() {
        // Elimina todos los usuarios antes de cada test
        usuarioService.listarTodosLosUsuarios().forEach(usuario -> {
            usuarioService.eliminarUsuario(usuario.getId());
        });
    }

    @Test
    void eliminarUsuario_DebeEliminarCorrectamente() {
        // 1. PREPARACIÓN: Crear usuario de prueba
        UsuarioDTO usuarioCreado = crearUsuarioDePrueba("Juan Pérez", "juan@gmail.com", "ARRENDADOR");
        
        // 2. EJECUCIÓN: Eliminar el usuario
        Long idUsuario = usuarioCreado.getId();
        usuarioService.eliminarUsuario(idUsuario);
        
        // 3. VERIFICACIÓN: Comprobar que ya no existe
        assertThrows(RuntimeException.class, () -> {
            usuarioService.obtenerUsuarioPorId(idUsuario);
        }, "Debería lanzar excepción al buscar usuario eliminado");
        
        // Verificar que la lista está vacía
        assertTrue(usuarioService.listarTodosLosUsuarios().isEmpty(), 
                 "La lista de usuarios debería estar vacía después de eliminar");
    }

    @Test
    void crearUsuario_DebeAsignarRolCorrectamente() {
        // 1. PREPARACIÓN: Datos de entrada
        String rolEsperado = "ARRENDADOR";
        
        // 2. EJECUCIÓN: Crear usuario
        UsuarioDTO usuarioCreado = crearUsuarioDePrueba("Carlos López", "carlos@gmail.com", rolEsperado);
        
        // 3. VERIFICACIÓN: Comprobar rol
        assertEquals(rolEsperado, usuarioCreado.getRol(), 
                    "El rol del usuario no coincide con el esperado");
        
        // Verificar que se creó correctamente
        UsuarioDTO usuarioObtenido = usuarioService.obtenerUsuarioPorId(usuarioCreado.getId());
        assertEquals(rolEsperado, usuarioObtenido.getRol());
    }

    @Test
    void actualizarUsuario_DebeCambiarEmailCorrectamente() {
        // 1. PREPARACIÓN: Crear usuario inicial
        UsuarioDTO usuarioOriginal = crearUsuarioDePrueba("Usuario Prueba", "original@gmail.com", "ARRENDATARIO");
        String nuevoEmail = "nuevo@gmail.com";
        
        // 2. EJECUCIÓN: Actualizar email
        usuarioOriginal.setEmail(nuevoEmail);
        UsuarioDTO usuarioActualizado = usuarioService.actualizarUsuario(
            usuarioOriginal.getId(), usuarioOriginal);
        
        // 3. VERIFICACIÓN: Comprobar cambio
        assertEquals(nuevoEmail, usuarioActualizado.getEmail(),
                   "El email no se actualizó correctamente");
        
        // Verificar persistencia del cambio
        UsuarioDTO usuarioObtenido = usuarioService.obtenerUsuarioPorId(usuarioOriginal.getId());
        assertEquals(nuevoEmail, usuarioObtenido.getEmail());
    }

    // Método auxiliar mejorado
    private UsuarioDTO crearUsuarioDePrueba(String nombre, String email, String rol) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setEmail(email);
        usuarioDTO.setRol(rol);
        return usuarioService.crearUsuario(usuarioDTO);
    }
}