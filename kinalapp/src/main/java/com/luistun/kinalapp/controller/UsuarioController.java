package com.luistun.kinalapp.controller;

import com.luistun.kinalapp.entity.Usuario;
import com.luistun.kinalapp.service.IUsuarioService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RestController = @Controller + @RequestBody

@RequestMapping("/usuarios")
//Todas las rutas de este controller debe enpezar por /usuarios
public class UsuarioController {
    //Inyectamos el servicio el servicio y no el repository
    //El controlador solo debe tener conexion con el servidor
    private final IUsuarioService usuarioService;
    //como buena practica de la Inyeccion de dependencias debe hacerce por el constructor
    public UsuarioController(IUsuarioService usuarioService){
        this.usuarioService = usuarioService;
    }

    //Responde peticiones Get
    @GetMapping

    public ResponseEntity<List<Usuario>> listar(){
        List<Usuario> usuarios = usuarioService.listarTodos();
        //delegamos al servicio
        return ResponseEntity.ok(usuarios);
        // 200 ok con la lista de usuarios
    }

    //{codigo} es una variable de ruta(valor a buscar)
    @GetMapping("/{codigo}")
    public ResponseEntity<Usuario> buscarPorCodigoUsuario(@PathVariable int codigo){
        //@PathVariable  toma el valor de la URL y la asigna al codigo
        return usuarioService.buscarPorCodigo(codigo)
                //si aptional tiene valor, devuelve 200 ok con el usuario
                .map(ResponseEntity::ok)
                //si optional esta vacio
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    //Crear un nuevo Usuario
    public ResponseEntity<?> guardar(@RequestBody Usuario usuario){
        //@RequestBody: Toma el JSON del grupo y lo convierte a un objeto de tipo usuario
        //<?> significa "tipo generico" puede ser un cliente o un string
        try{
            Usuario nuevoUsuario = usuarioService.guardar(usuario);
            //Intentamos guardar el usuario pero puede lanzar una excepcion
            //Tipo illegalArgumentException
            return new ResponseEntity<>(nuevoUsuario, HttpStatus.CREATED);
            //201 CREATED(mucho mas especifico que el 200 para la creacion de un usuario)
        }catch (IllegalArgumentException e){
            //si hay errores de validacion
             //400 BAD REQUES con el nombre del error
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Actualizar usuario a travez del codigo
    @PutMapping("/{codigo}")
    public ResponseEntity<?> actualizar(@PathVariable int codigo, @RequestBody Usuario usuario) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                //verificar si esxiste antes de poder actualizar
                return ResponseEntity.notFound().build();
                //404 Not Found
            }
            //Actualizar al usuario pero esto puede lanzar una excepcion
            Usuario usuarioActualizado = usuarioService.actualizar(codigo, usuario);
            return ResponseEntity.ok(usuarioActualizado);
            //200 ok con el usuario ya actualiza

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            //Posiblemente cualquier otro error: usuario no encontrado , etc
            //404 NOT FOUND
            return ResponseEntity.notFound().build();
        }
    }


    //DELETE ilimina un usuario
    @DeleteMapping("/{codigo}")
    public ResponseEntity<Void> eliminar(@PathVariable int codigo) {
        try {
            if (!usuarioService.existePorCodigo(codigo)) {
                return ResponseEntity.notFound().build();
                //404 Si no Existe
            }
            usuarioService.eliminar(codigo);
            return ResponseEntity.noContent().build();
            //204 NO CONTENT (se ejecuto correctamente y no devulve cuerpo )
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
            //404 NOT FOUND
        }

    }

    @GetMapping("/activos")
    public ResponseEntity<List<Usuario>> listarActivos() {
        List<Usuario> activos = usuarioService.listarActivos();
        if (activos.isEmpty()) {
            // 204 si no hay usuarios activos
            return ResponseEntity.noContent().build();
        }
        // 200 con la lista de activos
        return ResponseEntity.ok(activos);
    }

}

