<?php
$host="localhost";
$user="novaeno_agriasan";
$password="agriasan2019";
$db = "novaeno_agriasan";
 
$con = mysqli_connect($host,$user,$password,$db);
 
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }else{  //echo "Connect"; 
  
   
   }
 
?>