package com.pay.Controller;

import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.pay.Dao.MyorderRepository;
import com.pay.Entity.Myorder;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;

@Controller
public class Mycontroller {
	@Autowired
	private MyorderRepository myorderRespository;
	
	
	@RequestMapping("/hello")
	//@ResponseBody
 public String view() {
	 return "form";
 }


//create order
@PostMapping("/create_order")
@ResponseBody
public String create_order(@RequestBody Map<String,Object> data) {
	System.out.println("order is created");
	  int amt=Integer.parseInt(data.get("amount").toString());
	  try {
		var client =new RazorpayClient("rzp_test_ezwaiZLv8pDC7f","0s7SIHvuW29tmnghNdzHYXkX");
	    JSONObject ob=new JSONObject();
	    ob.put("amount", amt*100);
	    ob.put("currency", "INR");
	    ob.put("receipt", "txn_235425");
	  Order order=  client.orders.create(ob);
	  System.out.println(order);
	  //save the order in database
	    Myorder myorder=new Myorder();
	    myorder.setAmount(order.get("amount"));
	    myorder.setOrderId(order.get("id"));
	    myorder.setPayment_id(null);
	    myorder.setStatus("created");
	    myorder.setRecepit(order.get("receipt"));
	    this.myorderRespository.save(myorder);
	  return order.toString();
	  } catch (RazorpayException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	  return "done";
}
// update order
@PostMapping("/update_order")
public ResponseEntity<?> update(@RequestBody Map<String,Object>data){
	System.out.println(data);
Myorder myorder=	this.myorderRespository.findByOrderId(data.get("order_id").toString());
	 myorder.setPayment_id(data.get("payment_id").toString());
	 myorder.setStatus(data.get("status").toString());
	 this.myorderRespository.save(myorder);
 return ResponseEntity.ok("updte");
}
}
