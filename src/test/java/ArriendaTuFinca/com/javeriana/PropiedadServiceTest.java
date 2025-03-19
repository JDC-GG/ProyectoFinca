package ArriendaTuFinca.com.javeriana;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import ArriendaTuFinca.com.javeriana.dtos.PropiedadDTO;
import ArriendaTuFinca.com.javeriana.services.PropiedadService;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class PropiedadServiceTest {

    @Autowired
    private PropiedadService propiedadService;

    private PropiedadDTO crearPropiedadDePrueba() {
        PropiedadDTO propiedadDTO = new PropiedadDTO();
        propiedadDTO.setNombre("Casa Playa");
        propiedadDTO.setUbicacion("Cartagena");
        propiedadDTO.setPrecio(500000.0);
        return propiedadService.crearPropiedad(propiedadDTO);
    }

    @Test
    void eliminarPropiedad_DebeEliminarCorrectamente() {
        // 1. Crear propiedad
        PropiedadDTO propiedadCreada = crearPropiedadDePrueba();

        // 2. Verificar creación
        List<PropiedadDTO> propiedadesAntes = propiedadService.listarTodasLasPropiedades();
        assertEquals(1, propiedadesAntes.size(), "Debe existir 1 propiedad antes de eliminar");

        // 3. Eliminar
        propiedadService.eliminarPropiedad(propiedadCreada.getId());

        // 4. Verificar lista vacía
        List<PropiedadDTO> propiedadesDespues = propiedadService.listarTodasLasPropiedades();
        assertTrue(propiedadesDespues.isEmpty(), "La lista debe estar vacía después de eliminar");

        // 5. Verificar excepción
        try {
            propiedadService.obtenerPropiedadPorId(propiedadCreada.getId());
            fail("Debería haber lanzado una excepción");
        } catch (RuntimeException ex) {
            assertEquals("Propiedad no encontrada", ex.getMessage()); // Mensaje exacto
        }
    }
}