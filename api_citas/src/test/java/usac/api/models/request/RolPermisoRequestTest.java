/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.request;

import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import usac.api.models.Permiso;

/**
 * Clase de pruebas unitarias para la clase RolPermisoRequest. Se validan los
 * métodos setters, getters y el constructor de la clase.
 *
 *
 * @author Luis Monterroso
 */
public class RolPermisoRequestTest {

    private RolPermisoUpdateRequest rolPermisoRequest;
    private List<Permiso> permisos;
    private Permiso permiso1;
    private Permiso permiso2;

    /**
     * Configuración inicial antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        permiso1 = new Permiso("Permiso 1", "/ruta1");
        permiso2 = new Permiso("Permiso 2", "/ruta2");
        permisos = new ArrayList<>();
        permisos.add(permiso1);
        permisos.add(permiso2);

        // Inicializar objeto de prueba
        rolPermisoRequest = new RolPermisoUpdateRequest();
    }

    /**
     * Prueba para verificar el funcionamiento del constructor parametrizado.
     */
    @Test
    void testConstructorParametrizado() {
        // Crear una instancia de RolPermisoRequest con el constructor que recibe parámetros
        rolPermisoRequest = new RolPermisoUpdateRequest(1L, permisos);

        // Verificar que los valores se asignan correctamente
        assertEquals(1L, rolPermisoRequest.getIdRol());
        assertEquals(2, rolPermisoRequest.getPermisos().size());
        assertEquals(permiso1, rolPermisoRequest.getPermisos().get(0));
        assertEquals(permiso2, rolPermisoRequest.getPermisos().get(1));
    }

    /**
     * Prueba para verificar que los atributos están inicializados como null.
     */
    @Test
    void testAtributosInicialmenteNulos() {
        // Verificar que los atributos están inicialmente en null
        assertNull(rolPermisoRequest.getIdRol());
        assertNull(rolPermisoRequest.getPermisos());
    }

    /**
     * Prueba para verificar el correcto funcionamiento de los setters y
     * getters.
     */
    @Test
    void testSettersAndGetters() {
        // Establecer valores en la instancia de RolPermisoRequest
        rolPermisoRequest.setIdRol(2L);
        rolPermisoRequest.setPermisos(permisos);

        // Verificar que los valores se establecen y recuperan correctamente
        assertEquals(2L, rolPermisoRequest.getIdRol());
        assertEquals(2, rolPermisoRequest.getPermisos().size());
        assertEquals(permiso1, rolPermisoRequest.getPermisos().get(0));
        assertEquals(permiso2, rolPermisoRequest.getPermisos().get(1));
    }

}
