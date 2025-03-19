package ArriendaTuFinca.com.javeriana;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
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
public class SolicitudArriendoServiceTest {

    @Autowired
    private SolicitudArriendoService solicitudArriendoService;

    @Autowired
    private PropiedadService propiedadService;

    @Autowired
    private UsuarioService usuarioService;

    @Test
    void eliminarSolicitud_DebeEliminarCorrectamente() {
        // 1. Crear propiedad y usuario
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setNombre("Casa Montaña");
        propiedadDTO.setUbicacion("Medellín");
        propiedadDTO.setPrecio(300000.0);
        PropiedadDTO propiedadCreada = propiedadService.crearPropiedad(propiedadDTO);

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setNombre("Ana Gómez");
        usuarioDTO.setEmail("ana@gmail.com");
        usuarioDTO.setRol("ARRENDATARIO");
        UsuarioDTO usuarioCreado = usuarioService.crearUsuario(usuarioDTO);

        // 2. Crear solicitud
        SolicitudArriendoDTO solicitudDTO = new SolicitudArriendoDTO();
        solicitudDTO.setPropiedadId(propiedadCreada.getId());
        solicitudDTO.setUsuarioId(usuarioCreado.getId());
        solicitudDTO.setFechaSolicitud(Date.valueOf("2023-10-01"));
        solicitudDTO.setEstado("PENDIENTE");
        SolicitudArriendoDTO solicitudCreada = solicitudArriendoService.crearSolicitud(solicitudDTO);

        // 3. Verificar creación
        List<SolicitudArriendoDTO> solicitudesAntes = solicitudArriendoService.listarTodasLasSolicitudes();
        assertEquals(1, solicitudesAntes.size(), "Debe existir 1 solicitud antes de eliminar");

        // 4. Eliminar solicitud
        solicitudArriendoService.eliminarSolicitud(solicitudCreada.getId());

        // 5. Verificar lista vacía
        List<SolicitudArriendoDTO> solicitudesDespues = solicitudArriendoService.listarTodasLasSolicitudes();
        assertTrue(solicitudesDespues.isEmpty(), "La lista debe estar vacía después de eliminar");

        // 6. Intentar obtener solicitud eliminada
        try {
            solicitudArriendoService.obtenerSolicitudPorId(solicitudCreada.getId());
            fail("Debería haber lanzado una excepción");
        } catch (RuntimeException ex) {
            assertEquals("Solicitud no encontrada", ex.getMessage());
        }
    }
}