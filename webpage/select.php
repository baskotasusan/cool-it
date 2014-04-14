<?php
include('config/dbconfig.php');
 $sql="select * from coolit"; 
$query= mysql_query($sql) or die(mysql_error());

while($result=mysql_fetch_assoc($query))
{
        $output[]=$result;
}
print(json_encode($output));
?>
 
