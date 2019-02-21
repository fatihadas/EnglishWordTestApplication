<?php
$requestid = "Yds781227*";
$limit=5;
if($_SERVER['REQUEST_METHOD'] == "POST"){
	//touch(addslashes($_POST["requestid"])."girdi.txt");
	if (isset($_POST["requestid"])) {
		if ($_POST["requestid"] != $requestid) {//touch(addslashes($_POST["requestid"])."girdi if.txt");
			echo json_encode(array("hata" => "islem reddedildi."));//echo '{"hata":"true"}';
			exit;
		}else{
			
			/*if(file_exists('sayac.txt')) {
			    $dosya = fopen('sayac.txt', 'r');
				$icerik = fread($dosya, filesize('sayac.txt'));
				$icerik++;
				fclose($dosya);
				$dosya2 = fopen('sayac.txt', 'w');
				fwrite($dosya2, $icerik);
				fclose($dosya2);
			} else {
			   touch("sayac.txt");
			   $dosya = fopen('sayac.txt', 'r+');
				fwrite($dosya, "1");
				fclose($dosya);
			}*/
			
		}
	} else {//touch(addslashes($_POST["requestid"])."girdi else.txt");
		echo json_encode(array("hata" => "islem reddedildi."));//echo '{"hata":"true"}';
		exit;
	}
	if (isset($_POST["limit"])){
		$limit=$_POST["limit"];
	}
	
	$host = "mysql.hostinger.web.tr";
	$database = "u911768291_anket";
	$username = "u911768291_anket";
	$password = "Fatih4815162342";

	try {
		$db = new PDO("mysql:charset=latin5;host=" . $host . ";dbname=" . $database . ";charset=utf8", $username, $password);
	} catch (PDOException $e) {
		print $e->getMessage();
	}

	$response = array();
	$query = $db->query("SELECT * FROM kelimeler ORDER BY RAND( ) LIMIT ".$limit, PDO::FETCH_ASSOC);
	if ( $query->rowCount() ){
		 foreach( $query as $row ){
			  $str = array("num" => $row['id'],"eng" => rtrim(ltrim(trim($row['english']))),"tur" => rtrim(ltrim(trim($row['turkish']))));
			  $response[] = $str;
		 }
	}

	echo json_encode($response);
}
else{
	echo json_encode(array("hata" => "islem reddedildi."));
	exit;
}

?>