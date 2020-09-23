package com.fluna245827.model.repository;

import java.util.Optional;

import com.fluna245827.model.entity.PricingPolicy;

public class PricingPolicyRepository implements IPricingPolicyRepository {

  @Override
  public <S extends PricingPolicy> S save(S entity) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public <S extends PricingPolicy> Iterable<S> saveAll(Iterable<S> entities) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Optional<PricingPolicy> findById(String id) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public boolean existsById(String id) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Iterable<PricingPolicy> findAll() {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public Iterable<PricingPolicy> findAllById(Iterable<String> ids) {
    // TODO Auto-generated method stub
    return null;
  }

  @Override
  public long count() {
    // TODO Auto-generated method stub
    return 0;
  }

  @Override
  public void deleteById(String id) {
    // TODO Auto-generated method stub

  }

  @Override
  public void delete(PricingPolicy entity) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll(Iterable<? extends PricingPolicy> entities) {
    // TODO Auto-generated method stub

  }

  @Override
  public void deleteAll() {
    // TODO Auto-generated method stub

  }

  @Override
  public PricingPolicy findByName(String pricingPolicyName) {
    // TODO Auto-generated method stub
    return null;
  }

}
