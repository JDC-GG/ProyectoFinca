package ArriendaTuFinca.com.javeriana;

import static org.junit.jupiter.api.Assertions.*;
import java.sql.Date;
import java.time.LocalDate;
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
    private SolicitudArriendoService solicitudService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private PropiedadService propiedadService;

    
    private SolicitudArriendoDTO crearSolicitudValida() {
        // Crear usuario
        UsuarioDTO usuario = new UsuarioDTO();
        usuario.setNombre("Solicitante");
        usuario.setApellido("Test");
        usuario.setTelefono("123456789");
        usuario.setCorreo("solicitante" + System.currentTimeMillis() + "@test.com");
        usuario.setContrasena("password");
        usuario.setRol("ARRENDATARIO");
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuario);

        // Crear propiedad
        PropiedadDTO propiedad = new PropiedadDTO();
        propiedad.setNombre("Casa Test");
        propiedad.setUbicacion("Ubicación Test");
        propiedad.setPrecio(500000);
        PropiedadDTO propiedadCreada = propiedadService.crearPropiedad(propiedad);

        // Crear solicitud
        SolicitudArriendoDTO solicitud = new SolicitudArriendoDTO();
        solicitud.setUsuario(usuarioCreado);
        solicitud.setPropiedad(propiedadCreada);
        solicitud.setFechaSolicitud(Date.valueOf(LocalDate.now()));
        solicitud.setEstado("PENDIENTE");
        
        return solicitud;
    }

    @Test
    void crearSolicitud_ConDatosValidos_RetornaSolicitudConId() {
        SolicitudArriendoDTO solicitud = crearSolicitudValida();
        
        SolicitudArriendoDTO resultado = solicitudService.crearSolicitud(solicitud);
        
        assertNotNull(resultado.getId());
        assertEquals("PENDIENTE", resultado.getEstado());
    }

    @Test
    void obtenerSolicitudPorId_ConIdExistente_RetornaSolicitudCorrecta() {
        SolicitudArriendoDTO solicitudCreada = solicitudService.crearSolicitud(crearSolicitudValida());
        
        SolicitudArriendoDTO solicitudObtenida = solicitudService.obtenerSolicitudPorId(solicitudCreada.getId());
        
        assertEquals(solicitudCreada.getId(), solicitudObtenida.getId());
    }

    @Test
    void actualizarSolicitud_ConNuevoEstado_ActualizaCorrectamente() {
        SolicitudArriendoDTO solicitudOriginal = solicitudService.crearSolicitud(crearSolicitudValida());
        String nuevoEstado = "APROBADA";
        
        solicitudOriginal.setEstado(nuevoEstado);
        SolicitudArriendoDTO solicitudActualizada = solicitudService.actualizarSolicitud(
            solicitudOriginal.getId(), solicitudOriginal);
        
        assertEquals(nuevoEstado, solicitudActualizada.getEstado());
    }

    @Test
    void eliminarSolicitud_ConIdExistente_LaSolicitudNoDebeExistir() {
        SolicitudArriendoDTO solicitud = solicitudService.crearSolicitud(crearSolicitudValida());
        
        solicitudService.eliminarSolicitud(solicitud.getId());
        
        assertThrows(RuntimeException.class, () -> {
            solicitudService.obtenerSolicitudPorId(solicitud.getId());
        });
    }

}