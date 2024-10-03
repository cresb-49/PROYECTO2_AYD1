package usac.api.models.dto;

import java.util.List;

import usac.api.models.HorarioNegocio;
import usac.api.models.Negocio;

public class NegocioUpdateDTO {
    private Negocio negocio;
    private List<HorarioNegocio> horarios;

    public NegocioUpdateDTO(List<HorarioNegocio> horarios, Negocio negocio) {
        this.horarios = horarios;
        this.negocio = negocio;
    }

    public NegocioUpdateDTO() {
    }

    public Negocio getNegocio() {
        return negocio;
    }

    public void setNegocio(Negocio negocio) {
        this.negocio = negocio;
    }

    public List<HorarioNegocio> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioNegocio> horarios) {
        this.horarios = horarios;
    }
}
