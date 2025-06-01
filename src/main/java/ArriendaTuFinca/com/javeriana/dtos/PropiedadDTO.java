package ArriendaTuFinca.com.javeriana.dtos;

import ArriendaTuFinca.com.javeriana.entities.Propiedad.StatusPropiedad;
import ArriendaTuFinca.com.javeriana.entities.Propiedad.TipoIngreso;

/**
 *  DTO sin anotaciones JPA: puro transporte de datos.
 */
public class PropiedadDTO {

    private Long   id;
    private String nombre;
    private String departamento;
    private String municipio;
    private String descripcion;
    private int    habitaciones;
    private int    banos;
    private boolean mascotas;
    private boolean piscina;
    private boolean asador;
    private double valorNoche;
    private TipoIngreso    tipoIngreso;
    private StatusPropiedad status;

    private Long idUsuario;

    // GETTERS & SETTERS ──────────────── 

    public Long getId()                       { return id; }
    public void setId(Long id)                { this.id = id; }

    public String getNombre()                 { return nombre; }
    public void setNombre(String nombre)      { this.nombre = nombre; }

    public String getDepartamento()           { return departamento; }
    public void setDepartamento(String dep)   { this.departamento = dep; }

    public String getMunicipio()              { return municipio; }
    public void setMunicipio(String mun)      { this.municipio = mun; }

    public String getDescripcion()            { return descripcion; }
    public void setDescripcion(String d)      { this.descripcion = d; }

    public int  getHabitaciones()             { return habitaciones; }
    public void setHabitaciones(int h)        { this.habitaciones = h; }

    public int  getBanos()                    { return banos; }
    public void setBanos(int b)               { this.banos = b; }

    public boolean isMascotas()               { return mascotas; }
    public void    setMascotas(boolean m)     { this.mascotas = m; }

    public boolean isPiscina()                { return piscina; }
    public void    setPiscina(boolean p)      { this.piscina = p; }

    public boolean isAsador()                 { return asador; }
    public void    setAsador(boolean a)       { this.asador = a; }

    public double  getValorNoche()            { return valorNoche; }
    public void    setValorNoche(double v)    { this.valorNoche = v; }

    public TipoIngreso getTipoIngreso()       { return tipoIngreso; }
    public void setTipoIngreso(TipoIngreso t) { this.tipoIngreso = t; }

    public StatusPropiedad getStatus()               { return status; }
    public void           setStatus(StatusPropiedad s){ this.status = s; }

    public Long getIdUsuario()                { return idUsuario; }
    public void setIdUsuario(Long idUsuario)  { this.idUsuario = idUsuario; }
}
