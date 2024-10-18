/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.tools;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import org.springframework.stereotype.Component;

/**
 *
 * @author luid
 */
@Component
public class ManejadorTiempo {

    /**
     * Convierte una fecha a su nombre de día de la semana en español.
     *
     * @param date La fecha proporcionada de tipo {@link LocalDate}.
     * @return El nombre completo del día de la semana en español, por ejemplo,
     * "lunes", "martes".
     */
    public String localDateANombreDia(LocalDate date) {
        // Obtener el nombre del día de la semana en español
        return date.getDayOfWeek()
                .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    }

    /**
     * Verifica si los horarios de inicio y fin de una reservación están dentro
     * del horario de disponibilidad de la cancha.
     *
     * @param apertura Hora de apertura {@link LocalTime}.
     * @param cierre Hora de cierre {@link LocalTime}.
     * @param inicioComparacion Hora de inicio del horario a comparar
     * {@link LocalTime}.
     * @param finComparacion Hora de fin del horario a comparar
     * {@link LocalTime}.
     * @return {@code true} si la hora de inicio y fin de la reservación están
     * dentro del rango disponible (de apertura a cierre), {@code false} en caso
     * contrario.
     */
    public boolean isHorarioEnLimites(LocalTime apertura, LocalTime cierre,
            LocalTime inicioComparacion, LocalTime finComparacion) {
        // Verifica si la hora de inicio y la de fin de la reservación están dentro de los límites
        return !inicioComparacion.isBefore(apertura)
                && !finComparacion.isAfter(cierre);
    }

    /**
     * Calcula la diferencia en horas entre dos LocalTime.
     *
     * @param inicio El tiempo de inicio.
     * @param fin El tiempo de fin.
     * @return La diferencia en horas entre los dos tiempos.
     */
    public long calcularDiferenciaEnHoras(LocalTime inicio, LocalTime fin) {
        // Calcula la duración entre el tiempo de inicio y el tiempo de fin
        Duration duracion = Duration.between(inicio, fin);
        // Devuelve la cantidad de horas en esa duración
        return duracion.toHours();
    }

}
