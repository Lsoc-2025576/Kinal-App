package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface IUsuarioService {
    /*
     * Interfaz: es un contrato que dice que metodos debe tener
     * cualquier servicio de usuarios, no tiene
     * Implementacion, solo la definicion de los metodos
     * */

    //Metodo que devuelve una lista de todos los usuarios
    List<Usuario> listarTodos();
    /*
     * List<Usuario> lo que hace es devolver una lista
     * de objetos de la entidad Usuarios
     */

    //Metodo que guarda un Usuario en la base de DB
    Usuario guardar (Usuario usuario );
    //Parametros: recibe un objeto Usuario con los datos a
    //guardar

    //Optional - Contenedor que puede o no tener valor
    //evita el error de NullPointerException
    Optional<Usuario> buscarPorCodigo(int codigo);

    //Metodo que actualiza un Usuario
    Usuario actualizar(int codigo, Usuario usuario);

    /*
    * Metodo de tipo void para eliminar a un Usuario
    * void: no retorna ningun valor o dato
    * Elimina un Usuario por su codigo
    * */
    void eliminar(int codigo);

    //boolean - Retorna true si existe y false si no existe
    boolean existePorCodigo(int codigo);

    //List muestra la lista de usuarios activos
    List<Usuario> listarActivos();

}
