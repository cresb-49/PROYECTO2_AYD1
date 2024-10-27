/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.services;

import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import usac.api.enums.FileHttpMetaData;
import usac.api.models.Factura;
import usac.api.models.Reserva;
import usac.api.models.Usuario;
import usac.api.models.dto.ArchivoDTO;
import usac.api.reportes.imprimibles.FacturaImprimible;
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

    @Autowired
    private FacturaImprimible facturaImprimible;

    @Autowired
    @Lazy
    private UsuarioService usuarioService;

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

    public ArchivoDTO obtenerFacturaImprimible(Factura factura) throws Exception {
        byte[] reporte = facturaImprimible.init(factura, "pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(FileHttpMetaData.PDF.getContentType()));
        headers.setContentDisposition(ContentDisposition.builder(FileHttpMetaData.PDF.getContentDispoition())
                .filename(FileHttpMetaData.PDF.getFileName())
                .build());

        return new ArchivoDTO(headers, reporte);
    }

    public ArchivoDTO obtenerFacturaPorIdCliente(Long id) throws Exception {
        //obtener factura por id
        Factura factura = facturaRepository.findById(id).orElseThrow(
                () -> new Exception("Factura no econtrada."));

        this.verificarUsuarioJwt(factura.getReserva().getReservador());
        return obtenerFacturaImprimible(factura);
    }

    public ArchivoDTO obtenerFacturaPorIdAdmin(Long id) throws Exception {
        //obtener factura por id
        Factura factura = facturaRepository.findById(id).orElseThrow(
                () -> new Exception("Factura no econtrada."));
        return obtenerFacturaImprimible(factura);
    }

    /**
     * Obtiene todas las facturas asociadas al cliente autenticado.
     *
     * Este método verifica el usuario autenticado mediante JWT y recupera todas
     * las facturas asociadas a sus reservas en la base de datos.
     *
     * @return List de Factura que contiene todas las facturas del cliente
     * autenticado.
     * @throws Exception Si el usuario no está autorizado o ocurre un error en
     * el proceso.
     */
    public List<Factura> getFacturasCliente() throws Exception {
        Usuario usuario = usuarioService.getUsuarioUseJwt();
        //obtenemos las facturas del cliente
        return facturaRepository.findAllByReserva_Reservador_Id(usuario.getId());
    }

    /**
     * Obtiene todas las facturas asociadas al cliente autenticado.
     *
     * Este método verifica el usuario autenticado mediante JWT y recupera todas
     * las facturas asociadas a sus reservas en la base de datos.
     *
     * @return List de Factura que contiene todas las facturas del cliente
     * autenticado.
     * @throws Exception Si el usuario no está autorizado o ocurre un error en
     * el proceso.
     */
    public List<Factura> getFacturasAdmin() throws Exception {
        //obtenemos las facturas del cliente
        return (List<Factura>) facturaRepository.findAll();
    }

}
