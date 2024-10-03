package usac.api.models.dto;

import org.springframework.stereotype.Component;

@Component
public class NegocioPublicDTO {
    private Long id;
    private String nombre;
    private String logo;

    public NegocioPublicDTO() {
    }

    public NegocioPublicDTO(Long id, String nombre, String logo) {
        this.id = id;
        this.nombre = nombre;
        this.logo = logo;
    }

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

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }    
}
