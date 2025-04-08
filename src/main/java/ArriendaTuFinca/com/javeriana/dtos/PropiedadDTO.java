package ArriendaTuFinca.com.javeriana.dtos;

import java.util.List;

public class PropiedadDTO {
    private Long id;
    private String nombre;
    private String ubicacion;
    private double precio;
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

    public List<SolicitudArriendoDTO> getSolicitudes() { 
        return solicitudes; 
    }

    public void setSolicitudes(List<SolicitudArriendoDTO> solicitudes) { 
        this.solicitudes = solicitudes; 
    }
    
}