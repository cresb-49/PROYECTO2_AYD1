/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Clase de pruebas unitarias para la clase RolPermiso. Se validan los métodos
 * getters y setters, y las relaciones con Rol y Permiso.
 *
 * @author Luis Monterroso
 */
public class RolPermisoTest {

    private RolPermiso rolPermiso;
    private Rol rol;
    private Permiso permiso;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        // Inicializar objetos de prueba
        rol = new Rol("ADMIN");
        permiso = new Permiso("Permiso1", "/ruta/permiso");
        rolPermiso = new RolPermiso();
    }

    /**
     * Prueba para verificar la correcta asignación y recuperación del objeto
     * Rol.
     */
    @Test
    void testSetAndGetRol() {
        // Establecer el Rol en RolPermiso
        rolPermiso.setRol(rol);

        // Verificar que el Rol se recupera correctamente
        assertEquals(rol, rolPermiso.getRol());
    }

    /**
     * Prueba para verificar la correcta asignación y recuperación del objeto
     * Permiso.
     */
    @Test
    void testSetAndGetPermiso() {
        // Establecer el Permiso en RolPermiso
        rolPermiso.setPermiso(permiso);

        // Verificar que el Permiso se recupera correctamente
        assertEquals(permiso, rolPermiso.getPermiso());
    }

    /**
     * Prueba para validar que el constructor parametrizado establece
     * correctamente los valores de Rol y Permiso.
     */
    @Test
    void testConstructorParametrizado() {
        // Crear una instancia de RolPermiso con el constructor que recibe parámetros
        rolPermiso = new RolPermiso(rol, permiso);

        // Verificar que los valores de Rol y Permiso se establecen correctamente
        assertEquals(rol, rolPermiso.getRol());
        assertEquals(permiso, rolPermiso.getPermiso());
    }

    /**
     * Prueba para validar que el Rol y el Permiso están inicialmente en null.
     */
    @Test
    void testAtributosInicialmenteNulos() {
        // Crear una instancia vacía de RolPermiso
        rolPermiso = new RolPermiso();

        // Verificar que Rol y Permiso están inicialmente en null
        assertNull(rolPermiso.getRol());
        assertNull(rolPermiso.getPermiso());
    }

}
