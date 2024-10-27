import { Injectable } from '@angular/core';
import { DayConfig } from '../../components/schedule-conf/schedule-conf.component';

@Injectable({
  providedIn: 'root'
})
export class HorarioService {

  constructor() { }

  public isDayConfigValido(horarioNegocio: DayConfig[], eleccion: DayConfig) {
    // Buscamos por medio del ID del día
    const horario = horarioNegocio.find((day: DayConfig) => day.id === eleccion.id);

    if (!horario) {
      throw new Error(`El negocio no atiende el dia ${eleccion.day}`);
    }

    // Convertimos las horas a formato numérico (HH:mm a minutos desde las 00:00) para comparar
    const convertToMinutes = (time: string) => {
      const [hours, minutes] = time.split(':').map(Number);
      return hours * 60 + minutes;
    };

    const initEleccion = convertToMinutes(eleccion.init);
    const endEleccion = convertToMinutes(eleccion.end);
    const initHorario = convertToMinutes(horario.init);
    const endHorario = convertToMinutes(horario.end);

    // Verificamos si la hora init es menor que la configurada en el negocio
    if (initEleccion < initHorario) {
      throw new Error(`La hora de inicio (${eleccion.init}) es anterior a la hora permitida (${horario.init}) en el dia ${eleccion.day}`);
    }

    // Verificamos si la hora end es mayor que la configurada en el negocio
    if (endEleccion > endHorario) {
      throw new Error(`La hora de finalización (${eleccion.end}) es posterior a la hora permitida (${horario.end}) en el dia ${eleccion.day}`);
    }

    // Si todo está bien, retornamos true
    return true;
  }

}
