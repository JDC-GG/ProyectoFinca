package ArriendaTuFinca.com.javeriana.controllers;

import ArriendaTuFinca.com.javeriana.dtos.LoginRequest;
import ArriendaTuFinca.com.javeriana.dtos.AuthResponse;
import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.entities.Usuario;
import ArriendaTuFinca.com.javeriana.repositories.UsuarioRepository;
import ArriendaTuFinca.com.javeriana.security.JwtService;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AuthController {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UsuarioRepository usuarioRepository,
                          JwtService jwtService,
                          PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Usuario usuario = usuarioRepository.findByCorreo(request.getCorreo());

        if (usuario == null || !passwordEncoder.matches(request.getContrasena(), usuario.getContrasena())) {
            return ResponseEntity.status(401).body("Credenciales inválidas");
        }

        String token = jwtService.generateToken(usuario.getCorreo());

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(usuario.getId());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellido(usuario.getApellido());
        usuarioDTO.setTelefono(usuario.getTelefono());
        usuarioDTO.setCorreo(usuario.getCorreo());
        
        usuarioDTO.setContrasena(null);
        usuarioDTO.setRol(usuario.getRol());

        AuthResponse response = new AuthResponse(token, usuarioDTO);
        return ResponseEntity.ok(response);
    }
}
