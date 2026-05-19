package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.DetalleVenta;

import java.util.List;
import java.util.Optional;

public interface IDetalleVentaService {

    /*
     * Interfaz: es un contrato que dice que metodos debe tener
     * cualquier servicio de DetalleVenta, no tiene
     * Implementacion, solo la definicion de los metodos
     * */

    //Metodo que devuelve una lista de todos los detalles de venta
    List<DetalleVenta> listarTodos();
    /*
     * List<DetalleVenta> lo que hace es devolver una lista
     * de objetos de la entidad DetalleVenta
     */

    //Metodo que guarda un detalle de venta en la base de DB
    DetalleVenta guardar(DetalleVenta detalleVenta);
    //Parametros: recibe un objeto DetalleVenta con los datos a
    // guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<DetalleVenta> buscarPorCodigo(int codigo);

    //Metodo que actualiza un detalle de venta
    DetalleVenta actualizar(int codigo, DetalleVenta detalleVenta);
    /*
     * Parametros - codigo: codigo del detalle a actualizar
     * DetalleVenta detalleVenta: objeto con los datos nuevos
     * Retorna un objeto de tipo DetalleVenta ya actualizado
     * */

    /*
     * Metodo de tipo void para eliminar un DetalleVenta
     * void: no retorna ningun valor o dato
     * Elimina un DetalleVenta por su codigo
     * */
    void eliminar(int codigo);

    //boolean - Retorna true si existe y false si no existe
    boolean existePorCodigo(int codigo);
}