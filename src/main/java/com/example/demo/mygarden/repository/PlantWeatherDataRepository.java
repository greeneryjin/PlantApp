package com.example.demo.mygarden.repository;

import com.example.demo.mygarden.entity.PlantWeather;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PlantWeatherDataRepository extends JpaRepository<PlantWeather, Long> {

    Optional<PlantName> findOnlyByPlantName(String plantName);

    Optional<PlantWeather> findByPlantName(String plantName);

    List<PlantWeather> findAllById(Long id);

    @Query("select p from PlantWeather p where p.plantName like concat('%',:plantName,'%')")
    Page<PlantWeather> findAllByPlantName(Pageable pageable, @Param("plantName") String plantName);
}
