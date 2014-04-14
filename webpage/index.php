<?php
include('config/dbconfig.php');
$sql="select * from coolit where location='kathmandu' order by id desc"; 
$query= mysql_query($sql) or die(mysql_error());
$data=mysql_fetch_array($query);
 
 
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

     <!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->
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
                          <li class="active"><a href="index.php">Home</a></li>
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
       <h1>Welcome to coll it !</h1>
         <div class="main_wrapper">
             <img class="cloud cloud1" src="images/clouds-small.png">
             <img class="cloud cloud2"  src="images/clouds-mini.png">
             <img src="images/clouds-small-1.png" class="cloud cloud3">
              <div class="col-sm-12"><!--col-lg-12-->
                <h2>Location: <span>Kathmandu</span></h2>
                <div class="row">
                    <div class="col-sm-6 text-center">
                        <div class="data_content">
                            <div class="image">
                            <?php
							$current_temp=20;
							if($current_temp>12 && $current_temp<31)
							{
							?>
                                <img src="images/normal.png"/>
                            <?php
							} 
							elseif($current_temp>30)
							{
						?>
								<img src="images/high.png"/>
                           <?php     
							}elseif($current_temp<12)
							{
								
							?>	
                            <img src="images/low.png"/>
							<?php
							}
							?>
                            
                            </div>
                            <h4>Temperatur:<?php echo $data['temperature'];?><span class="position">0</span>  c</h4>
                        </div><!--data content-->
                    </div><!--col-sm-6-->
                    <div class="col-sm-6 text-center"><!--col-sm-6-->
                        <div class="data_content">
                            <div class="image"><!--image-->
              					 <?php
							$current_humidity=20;
							if($current_humidity>12 && $current_humidity<31)
							{
							?>
                                <img src="images/normal.png"/>
                            <?php
							} 
							elseif($current_humidity>30)
							{
						?>
								<img src="images/high.png"/>
                           <?php     
							}elseif($current_humidity<12)
							{
								
							?>	
                            <img src="images/low.png"/>
							<?php
							}
							?>              
                                
                            </div><!-- EOF image-->
                            <h4>Humidity:<?php echo $data['humidity'];?><span class="position">0</span>  c</h4>
                        </div><!--data content-->
                    </div><!--EOF col-sm-6-->
                </div><!--EOF row-->
              </div><!-- Eof col-lg-12-->
          </div><!--main_wrapper-->
       </div><!-- EOF container-->
     </div><!-- EOF main_body_content-->
 </body>
</html>
