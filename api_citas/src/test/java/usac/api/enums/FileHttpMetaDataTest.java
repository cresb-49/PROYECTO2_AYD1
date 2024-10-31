package usac.api.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class FileHttpMetaDataTest {

    /**
     * Prueba para verificar que cada valor enum tiene los atributos correctos y
     * que los getters devuelven los valores esperados.
     */
    @Test
    void testEnumValues() {
        // Verificar valores del enum EXCEL
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", FileHttpMetaData.EXCEL.getContentType());
        assertEquals("attachment", FileHttpMetaData.EXCEL.getContentDispoition());
        assertEquals("reporte.xlsx", FileHttpMetaData.EXCEL.getFileName());
        assertEquals("xlsx", FileHttpMetaData.EXCEL.getExtension());
        assertEquals("excel", FileHttpMetaData.EXCEL.getFileType());

        // Verificar valores del enum WORD
        assertEquals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", FileHttpMetaData.WORD.getContentType());
        assertEquals("attachment", FileHttpMetaData.WORD.getContentDispoition());
        assertEquals("reporte.docx", FileHttpMetaData.WORD.getFileName());
        assertEquals("docx", FileHttpMetaData.WORD.getExtension());
        assertEquals("word", FileHttpMetaData.WORD.getFileType());

        // Verificar valores del enum IMG
        assertEquals("image/png", FileHttpMetaData.IMG.getContentType());
        assertEquals("attachment", FileHttpMetaData.IMG.getContentDispoition());
        assertEquals("reporte.png", FileHttpMetaData.IMG.getFileName());
        assertEquals("png", FileHttpMetaData.IMG.getExtension());
        assertEquals("img", FileHttpMetaData.IMG.getFileType());

        // Verificar valores del enum PDF
        assertEquals("application/pdf", FileHttpMetaData.PDF.getContentType());
        assertEquals("inline", FileHttpMetaData.PDF.getContentDispoition());
        assertEquals("reporte.pdf", FileHttpMetaData.PDF.getFileName());
        assertEquals("pdf", FileHttpMetaData.PDF.getExtension());
        assertEquals("pdf", FileHttpMetaData.PDF.getFileType());
    }

    /**
     * Prueba para verificar que el método getFileName devuelve el nombre del
     * archivo con la extensión adecuada.
     */
    @Test
    void testGetFileName() {
        assertEquals("reporte.xlsx", FileHttpMetaData.EXCEL.getFileName());
        assertEquals("reporte.docx", FileHttpMetaData.WORD.getFileName());
        assertEquals("reporte.png", FileHttpMetaData.IMG.getFileName());
        assertEquals("reporte.pdf", FileHttpMetaData.PDF.getFileName());
    }

    /**
     * Prueba para verificar que el método getContentType devuelve el tipo de
     * contenido (MIME) correcto.
     */
    @Test
    void testGetContentType() {
        assertEquals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", FileHttpMetaData.EXCEL.getContentType());
        assertEquals("application/vnd.openxmlformats-officedocument.wordprocessingml.document", FileHttpMetaData.WORD.getContentType());
        assertEquals("image/png", FileHttpMetaData.IMG.getContentType());
        assertEquals("application/pdf", FileHttpMetaData.PDF.getContentType());
    }

    /**
     * Prueba para verificar que el método getContentDisposition devuelve la
     * disposición correcta.
     */
    @Test
    void testGetContentDisposition() {
        assertEquals("attachment", FileHttpMetaData.EXCEL.getContentDispoition());
        assertEquals("attachment", FileHttpMetaData.WORD.getContentDispoition());
        assertEquals("attachment", FileHttpMetaData.IMG.getContentDispoition());
        assertEquals("inline", FileHttpMetaData.PDF.getContentDispoition());
    }

    /**
     * Prueba para verificar que el método getFileType devuelve el tipo de
     * archivo correcto.
     */
    @Test
    void testGetFileType() {
        assertEquals("excel", FileHttpMetaData.EXCEL.getFileType());
        assertEquals("word", FileHttpMetaData.WORD.getFileType());
        assertEquals("img", FileHttpMetaData.IMG.getFileType());
        assertEquals("pdf", FileHttpMetaData.PDF.getFileType());
    }
}
