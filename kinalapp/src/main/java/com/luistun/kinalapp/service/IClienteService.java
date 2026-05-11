package com.luistun.kinalapp.service;


import com.luistun.kinalapp.entity.Cliente;

import java.util.List;
import java.util.Optional;

public interface IClienteService {

    /*
    * Interfaz: es un contrato que dice que metodos debe tener
    * cualquier servicio de clientes, no tiene
    * Implementacion, solo la definicion de los metodos
    * */

    //Metodo que devuelve una lsita de todos los clientes

    List<Cliente> listarTodos();
    /*
    * Liste<Cliente> lo que hace es devolver una lista
    * de objetos de la entidad Cliente
     */


    //Metodo que guarda un cliente en la base de DB
    Cliente guardar(Cliente cliente);
    //Parametros: recibe un objeto Cliente con los datos a
    // guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Cliente> buscarPorDPI(String dpi);

    //Metodo que actualiza un cliente
    Cliente actualizar(String dpi, Cliente cliente);

    /*
    * Parametros - dpi: DPI del cliente a actualizar
    * Cliente cliente: objeto con los datos nuevos
    * Retorna un objeto de tipo Cliente ya actualiza
    * */

    /*
    * Metodo de tipo void para eliminar a un Cliente
    * void: no retorna ningun valor o dato
    * Elimina un Cliente pos su DPI
    * */
    void eliminar(String dpi);


    //boolean - Retorna true si existe y false si no existe
    boolean existePorDPI(String dpi);

    List<Cliente> listarActivos();

}
