/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import org.springframework.stereotype.Component;

/**
 *
 * @author Luis Monterroso
 */
@Component
public class ReporteExportRequest extends ReporteRequest {

    @NotBlank(message = "El nombre de la categoria no puede estar vacío.")
    @NotNull(message = "El nombre de la categoria no puede ser nulo")
    @Pattern(regexp = "^(reporteVentas|reporteClientes|reporteServicios|reporteDisponibilidad)$",
            message = "El nombre del reporte debe ser "
            + "uno de los valores permitidos: reporteVentas,"
            + " reporteClientes, reporteServicios o reporteDisponibilidad")
    private String tipoReporte;
    @NotBlank(message = "El nombre de la categoria no puede estar vacío.")
    @NotNull(message = "El nombre de la categoria no puede ser nulo")
    @Pattern(regexp = "^(excel|pdf|word|img)$",
            message = "El tipo de exporte debe ser uno "
            + "de los valores permitidos: excel, pdf, word e img.")
    private String tipoExporte;

    public ReporteExportRequest(String tipoReporte, LocalDate fecha1, LocalDate fecha2,
            String tipoExporte) {
        super(fecha1, fecha2);
        this.tipoReporte = tipoReporte;
        this.tipoExporte = tipoExporte;
    }

    public ReporteExportRequest() {
    }

    public String getTipoReporte() {
        return tipoReporte;
    }

    public void setTipoReporte(String tipoReporte) {
        this.tipoReporte = tipoReporte;
    }

    public String getTipoExporte() {
        return tipoExporte;
    }

    public void setTipoExporte(String tipoExporte) {
        this.tipoExporte = tipoExporte;
    }

}
