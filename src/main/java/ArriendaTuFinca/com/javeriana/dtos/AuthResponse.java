package ArriendaTuFinca.com.javeriana.dtos;

public class AuthResponse {
    private String token;
    private UsuarioDTO usuario; 

    public AuthResponse(String token, UsuarioDTO usuario) {
        this.token = token;
        this.usuario = usuario;
    }

    public String getToken() {
        return token;
    }

    public UsuarioDTO getUsuario() {
        return usuario;
    }
}
