Before deploy with webstart

Create a keystore:

$ keytool -genkey -alias YOUR_ALIAS -keystore YOUR_KEYSTORE_FILE -keyalg RSA

Sign the jar:

$ jarsigner -keystore rsa-keystore -signedjar YOUR_SIGNED.jar YOUR_ORIGINAL.jar YOUR_ALIAS
