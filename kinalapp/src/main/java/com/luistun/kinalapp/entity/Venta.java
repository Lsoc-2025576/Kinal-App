package com.luistun.kinalapp.entity;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "ventas")
public class Venta {
    @Id
    @Column(name = "codigo_venta")
    private int codigoVenta;

    @Column(name = "fecha_venta")
    private LocalDate fechaVenta;

    @Column(name = "total")
    private BigDecimal total;

    @Column(name = "estado")
    private int estado;

    @ManyToOne(fetch = FetchType.LAZY)
    //le dice a JPA que muchas ventas pueden pertenecer a un mismo cliente
    @JoinColumn(name = "Clientes_dpi_cliente")
    //JoinColumn quiere decir que es exactamente qué columna de la tabla ventas es la llave foránea
    // El name debe coincidir exactamente con el nombre de la columna en la BD
    private Cliente cliente;

    //(fetch = FetchType.LAZY) o "perezosa" trae el objeto principal, no trae los datos de las relaciones automaticamente
    @ManyToOne (fetch = FetchType.LAZY)
    @JoinColumn(name = "Usuarios_codigo_usuario")
    private Usuario usuario;

    public Venta() {
    }

    public Venta(int codigoVenta, LocalDate fechaVenta, BigDecimal total, int estado, Cliente cliente, Usuario usuario) {
        this.codigoVenta = codigoVenta;
        this.fechaVenta = fechaVenta;
        this.total = total;
        this.estado = estado;
        this.cliente = cliente;
        this.usuario = usuario;
    }

    public int getCodigoVenta() {
        return codigoVenta;
    }

    public void setCodigoVenta(int codigoVenta) {
        this.codigoVenta = codigoVenta;
    }

    public LocalDate getFechaVenta() {
        return fechaVenta;
    }

    public void setFechaVenta(LocalDate fechaVenta) {
        this.fechaVenta = fechaVenta;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

}
