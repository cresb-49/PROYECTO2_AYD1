/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Enum.java to edit this template
 */
package usac.api.enums;

/**
 *
 * @author luid
 */
public enum PermisoEnum {
    /*  ROLES */
    CREAR_ROLES("Crear roles",
            "/api/rol/private/restricted/crearRol"),
    ACTUALIZAR_ROLES("Actualizar roles",
            "/api/rol/private/restricted/actualizarRol"),
    ACTUALIZAR_PERMISOS_ROLES("Actualizar permisos en roles",
            "/api/rol/private/restricted/actualizarPermisosRol"),
    TERMINAR_RESERVAS("Marcar como 'Realizadas' las reservas",
            "/api/reserva/private/restricted/realizarReserva"),
    //Ver comprobantes de citas
    EXPORTAR_COMPROBANTES("Exportar comprobantes de citas",
            "/api/reserva/private/restricted/comprobanteReservaPorId"),
    //REPORTES

    EXPORTAR_REPORTES("Exportar reportes",
            "/api/reporte/private/restricted/exportarReporte"),
    REPORTE_VENTAS("Generar reportes de ventas",
            "/api/reporte/private/restricted/reporteVentas"),;

    private final String nombrePermiso;
    private final String ruta;

    private PermisoEnum(String nombrePermiso, String ruta) {
        this.nombrePermiso = nombrePermiso;
        this.ruta = ruta;
    }

    @Override
    public String toString() {
        return this.ruta;
    }

    public String getRuta() {
        return this.ruta;
    }

    public String getNombrePermiso() {
        return nombrePermiso;
    }
}
