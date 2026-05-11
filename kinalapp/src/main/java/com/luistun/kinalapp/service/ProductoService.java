package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Producto;
import com.luistun.kinalapp.repository.ProductoRepository;
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


public class ProductoService implements IProductoService {
    private final ProductoRepository productoRepository;
    /*
     * Private: solo es accesible dentro de la misma clase
     * final: No puede cambiar por que no es constante
     * ClienteRepository: El repositorio para acceder a la base DB
     * Inyeccion de Dependencias ya que Spring nos da el repositorio
     * */

    /*
     * Constructor: este se ejecuta al crear un objeto
     * Spring pasa el repositorio automaticamente(Inyeccion)
     * */
    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
        //Asigna el repositorio a nuestra variable de clase
    }

    //Indica que esta implemenando un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la DB
    @Transactional(readOnly = true)
    public List<Producto> listarTodos() {
        return productoRepository.findAll();
        //fillAll() es un metodo de spring que hace el select * from producto
        //este metodo es de JpaRepository
    }


    @Override
    public Producto guardar(Producto producto) {
        /*
         * Metodo de guardar, crea un producto
         * Aca es donde colocamos la lagica del negocio Antes de guardar
         * Primero validamos el dato
         * */
        validarProducto(producto);
        return productoRepository.save(producto);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> buscarPorCodigo(int codigo) {
        //Buscar un cliente por codigo
        return productoRepository.findById(codigo);
        //Opcional nos evita el nullPointer
    }


    @Override
    public Producto actualizar(int codigo, Producto producto) {
        //Metodo para actualizar un producto existente
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto no se encontro con codigo " + codigo);
            //si no existe se lanza una excepcion (error controlado)
        }
        producto.setCodigoProducto(codigo);
        //Asegurarnos que el codigo del objeto coincida en el URL
        //Por seguridad usamos el codigo de la URL y no el que viene en el JSON
        validarProducto(producto);
        return productoRepository.save(producto);
        /*
         * save() este sirve para guardar y tambien para actualizar si el dato
         * Existe (codigo) Entonces hace UPDATE pero si no existe hace un Insert pero
         * antes verificamos si existe o no el registro
         * */
    }


    @Override
    public void eliminar(int codigo) {
        //Eliminar producto
        if (!productoRepository.existsById(codigo)) {
            throw new RuntimeException("El producto no se encontro con codigo " + codigo);
        }
        productoRepository.deleteById(codigo);
    }



    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        //verificar si existe un producto
        return productoRepository.existsById(codigo);
    }

    //Metodo privado(solo puede utilizarxe dentro de la clase)
    private void validarProducto(Producto producto) {
        /*
         * Validaciones del negocio: Este metodo se hara privado por que es
         * algo interno del servicio
         * */
        if (producto.getNombreProducto() == null || producto.getNombreProducto().trim().isEmpty()) {
            //Si el codigo es null o esta vacio despuesd de quitar espacios
            //Lanza una excepcion con un mensaje
            throw new IllegalArgumentException("El nombre del producto es un dato obligatorio");
        }
        if (producto.getPrecio() == null || producto.getPrecio().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("El precio debe ser mayor a cero");
        }
        if (producto.getStock() < 0) {
            throw new IllegalArgumentException("El stock no puede ser negativo");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<Producto> listarActivos() {
        List<Producto> productos = productoRepository.findAll();
        List<Producto> activos = new ArrayList<>();
        for (Producto producto : productos) {
            if (producto.getEstado() == 1) {
                activos.add(producto);
            }
        }
        return activos;
    }


}
