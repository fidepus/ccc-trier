<?php
$authToken = '';
include("HTTP/Request.php");
function deleteRegistration($db, $id) {
	sqlite_query($db, 'DELETE from phones WHERE registration_id == \''.sqlite_escape_string($id).'\'');
}
if($db = sqlite_open('db.sqlite')) {
	$result = sqlite_query($db, 'SELECT * from phones');
	echo "Sending…\n";
	#$request = new HttpRequest('https://android.apis.google.com/c2dm/send', HttpRequest::METH_POST);
	$request = new Http_Request('https://android.apis.google.com/c2dm/send');
	//$request = new Http_Request('http://127.0.0.1:1337/c2dm/send');

	$request->setMethod(HTTP_REQUEST_METHOD_POST);

	#$request->addHeaders (array(
	#	'Authorization: GoogleLogin auth='.$authToken,
	#));
	$request->addHeader('Authorization', 'GoogleLogin auth='.$authToken);

	#$request->addPostFields (array(
	#	'collapse_key' => 'c3tOnlineStatusChanged'
	#));
	$request->addPostData('collapse_key', 'c3tOnlineStatusChanged');

	while ($row = sqlite_fetch_array($result)) {
		//print_r($row);
		#$request->addPostFields (array(
		#	'registration_id' => $row['registration_id'],
		#));
		$request->addPostData('registration_id', $row['registration_id']);
		
		#$request->send();
		$request->sendRequest();
		if($request->getResponseCode() == 200){
			$body = $request->getResponseBody();
			if(strpos($body,"id=")!==false) {
				echo "Sucess\n";
			} else if (strpos($body,"Error=QuotaExceeded")!==false) { //strpos because $body could contain Linefeeds
				echo "QuotaExceeded, Exiting\n";
				exit(2);
			} else if (strpos($body,"Error=DeviceQuotaExceeded")!==false) {
				echo "DeviceQuotaExceeded\n";
			} else if (strpos($body,"Error=InvalidRegistration")!==false) {
				echo "InvalidRegistration, deleting ID from DB... ";
				deleteRegistration($db, $row['registration_id']);
				echo "[OK]\n";
			} else if (strpos($body,"Error=NotRegistered")!==false) {
				echo "NotRegistered, deleting ID from DB... ";
				deleteRegistration($db, $row['registration_id']);
				echo "[OK]\n";
			} else {
				echo "Unexpeced error, Exiting\n";
				exit(255);
			}
		} else if ($request->getResponseCode() == 503) {
			echo 'Error 503\nIndicates that the server is temporarily unavailable (i.e., because of timeouts, etc ). Sender must retry later, honoring any Retry-After header included in the response. Application servers must implement exponential back off. Senders that create problems risk being blacklisted.';
			//Text "stolen" from http://code.google.com/android/c2dm/index.html#push
			exit(1);
		} else if ($request->getResponseCode() == 401) {
			echo 'Error 401\nPlease add a new authToken to the php';
		} else {
			echo 'Unbekannter ResponseCode: '.$request->getResponseCode();
		}
	}
} else
	echo 'unable to open DB';
