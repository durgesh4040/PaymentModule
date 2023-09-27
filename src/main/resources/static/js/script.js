

const paymentstart = () => {
  console.log("start payment");
  var amount = $("#checkout").val();
  console.log(amount);
  
  if (amount === "" || amount === null) {
    console.log("not print");
    alert("Value is required!");
    return;
  }
  
  
 $.ajax({
    url: '/create_order',
    type: 'POST',
    dataType: 'json',
    contentType: 'application/json',
    data: JSON.stringify({ amount: amount, info: 'order_request' }),
    success: function(response) {
      console.log(response);
      // Handle the successful response here
      if(response.status=="created"){
        let options={
          key:'rzp_test_ezwaiZLv8pDC7f',
          amount:response.amount,
          currency:'INR',
          name:'Public Money',
          description:'raisefund',
          image:'https://companieslogo.com/img/orig/IBN-af38b5c0.png?t=1648383607',
          order_id:response.id,
          handler:function(response){
            console.log(response.razorpay_payment_id);
            console.log(response.razorpay_order_id);
            //console.log(razorpay_signature);
            console.log('payment successful');
updatePaymentOnServer(response.razorpay_payment_id,response.razorpay_order_id,'paid')
            alert("congrats successfull !!!");
          },
          prefill:{
            name:"",
            email:"",
            contact:"",
          },
          notes:{
            address:"funding for ai development",
          },
          theme:{
            color:"#3399cc"
          },
        };
        let rzp=new Razorpay(options);
        rzp.on("payment.failed",function(response){
        console.log(response.error.code);
        console.log(response.error.description);
        console.log(response.error.source);
        console.log(response.error.step);
        console.log(response.error.reason);
        console.log(response.error.metadata.order.id);
        console.log(response.error.metadata.payment.id);
        alert("oops payment fail");
        });
        rzp.open();
      }
    },
    error: function(error) {
      console.log(error);
      alert('Something went wrong');
      // Handle the error here
    }
  });
  }
  function updatePaymentOnServer(payment_id,order_id,status){
    $.ajax({
    url:'/update_order',
    data: JSON.stringify({ payment_id: payment_id, order_id:order_id,status:status}),
    contentType: 'application/json',
    type:'POST',
    datatype:'json',
    success : function(response){
    console.log("successful")
    },
    error:function(response){
   console.log("not successful")
    }
    });
  }



