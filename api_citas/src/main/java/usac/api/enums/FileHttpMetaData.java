/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package usac.api.enums;

/**
 *
 * @author luid
 */
public enum FileHttpMetaData {

    EXCEL("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "attachment",
            "reporte",
            "xlsx",
            "excel"),
    WORD("application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "attachment",
            "reporte",
            "docx",
            "word"),
    IMG("image/png",
            "attachment",
            "reporte",
            "png",
            "img"),
    PDF("application/pdf",
            "inline",
            "reporte",
            "pdf",
            "pdf");

    private final String contentType;
    private final String contentDisposition;
    private final String fileName;
    private final String extension;
    private final String fileType;

    private FileHttpMetaData(String contentType, String contentDisposition, String fileName, String extension, String fileType) {
        this.contentType = contentType;
        this.contentDisposition = contentDisposition;
        this.fileName = fileName + "." + extension;
        this.extension = extension;
        this.fileType = fileType;
    }

    /**
     *
     * @return Nombre del archivo con la extension del archivo en cuestion
     */
    public String getFileName() {
        return fileName;
    }

    /**
     *
     * @return Extension del archivo en cuestion
     */
    public String getExtension() {
        return extension;
    }

    /**
     *
     * @return Mime type del archivo en cuestion
     */
    public String getContentType() {
        return contentType;
    }

    /**
     *
     * @return Disposicion en la que sera entregada a navegadores el archivo en
     * cuestion
     */
    public String getContentDispoition() {
        return contentDisposition;
    }

    /**
     *
     * @return Nombre del tipo de archivo
     */
    public String getFileType() {
        return fileType;
    }

}
