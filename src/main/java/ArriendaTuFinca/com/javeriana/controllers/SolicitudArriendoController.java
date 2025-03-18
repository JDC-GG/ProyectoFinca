package ArriendaTuFinca.com.javeriana.controllers;

import ArriendaTuFinca.com.javeriana.dtos.SolicitudArriendoDTO;
import ArriendaTuFinca.com.javeriana.services.SolicitudArriendoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/solicitudes")
public class SolicitudArriendoController {

    @Autowired
    private SolicitudArriendoService solicitudArriendoService;

    @PostMapping
    public ResponseEntity<SolicitudArriendoDTO> crearSolicitud(@RequestBody SolicitudArriendoDTO solicitudArriendoDTO) {
        return ResponseEntity.ok(solicitudArriendoService.crearSolicitud(solicitudArriendoDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SolicitudArriendoDTO> obtenerSolicitud(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudArriendoService.obtenerSolicitudPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SolicitudArriendoDTO> actualizarSolicitud(@PathVariable Long id, @RequestBody SolicitudArriendoDTO solicitudArriendoDTO) {
        return ResponseEntity.ok(solicitudArriendoService.actualizarSolicitud(id, solicitudArriendoDTO));
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