package ArriendaTuFinca.com.javeriana.repositories;

import java.util.Optional;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.Propiedad;

public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    Optional<Propiedad> findByNombreAndUbicacion(String nombre, String ubicacion);

    List<Propiedad> findByUsuarioId(Long idUsuario); // ✅ ESTE MÉTODO ES OBLIGATORIO
}
