package com.lcwd.electronic.store.repositories;

import com.lcwd.electronic.store.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, String> {

    //search
    List<Product> findByTitleContaining(String subtitle);
    //List<Product> findByLiveTrue(boolean live);

    //other methods
    //custom finder methods
    //query methods
}
