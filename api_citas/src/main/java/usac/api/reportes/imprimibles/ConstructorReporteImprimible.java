package usac.api.reportes.imprimibles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Map;
import javax.imageio.ImageIO;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRGraphics2DExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleGraphics2DExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Negocio;
import usac.api.services.B64Service;
import usac.api.services.NegocioService;

/*
 * Clase ConstructorReporteImprimible
 * 
 * Esta clase se encarga de gestionar la generación y exportación de reportes 
 * en diferentes formatos (PDF, Excel, Word, Imagen). Utiliza JasperReports para 
 * generar el contenido de los reportes y ofrece métodos para exportarlos a distintos formatos.
 */
public class ConstructorReporteImprimible {

    // Inyección de dependencias para obtener información del negocio
    @Autowired
    protected NegocioService tiendaConfigService;

    /**
     * Método principal que exporta un reporte en diferentes formatos (PDF,
     * Excel, Word, Imagen) según el tipo de reporte solicitado.
     *
     * @param reportePath La ruta donde se encuentra el archivo del reporte
     * .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @param tipoReporte Tipo de reporte a exportar (excel, word, img, pdf)
     * @return Un array de bytes que representa el reporte exportado.
     * @throws Exception
     */
    protected byte[] exportarReporte(String reportePath, Map parametros, String tipoReporte) throws Exception {
        return switch (tipoReporte) {
            case "excel" ->
                exportarExcel(reportePath, parametros);
            case "word" ->
                exportarWord(reportePath, parametros);
            case "img" ->
                exportToImage(reportePath, parametros);
            default ->
                exportarPdf(reportePath, parametros);
        };
    }

    /**
     * Exporta el reporte en formato PDF.
     *
     * @param reportePath Ruta del archivo .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @return Un array de bytes que representa el archivo PDF generado.
     * @throws Exception
     */
    private byte[] exportarPdf(String reportePath, Map parametros) throws Exception {
        JasperPrint jasperPrint = this.calcarReporte(reportePath, parametros);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JasperExportManager.exportReportToPdfStream(jasperPrint, out);
        return out.toByteArray();
    }

    /**
     * Exporta el reporte en formato Excel.
     *
     * @param reportePath Ruta del archivo .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @return Un array de bytes que representa el archivo XLSX generado.
     * @throws Exception
     */
    private byte[] exportarExcel(String reportePath, Map<String, Object> parametros) throws Exception {
        JasperPrint jasperPrint = this.calcarReporte(reportePath, parametros);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(false);
        configuration.setDetectCellType(true);
        exporter.setConfiguration(configuration);

        exporter.exportReport();
        return out.toByteArray();
    }

    /**
     * Exporta el reporte como una imagen (formato PNG).
     *
     * @param reportePath Ruta del archivo .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @return Un array de bytes que representa la imagen generada.
     * @throws Exception
     */
    private byte[] exportToImage(String reportePath, Map<String, Object> parametros) throws Exception {
        JasperPrint jasperPrint = this.calcarReporte(reportePath, parametros);
        int pageWidth = jasperPrint.getPageWidth();
        int pageHeight = jasperPrint.getPageHeight();

        BufferedImage bufferedImage = new BufferedImage(pageWidth, pageHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = bufferedImage.createGraphics();

        JRGraphics2DExporter exporter = new JRGraphics2DExporter();
        SimpleExporterInput input = new SimpleExporterInput(jasperPrint);
        exporter.setExporterInput(input);

        SimpleGraphics2DExporterOutput output = new SimpleGraphics2DExporterOutput();
        output.setGraphics2D(g2d);
        exporter.setExporterOutput(output);

        exporter.exportReport();
        g2d.dispose();

        File tempFile = File.createTempFile("jasper_report", ".png");
        ImageIO.write(bufferedImage, "png", tempFile);

        return java.nio.file.Files.readAllBytes(tempFile.toPath());
    }

    /**
     * Exporta el reporte en formato Word.
     *
     * @param reportePath Ruta del archivo .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @return Un array de bytes que representa el archivo DOCX generado.
     * @throws Exception
     */
    private byte[] exportarWord(String reportePath, Map<String, Object> parametros) throws Exception {
        JasperPrint jasperPrint = this.calcarReporte(reportePath, parametros);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JRDocxExporter exporter = new JRDocxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));
        exporter.exportReport();
        return out.toByteArray();
    }

    /**
     * Método que se encarga de llenar (calcar) el reporte con los datos y
     * parámetros proporcionados.
     *
     * @param reportePath Ruta del archivo .jasper
     * @param parametros Parámetros a inyectar en el reporte.
     * @return El objeto JasperPrint resultante.
     * @throws Exception
     */
    private JasperPrint calcarReporte(String reportePath, Map parametros) throws Exception {
        // Obtener la configuración de la tienda y agregar el logo y el nombre a los parámetros
        Negocio tiendaConfig = this.tiendaConfigService.obtenerNegocio();
        parametros.put("direccion_tienda", tiendaConfig.getDireccion());
        parametros.put("nombre_tienda", tiendaConfig.getNombre());
        parametros.put("imagen_tienda", new ByteArrayInputStream(B64Service.base64ToByteArray(tiendaConfig.getLogo())));

        // Cargar el reporte .jasper desde la ruta especificada
        JasperReport reporte = (JasperReport) JRLoader.loadObject(getClass().getResource("/imprimibles/" + reportePath + ".jasper"));

        // Llenar el reporte con los datos
        return JasperFillManager.fillReport(reporte, parametros, new JREmptyDataSource());
    }
}
