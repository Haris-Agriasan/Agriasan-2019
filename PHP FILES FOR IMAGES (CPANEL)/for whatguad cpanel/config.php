<?php
$host="localhost";
$user="wheatgua_agriasa";
$password="agriasan2019";
$db = "wheatgua_agriasan";
 
$con = mysqli_connect($host,$user,$password,$db);
 
// Check connection
if (mysqli_connect_errno())
  {
  echo "Failed to connect to MySQL: " . mysqli_connect_error();
  }else{  //echo "Connect"; 
  
   
   }
 
?>