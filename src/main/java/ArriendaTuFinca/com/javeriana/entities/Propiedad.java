package ArriendaTuFinca.com.javeriana.entities;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "propiedad")
public class Propiedad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String ubicacion;

    @Column(nullable = false)
    private double precio;

    @OneToMany(mappedBy = "propiedad", cascade = CascadeType.ALL)
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

    public String getUbicacion() { 
        return ubicacion; 
    }

    public void setUbicacion(String ubicacion) { 
        this.ubicacion = ubicacion; 
    }

    public double getPrecio() { 
        return precio; 
    }

    public void setPrecio(double precio) { 
        this.precio = precio; 
    }

    public List<SolicitudArriendo> getSolicitudes() { 
        return solicitudes; 
    }

    public void setSolicitudes(List<SolicitudArriendo> solicitudes) { 
        this.solicitudes = solicitudes; 
    }
    
}