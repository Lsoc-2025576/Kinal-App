package com.luistun.kinalapp.controller;

import com.luistun.kinalapp.entity.Venta;
import com.luistun.kinalapp.service.IVentaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ventas")
public class VentaController {

    //Inyectamos el servicio y no el repository
    //El controlador solo debe de tener conexion con el servicio
    private final IVentaService ventaService;

    //Inyeccion de dependencias por constructor
    public VentaController(IVentaService ventaService) {
        this.ventaService = ventaService;
    }

    //Responde peticiones Get - lista todas las ventas
    @GetMapping
    public ResponseEntity<List<Venta>> listar(){
        List<Venta> ventas = ventaService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(ventas);
        // 200 ok con la lista de ventas
    }

    //{codigo} es una variable de ruta (valor a buscar)
    @GetMapping("/{codigo}")
    public ResponseEntity<Venta> buscarPorCodigo(@PathVariable int codigo){
        //@PathVariable toma el valor de la URL y la asigna al codigo
        return ventaService.buscarPorCodigo(codigo)
                //si optional tiene valor, devuelve 200 ok con la venta
                .map(ResponseEntity::ok)
                //si optional esta vacio
                .orElse(ResponseEntity.notFound().build());
    }

    //Post - crear una nueva venta
    @PostMapping
    public ResponseEntity<?> guardar(@RequestBody Venta venta){
        //@RequestBody: Toma el JSON del cuerpo y lo convierte a un objeto de tipo Venta
        // <?> significa "tipo generico" puede ser una venta o un string
        try{
            Venta nuevaVenta = ventaService.guardar(venta);
            //Intentamos guardar la venta pero puede lanzar una excepcion
            return new ResponseEntity<>(nuevaVenta, HttpStatus.CREATED);
            //201 CREATED
        }catch (IllegalArgumentException e){
            //si hay errores de validacion
            //400 BAD REQUEST con el mensaje del error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //DELETE - elimina una venta
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo){
        //ResponseEntity<Void>: no devuelve cuerpo en la respuesta
        try{
            if(!ventaService.existePorCodigo(codigo)){
                return ResponseEntity.notFound().build();
                //404 si no existe
            }
            ventaService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT
        }catch (RuntimeException e){
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    //Actualizar venta a traves del codigo
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Venta venta){
        try{
            if(!ventaService.existePorCodigo(codigo)){
                //verificar si existe antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 Not Found
            }
            //Actualizar la venta pero esto puede lanzar una excepcion
            Venta ventaActualizada = ventaService.actualizar(codigo, venta);
            return ResponseEntity.ok(ventaActualizada);
            //200 ok con la venta ya actualizada
        }catch(IllegalArgumentException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }catch (RuntimeException e){
            //Posiblemente cualquier otro error
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Venta>> listarActivos(){
        List<Venta> activos = ventaService.listarActivos();
        if(activos.isEmpty()){
            // 204 si no hay ventas activas
            return ResponseEntity.noContent().build();
        }
        // 200 con la lista de activos
        return ResponseEntity.ok(activos);
    }
}
