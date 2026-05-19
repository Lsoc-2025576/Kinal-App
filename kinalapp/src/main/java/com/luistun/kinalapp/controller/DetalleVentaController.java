package com.luistun.kinalapp.controller;

import com.luistun.kinalapp.entity.DetalleVenta;
import com.luistun.kinalapp.service.IDetalleVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/detalle-ventas")
public class DetalleVentaController {

    //Inyectamos el servicio y no el repository
    private final IDetalleVentaService detalleVentaService;

    //Inyeccion de dependencias por constructor
    public DetalleVentaController(IDetalleVentaService detalleVentaService) {
        this.detalleVentaService = detalleVentaService;
    }

    //Responde peticiones Get - lista todos los detalles
    @GetMapping
    public ResponseEntity<List<DetalleVenta>> listar(){
        List<DetalleVenta> detalles = detalleVentaService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(detalles);
        // 200 ok con la lista de detalles
    }

    //{codigo} es una variable de ruta (valor a buscar)
    @GetMapping("/{codigo}")
    public ResponseEntity<DetalleVenta> buscarPorCodigo(@PathVariable int codigo){
        //@PathVariable toma el valor de la URL y la asigna al codigo
        return detalleVentaService.buscarPorCodigo(codigo)
                //si optional tiene valor, devuelve 200 ok con el detalle
                .map(ResponseEntity::ok)
                //si optional esta vacio devuelve 404
                .orElse(ResponseEntity.notFound().build());
    }

    //Post - crear un nuevo detalle de venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody DetalleVenta detalleVenta){
        //@RequestBody: Toma el JSON y lo convierte a objeto DetalleVenta
        // <?> tipo generico, puede ser un DetalleVenta o un String
        try{
            DetalleVenta nuevoDetalle = detalleVentaService.guardar(detalleVenta);
            //Intentamos guardar pero puede lanzar una excepcion
            return new ResponseEntity<>(nuevoDetalle, HttpStatus.CREATED);
            //201 CREATED
        }catch (IllegalArgumentException e){
            //si hay errores de validacion
            //400 BAD REQUEST con el mensaje del error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE - elimina un detalle de venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
        //ResponseEntity<Void>: no devuelve cuerpo en la respuesta
        try{
            if(!detalleVentaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            detalleVentaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //Actualizar detalle a traves del codigo
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody DetalleVenta detalleVenta){
        try{
            if(!detalleVentaService.existePorCodigo(codigo)){
                //verificar si existe antes de actualizar
                return ResponseEntity.notFound().build();
                //404 Not Found
            }
            //Actualizar el detalle pero puede lanzar una excepcion
            DetalleVenta detalleActualizado = detalleVentaService.actualizar(codigo, detalleVenta);
            return ResponseEntity.ok(detalleActualizado);
            //200 ok con el detalle ya actualizado
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            //Cualquier otro error
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }
}