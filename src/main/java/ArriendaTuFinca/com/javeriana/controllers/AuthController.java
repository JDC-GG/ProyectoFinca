package ArriendaTuFinca.com.javeriana.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ArriendaTuFinca.com.javeriana.dtos.AuthRequest;
import ArriendaTuFinca.com.javeriana.dtos.AuthResponse;
import ArriendaTuFinca.com.javeriana.dtos.UsuarioDTO;
import ArriendaTuFinca.com.javeriana.services.CustomUserDetailsService;
import ArriendaTuFinca.com.javeriana.services.JWTService;
import ArriendaTuFinca.com.javeriana.services.UsuarioService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UsuarioService usuarioService;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;
    private final CustomUserDetailsService userDetailsService;

    @PostMapping("/registro")
    public ResponseEntity<AuthResponse> registrar(@RequestBody UsuarioDTO usuarioDTO) {
        UsuarioDTO usuarioRegistrado = usuarioService.crearUsuario(usuarioDTO);
        UserDetails userDetails = userDetailsService.loadUserByUsername(usuarioRegistrado.getCorreo());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }

    @PostMapping("/autenticar")
    public ResponseEntity<AuthResponse> autenticar(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getCorreo(),
                        request.getContrasena()
                )
        );
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getCorreo());
        String jwtToken = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new AuthResponse(jwtToken));
    }
}