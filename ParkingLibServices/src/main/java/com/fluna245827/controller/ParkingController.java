package com.fluna245827.controller;

import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fluna245827.model.entity.Parking;
import com.fluna245827.model.entity.Place;
import com.fluna245827.model.entity.PricingPolicy;
import com.fluna245827.model.service.IParkingService;
import com.fluna245827.model.service.IPricingPolicyService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
public class ParkingController {
  @Autowired
  IParkingService parkSrv;

  @Autowired
  @Qualifier("mainPricingPolicyService")
  IPricingPolicyService pricingSrv;

  public static enum CarTypes {
    sedan, e_high, e_low
  };

  private static enum ActionType {
    IN, OUT
  };

  private static enum PricingColumn {
    hours, price_hour, price_fixed_amount
  };

  // can be labeled as anything. For example: "FREE" or something else.
  private final char EMPTY = 'o';
  private final char TAKEN = 'x';

  private final String ERROR = "err";

  @RequestMapping(value = "/comeIn", method = RequestMethod.GET)
  @Operation(summary = "Register a car type entering an specific parking.")
  public String comeInAction(@RequestParam(name = "parkingName", required = true) String parkingName, @RequestParam(name = "carType", required = true) String carType) {

    Object[] data = null;

    try {
      data = validateParkingAndCarType(parkingName, carType);
    } catch (Exception e) {
      return e.getMessage();
    }

    Parking park = (Parking) data[0];
    CarTypes ct = (CarTypes) data[1];

    // validate and at the same time get the car type enum
    try {
      ct = CarTypes.valueOf(carType);
    } catch (Exception e) {
      return "Sorry, the car type '" + carType + "' is not recognized";
    }

    String placeNum = processAvailablePlace(ActionType.IN, park, ct, 0);

    if (placeNum == null) {
      return "Sorry, the parking is full for this type of car.";
    }

    return "Welcome to parking <b>" + parkingName + "</b> ! You can now park your '" + carType + "' car on place number <b>" + parkingName + "-" + carType + ":" + placeNum + "</b>";
  }

  @RequestMapping(value = "/comeOut", method = RequestMethod.GET)
  @Operation(summary = "De-register a car type exiting an specific parking, and calculate the bill to pay according to the parking's pricing policy.")
  public String comeOutAction(@RequestParam(name = "parkingName", required = true) String parkingName, @RequestParam(name = "carType", required = true) String carType,
      @RequestParam(name = "placeNumber", required = true) String placeNumber) {

    Object[] data = null;

    try {
      data = validateParkingAndCarType(parkingName, carType);
    } catch (Exception e) {
      return e.getMessage();
    }

    Parking park = (Parking) data[0];
    CarTypes ct = (CarTypes) data[1];

    // validate the place number
    int pn = -1;

    try {
      pn = Integer.parseInt(placeNumber);
      int maxVal = 0;

      switch (ct) {
      case sedan:
        maxVal = park.getCapacity_sedan();
        break;

      case e_high:
        maxVal = park.getCapacity_ehigh();
        break;

      case e_low:
        maxVal = park.getCapacity_elow();
        break;

      default:
      }

      if (pn >= maxVal || pn < 0) {
        return "The place number is not valid.";
      }

    } catch (Exception e) {
      return "Sorry but the place number is not recognized as being a number.";
    }

    String hoursAndAmountToPay = processAvailablePlace(ActionType.OUT, park, ct, pn);
    String[] tempo = hoursAndAmountToPay.split(";");

    if (tempo[0].equals(ERROR)) {
      return tempo[1];
    }

    return "Goodbye from parking <b>" + parkingName + "</b> !</br>You were parked " + tempo[0] + " hour(s). According to the parking price policy, you owe: " + tempo[1];
  }

  private Object[] validateParkingAndCarType(String parkingName, String carType) throws Exception {
    Object[] data = new Object[2];

    Parking park = getParking(parkingName);

    if (park == null) {
      throw new Exception("Sorry but parking name '" + parkingName + "' doesn't exist.");
    }

    CarTypes ct = null;

    // validate and at the same time get the car type enum
    try {
      ct = CarTypes.valueOf(carType);
    } catch (Exception e) {
      throw new Exception("Sorry, the car type '" + carType + "' is not recognized");
    }

    data[0] = park;
    data[1] = ct;

    return data;
  }

  private Parking getParking(String parkingName) {
    return parkSrv.findByName(parkingName);
  }

