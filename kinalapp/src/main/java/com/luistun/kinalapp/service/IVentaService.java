package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Venta;

import java.util.List;
import java.util.Optional;

public interface IVentaService {

    /*
     * Interfaz: es un contrato que dice que metodos debe tener
     * cualquier servicio de venta, no tiene
     * Implementacion, solo la definicion de los metodos
     * */

    //Metodo que devuelve una lista de todas las ventas
    List<Venta> listarTodos();

    //Metodo que guarda una venta en la base de DB
    Venta guardar(Venta venta);
    //Parametros: recibe un objeto venta con los datos a
    // guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Venta> buscarPorCodigo(int codigo);

    //Metodo que actualiza una venta
    Venta actualizar(int codigo, Venta venta);

     /*
     * Metodo de tipo void para eliminar a una venta
     * void: no retorna ningun valor o dato
     * Elimina una venta por su codigo
     * */
    void eliminar(int codigo);

    //boolean - Retorna true si existe y false si no existe
    boolean existePorCodigo(int codigo);

    //devuelve una lista de venta activos
    List<Venta> listarActivos();
}
