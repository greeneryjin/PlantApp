package com.example.demo.mygarden.repository;

import com.example.demo.mygarden.entity.MyPlant;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MyPlantRepository extends JpaRepository<MyPlant, Long> {

    Optional<Page<MyPlant>> findAllByAccountId(Pageable pageable, Long id);

    Page<MyPlant> findAll(Pageable pageable);

    Optional<List<MyPlant>> findAllByAccountId(Long id);
}
