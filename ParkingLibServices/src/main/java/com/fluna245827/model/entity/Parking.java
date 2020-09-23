package com.fluna245827.model.entity;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Entity
public class Parking {
  @Id
  @GeneratedValue
  private Long id;

  @NotNull(message = "Parking's name can't be null")
  @Column(length = 250)
  private String name;

  @NotNull(message = "Capacity for sedan cars can't be null")
  private int capacity_sedan;

  @Column(length = 500)
  private String available_sedan;

  @NotNull(message = "Capacity for electric low power (20kWh) cars can't be null")
  private int capacity_elow;

  @Column(length = 500)
  private String available_elow;

  @NotNull(message = "Capacity for electric high power (50kWh) cars can't be null")
  private int capacity_ehigh;

  @Column(length = 500)
  private String available_ehigh;

  @NotNull(message = "Parking's pricing policy name can't be null")
  private String pricing_policy_name;

  @PositiveOrZero
  private float price_fixed_amount;

  @PositiveOrZero
  private float price_hour;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parking")
  private Set<Place> places = new HashSet<>();

  public Long getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public int getCapacity_sedan() {
    return capacity_sedan;
  }

  public void setCapacity_sedan(int capacity_sedan) {
    this.capacity_sedan = capacity_sedan;
  }

  public String getAvailable_sedan() {
    return available_sedan;
  }

  public void setAvailable_sedan(String available_sedan) {
    this.available_sedan = available_sedan;
  }

  public int getCapacity_elow() {
    return capacity_elow;
  }

  public void setCapacity_elow(int capacity_elow) {
    this.capacity_elow = capacity_elow;
  }

  public String getAvailable_elow() {
    return available_elow;
  }

  public void setAvailable_elow(String available_elow) {
    this.available_elow = available_elow;
  }

  public int getCapacity_ehigh() {
    return capacity_ehigh;
  }

  public void setCapacity_ehigh(int capacity_ehigh) {
    this.capacity_ehigh = capacity_ehigh;
  }

  public String getAvailable_ehigh() {
    return available_ehigh;
  }

  public void setAvailable_ehigh(String available_ehigh) {
    this.available_ehigh = available_ehigh;
  }

  public String getPricing_policy_name() {
    return pricing_policy_name;
  }

  public void setPricing_policy_name(String pricing_policy_name) {
    this.pricing_policy_name = pricing_policy_name;
  }

  public double getPrice_fixed_amount() {
    return price_fixed_amount;
  }

  public void setPrice_fixed_amount(float price_fixed_amount) {
    this.price_fixed_amount = price_fixed_amount;
  }

  public double getPrice_hour() {
    return price_hour;
  }

  public void setPrice_hour(float price_hour) {
    this.price_hour = price_hour;
  }

  public Set<Place> getPlaces() {
    return places;
  }

  public void setPlaces(Set<Place> places) {
    this.places = places;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((available_ehigh == null) ? 0 : available_ehigh.hashCode());
    result = prime * result + ((available_elow == null) ? 0 : available_elow.hashCode());
    result = prime * result + ((available_sedan == null) ? 0 : available_sedan.hashCode());
    result = prime * result + capacity_ehigh;
    result = prime * result + capacity_elow;
    result = prime * result + capacity_sedan;
    result = prime * result + ((id == null) ? 0 : id.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
    result = prime * result + Float.floatToIntBits(price_fixed_amount);
    result = prime * result + Float.floatToIntBits(price_hour);
    result = prime * result + ((pricing_policy_name == null) ? 0 : pricing_policy_name.hashCode());
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
    Parking other = (Parking) obj;
    if (available_ehigh == null) {
      if (other.available_ehigh != null)
        return false;
    } else if (!available_ehigh.equals(other.available_ehigh))
      return false;
    if (available_elow == null) {
      if (other.available_elow != null)
        return false;
    } else if (!available_elow.equals(other.available_elow))
      return false;
    if (available_sedan == null) {
      if (other.available_sedan != null)
        return false;
    } else if (!available_sedan.equals(other.available_sedan))
      return false;
    if (capacity_ehigh != other.capacity_ehigh)
      return false;
    if (capacity_elow != other.capacity_elow)
      return false;
    if (capacity_sedan != other.capacity_sedan)
      return false;
    if (id == null) {
      if (other.id != null)
        return false;
    } else if (!id.equals(other.id))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    if (places == null) {
      if (other.places != null)
        return false;
    } else if (!places.equals(other.places))
      return false;
    if (Float.floatToIntBits(price_fixed_amount) != Float.floatToIntBits(other.price_fixed_amount))
      return false;
    if (Float.floatToIntBits(price_hour) != Float.floatToIntBits(other.price_hour))
      return false;
    if (pricing_policy_name == null) {
      if (other.pricing_policy_name != null)
        return false;
    } else if (!pricing_policy_name.equals(other.pricing_policy_name))
      return false;
    return true;
  }
}
