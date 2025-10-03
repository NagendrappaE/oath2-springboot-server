Spring autherization server runs on 8080 port and issues the token by registering client.

curl -X POST http://localhost:8080/oauth2/token   -u emandate-switch:secret   -H "Content-Type: application/x-www-form-urlencoded"   -d "grant_type=client_credentials&scope=account-enquiry:read"
{"access_token":"eyJraWQiOiJiOThiOTI4Yi02ZWRmLTQ0MDYtYjczZi05NjgwOGE5NWEyOGQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlbWFuZGF0ZS1zd2l0Y2giLCJhdWQiOiJlbWFuZGF0ZS1zd2l0Y2giLCJuYmYiOjE3NTk0NTk2NjYsInNjb3BlIjpbImFjY291bnQtZW5xdWlyeTpyZWFkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImV4cCI6MTc1OTQ1OTk2NiwiaWF0IjoxNzU5NDU5NjY2LCJqdGkiOiI5ODI5ZTQ5Mi1hYzFkLTRmM2UtYWE5My02ZTAxYzNiMWVkMTUifQ.myxk-N5M9fCTSJ3HMK-doGV5nQUyfFnywVbW0FrxFHMJAab2hVKrbmnsmEYPS2gHiGiB5yrH3r__dkgSYXt9guoz04IPLvdFXMWxkupBtNP4mB1sWKPlyFPZUaMLpHwCovzD02RGjEjinkaOz2Z-RLt54x5BQs5EsOhESE0bmUSptPu29m2sCChmSWuqGWj_lq24BM8j-QscnWKaauTu_Kr-oEhONLZySDfA3eJ_nnc6SPbd4YCEqQs0H8Xc_Ztyv82Zx1dAMHq9g2YWKcRyUNAEmeZEoRO7nJHl3KfCV_mB9cJTK1UHxVtv06BiHeJKIE6xv2ltONNLD3TrceKEOw","scope":"account-enquiry:read","token_type":"Bearer","expires_in":299}



Spring resource server authenticate the token and allows to access protected end points like account enquiry.

curl -X GET http://localhost:8081/account-enquiry/secure -H "Authorization: Bearer eyJraWQiOiJiOThiOTI4Yi02ZWRmLTQ0MDYtYjczZi05NjgwOGE5NWEyOGQiLCJhbGciOiJSUzI1NiJ9.eyJzdWIiOiJlbWFuZGF0ZS1zd2l0Y2giLCJhdWQiOiJlbWFuZGF0ZS1zd2l0Y2giLCJuYmYiOjE3NTk0NTk4NTgsInNjb3BlIjpbImFjY291bnQtZW5xdWlyeTpyZWFkIl0sImlzcyI6Imh0dHA6Ly9sb2NhbGhvc3Q6ODA4MCIsImV4cCI6MTc1OTQ2MDE1OCwiaWF0IjoxNzU5NDU5ODU4LCJqdGkiOiIwYzRjNzE2OS1mM2U1LTQxOTYtODg4OS1jOGYxOGQ2MmRjZGMifQ.uuet8txKGO5WD0jBisqeYPMPWnU2CAzs5rFFcECqwFhkQbOYhq5ewbrbItUTv6Ie2jEplL-OWQVjdD71dJMFJk2gOvspbbHtmVHOIxRtWw2tMEaWIymCYuHRSNVx0fVBL3dg1OwN07zlwQbCWC24SqGEy4kPuuMMFKK8n7eL66ORnPmB5EuKQwcBelo_cvdZDt6tP9aZr1xCOf6in-q3shf5qetZvBP14kiRCU4agFyUFLaznz7qoM9fqmy_rt6QNFF_WgBwbU8-wI1LnC_PlCnQdSe7c-SW_0__-_z8roDUlgcYOEFBa5NCZPm6T8lm0mJFhsFdAKMgGBlsukq_tA"
This is a secure endpoint, accessible only with 'account-enquiry:read'


Spring client-server-OpenFinance API consume.

 The below comand to run the jar  ensure the mtls(2 way ssl handshake)
 javax.net.ssl.keyStore -->Contains the PrivateKey and Public Key of the client used for the MTLS.
 javax.net.ssl.trustStore--> constains server's CA/Public certificate (Altareq's public cert and Amazon CA certificate(jkws))
 
-Djavax.net.debug=all -Djavax.net.ssl.keyStore=/home/nagendrappae/Documents/openFinance/finAxisPKCS12.pfx -Djavax.net.ssl.keyStorePassword=Flux@123 -Djavax.net.ssl.trustStore=/home/nagendrappae/Documents/openFinance/altareqTruststore.jks -Djavax.net.ssl.trustStorePassword=Flux@123


Consent autherization:
http://localhost:8080/api/login/caap


Accesssing payment Url:
http://localhost:8080/api/login/payment/<GrantTokenId>/<ConsentId>
http://localhost:8080/api/login/payment/1a7a623d-b2fa-4e45-befe-a2b63cabca63/463110f1-dfb4-4a5e-9c39-ca484224fa29


