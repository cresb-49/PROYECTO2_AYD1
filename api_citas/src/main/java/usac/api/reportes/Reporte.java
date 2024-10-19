/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.reportes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import usac.api.reportes.imprimibles.ConstructorReporteImprimible;
import usac.api.tools.ManejadorTiempo;

/**
 *
 * @author luid
 */
@Component
public class Reporte extends ConstructorReporteImprimible {

    @Autowired
    protected ManejadorTiempo manejadorTiempo;

}
