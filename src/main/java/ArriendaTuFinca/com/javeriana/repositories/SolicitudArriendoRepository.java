package ArriendaTuFinca.com.javeriana.repositories;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;

public interface SolicitudArriendoRepository extends JpaRepository<SolicitudArriendo, Long> {
List<SolicitudArriendo> findByPropiedadIdIn(List<Long> propiedadIds);

}