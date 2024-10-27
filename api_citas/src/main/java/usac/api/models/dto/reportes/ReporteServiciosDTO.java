/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto.reportes;

import java.util.List;

/**
 *
 * @author Luis Monterroso
 */
public class ReporteServiciosDTO {

    private List<ServicioMasDemandadoDto> servicios;
    private List<ServicioMasDemandadoDto> canchas;
    private String fecha1;
    private String fecha2;
    private Integer numeroReservaciones;

    public ReporteServiciosDTO(List<ServicioMasDemandadoDto> servicios, List<ServicioMasDemandadoDto> canchas, String fecha1, String fecha2, Integer numeroReservaciones) {
        this.servicios = servicios;
        this.canchas = canchas;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
        this.numeroReservaciones = numeroReservaciones;
    }

    public List<ServicioMasDemandadoDto> getServicios() {
        return servicios;
    }

    public void setServicios(List<ServicioMasDemandadoDto> servicios) {
        this.servicios = servicios;
    }

    public List<ServicioMasDemandadoDto> getCanchas() {
        return canchas;
    }

    public void setCanchas(List<ServicioMasDemandadoDto> canchas) {
        this.canchas = canchas;
    }

    public String getFecha1() {
        return fecha1;
    }

    public void setFecha1(String fecha1) {
        this.fecha1 = fecha1;
    }

    public String getFecha2() {
        return fecha2;
    }

    public void setFecha2(String fecha2) {
        this.fecha2 = fecha2;
    }

    public Integer getNumeroReservaciones() {
        return numeroReservaciones;
    }

    public void setNumeroReservaciones(Integer numeroReservaciones) {
        this.numeroReservaciones = numeroReservaciones;
    }

}
