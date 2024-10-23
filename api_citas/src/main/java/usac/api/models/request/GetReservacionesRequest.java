/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 *
 * @author Luis Monterroso
 */
public class GetReservacionesRequest {

    @NotNull(message = "El mes no puede ser nulo.")
    @Min(value = 1, message = "El mes debe ser mayor o igual a 1.")
    @Max(value = 12, message = "El mes debe ser menor o igual a 12.")
    private Integer mes;
    @NotNull(message = "El año no puede ser nulo.")
    @Min(value = 1900, message = "El año debe ser mayor o igual a 1900.")
    private Integer anio;

    public GetReservacionesRequest(Integer mes, Integer anio) {
        this.mes = mes;
        this.anio = anio;
    }

    public GetReservacionesRequest() {
    }

    public Integer getMes() {
        return mes;
    }

    public void setMes(Integer numeroMes) {
        this.mes = numeroMes;
    }

    public Integer getAnio() {
        return anio;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

}
