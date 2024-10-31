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
public class ReporteDisponibilidadDTO {

    private List<DisponiblilidadRecursoDTO> disponibilidadEmpleados;
    private List<DisponiblilidadRecursoDTO> disponibilidadCanchas;
    private String fecha1;
    private String fecha2;

    public ReporteDisponibilidadDTO(List<DisponiblilidadRecursoDTO> disponibilidadEmpleados, List<DisponiblilidadRecursoDTO> disponibilidadCanchas, String fecha1, String fecha2) {
        this.disponibilidadEmpleados = disponibilidadEmpleados;
        this.disponibilidadCanchas = disponibilidadCanchas;
        this.fecha1 = fecha1;
        this.fecha2 = fecha2;
    }

    public List<DisponiblilidadRecursoDTO> getDisponibilidadEmpleados() {
        return disponibilidadEmpleados;
    }

    public void setDisponibilidadEmpleados(List<DisponiblilidadRecursoDTO> disponibilidadEmpleados) {
        this.disponibilidadEmpleados = disponibilidadEmpleados;
    }

    public List<DisponiblilidadRecursoDTO> getDisponibilidadCanchas() {
        return disponibilidadCanchas;
    }

    public void setDisponibilidadCanchas(List<DisponiblilidadRecursoDTO> disponibilidadCanchas) {
        this.disponibilidadCanchas = disponibilidadCanchas;
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

}
