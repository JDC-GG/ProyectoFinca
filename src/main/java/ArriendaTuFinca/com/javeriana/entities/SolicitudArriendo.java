package ArriendaTuFinca.com.javeriana.entities;

import jakarta.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "solicitud_arriendo")
public class SolicitudArriendo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "propiedad_id", nullable = false)
    private Propiedad propiedad;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @Column(nullable = false)
    private Date fechaSolicitud;

    @Column(nullable = false)
    private String estado;

    // Getters y Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public Propiedad getPropiedad() { 
        return propiedad; 
    }

    public void setPropiedad(Propiedad propiedad) { 
        this.propiedad = propiedad; 
    }

    public Usuario getUsuario() { 
        return usuario; 
    }

    public void setUsuario(Usuario usuario) { 
        this.usuario = usuario; 
    }

    public Date getFechaSolicitud() { 
        return fechaSolicitud; 
    }

    public void setFechaSolicitud(Date fechaSolicitud) { 
        this.fechaSolicitud = fechaSolicitud; 
    }

    public String getEstado() { 
        return estado; 
    }

    public void setEstado(String estado) { 
        this.estado = estado; 
    }
    
}