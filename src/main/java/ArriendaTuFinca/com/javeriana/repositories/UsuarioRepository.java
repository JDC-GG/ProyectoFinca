package ArriendaTuFinca.com.javeriana.repositories;

import ArriendaTuFinca.com.javeriana.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query("SELECT u FROM Usuario u WHERE LOWER(u.correo) = LOWER(:correo)")
    Usuario findByCorreo(@Param("correo") String correo);
}
