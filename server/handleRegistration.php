<?php
if($_GET['registration_id'] != '') {
	if($db = new SQLite3("db.sqlite")) {
		$registration_id = $db->escapeString($_GET['registration_id']);
		$result = $db->exec(
			'INSERT OR REPLACE 
			INTO phones VALUES ('.time().', \''.$registration_id.'\');');
		echo "Sucess";
	} else 
		echo "Unable to open DB";
} else
	echo "No ID";
echo "\n";