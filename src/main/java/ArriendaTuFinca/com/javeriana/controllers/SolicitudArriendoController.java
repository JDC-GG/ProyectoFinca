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
}
