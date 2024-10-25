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
public class ReporteClientesFrecuentesDto {

    private List<ClienteFrecuenteDto> clienteFrecuentes;
    private Integer numeroVentas;
    private Double totalVentas;
    private String fecha1;
    private String fecha2;

    public ReporteClientesFrecuentesDto(List<ClienteFrecuenteDto> clienteFrecuentes, Integer numeroVentas, Double totalVentas, String fecha1, String fecha2) {
        this.clienteFrecuentes = clienteFrecuentes;
        this.numeroVentas = numeroVentas;
        this.totalVentas = totalVentas;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
    }

    public ReporteClientesFrecuentesDto() {
    }

    public List<ClienteFrecuenteDto> getClienteFrecuentes() {
        return clienteFrecuentes;
    }

    public void setClienteFrecuentes(List<ClienteFrecuenteDto> clienteFrecuentes) {
        this.clienteFrecuentes = clienteFrecuentes;
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

    public Integer getNumeroVentas() {
        return numeroVentas;
    }

    public void setNumeroVentas(Integer numeroVentas) {
        this.numeroVentas = numeroVentas;
    }

    public Double getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Double totalVentas) {
        this.totalVentas = totalVentas;
    }

}
