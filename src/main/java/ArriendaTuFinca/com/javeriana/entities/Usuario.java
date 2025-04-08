package ArriendaTuFinca.com.javeriana.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String rol;

    @OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
    private List<SolicitudArriendo> solicitudes;

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

    public List<SolicitudArriendo> getSolicitudes() { 
        return solicitudes; 
    }

    public void setSolicitudes(List<SolicitudArriendo> solicitudes) { 
        this.solicitudes = solicitudes; 
    }
    
}