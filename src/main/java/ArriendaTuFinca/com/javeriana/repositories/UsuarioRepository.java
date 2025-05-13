package ArriendaTuFinca.com.javeriana.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.Usuario;

import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCorreo(String correo);
}
