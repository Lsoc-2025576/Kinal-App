package com.luistun.kinalapp.controller;

import com.luistun.kinalapp.entity.Cliente;
import com.luistun.kinalapp.service.IClienteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody
@RequestMapping("/clientes")
//Todas las rutas de este controlador debe expezar por / clientes
public class ClienteController {
    //Inyectamos el servicio y no el repository
    //El controlador solo debe de tener conexion con el servidor
    private final IClienteService clienteService;
    //Como buena practica de la Inyeccion de dependemcias debe hacerce por el contructor
    public ClienteController(IClienteService clienteService) {
        this.clienteService = clienteService;
    }

    //Responde peticiones Get
    @GetMapping
    //
    public ResponseEntity<List<Cliente>> listar(){
        List<Cliente> clientes =clienteService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(clientes);
        // 200 ok con la lista de clientes
    }

    //{dpi} es una variable de ruta( valor a buscar)
    @GetMapping("/{dpi}")
    public ResponseEntity<Cliente> buscarPorDPI(@PathVariable String dpi){
        //@PathVariable toma el valor de la URL y la asigna al dpi
        return clienteService.buscarPorDPI(dpi)
                //si optional tiene valor, devuelve 200 ok con el cliente
                .map(ResponseEntity::ok)
                //si optional esta vacio
                .orElse(ResponseEntity.notFound().build());
    }

    //Post  crear un nuevo cliente
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Cliente cliente){
        //@RequestBody: Toma el JSON del grupo y lo convierte a un objeto de tipo cliente
        // <?> significa "tipo generico " puede ser un cliente o un string
        try{
            Cliente nuevoCliente = clienteService.guardar(cliente);
            //Instentamos guardar el cliente pero puede lanzar una excepcion
            //Tipo IllegañArgumentException
            return new ResponseEntity<>(nuevoCliente, HttpStatus.CREATED);
            //201 CREATED(Mucho mas especifico que el 200 para la creacion de un cliente)
        }catch (IllegalArgumentException e){
            //si hay errores de validacion
            //400 BAD REQUEST con el nombre del error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    //DELETE ilimina un cliente
    @DeleteMapping("/{dpi}")
    public ResponseEntity<Void> eliminar(@PathVariable String dpi){
        //ResponseEntity<Void>: no devuelve cuerpo en la respuesta

        try{
            if(!clienteService.existePorDPI(dpi)){
                return ResponseEntity.notFound().build();
                //404 Si no Existe
            }
            clienteService.eliminar(dpi);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuto correctamente y no devulve cuerpo )
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    //Actualizar cliente a travez del DPI
    @PutMapping("/{dpi}")
    public ResponseEntity<?> actualizar(@PathVariable String dpi, @RequestBody Cliente cliente){
        try{
            if(!clienteService.existePorDPI(dpi)){
                //verificar si esxiste antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 Not Found

            }
            //Actualizar al cliente pero esto puede lanzar una excepcion
            Cliente clienteActualizado = clienteService.actualizar(dpi, cliente);
            return ResponseEntity.ok(clienteActualizado);
            //200 ok con el cliente ya actualiza

        }catch(IllegalArgumentException e ){
            return ResponseEntity.badRequest().body(e.getMessage());

        }catch (RuntimeException e ){
            //Posiblemente cualquier otro error: Cliente no encontrado , etc
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Cliente>> listarActivos() {
        List<Cliente> activos = clienteService.listarActivos();
        if (activos.isEmpty()) {
            // 204 si no hay clientes activos
            return ResponseEntity.noContent().build();
        } else {
            // 200 con la lista de activos
            return ResponseEntity.ok(activos);
        }
    }



}
