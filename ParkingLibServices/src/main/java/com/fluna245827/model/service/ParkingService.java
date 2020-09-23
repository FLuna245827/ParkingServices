package com.fluna245827.model.service;

import java.sql.Timestamp;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fluna245827.model.entity.Parking;
import com.fluna245827.model.entity.Place;
import com.fluna245827.model.repository.IParkingRepository;
import com.fluna245827.model.repository.IPlaceRepository;

@Service
public class ParkingService implements IParkingService {

  @Autowired
  IParkingRepository parkRepo;

  @Autowired
  IPlaceRepository placesRepo;

  @Override
  public Parking findByName(String parkingName) {
    return parkRepo.findByName(parkingName);
  }

  @Override
  @Transactional
  public void registerIn(Parking park, String carType, int placeNumber) {
    Place p = new Place();
    p.setParking(park);
    p.setCar_type(carType);
    p.setSlot_number(placeNumber);
    p.setEntrance_timestamp(new Timestamp((new Date()).getTime()));

    placesRepo.save(p);
    parkRepo.save(park);
  }

  @Override
  @Transactional
  public void registerOut(Parking park, Place placeToFree) {
    park.getPlaces().remove(placeToFree);
    placesRepo.delete(placeToFree);
    parkRepo.save(park);
  }
}
