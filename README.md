# AutoStartGPS_plugin
Ściągnij cały projekt pluginu z githuba https://github.com/Janeeczek/GpsPlugin/archive/refs/heads/master.zip 
Rozpakuj go


0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
2. Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\GpsPlugin

3. Należy edytować plik LocationService.java (\platforms\android\app\src\main\java\com\example\cordova\plugin\service) importując w nim klasę MainActivity zależnie od pakietu aplikacji. (Wiecej info po otworzeniu tego pliku)

4. Zbuildować i uruchomić apkę. 
5. Zaakceptować pozwolenia
6. Od teraz gdy telefon zostanie uruchomiony to usługa pobierania lokalizacji będzie odczytywać położenie telefonu do momentu aż nie zostanie uruchomiona aplikacja główna przez użytkownika

