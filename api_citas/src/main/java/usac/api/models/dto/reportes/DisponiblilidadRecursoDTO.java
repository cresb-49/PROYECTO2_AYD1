/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto.reportes;

import java.time.LocalDate;
import java.time.LocalTime;
import usac.api.tools.ManejadorTiempo;

/**
 *
 * @author Luis Monterroso
 */
public class DisponiblilidadRecursoDTO {

    private String nombreReservador;
    private LocalTime horrioInicio;
    private LocalTime horarioFin;
    private LocalDate fechaReservacion;
    private String nombreRecurso;
    private String fechaReservacionStr;

    public DisponiblilidadRecursoDTO(String nombreReservador, LocalTime horrioInicio, LocalTime horarioFin, LocalDate fechaReservacion, String nombreRecurso) {
        this.nombreReservador = nombreReservador;
        this.horrioInicio = horrioInicio;
        this.horarioFin = horarioFin;
        this.fechaReservacion = fechaReservacion;
        this.nombreRecurso = nombreRecurso;
    }

    public DisponiblilidadRecursoDTO() {

    }

    public String getNombreReservador() {
        return nombreReservador;
    }

    public void setNombreReservador(String nombreReservador) {
        this.nombreReservador = nombreReservador;
    }

    public LocalTime getHorrioInicio() {
        return horrioInicio;
    }

    public void setHorrioInicio(LocalTime horrioInicio) {
        this.horrioInicio = horrioInicio;
    }

    public LocalTime getHorarioFin() {
        return horarioFin;
    }

    public void setHorarioFin(LocalTime horarioFin) {
        this.horarioFin = horarioFin;
    }

    public LocalDate getFechaReservacion() {
        return fechaReservacion;
    }

    public void setFechaReservacion(LocalDate fechaReservacion) {
        this.fechaReservacion = fechaReservacion;
    }

    public String getNombreRecurso() {
        return nombreRecurso;
    }

    public void setNombreRecurso(String nombreRecurso) {
        this.nombreRecurso = nombreRecurso;
    }

    public String getFechaReservacionStr() {
        return fechaReservacionStr;
    }

    public void setFechaReservacionStr(String fechaReservacionStr) {
        this.fechaReservacionStr = fechaReservacionStr;
    }

}
