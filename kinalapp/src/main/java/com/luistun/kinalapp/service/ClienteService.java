package com.luistun.kinalapp.service;


import com.luistun.kinalapp.entity.Cliente;
import com.luistun.kinalapp.repository.ClienteRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
public class ClienteService implements IClienteService {
    /*
    * Private: solo es accesible dentro de la misma clase
    * final: No puede cambiar por que no es constante
    * ClienteRepository: El repositorio para acceder a la base DB
    * Inyeccion de Dependencias ya que Spring nos da el repositorio
    * */
    private final ClienteRepository clienteRepository;

    /*
    * Constructor: est6e se ejecuta al crear un objeto
    * Spring pasa el repositorio automaticamente(Inyeccion)
    * */
    public ClienteService(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
        //Asigna el repositorio a nuestra variable de clase
    }

    //Indica que esta implemenando un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la DB
    @Transactional(readOnly = true)
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
        //fillAll() es un metodo de spring que hace el select * from cleintes
        //este metodo es de JpaRepository
    }



    @Override
    public Cliente guardar(Cliente cliente) {
        /*
        * Metodo de guardar, crea un Cliente
        * Aca es donde colocamos la lagica del negocio Antes de guardar
        * Primero validamos el dato
        * */
        //validarCliente(cliente);
        //if(cliente.getEstado() == 0)
          //  cliente.setEstado(1);
        return clienteRepository.save(cliente);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Cliente> buscarPorDPI(String dpi) {
        //Buscar un cliente por DPi
        return clienteRepository.findById(dpi);
        //Opcional nos evita el nullPointer

    }

    @Override
    public Cliente actualizar(String dpi, Cliente cliente) {
        //Metodo para actualizar un cliente existente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro por DPI" +dpi);
            //si no existe se lanza una excepcion (error controlado)
        }
        cliente.setDPICliente(dpi);
        //Asegurarnos que el DPI del objeto coincida en el URL
        //Por seguridad usamos el DPI de la URL y no el que viene en el JSON
        validarCliente(cliente);


        return clienteRepository.save(cliente);
        /*
        * save() este sirve para guardar y tambien para actualizar si el dato
        * Existe (dpi) Entonces hace UPDATE pero si no existe hace un Insert pero
        * antes verificamos si existe o no el registro
        * */
    }

    @Override
    public void eliminar(String dpi) {
        //Eliminar cliente
        if(!clienteRepository.existsById(dpi)){
            throw new RuntimeException("El cliente no se encontro por el DPi "+dpi);
        }
        clienteRepository.deleteById(dpi);
    }




    @Override
    @Transactional(readOnly = true)
    public boolean existePorDPI(String dpi) {
        //verificar si existe un cliente
        return clienteRepository.existsById(dpi);
    }

    //Metodo privado(solo puede utilizarxe dentro de la clase)
    private void validarCliente(Cliente cliente){
        /*
        * Validaciones del negocio: Este metodo se hara privado por que es
        * algo interno del servicio
        * */

        if(cliente.getDPICliente() == null || cliente.getDPICliente().trim().isEmpty()){
            //Si el DPI es null o esta vacio despuesd de quitar espacios
            //Lanza una excepcion con un mensaje
            throw  new IllegalArgumentException("El DPI es un dato obligatorio");

        }
        if (cliente.getNombreCliente() == null || cliente.getNombreCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El nombre es un dato obligatorio");
        }
        if (cliente.getApellidoCliente() == null || cliente.getApellidoCliente().trim().isEmpty()){
            throw new IllegalArgumentException("El apellido es un dato obligatorio");
        }

    }

    @Override
    @Transactional(readOnly = true)
    public List<Cliente> listarActivos() {
        List<Cliente> clientes = clienteRepository.findAll();
        //if me sirve para poder ver los activos
        List<Cliente> activos = new java.util.ArrayList<>();
        for (Cliente cliente : clientes) {
            if (cliente.getEstado() == 1) {
                activos.add(cliente);
            } else {
            }
        }
        return activos;
    }




}
