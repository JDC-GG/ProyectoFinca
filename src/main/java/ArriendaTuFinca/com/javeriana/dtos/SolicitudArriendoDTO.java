package ArriendaTuFinca.com.javeriana.dtos;

import java.sql.Date;

public class SolicitudArriendoDTO {
    private Long id;
    private PropiedadDTO propiedad;
    private UsuarioDTO usuario;
    private Date fechaSolicitud;
    private String estado;

    // Getters y Setters
    public Long getId() { 
        return id; 
    }

    public void setId(Long id) { 
        this.id = id; 
    }

    public PropiedadDTO getPropiedad() { 
        return propiedad; 
    }

    public void setPropiedad(PropiedadDTO propiedad) { 
        this.propiedad = propiedad; 
    }

    public UsuarioDTO getUsuario() { 
        return usuario; 
    }

    public void setUsuario(UsuarioDTO usuario) { 
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