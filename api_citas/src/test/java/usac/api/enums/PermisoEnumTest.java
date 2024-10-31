package usac.api.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PermisoEnumTest {

    /**
     * Prueba para verificar que cada enum tiene el nombre del permiso y la ruta
     * correcta.
     */
    @Test
    void testEnumValues() {
        assertEquals("Crear roles", PermisoEnum.CREAR_ROLES.getNombrePermiso());
        assertEquals("/api/rol/private/restricted/crearRol", PermisoEnum.CREAR_ROLES.getRuta());

        assertEquals("Actualizar roles", PermisoEnum.ACTUALIZAR_ROLES.getNombrePermiso());
        assertEquals("/api/rol/private/restricted/actualizarRol", PermisoEnum.ACTUALIZAR_ROLES.getRuta());

        assertEquals("Actualizar permisos en roles", PermisoEnum.ACTUALIZAR_PERMISOS_ROLES.getNombrePermiso());
        assertEquals("/api/rol/private/restricted/actualizarPermisosRol", PermisoEnum.ACTUALIZAR_PERMISOS_ROLES.getRuta());

        assertEquals("Marcar como 'Realizadas' las reservas", PermisoEnum.TERMINAR_RESERVAS.getNombrePermiso());
        assertEquals("/api/reserva/private/restricted/realizarReserva", PermisoEnum.TERMINAR_RESERVAS.getRuta());

        assertEquals("Exportar comprobantes de citas", PermisoEnum.EXPORTAR_COMPROBANTES.getNombrePermiso());
        assertEquals("/api/reserva/private/restricted/comprobanteReservaPorId", PermisoEnum.EXPORTAR_COMPROBANTES.getRuta());

        assertEquals("Ver facturas del negocio", PermisoEnum.VER_FACTURAS.getNombrePermiso());
        assertEquals("/api/factura/private/restricted/facturas", PermisoEnum.VER_FACTURAS.getRuta());

        assertEquals("Exportar las facturas del negocio a PDF", PermisoEnum.EXPORTAR_PDF_FACTURAS.getNombrePermiso());
        assertEquals("/api/factura/private/restricted/facturaPorId", PermisoEnum.EXPORTAR_PDF_FACTURAS.getRuta());

        assertEquals("Exportar reportes", PermisoEnum.EXPORTAR_REPORTES.getNombrePermiso());
        assertEquals("/api/reporte/private/restricted/exportarReporte", PermisoEnum.EXPORTAR_REPORTES.getRuta());

        assertEquals("Generar reportes de ventas", PermisoEnum.REPORTE_VENTAS.getNombrePermiso());
        assertEquals("/api/reporte/private/restricted/reporteVentas", PermisoEnum.REPORTE_VENTAS.getRuta());

        assertEquals("Generar reportes de la disponibilidad de los recursos", PermisoEnum.REPORTE_DiSPONIBILIDAD_RECURSOS.getNombrePermiso());
        assertEquals("/api/reporte/private/restricted/disponiblilidadRecursos", PermisoEnum.REPORTE_DiSPONIBILIDAD_RECURSOS.getRuta());

        assertEquals("Generar reportes de clientes frecuentes", PermisoEnum.REPORTE_CLIENTES.getNombrePermiso());
        assertEquals("/api/reporte/private/restricted/reporteClientes", PermisoEnum.REPORTE_CLIENTES.getRuta());
    }

    /**
     * Prueba para verificar que el método toString devuelve la ruta
     * correctamente.
     */
    @Test
    void testToString() {
        assertEquals("/api/rol/private/restricted/crearRol", PermisoEnum.CREAR_ROLES.toString());
        assertEquals("/api/reporte/private/restricted/reporteClientes", PermisoEnum.REPORTE_CLIENTES.toString());
    }

    /**
     * Prueba para verificar que los métodos getNombrePermiso y getRuta
     * funcionan correctamente para cada enum.
     */
    @Test
    void testGetters() {
        PermisoEnum permiso = PermisoEnum.CREAR_ROLES;
        assertEquals("Crear roles", permiso.getNombrePermiso());
        assertEquals("/api/rol/private/restricted/crearRol", permiso.getRuta());

        permiso = PermisoEnum.EXPORTAR_COMPROBANTES;
        assertEquals("Exportar comprobantes de citas", permiso.getNombrePermiso());
        assertEquals("/api/reserva/private/restricted/comprobanteReservaPorId", permiso.getRuta());
    }
}
