package ArriendaTuFinca.com.javeriana.repositories;

import ArriendaTuFinca.com.javeriana.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Usuario findByCorreo(String correo); // 👈 este método es clave para login
}
