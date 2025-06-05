package ArriendaTuFinca.com.javeriana.entities;

import jakarta.persistence.*;

@Entity
@Table(name = "propiedad")
public class Propiedad {

    public enum TipoIngreso {
        EN_MUNICIPIO,
        CARRETERA_PRINCIPAL,
        CARRETERA_SECUNDARIA,
        CARRETERA_TERCIARIA
    }

    public enum StatusPropiedad {
        ACTIVA,
        INACTIVA,
    }


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String departamento;
    private String municipio;
    private String descripcion;
    private int habitaciones;
    private int banos;               
    private boolean mascotas;
    private boolean piscina;
    private boolean asador;
    @Column(name = "valor_noche")
    private double valorNoche;      
    
    @Enumerated(EnumType.STRING)
    private TipoIngreso tipoIngreso;

    @Enumerated(EnumType.STRING)
    private StatusPropiedad status;      

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;


    // GETTERS & SETTERS ────────────────────────────────────
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDepartamento() { return departamento; }
    public void setDepartamento(String departamento) { this.departamento = departamento; }

    public String getMunicipio() { return municipio; }
    public void setMunicipio(String municipio) { this.municipio = municipio; }

    public TipoIngreso getTipoIngreso() { return tipoIngreso; }
    public void setTipoIngreso(TipoIngreso tipoIngreso) { this.tipoIngreso = tipoIngreso; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getHabitaciones() { return habitaciones; }
    public void setHabitaciones(int habitaciones) { this.habitaciones = habitaciones; }

    public int getBanos() { return banos; }
    public void setBanos(int banos) { this.banos = banos; }

    public boolean isMascotas() { return mascotas; }
    public void setMascotas(boolean mascotas) { this.mascotas = mascotas; }

    public boolean isPiscina() { return piscina; }
    public void setPiscina(boolean piscina) { this.piscina = piscina; }

    public boolean isAsador() { return asador; }
    public void setAsador(boolean asador) { this.asador = asador; }

    public double getValorNoche() { return valorNoche; }
    public void setValorNoche(double valorNoche) { this.valorNoche = valorNoche; }

    public StatusPropiedad getStatus() { return status; }
    public void setStatus(StatusPropiedad status) { this.status = status; }

    public Usuario getUsuario() { return usuario; }
    public void setUsuario(Usuario usuario) { this.usuario = usuario; }
}
