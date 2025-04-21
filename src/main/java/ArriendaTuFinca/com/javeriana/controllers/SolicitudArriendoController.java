package ArriendaTuFinca.com.javeriana.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.services.SolicitudArriendoService;

@RestController
@RequestMapping("/api/solicitudes")

public class SolicitudArriendoController {
    
    @Autowired
    private SolicitudArriendoService solicitudArriendoService;

    @PostMapping
    public ResponseEntity<SolicitudArriendoDTO> crearSolicitud(@RequestBody SolicitudArriendoDTO solicitudDTO) {
        return ResponseEntity.ok(solicitudArriendoService.crearSolicitud(solicitudDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudArriendoDTO> obtenerSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudArriendoService.obtenerSolicitudPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudArriendoDTO> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudArriendoDTO solicitudDTO) {
        return ResponseEntity.ok(solicitudArriendoService.actualizarSolicitud(id, solicitudDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarSolicitud(@PathVariable Long id) {
        solicitudArriendoService.eliminarSolicitud(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<List<SolicitudArriendoDTO>> listarSolicitudes() {
        return ResponseEntity.ok(solicitudArriendoService.listarTodasLasSolicitudes());
    }
}