  @Transactional(rollbackFor = Exception.class) // abort DB operations on any
                                                // exception
  private String processAvailablePlace(ActionType action, Parking park, CarTypes carType, int placeToBeFreed) {
    String parkingSlots = null;
    int carTypeCapacity = 0;

    // get parking data
    switch (carType) {
    case sedan:
      parkingSlots = park.getAvailable_sedan();
      carTypeCapacity = park.getCapacity_sedan();
      break;

    case e_high:
      parkingSlots = park.getAvailable_ehigh();
      carTypeCapacity = park.getCapacity_ehigh();
      break;

    case e_low:
      parkingSlots = park.getAvailable_elow();
      carTypeCapacity = park.getCapacity_elow();
      break;

    default:
    }

    // get parking slots
    char[] slots = new char[carTypeCapacity];

    if (parkingSlots == null) {
      StringBuffer sb = new StringBuffer(carTypeCapacity);

      for (int i = 0; i < carTypeCapacity; i++) {
        sb.append(EMPTY);
      }

      slots = sb.toString().toCharArray();
    } else {
      slots = parkingSlots.toCharArray();
    }

    int placeNum = -1;

    if (action.equals(ActionType.IN)) {
      // find the first empty slot
      int idx = 0;

      for (char c : slots) {
        if (c == EMPTY) {
          placeNum = idx;
          break;
        }

        idx++;
      }

      // all full ?
      if (placeNum == -1) {
        return null;
      }

      // reserve place
      slots[placeNum] = TAKEN;

    } else {
      // free place
      if (placeToBeFreed > slots.length) {
        return ERROR + ";The given place number is empty. There is no car coming out.";
      }

      slots[placeToBeFreed] = EMPTY;
    }

    // update parking places info
    String updatedParkingSlots = (new StringBuffer(carTypeCapacity)).append(slots).toString();

    switch (carType) {
    case sedan:
      park.setAvailable_sedan(updatedParkingSlots);
      break;

    case e_high:
      park.setAvailable_ehigh(updatedParkingSlots);
      break;

    case e_low:
      park.setAvailable_elow(updatedParkingSlots);
      break;

    default:
    }

    if (action.equals(ActionType.IN)) {
      // save parking data
      parkSrv.registerIn(park, carType.name(), placeNum);
      return String.valueOf(placeNum); // return place number

    } else {
      // fetch the occupied place
      park.getPlaces().size(); // force fetch (because is LAZY)
      List<Place> places = park.getPlaces().stream().filter(p -> carType.name().equals(p.getCar_type()) && p.getSlot_number() == placeToBeFreed).collect(Collectors.toList());

      if (places.isEmpty()) { // should not
        return ERROR + ";Internal Error: Couldn't find the allocated place number for the requested car type. That place may be empty.";
      }

      Place takenPlace = places.get(0);

      // calculate hours spent and data for billing
      Duration dur = Duration.between(takenPlace.getEntrance_timestamp().toInstant(), Instant.now());
      long hours = dur.toHours();
      double price_hour = park.getPrice_hour();
      double price_fixed_amount = park.getPrice_fixed_amount();

      PricingPolicy pricingPol = null;

      try {
        pricingPol = pricingSrv.getFormule(park.getPricing_policy_name());
      } catch (Exception e) {
        return ERROR + ";Internal Error: Couldn't find the pricing formule name for this parking: " + park.getPricing_policy_name();
      }

      // save parking data
      parkSrv.registerOut(park, takenPlace);

      // finally, calculate bill
      return applyPricingFormule(pricingPol.getFormule(), hours, price_hour, price_fixed_amount);
    }
  }

  // This method can be changed in the future with other pricing policy rules
  private String applyPricingFormule(String formule, long hours, double price_hour, double price_fixed_amount) {
    // replace data in formula
    String f = formule.replace(PricingColumn.hours.name(), doubleToString(hours, 2));
    f = f.replace(PricingColumn.price_hour.name(), doubleToString(price_hour, 2));
    f = f.replace(PricingColumn.price_fixed_amount.name(), doubleToString(price_fixed_amount, 2));

    // evaluate formula
    ScriptEngineManager scriptMgr = new ScriptEngineManager();
    ScriptEngine jsEngine = scriptMgr.getEngineByName("JavaScript");

    String toPay = null;

    try {
      Double toPayObj = (Double) jsEngine.eval(f);
      toPay = doubleToString(toPayObj.doubleValue(), 2);
    } catch (ScriptException e) {
      return ERROR + ";Internal Error: Can't evaluate the formule: " + f;
    }

    return hours + ";" + toPay + " euros."; // return hours and amount to pay
  }

  // small tool helpful for displaying double values with the specified number
  // of decimals
  private String doubleToString(double doubleValue, int numDecimals) {
    String d = String.valueOf(doubleValue);
    int idx = d.indexOf(".");

    if (idx == -1) {
      return d;
    }

    // complete
    int padding = (idx + numDecimals + 1) - d.length();

    for (int i = 0; i < padding; i++) {
      d += "0";
    }

    return d.substring(0, idx + numDecimals + 1);
  }

}
