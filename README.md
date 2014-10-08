# Mole App

Android application for reporting radio network information.
It will be able to save it to a file or to do a push request into a server.

## Permissions

* READ_PHONE_STATE
* ACCESS_COARSE_LOCATION
* ACCESS_FINE_LOCATION
* ACCESS_NETWORK_STATE
* WRITE_EXTERNAL_STORAGE

## Notes

Currently it has minSdkVersion 19, I think its possible to run it on SDK version 16 but I would love to make it available to versions as far as 8.

## To-do list

* Save it to a file in external devices (Problem with devices as Moto G that have emulated external storage.
* Do the push to a server. It's not a big deal but it's based on the saved file.
