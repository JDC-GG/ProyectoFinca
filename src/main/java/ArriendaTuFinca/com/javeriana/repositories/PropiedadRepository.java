package ArriendaTuFinca.com.javeriana.repositories;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {
    // Buscar una propiedad por nombre y ubicación
    Optional<Propiedad> findByNombreAndUbicacion(String nombre, String ubicacion);
}