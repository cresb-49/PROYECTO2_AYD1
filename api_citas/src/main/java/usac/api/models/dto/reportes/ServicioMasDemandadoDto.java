/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto.reportes;

/**
 *
 * @author Luis Monterroso
 */
public class ServicioMasDemandadoDto {

    private Long id;
    private String nombre;
    private Long cantidadReservas;

    public ServicioMasDemandadoDto(Long id, String nombre, Long cantidadReservas) {
        this.id = id;
        this.nombre = nombre;
        this.cantidadReservas = cantidadReservas;
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

    public Long getCantidadReservas() {
        return cantidadReservas;
    }

    public void setCantidadReservas(Long cantidadReservas) {
        this.cantidadReservas = cantidadReservas;
    }

}
