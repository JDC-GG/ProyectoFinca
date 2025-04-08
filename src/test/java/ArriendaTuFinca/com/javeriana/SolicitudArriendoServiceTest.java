package ArriendaTuFinca.com.javeriana;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.PropiedadService;
import ArriendaTuFinca.com.javeriana.services.SolicitudArriendoService;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
class SolicitudArriendoServiceTest {

    @Autowired
    private SolicitudArriendoService solicitudArriendoService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private UsuarioService usuarioService;

    @BeforeEach
    void setUp() {
        limpiarSolicitudesExistentes();
    }

    @Test
    void crearSolicitud_DebeCrearCorrectamente() {
        // 1. Crear propiedad y usuario
        PropiedadDTO propiedadDTO = crearPropiedadEjemplo();
        UsuarioDTO usuarioDTO = crearUsuarioEjemplo();

        // 2. Crear solicitud
        SolicitudArriendoDTO solicitudDTO = new SolicitudArriendoDTO();
        solicitudDTO.setPropiedad(propiedadDTO);
        solicitudDTO.setUsuario(usuarioDTO);
        solicitudDTO.setFechaSolicitud(Date.valueOf("2023-10-01"));
        solicitudDTO.setEstado("PENDIENTE");
        
        SolicitudArriendoDTO solicitudCreada = solicitudArriendoService.crearSolicitud(solicitudDTO);

        // 3. Verificar creación
        assertNotNull(solicitudCreada.getId());
        assertEquals("PENDIENTE", solicitudCreada.getEstado());
        assertEquals(propiedadDTO.getId(), solicitudCreada.getPropiedad().getId());
        assertEquals(usuarioDTO.getId(), solicitudCreada.getUsuario().getId());
    }

    @Test
    void obtenerSolicitudPorId_DebeRetornarCorrectamente() {
        // 1. Crear datos de prueba
        SolicitudArriendoDTO solicitudCreada = crearSolicitudEjemplo();

        // 2. Obtener solicitud
        SolicitudArriendoDTO solicitudObtenida = solicitudArriendoService.obtenerSolicitudPorId(solicitudCreada.getId());

        // 3. Verificar
        assertNotNull(solicitudObtenida);
        assertEquals(solicitudCreada.getId(), solicitudObtenida.getId());
        assertEquals(solicitudCreada.getPropiedad().getId(), solicitudObtenida.getPropiedad().getId());
        assertEquals(solicitudCreada.getUsuario().getId(), solicitudObtenida.getUsuario().getId());
    }

    @Test
    void actualizarSolicitud_DebeActualizarCorrectamente() {
        // 1. Crear datos iniciales
        SolicitudArriendoDTO solicitudOriginal = crearSolicitudEjemplo();
        
        // 2. Crear nuevos datos para actualización
        PropiedadDTO nuevaPropiedad = crearPropiedadEjemplo("Finca Nueva", "Bogotá", 400000.0);
        UsuarioDTO nuevoUsuario = crearUsuarioEjemplo("Carlos Ruiz", "carlos@email.com", "PROPIETARIO");
        
        // 3. Actualizar solicitud
        SolicitudArriendoDTO solicitudActualizada = new SolicitudArriendoDTO();
        solicitudActualizada.setPropiedad(nuevaPropiedad);
        solicitudActualizada.setUsuario(nuevoUsuario);
        solicitudActualizada.setFechaSolicitud(Date.valueOf("2023-11-01"));
        solicitudActualizada.setEstado("APROBADA");
        
        SolicitudArriendoDTO resultado = solicitudArriendoService.actualizarSolicitud(
            solicitudOriginal.getId(), solicitudActualizada);

        // 4. Verificar cambios
        assertEquals(solicitudOriginal.getId(), resultado.getId());
        assertEquals("APROBADA", resultado.getEstado());
        assertEquals(nuevaPropiedad.getId(), resultado.getPropiedad().getId());
        assertEquals(nuevoUsuario.getId(), resultado.getUsuario().getId());
    }

    @Test
    void eliminarSolicitud_DebeEliminarCorrectamente() {
        // 1. Crear solicitud de prueba
        SolicitudArriendoDTO solicitudCreada = crearSolicitudEjemplo();

        // 2. Verificar existencia
        List<SolicitudArriendoDTO> solicitudesAntes = solicitudArriendoService.listarTodasLasSolicitudes();
        assertEquals(1, solicitudesAntes.size(), "Debería existir 1 solicitud antes de eliminar");

        // 3. Eliminar
        solicitudArriendoService.eliminarSolicitud(solicitudCreada.getId());

        // 4. Verificar eliminación
        List<SolicitudArriendoDTO> solicitudesDespues = solicitudArriendoService.listarTodasLasSolicitudes();
        assertTrue(solicitudesDespues.isEmpty(), "La lista debería estar vacía después de eliminar");

        // 5. Verificar que no se puede obtener
        assertThrows(RuntimeException.class, () -> {
            solicitudArriendoService.obtenerSolicitudPorId(solicitudCreada.getId());
        }, "Debería lanzar excepción al buscar solicitud eliminada");
    }

