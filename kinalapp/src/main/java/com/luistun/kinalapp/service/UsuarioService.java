package com.luistun.kinalapp.service;

import com.luistun.kinalapp.entity.Usuario;
import com.luistun.kinalapp.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

 /*
 * Anotacion: que registra un Bean como un Bean de Spring
 * Que la clase contiene la logica del negocio
 * */
@Service

@Transactional
 /*
 * Por defecto todos los metodos de esta clase seran transaccional
 * Una transaccion es que puede o no ocurrir algo
 * */

public class UsuarioService implements IUsuarioService {
     /*
      * Private: solo es accesible dentro de la misma clase
      * final: No puede cambiar por que no es constante
      * ClienteRepository: El repositorio para acceder a la base DB
      * Inyeccion de Dependencias ya que Spring nos da el repositorio
      * */
    private final UsuarioRepository usuarioRepository;
     /*
      * Constructor: este se ejecuta al crear un objeto
      * Spring pasa el repositorio automaticamente(Inyeccion)
      * */

    public UsuarioService(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
        //Asigna el repositorio a nuestra variable de clase
    }

     //Indica que esta implemenando un metodo de la interfaz
    @Override
    //Optimizar la consulta, solo lectura, para que no bloquee la DB
    @Transactional(readOnly = true)
    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
        //fillAll() es un metodo de spring que hace el select * from cleintes
        //este metodo es de JpaRepository
    }

    @Override
    public Usuario guardar(Usuario usuario) {
        /*
         * Metodo de guardar, crea un Usuario
         * Aca es donde colocamos la lagica del negocio Antes de guardar
         * Primero validamos el dato
         * */
        //validarCliente(cliente);
        //if(Usuario.getEstado() == 0)
        //  usuario.setEstado(1);
        validarUsuario(usuario);
        return usuarioRepository.save(usuario);
    }


    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorCodigo(int codigo) {
        //Buscar un usuario por codigo
        return usuarioRepository.findById(codigo);
        //Opcional nos evita el nullPointer
    }


    @Override
    public Usuario actualizar(int codigo, Usuario usuario) {
        //Metodo para actualizar un usuario existente
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("El usuario no se encontro con codigo " + codigo);
            //si existe se lanza una excepcion (error controlado)
        }

        usuario.setCodigoUsuario(codigo);
        //Asegurarse que codigo del objeto coincida con el URL
        //Por seguridad usamos el codigo de la URL y no el que viene en el JSON
        validarUsuario(usuario);

        return usuarioRepository.save(usuario);
        /*
         * save() este sirve para guardar y tambien para actualizar si el dato
         * Existe (codigo) Entonces hace UPDATE pero si no existe hace un Insert pero
         * antes verificamos si existe o no el registro
         * */
    }

    @Override
    public void eliminar(int codigo) {
        //Eliminar usuario
        if (!usuarioRepository.existsById(codigo)) {
            throw new RuntimeException("El usuario no se encontro con codigo " + codigo);
        }
        usuarioRepository.deleteById(codigo);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorCodigo(int codigo) {
        //verifica si existe un cliente
        return usuarioRepository.existsById(codigo);
    }

    //Metodo privado(solo puede utilizarse dentro de la clase)
    private void validarUsuario(Usuario usuario) {
        /*
         * Validaciones del negocio: Este metodo se hara privado por que es
         * algo interno del servicio
         * */

        if (usuario.getUsername() == null || usuario.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("El username es un dato obligatorio");
        }
        if (usuario.getPassword() == null || usuario.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("El password es un dato obligatorio");
        }
        if (usuario.getEmail() == null || usuario.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("El email es un dato obligatorio");
        }
    }

    @Override
    //lista los usuarios activos
    @Transactional(readOnly = true)
    public List<Usuario> listarActivos() {
        List<Usuario> usuarios = usuarioRepository.findAll();
        List<Usuario> activos = new ArrayList<>();
        //for-if me sirve para poder ver la lista de activoz
        for (Usuario usuario : usuarios) {
            if (usuario.getEstado() == 1) {
                activos.add(usuario);
            }
        }
        return activos;
    }
}