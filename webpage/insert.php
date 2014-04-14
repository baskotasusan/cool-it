<?php
include('config/dbconfig.php');
 $temp =$_POST['temp'];
 $humidity =$_POST['humidity'];
 $location =$_POST['location'];
 $current_temp=20;
 $current_humidity=28;
 $temp_differ=($current_temp)-($temp);
 $humidity_differ=($current_humidity)-($humidity);
 
  $sql="INSERT INTO coolit(location,temperature,humidity,tem_difference,humidity_difference) VALUES (' $location','$temp','$humidity','$temp_differ','$humidity_differ')";  //echo $sql;
		     // die();
    $query= mysql_query($sql) or die(mysql_error());
 
?>