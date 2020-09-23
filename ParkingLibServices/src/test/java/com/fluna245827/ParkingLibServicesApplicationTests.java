package com.fluna245827;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fluna245827.controller.ParkingController;
import com.fluna245827.model.entity.Parking;
import com.fluna245827.model.entity.Place;
import com.fluna245827.model.entity.PricingPolicy;
import com.fluna245827.model.repository.PricingPolicyRepository;
import com.fluna245827.model.service.IParkingService;
import com.fluna245827.model.service.IPricingPolicyService;

@WebMvcTest(ParkingController.class)
@ComponentScan({ "com.fluna245827.model.service" })
@Import({ PricingPolicyRepository.class })
class ParkingLibServicesApplicationTests {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private IParkingService parkSrvMocked;

  @MockBean
  private IPricingPolicyService ppSrvMocked;

  private final String comeInAction = "comeIn";
  private final String comeOutAction = "comeOut";

  private final static String p2Name = "P2";
  private final static String byTheHourPricingPolName = "by_the_hour";
  private final static String fixedByTheHourPricingPolName = "fixed_and_by_the_hour";

  private static Parking p2;

  @BeforeAll
  static void setup() {
    // ('P2', 0, NULL, 200, NULL, 100, NULL, 'by_the_hour', 0, 7.99)
    p2 = new Parking();
    p2.setName(p2Name);
    p2.setCapacity_sedan(0);
    p2.setCapacity_elow(200);
    p2.setCapacity_ehigh(100);
    p2.setPricing_policy_name(byTheHourPricingPolName);
    p2.setPrice_fixed_amount(0);
    p2.setPrice_hour(7.99f);
  }

  @Test
  void should_fail_on_bad_inputs() {
    try {
      when(parkSrvMocked.findByName(p2Name)).thenReturn(null);
      String carType = "e_low";
      sendRequest(comeInAction, p2Name, carType, null, "Sorry but parking name '" + p2Name + "' doesn't exist.");

      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);
      carType = "unknown";
      sendRequest(comeInAction, p2Name, carType, null, "Sorry, the car type '" + carType + "' is not recognized");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_reserve_places() {
    try {
      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);
      String carType = "e_low";

      for (int i = 0; i < 3; i++) {
        sendRequest(comeInAction, p2Name, carType, null, "Welcome to parking <b>" + p2Name + "</b> ! You can now park your '" + carType + "' car on place number <b>P2-" + carType + ":" + i + "</b>");
      }

      carType = "e_high";

      for (int i = 0; i < 3; i++) {
        sendRequest(comeInAction, p2Name, carType, null, "Welcome to parking <b>" + p2Name + "</b> ! You can now park your '" + carType + "' car on place number <b>P2-" + carType + ":" + i + "</b>");
      }

      carType = "sedan";
      sendRequest(comeInAction, p2Name, carType, null, "Sorry, the parking is full for this type of car.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_fail_on_full_parking() {
    try {
      p2.setCapacity_sedan(3);
      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);

      String carType = "sedan";

      for (int i = 0; i < 3; i++) {
        sendRequest(comeInAction, p2Name, carType, null, "Welcome to parking <b>" + p2Name + "</b> ! You can now park your '" + carType + "' car on place number <b>P2-" + carType + ":" + i + "</b>");
      }

      sendRequest(comeInAction, p2Name, carType, null, "Sorry, the parking is full for this type of car.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_fail_on_empty_come_out() {
    try {
      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);
      String carType = "sedan";
      sendRequest(comeOutAction, p2Name, carType, "0", "Internal Error: Couldn't find the allocated place number for the requested car type. That place may be empty.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_fail_on_empty_come_out_input() {
    try {
      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);
      String carType = "sedan";
      sendRequest(comeOutAction, p2Name, carType, "X", "Sorry but the place number is not recognized as being a number.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_bill_on_come_out_zero_hours() {
    try {
      String carType = "e_high";
      String slotNumber = "1";

      Set<Place> places = new HashSet<Place>(1);
      Place place1 = new Place();
      place1.setCar_type(carType);
      place1.setEntrance_timestamp(new Timestamp((new Date()).getTime()));
      place1.setParking(p2);
      place1.setSlot_number(Integer.parseInt(slotNumber));

      places.add(place1);

      p2.setAvailable_ehigh(null);
      p2.setPlaces(places);

      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);

      PricingPolicy pp = new PricingPolicy();
      pp.setName(byTheHourPricingPolName);
      pp.setFormule("hours*price_hour");

      when(ppSrvMocked.getFormule(byTheHourPricingPolName)).thenReturn(pp);

      sendRequest(comeOutAction, p2Name, carType, slotNumber,
          "Goodbye from parking <b>" + p2Name + "</b> !</br>You were parked 0 hour(s). According to the parking price policy, you owe: 0.00 euros.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Test
  void should_bill_on_come_out_two_hours() {
    try {
      String carType = "e_low";
      String slotNumber = "2";
      long twoHoursMilis = 2 * 60 * 60 * 1000;

      Set<Place> places = new HashSet<Place>(1);
      Place place1 = new Place();
      place1.setCar_type(carType);
      place1.setEntrance_timestamp(new Timestamp((new Date()).getTime() - twoHoursMilis));
      place1.setParking(p2);
      place1.setSlot_number(Integer.parseInt(slotNumber));

      places.add(place1);

      p2.setAvailable_elow(null);
      p2.setPlaces(places);
      p2.setPricing_policy_name(fixedByTheHourPricingPolName);
      p2.setPrice_fixed_amount(10);

      when(parkSrvMocked.findByName(p2Name)).thenReturn(p2);

      PricingPolicy pp = new PricingPolicy();
      pp.setName(fixedByTheHourPricingPolName);
      pp.setFormule("price_fixed_amount+hours*price_hour");

      when(ppSrvMocked.getFormule(fixedByTheHourPricingPolName)).thenReturn(pp);

      sendRequest(comeOutAction, p2Name, carType, slotNumber,
          "Goodbye from parking <b>" + p2Name + "</b> !</br>You were parked 2 hour(s). According to the parking price policy, you owe: 25.96 euros.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void sendRequest(String action, String parkingName, String carType, String placeNumber, String expectedMsg) throws Exception {
    String pn = (placeNumber == null || placeNumber.isEmpty()) ? "" : "&placeNumber=" + placeNumber;
    mockMvc.perform(get("/" + action + "?parkingName=" + parkingName + "&carType=" + carType + pn).contentType(MediaType.TEXT_PLAIN)).andExpect(status().isOk())
        .andExpect(content().string(expectedMsg));
  }

}
