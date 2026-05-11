package com.luistun.kinalapp.repository;

import com.luistun.kinalapp.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductoRepository extends JpaRepository<Producto,Integer> {
}
