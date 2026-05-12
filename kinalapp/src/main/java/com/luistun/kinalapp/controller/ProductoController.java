package com.luistun.kinalapp.controller;

import com.luistun.kinalapp.entity.Producto;
import com.luistun.kinalapp.service.IProductoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody
@RequestMapping("/productos")
//Todas las rutas de este controlador debe expezar por /productos
public class ProductoController {
    //Inyectamos el servicio y no el repository
    //El controlador solo debe de tener conexion con el servidor

    private final IProductoService productoService;
    //Como buena practica de la Inyeccion de dependemcias debe hacerce por el contructor
    public ProductoController(IProductoService productoService) {
        this.productoService = productoService;
    }

    //Responde peticiones Get
    @GetMapping
    public ResponseEntity<List<Producto>> listar() {
        List<Producto> productos = productoService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(productos);
        //200 ok con la lista de producto
    }

    //{codigo} es una variable de ruta( valor a buscar)
    @GetMapping("/{codigo}")
    public ResponseEntity<Producto> buscarPorCodigo(@PathVariable int codigo) {
        //@PathVariable toma el valor de la URL y la asigna al codigo
        return productoService.buscarPorCodigo(codigo)
                //si optional tiene valor, devuelve 200 ok con el producto
                .map(ResponseEntity::ok)
                //si optional esta vacio
                .orElse(ResponseEntity.notFound().build());
    }

    //Post  crear un nuevo producto
    @PostMapping
    //@RequestBody: Toma el JSON del grupo y lo convierte a un objeto de tipo producto
    // <?> significa "tipo generico " puede ser un producto o un string
    public ResponseEntity<?> guardar(@RequestBody Producto producto) {
        try {
            Producto nuevoProducto = productoService.guardar(producto);
            //Instentamos guardar el producto pero puede lanzar una excepcion
            //Tipo IllegañArgumentException
            return new ResponseEntity<>(nuevoProducto, HttpStatus.CREATED);
            //201 CREATED(Mucho mas especifico que el 200 para la creacion de un producto)
        } catch (IllegalArgumentException e) {
            //si hay errores de validacion
            //400 BAD REQUEST con el nombre del error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Producto producto) {
        try {
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            Producto productoActualizado = productoService.actualizar(codigo, producto);
            return ResponseEntity.ok(productoActualizado);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    //DELETE eilimina un producto
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo) {
        //ResponseEntity<Void>: no devuelve cuerpo en la respuesta
        try {
            if (!productoService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
            }
            productoService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuto correctamente y no devulve cuerpo )
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }
    }

    @GetMapping("/activos")
    public ResponseEntity<List<Producto>> listarActivos() {
        List<Producto> activos = productoService.listarActivos();
        if (activos.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(activos);
    }
}
