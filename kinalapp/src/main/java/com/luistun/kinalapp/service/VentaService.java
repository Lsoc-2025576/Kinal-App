package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Venta;
import com.luistun.kinalapp.repository.VentaRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 * Anotacion: que registra un Bean como un Bean de Spring
 * Que la clase contiene la logica del negocio
 * */
@Service
/*
 * Por defecto todos los metodos de esta clase seran transaccional
 * Una transaccion es que puede o no ocurrir algo
 * */
@Transactional
public class VentaService implements IVentaService{
    /*
     * Private: solo es accesible dentro de la misma clase
     * final: No puede cambiar por que no es constante
     * VentaRepository: El repositorio para acceder a la base DB
     * Inyeccion de Dependencias ya que Spring nos da el repositorio
     * */
    private final VentaRepository ventaRepository;

    /*
     * Constructor: este se ejecuta al crear un objeto
     * Spring pasa el repositorio automaticamente(Inyeccion)
     * */
    public VentaService(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
        //Asigna el repositorio a nuestra variable de clase
    }

    //Indica que esta implemenando un metodo de la interfaz
    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarTodos() {
        return ventaRepository.findAll();
        //fillAll() es un metodo de spring que hace el select * from venta
        //este metodo es de JpaRepository
    }


    @Override
    public Venta guardar(Venta venta) {
        /*
         * Metodo de guardar, crea una venta
         * Aca es donde colocamos la lagica del negocio Antes de guardar
         * Primero validamos el dato
         * */
        validarVenta(venta);
        return ventaRepository.save(venta);
    }

    //Busca una venta por codigu
    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> buscarPorCodigo(int codigo) {
        return ventaRepository.findById(codigo);
    }


    @Override
    public Venta actualizar(int codigo, Venta venta) {
        //Metodo para actualizar una venta existente
        if (!ventaRepository.existsById(codigo)) {
            throw new RuntimeException("La venta no se encontro con codigo " + codigo);
        }
        venta.setCodigoVenta(codigo);
        validarVenta(venta);
        return ventaRepository.save(venta);
    }

    @Override
    public void eliminar(int codigo) {
        if (!ventaRepository.existsById(codigo)) {
            throw new RuntimeException("La venta no se encontro con codigo " + codigo);
        }
        ventaRepository.deleteById(codigo);
    }


    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        //verificar si existe una venta
        return ventaRepository.existsById(codigo);
    }


    private void validarVenta(Venta venta) {
        if (venta.getFechaVenta() == null) {
            throw new IllegalArgumentException("La fecha de venta es un dato obligatorio");
        }
        if (venta.getTotal() == null || venta.getTotal().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El total debe ser mayor a cero");
        }
        if (venta.getCliente() == null) {
            throw new IllegalArgumentException("El cliente es un dato obligatorio");
        }
        if (venta.getUsuario() == null) {
            throw new IllegalArgumentException("El usuario es un dato obligatorio");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Venta> listarActivos() {
        List<Venta> ventas = ventaRepository.findAll();
        List<Venta> activos = new ArrayList<>();
        for (Venta venta : ventas) {
            if (venta.getEstado() == 1) {
                activos.add(venta);
            }
        }
        return activos;
    }
}
