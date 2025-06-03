package ArriendaTuFinca.com.javeriana.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.SolicitudArriendo;

public interface SolicitudArriendoRepository extends JpaRepository<SolicitudArriendo, Long> {
    // Para listar solicitudes que hizo ESE arrendatario:
    List<SolicitudArriendo> findAllByUsuarioIdOrderByFechaSolicitudDesc(Long usuarioId);
    
    // Para listar solicitudes dirigidas a propiedades de un dueño:
    List<SolicitudArriendo> findAllByPropiedadUsuarioIdOrderByFechaSolicitudDesc(Long duenoId);
}
