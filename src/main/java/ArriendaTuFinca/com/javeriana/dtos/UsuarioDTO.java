package ArriendaTuFinca.com.javeriana.dtos;

import java.util.List;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email;
    private String rol;
    private List<SolicitudArriendoDTO> solicitudes;

    // Getters y Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public String getNombre() { 
        return nombre; 
    }

    public void setNombre(String nombre) { 
        this.nombre = nombre; 
    }

    public String getEmail() { 
        return email; 
    }

    public void setEmail(String email) { 
        this.email = email; 
    }

    public String getRol() { 
        return rol; 
    }

    public void setRol(String rol) { 
        this.rol = rol; 
    }

    public List<SolicitudArriendoDTO> getSolicitudes() { 
        return solicitudes; 
    }

    public void setSolicitudes(List<SolicitudArriendoDTO> solicitudes) { 
        this.solicitudes = solicitudes; 
    }
}