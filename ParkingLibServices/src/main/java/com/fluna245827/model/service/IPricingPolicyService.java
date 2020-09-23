package com.fluna245827.model.service;

import com.fluna245827.model.entity.PricingPolicy;

public interface IPricingPolicyService {

  PricingPolicy getFormule(String pricingPolicyName);
}
