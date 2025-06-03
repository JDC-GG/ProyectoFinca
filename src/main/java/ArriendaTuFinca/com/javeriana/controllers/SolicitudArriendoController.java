package ArriendaTuFinca.com.javeriana.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.services.SolicitudArriendoService;

@RestController
@RequestMapping("/solicitud-arriendo")
public class SolicitudArriendoController {

    private final SolicitudArriendoService solicitudService;

    public SolicitudArriendoController(SolicitudArriendoService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @PostMapping
    public ResponseEntity<SolicitudArriendoDTO> crearSolicitud(@RequestBody SolicitudArriendoDTO dto) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = auth.getName();
        SolicitudArriendoDTO respuesta = solicitudService.crearSolicitud(dto, correoUsuario);
        return ResponseEntity.ok(respuesta);
    }

    @GetMapping("/mis-solicitudes")
    public ResponseEntity<List<SolicitudArriendoDTO>> misSolicitudes() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String correoUsuario = auth.getName();
        List<SolicitudArriendoDTO> lista = solicitudService.listarSolicitudesPorUsuario(correoUsuario);
        return ResponseEntity.ok(lista);
    }

    
    @PutMapping("/{id}/aceptar")
    public ResponseEntity<SolicitudArriendoDTO> aceptarSolicitud(@PathVariable Long id) {
        SolicitudArriendoDTO dto = solicitudService.aceptarSolicitud(id);
        return ResponseEntity.ok(dto);
    }

    
    @PutMapping("/{id}/rechazar")
    public ResponseEntity<SolicitudArriendoDTO> rechazarSolicitud(@PathVariable Long id) {
        SolicitudArriendoDTO dto = solicitudService.rechazarSolicitud(id);
        return ResponseEntity.ok(dto);
    }

   
    @PutMapping("/{id}/pagar")
    public ResponseEntity<SolicitudArriendoDTO> pagarSolicitud(@PathVariable Long id) {
        SolicitudArriendoDTO dto = solicitudService.marcarComoPagada(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/dueno/{usuarioId}")
    public ResponseEntity<List<SolicitudArriendoDTO>> solicitudesPorPropietario(
            @PathVariable Long usuarioId) {
        List<SolicitudArriendoDTO> lista = solicitudService.listarSolicitudesPorPropietario(usuarioId);
        return ResponseEntity.ok(lista);
    }

    /**
     * Este endpoint permite traer una sola solicitud por su ID.
     * Deberá devolver un SolicitudArriendoDTO con todos sus datos.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudArriendoDTO> obtenerPorId(@PathVariable Long id) {
        SolicitudArriendoDTO dto = solicitudService.obtenerSolicitudPorId(id);
        return ResponseEntity.ok(dto);
    }
}
