<?php
include('config/dbconfig.php');

 $sql="select * from coolit"; //echo $sql;
		     // die();
 $query= mysql_query($sql) or die(mysql_error());
  
?>


<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cool it!</title>

    <!-- Bootstrap -->
    <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">

     <!-- jQuery (necessary for Bootstrap's JavaScript plugins) 
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>-->
    <!-- Include all compiled plugins (below), or include individual files as needed -->
    <script src="js/jquery-1.8.3.min.js"></script>
    <script src="js/bootstrap.min.js"></script>
    <!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
    <!--[if lt IE 9]>
      <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
      <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->
  </head>
  <body>
     <div id="header"><!--header-->
         <div class="container"><!--container-->
           <div class="row">
             <div class="col-sm-2">
             	<div id="logo">
                	<a href="index.html"><img src="images/logo.png" alt="logo"></a>
                </div><!--logo-->
             	<!--<h1> Cool it !!</h1>-->
              </div><!--col-sm-12-->
               <div class="col-sm-8">
                    <div class="main-menu">
                       <ul class="navigation_wrap">
                          <li ><a href="index.php">Home</a></li>
                          <li class="active"><a href="nearbylocation.php">Near By Location</a></li>
                          <li><a href="suggestion.php">Suggestion</a></li>
                       </ul><!--navigation-wrapper-->
                    </div><!--main menu-->
                 </div><!--col-sm-8-->
            </div><!--row-->
          </div><!-- EOf container-->
      </div><!-- EOf header-->
                
    <div id="main_body_content"><!--main_body_subpage-->
       <div class="container"><!--container-->
       <h1>Near By Location:</h1>
       <div class="main_wrapper">
         <div class="row"><!--row-->
          <div class="col-lg-12"><!--col-lg-12-->
              <table class="table table-striped" border="0" cellspacing="0" cellpadding="0">
               <tr style="color:#FFF;" bordercolor="0"> 
				<th>Location</th>
                <th>Temperature</th>
                <th>Humidity</th>
                <th>Temperature Difference</th>
                 <th>Humidity Difference</th>

             </tr>  
                <?php
			   while($result=mysql_fetch_array($query))
			 {
				 
			  ?>
                <tr class="success">
                  <td><?php echo $result['location'];  ?></td>
                  <td><?php echo $result['temperature'];?><sup>o</sup>C</td>
                  <td><?php echo $result['humidity'];  ?>%</td>
                  <td><?php echo $result['tem_difference'];  ?></td>
                  <td><?php echo $result['humidity_difference'];  ?></td>
                </tr> 
               <?php
			    }
			   ?>
                      
             </table>
          </div><!-- Eof col-lg-12-->
         </div><!--EOF row-->
         </div><!--main wrapper-->
       </div><!-- EOF container-->
     </div><!-- EOF main_body_subpage-->
      </body>
   </html>
