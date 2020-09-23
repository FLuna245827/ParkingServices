package com.fluna245827.model.service;

import com.fluna245827.model.entity.Parking;
import com.fluna245827.model.entity.Place;

public interface IParkingService {

  Parking findByName(String parkingName);

  void registerIn(Parking park, String carType, int placeNumber);

  void registerOut(Parking park, Place placeToFree);
}
