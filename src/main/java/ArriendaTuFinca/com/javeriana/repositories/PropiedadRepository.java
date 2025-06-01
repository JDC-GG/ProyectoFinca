package ArriendaTuFinca.com.javeriana.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import ArriendaTuFinca.com.javeriana.entities.Propiedad;

@Repository
public interface PropiedadRepository extends JpaRepository<Propiedad, Long> {

    Optional<Propiedad> findByNombreAndDepartamentoAndMunicipio(
            String nombre,
            String departamento,
            String municipio);

    List<Propiedad> findByUsuarioId(Long idUsuario);

}
