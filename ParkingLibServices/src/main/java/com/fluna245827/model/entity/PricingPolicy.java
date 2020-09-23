package com.fluna245827.model.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Entity
public class PricingPolicy {
  @Id
  @NotNull(message = "Pricing policy name can't be null")
  @Column(length = 250)
  private String name;
  
  @NotNull(message = "Pricing policy formule can't be null")
  @Column(length = 250)
  private String formule;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFormule() {
    return formule;
  }

  public void setFormule(String formule) {
    this.formule = formule;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((formule == null) ? 0 : formule.hashCode());
    result = prime * result + ((name == null) ? 0 : name.hashCode());
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
    PricingPolicy other = (PricingPolicy) obj;
    if (formule == null) {
      if (other.formule != null)
        return false;
    } else if (!formule.equals(other.formule))
      return false;
    if (name == null) {
      if (other.name != null)
        return false;
    } else if (!name.equals(other.name))
      return false;
    return true;
  }

}
