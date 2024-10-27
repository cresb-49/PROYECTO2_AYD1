/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import usac.api.models.Factura;
import usac.api.models.Reserva;
import usac.api.repositories.FacturaRepository;

/**
 *
 * @author Luis Monterroso
 */
@org.springframework.stereotype.Service
public class FacturaService extends Service {

    @Autowired
    private FacturaRepository facturaRepository;

    @Autowired
    private ReservaService reservaService;

    /**
     * Crea una factura para una reserva.
     *
     * @param factura Objeto Factura ya creado.
     * @param reservaId ID de la reserva relacionada.
     * @return Factura guardada.
     * @throws Exception si la reserva ya tiene una factura o si ocurre un
     * error.
     */
    @Transactional(rollbackOn = Exception.class)
    public Factura crearFactura(Factura factura, Long reservaId) throws Exception {
        // Obtener la reserva
        Reserva reserva = reservaService.obtenerReservaPorId(reservaId);
        String nit;
        // Verificar si la reserva ya tiene una factura asociada
        if (reserva.getFactura() != null) {
            throw new Exception("La reserva ya tiene una factura asociada.");
        }
        if (reserva.getReservador().getNit() == null) {
            nit = "C/F";
        } else {
            nit = reserva.getReservador().getNit();
        }
        //setear nit
        factura.setNit(nit);

        // Asignar la factura a la reserva
        factura.setReserva(reserva);


        // Guardar la factura
        return facturaRepository.save(factura);
    }

}
