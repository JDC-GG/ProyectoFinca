package ArriendaTuFinca.com.javeriana.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ArriendaTuFinca.com.javeriana.entities.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}