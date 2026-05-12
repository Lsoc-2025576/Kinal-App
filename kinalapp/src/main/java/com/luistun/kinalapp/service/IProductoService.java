package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Producto;

import java.util.List;
import java.util.Optional;

public interface IProductoService {

     /*
     * Interfaz: es un contrato que dice que metodos debe tener
     * cualquier servicio de producto, no tiene
     * Implementacion, solo la definicion de los metodos
     * */

    //Metodo que devuelve una lsita de todos los Productos
    List<Producto> listarTodos();
    /*
     * Liste<Producto> lo que hace es devolver una lista
     * de objetos de la entidad Producto
     */

    //Metodo que guarda un Producto en la base de DB
    Producto guardar(Producto producto);
    //Parametros: recibe un objeto Producto con los datos a
    // guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Producto> buscarPorCodigo(int codigo);

    //Metodo que actualiza un Producto
    Producto actualizar(int codigo, Producto producto);

    /*
     * Metodo de tipo void para eliminar un Producto
     * void: no retorna ningun valor o dato
     * Elimina un Producto por su codigo
     * */
    void eliminar(int codigo);

    //boolean - Retorna true si existe y false si no existe
    boolean existePorCodigo(int codigo);

    List<Producto> listarActivos();
}
