#!/bin/sh
sharedSecret=''

nonce=`date +"%s"`
token=`echo -n $sharedSecret$nonce | sha1sum | cut -d " " -f 1`

wget -O - --no-verbose "http://clubstatus.fidepus.de/sendStatusChangedMessage.php?nonce=$nonce&token=$token"
