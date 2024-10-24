/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.time.LocalDate;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteRequest {

    private LocalDate fecha1;
    private LocalDate fecha2;

    public ReporteRequest(LocalDate fecha1, LocalDate fecha2) {
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
    }

    public ReporteRequest() {
    }

    public LocalDate getFecha1() {
        return fecha1;
    }

    public void setFecha1(LocalDate fecha1) {
        this.fecha1 = fecha1;
    }

    public LocalDate getFecha2() {
        return fecha2;
    }

    public void setFecha2(LocalDate fecha2) {
        this.fecha2 = fecha2;
    }

}
