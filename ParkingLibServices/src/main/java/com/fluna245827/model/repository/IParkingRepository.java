package com.fluna245827.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fluna245827.model.entity.Parking;

@Repository
public interface IParkingRepository extends CrudRepository<Parking, Long> {

  Parking findByName(String parkingName);
}
