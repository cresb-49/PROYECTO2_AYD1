/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package usac.api.models.dto.reportes;

/**
 *
 * @author Luis Monterroso
 */
public class ClienteFrecuenteDto {

    private Long id;
    private String nombre;
    private Long numeroReservaciones;
    private Double valorTotalCompras;
    private Double ticketPromedio;

    private String valorTotalComprasStr;
    private String ticketPromedioStr;

    public ClienteFrecuenteDto() {
    }

    public ClienteFrecuenteDto(Long id, String nombres, Long numeroPedidos, Double valorTotalCompras, Double ticketPromedio) {
        this.id = id;
        this.nombre = nombres;
        this.numeroReservaciones = numeroPedidos;
        this.valorTotalCompras = valorTotalCompras;
        this.ticketPromedio = ticketPromedio;

        this.valorTotalComprasStr = "Q " + valorTotalCompras;
        this.ticketPromedioStr = "Q " + this.ticketPromedio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Long getNumeroPedidos() {
        return numeroReservaciones;
    }

    public void setNumeroPedidos(Long numeroPedidos) {
        this.numeroReservaciones = numeroPedidos;
    }

    public Double getValorTotalCompras() {
        return valorTotalCompras;
    }

    public void setValorTotalCompras(Double valorTotalCompras) {
        this.valorTotalCompras = valorTotalCompras;
    }

    public Double getTicketPromedio() {
        return ticketPromedio;
    }

    public void setTicketPromedio(Double ticketPromedio) {
        this.ticketPromedio = ticketPromedio;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getNumeroReservaciones() {
        return numeroReservaciones;
    }

    public void setNumeroReservaciones(Long numeroReservaciones) {
        this.numeroReservaciones = numeroReservaciones;
    }

    public String getValorTotalComprasStr() {
        return valorTotalComprasStr;
    }

    public void setValorTotalComprasStr(String valorTotalComprasStr) {
        this.valorTotalComprasStr = valorTotalComprasStr;
    }

    public String getTicketPromedioStr() {
        return ticketPromedioStr;
    }

    public void setTicketPromedioStr(String ticketPromedioStr) {
        this.ticketPromedioStr = ticketPromedioStr;
    }
    
    
}
