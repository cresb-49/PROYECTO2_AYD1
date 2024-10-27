/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto.reportes;

import java.util.List;
import org.springframework.stereotype.Component;
import usac.api.models.Factura;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteVentasDTO {

    private Double total;
    private Integer noVentas;
    private Double totalAdelantos;
    private Double totalNoAdelanto;
    private List<Factura> facturas;
    private String fecha1;
    private String fecha2;

    public ReporteVentasDTO(Double total, Integer noVentas, Double totalAdelantos, Double totalNoAdelanto, List<Factura> facturas, String fecha1, String fecha2) {
        this.total = total;
        this.noVentas = noVentas;
        this.totalAdelantos = totalAdelantos;
        this.totalNoAdelanto = totalNoAdelanto;
        this.facturas = facturas;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
    }

    public ReporteVentasDTO() {
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public Integer getNoVentas() {
        return noVentas;
    }

    public void setNoVentas(Integer noVentas) {
        this.noVentas = noVentas;
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

    public Double getTotalAdelantos() {
        return totalAdelantos;
    }

    public void setTotalAdelantos(Double totalAdelantos) {
        this.totalAdelantos = totalAdelantos;
    }

    public Double getTotalNoAdelanto() {
        return totalNoAdelanto;
    }

    public void setTotalNoAdelanto(Double totalNoAdelanto) {
        this.totalNoAdelanto = totalNoAdelanto;
    }

    public List<Factura> getFacturas() {
        return facturas;
    }

    public void setFacturas(List<Factura> facturas) {
        this.facturas = facturas;
    }

}
