package com.fluna245827.model.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.fluna245827.model.entity.PricingPolicy;

@Repository
public interface IPricingPolicyRepository extends CrudRepository<PricingPolicy, String> {

  PricingPolicy findByName(String pricingPolicyName);
}
