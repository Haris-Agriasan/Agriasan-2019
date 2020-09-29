<?php
$host="localhost";
$user="roshne0_agriasan";
$password="agriasan2019";
$db = "roshne0_agriasan";
 
$con = mysqli_connect($host,$user,$password,$db);
 
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }else{  //echo "Connect"; 
  
   
   }
 
?>