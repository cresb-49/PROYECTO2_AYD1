package usac.api.models.request;

import java.util.List;

import usac.api.models.Cancha;
import usac.api.models.HorarioCancha;

public class CanchaRequest {
    private Cancha cancha;
    private List<HorarioCancha> horarios;

    public CanchaRequest() {
    }

    public CanchaRequest(Cancha cancha, List<HorarioCancha> horarios) {
        this.cancha = cancha;
        this.horarios = horarios;
    }

    public Cancha getCancha() {
        return cancha;
    }

    public void setCancha(Cancha cancha) {
        this.cancha = cancha;
    }

    public List<HorarioCancha> getHorarios() {
        return horarios;
    }

    public void setHorarios(List<HorarioCancha> horarios) {
        this.horarios = horarios;
    }

    @Override
    public String toString() {
        return "CanchaRequest [cancha=" + cancha + ", horarios=" + horarios + "]";
    }
    
}
