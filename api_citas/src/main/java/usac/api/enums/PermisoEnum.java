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
            "/api/rol/protected/crearRol"),
    ACTUALIZAR_ROLES("Actualizar roles",
            "/api/rol/protected/actualizarRol"),
    ACTUALIZAR_PERMISOS_ROLES("Actualizar permisos en roles",
            "/api/rol/protected/actualizarRol");

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
