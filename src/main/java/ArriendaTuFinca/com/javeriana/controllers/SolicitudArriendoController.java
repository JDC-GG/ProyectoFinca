package ArriendaTuFinca.com.javeriana.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.services.SolicitudArriendoService;

import java.util.List;

@RestController
@RequestMapping("/api/solicitudes")
@CrossOrigin(origins = "http://localhost:4200")
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