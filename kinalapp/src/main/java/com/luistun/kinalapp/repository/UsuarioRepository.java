package com.luistun.kinalapp.repository;

import com.luistun.kinalapp.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario,Integer> {
    
}
