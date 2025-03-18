package ArriendaTuFinca.com.javeriana.controllers;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.services.PropiedadService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/propiedades")
public class PropiedadController {
    @Autowired
    private PropiedadService propiedadService;

    // Crear propiedad 
    @PostMapping
    public ResponseEntity<PropiedadDTO> crearPropiedad(@RequestBody PropiedadDTO propiedadDTO) {
        return ResponseEntity.ok(propiedadService.crearPropiedad(propiedadDTO));
    }

    // Obtener propiedad por ID
    @GetMapping("/{id}")
    public ResponseEntity<PropiedadDTO> obtenerPropiedad(@PathVariable Long id) {
        return ResponseEntity.ok(propiedadService.obtenerPropiedadPorId(id));
    }

    // Actualizar propiedad
    @PutMapping("/{id}")
    public ResponseEntity<PropiedadDTO> actualizarPropiedad(@PathVariable Long id, @RequestBody PropiedadDTO propiedadDTO) {
        return ResponseEntity.ok(propiedadService.actualizarPropiedad(id, propiedadDTO));
    }

    // Eliminar propiedad
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPropiedad(@PathVariable Long id) {
        propiedadService.eliminarPropiedad(id);
        return ResponseEntity.noContent().build();
    }

    // Listar todas las propiedades
    @GetMapping
    public ResponseEntity<List<PropiedadDTO>> listarPropiedades() {
        return ResponseEntity.ok(propiedadService.listarTodasLasPropiedades());
    }
}