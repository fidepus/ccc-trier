#!/bin/sh
sharedSecret=''

nonce=`date +"%s"`
token=`echo -n $sharedSecret$nonce | sha1sum | cut -d " " -f 1`

wget -O - --no-verbose "http://84.200.208.230/android-clubstatus/sendStatusChangedMessage.php?nonce=$nonce&token=$token"