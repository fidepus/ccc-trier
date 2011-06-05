<?php
if($_GET['registration_id'] != '') {
	$registration_id = sqlite_escape_string($_GET['registration_id']);
	if($db = sqlite_open("db.sqlite")) {
		$result = sqlite_query($db, 
			'INSERT OR REPLACE 
			INTO phones VALUES ('.time().', \''.$registration_id.'\');');
		echo "Sucess";
	} else 
		echo "Unable to open DB";
} else
	echo "No ID";
echo "\n";