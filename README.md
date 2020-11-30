# Backend
Część serwerowa aplikacji

# Konfiguracja SSL
## Wygenerowanie ceryfikatu
Do wygenerowania certyfikatu najlepiej użyć aplikacji keytool, która jest instalowana razem z
JDK i znajduje się w bin, należy tam wejść przez cmd jako administrator i użyć poniższego polecenia:
```
keytool -genkeypair -alias selfsigned_localhost_sslserver -keyalg RSA -keysize 2048 -storetype PKCS12 -cert.p12 -validity 3650
```
## Wgranie naszego certyfikatu do programu
Nasz certyfikat cert.p12 należy przenieść do
```src/main/resources/.cert```

Następnie w pliku ```src/main/resources/application.properties``` należy uzupełnić poniższe linie
```$xslt
server.ssl.key-store-password=
server.ssl.key-store=src/main/resources/.cert/cert.p12
```
Naszym hasłem jakie ustawiliśmy przy generowaniu certyfikatu, oraz ścieżkę jeśli mamy inną nazwę bądź lokalizację.

