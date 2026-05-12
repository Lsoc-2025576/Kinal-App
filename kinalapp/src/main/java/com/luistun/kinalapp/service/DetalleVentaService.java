package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.DetalleVenta;
import com.luistun.kinalapp.repository.DetalleVentaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DetalleVentaService implements IDetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;

    //Inyeccion de dependencias por constructor
    public DetalleVentaService(DetalleVentaRepository detalleVentaRepository) {
        this.detalleVentaRepository = detalleVentaRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<DetalleVenta> listarTodos() {
        return detalleVentaRepository.findAll();
        //findAll() hace el SELECT * FROM detalle_venta
    }

    @Override
    public DetalleVenta guardar(DetalleVenta detalleVenta) {
        //validarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<DetalleVenta> buscarPorCodigo(int codigo) {
        return detalleVentaRepository.findById(codigo);
        //Optional nos evita el nullPointer
    }

    @Override
    public DetalleVenta actualizar(int codigo, DetalleVenta detalleVenta) {
        if(!detalleVentaRepository.existsById(codigo)){
            throw new RuntimeException("El detalle de venta no se encontro con codigo " + codigo);
        }
        detalleVenta.setCodigoDetalleVenta(codigo);
        //Por seguridad usamos el codigo de la URL
        validarDetalleVenta(detalleVenta);
        return detalleVentaRepository.save(detalleVenta);
        //save() actualiza si el registro existe
    }

    @Override
    public void eliminar(int codigo) {
        if(!detalleVentaRepository.existsById(codigo)){
            throw new RuntimeException("El detalle de venta no se encontro con codigo " + codigo);
        }
        detalleVentaRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        return detalleVentaRepository.existsById(codigo);
    }

    //Metodo privado solo accesible dentro de la clase
    private void validarDetalleVenta(DetalleVenta detalleVenta){
        if(detalleVenta.getCantidad() <= 0){
            throw new IllegalArgumentException("La cantidad debe ser mayor a cero");
        }
        if(detalleVenta.getPrecioUnitario() == null || detalleVenta.getPrecioUnitario().compareTo(BigDecimal.ZERO) <= 0){
            throw new IllegalArgumentException("El precio unitario debe ser mayor a cero");
        }
        if(detalleVenta.getProducto() == null){
            throw new IllegalArgumentException("El producto es un dato obligatorio");
        }
        if(detalleVenta.getVenta() == null){
            throw new IllegalArgumentException("La venta es un dato obligatorio");
        }
    }
}