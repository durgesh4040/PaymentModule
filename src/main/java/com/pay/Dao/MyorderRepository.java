package com.pay.Dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.pay.Entity.Myorder;

public interface MyorderRepository extends JpaRepository<Myorder,Integer>{
 public Myorder findByOrderId(String orderId);
}
