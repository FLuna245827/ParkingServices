package com.fluna245827.model.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fluna245827.model.entity.PricingPolicy;
import com.fluna245827.model.repository.IPricingPolicyRepository;

@Service("mainPricingPolicyService")
public class PricingPolicyService implements IPricingPolicyService {
  @Autowired
  IPricingPolicyRepository pricingRepo;

  @Override
  public PricingPolicy getFormule(String pricingPolicyName) {
    return pricingRepo.findByName(pricingPolicyName);
  }
}
