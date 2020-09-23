package com.fluna245827.model.entity;

import java.sql.Timestamp;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Place {

  @Id
  @GeneratedValue
  @Basic(optional = false)
  @Column(name = "id", nullable = false)
  private Long id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parking_id", nullable = true)
  private Parking parking;

  @Column(length = 250)
  private String car_type;

  private int slot_number;

  private Timestamp entrance_timestamp;

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Parking getParking() {
    return parking;
  }

  public void setParking(Parking parking) {
    this.parking = parking;
  }

  public String getCar_type() {
    return car_type;
  }

  public void setCar_type(String car_type) {
    this.car_type = car_type;
  }

  public int getSlot_number() {
    return slot_number;
  }

  public void setSlot_number(int slot_number) {
    this.slot_number = slot_number;
  }

  public Timestamp getEntrance_timestamp() {
    return entrance_timestamp;
  }

  public void setEntrance_timestamp(Timestamp entrance_timestamp) {
    this.entrance_timestamp = entrance_timestamp;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((car_type == null) ? 0 : car_type.hashCode());
    result = prime * result + ((entrance_timestamp == null) ? 0 : entrance_timestamp.hashCode());
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((parking == null) ? 0 : parking.hashCode());
    result = prime * result + slot_number;
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null)
      return false;
    if (getClass() != obj.getClass())
      return false;
    Place other = (Place) obj;
    if (car_type == null) {
      if (other.car_type != null)
        return false;
    } else if (!car_type.equals(other.car_type))
      return false;
    if (entrance_timestamp == null) {
      if (other.entrance_timestamp != null)
        return false;
    } else if (!entrance_timestamp.equals(other.entrance_timestamp))
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (parking == null) {
      if (other.parking != null)
        return false;
    } else if (!parking.equals(other.parking))
      return false;
    if (slot_number != other.slot_number)
      return false;
    return true;
  }
}
