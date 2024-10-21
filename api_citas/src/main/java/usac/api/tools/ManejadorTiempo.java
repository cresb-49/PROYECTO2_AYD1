/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.tools;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
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
     * Suma una cantidad de horas, representada por un double, a un LocalTime.
     *
     * @param horaInicio La hora de inicio en formato LocalTime.
     * @param horasADurar La cantidad de horas que se desean sumar, representada
     * como un double.
     * @return LocalTime La hora final calculada después de sumar las horas
     * proporcionadas.
     */
    public LocalTime sumarHoras(LocalTime horaInicio, Double horasADurar) {
        // Convertir el valor de horasADurar a minutos (ya que LocalTime maneja minutos y segundos)
        long minutosASumar = (long) (horasADurar * 60);

        // Sumar los minutos al LocalTime proporcionado
        return horaInicio.plus(Duration.ofMinutes(minutosASumar));
    }

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
     * Compara dos objetos LocalTime para verificar si la primera hora es
     * anterior a la segunda.
     *
     * @param hora1 La primera hora a comparar (LocalTime).
     * @param hora2 La segunda hora a comparar (LocalTime).
     * @return true si la primera hora es anterior a la segunda, false en caso
     * contrario.
     */
    public boolean esPrimeraHoraAntes(LocalTime hora1, LocalTime hora2) {
        return hora1.isBefore(hora2);
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

    /**
     * Convierte un Local date ha un formato de fecha recional en dd/MM/yyyy
     *
     * @param fechaLocal fecha a convertir
     * @return
     */
    public String parsearFechaYHoraAFormatoRegional(LocalDate fechaLocal) {
        if (fechaLocal == null) { // si la fecha es nula retornamos vacío
            return "-";
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String fechaFormateada = fechaLocal.format(formatter);
        return fechaFormateada;
    }

}