    @Test
    void listarTodasLasSolicitudes_DebeRetornarListaCorrecta() {
        // 1. Verificar lista vacía inicial
        assertTrue(solicitudArriendoService.listarTodasLasSolicitudes().isEmpty());
        
        // 2. Crear dos solicitudes diferentes
        SolicitudArriendoDTO solicitud1 = crearSolicitudEjemplo();
        SolicitudArriendoDTO solicitud2 = crearSolicitudEjemploConDatosDiferentes();
        
        // 3. Obtener lista
        List<SolicitudArriendoDTO> solicitudes = solicitudArriendoService.listarTodasLasSolicitudes();
        
        // 4. Verificaciones básicas
        assertEquals(2, solicitudes.size(), "Deberían existir exactamente 2 solicitudes");
        
        // 5. Verificar contenido completo de la primera solicitud
        SolicitudArriendoDTO encontrada1 = solicitudes.stream()
            .filter(s -> s.getId().equals(solicitud1.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Solicitud1 no encontrada en la lista"));
        
        assertEquals(solicitud1.getEstado(), encontrada1.getEstado());
        assertEquals(solicitud1.getFechaSolicitud(), encontrada1.getFechaSolicitud());
        assertEquals(solicitud1.getPropiedad().getId(), encontrada1.getPropiedad().getId());
        assertEquals(solicitud1.getUsuario().getId(), encontrada1.getUsuario().getId());
        
        // 6. Verificar contenido completo de la segunda solicitud
        SolicitudArriendoDTO encontrada2 = solicitudes.stream()
            .filter(s -> s.getId().equals(solicitud2.getId()))
            .findFirst()
            .orElseThrow(() -> new AssertionError("Solicitud2 no encontrada en la lista"));
        
        assertEquals(solicitud2.getEstado(), encontrada2.getEstado());
        assertEquals(solicitud2.getFechaSolicitud(), encontrada2.getFechaSolicitud());
        assertEquals(solicitud2.getPropiedad().getId(), encontrada2.getPropiedad().getId());
        assertEquals(solicitud2.getUsuario().getId(), encontrada2.getUsuario().getId());
    }

    // Métodos auxiliares
    private void limpiarSolicitudesExistentes() {
        List<SolicitudArriendoDTO> solicitudes = solicitudArriendoService.listarTodasLasSolicitudes();
        for (SolicitudArriendoDTO solicitud : solicitudes) {
            try {
                solicitudArriendoService.eliminarSolicitud(solicitud.getId());
            } catch (Exception e) {
                // Ignorar errores al limpiar
            }
        }
    }

    private SolicitudArriendoDTO crearSolicitudEjemplo() {
        PropiedadDTO propiedad = crearPropiedadEjemplo();
        UsuarioDTO usuario = crearUsuarioEjemplo();
        
        SolicitudArriendoDTO solicitudDTO = new SolicitudArriendoDTO();
        solicitudDTO.setPropiedad(propiedad);
        solicitudDTO.setUsuario(usuario);
        solicitudDTO.setFechaSolicitud(Date.valueOf("2023-10-01"));
        solicitudDTO.setEstado("PENDIENTE");
        
        return solicitudArriendoService.crearSolicitud(solicitudDTO);
    }

    private SolicitudArriendoDTO crearSolicitudEjemploConDatosDiferentes() {
        PropiedadDTO propiedad = crearPropiedadEjemplo("Casa Playa", "Cartagena", 500000.0);
        UsuarioDTO usuario = crearUsuarioEjemplo("Pedro Sánchez", "pedro@gmail.com", "PROPIETARIO");
        
        SolicitudArriendoDTO solicitudDTO = new SolicitudArriendoDTO();
        solicitudDTO.setPropiedad(propiedad);
        solicitudDTO.setUsuario(usuario);
        solicitudDTO.setFechaSolicitud(Date.valueOf("2023-11-15"));
        solicitudDTO.setEstado("APROBADA");
        
        return solicitudArriendoService.crearSolicitud(solicitudDTO);
    }

    private PropiedadDTO crearPropiedadEjemplo() {
        return crearPropiedadEjemplo("Casa Montaña", "Medellín", 300000.0);
    }

    private PropiedadDTO crearPropiedadEjemplo(String nombre, String ubicacion, double precio) {
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setNombre(nombre);
        propiedadDTO.setUbicacion(ubicacion);
        propiedadDTO.setPrecio(precio);
        return propiedadService.crearPropiedad(propiedadDTO);
    }

    private UsuarioDTO crearUsuarioEjemplo() {
        return crearUsuarioEjemplo("Ana Gómez", "ana@gmail.com", "ARRENDATARIO");
    }

    private UsuarioDTO crearUsuarioEjemplo(String nombre, String email, String rol) {
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre(nombre);
        usuarioDTO.setEmail(email);
        usuarioDTO.setRol(rol);
        return usuarioService.crearUsuario(usuarioDTO);
    }
}