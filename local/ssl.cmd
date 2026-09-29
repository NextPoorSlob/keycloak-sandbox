openssl req -x509 -newkey rsa:2048 -nodes -days 1024 ^
  -keyout nginx/certs/local-auth.key ^
  -out nginx/certs/local-auth.crt ^
  -subj "/CN=local-auth/C=XX/ST=XX/L=XX/O=local-auth/OU=local-auth/emailAddress=XX" ^
  -addext "subjectAltName=DNS:local-auth,DNS:admin.local-auth"

openssl pkcs12 -export -in nginx/certs/local-auth.crt -inkey nginx/certs/local-auth.key ^
  -out local-auth.p12 ^
  -passout pass:password
