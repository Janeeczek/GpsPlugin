# AutoStartGPS_plugin
Ściągnij cały projekt pluginu z githuba https://github.com/Janeeczek/GpsPlugin/archive/refs/heads/master.zip 
Rozpakuj go

ANDROID:
0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
2. Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\GpsPlugin

3. Należy edytować plik LocationService.java (\platforms\android\app\src\main\java\com\example\cordova\plugin\service) importując w nim klasę MainActivity zależnie od pakietu aplikacji. (Wiecej info po otworzeniu tego pliku)

4. Zbuildować i uruchomić apkę. 
5. Zaakceptować pozwolenia
6. Od teraz gdy telefon zostanie uruchomiony to usługa pobierania lokalizacji będzie odczytywać położenie telefonu do momentu aż nie zostanie uruchomiona aplikacja główna przez użytkownika

IOS:
1. Aby plugin działał konieczne jest dodanie pluginu wspierającego swift PRZED dodaniem pluginu z gps oraz przed wpisaniem komendy cordova platform add ios.
1.2. Dodawanie pluginu(wpisać będąc w katalogu projektu cordova) cordova plugin add cordova-plugin-add-swift-support --save
2. Następnie wpisać:  cordova platform add ios
3. Następnie dodanie pluginu głównego:  plugman install --platform ios --project {ścieżka do /platforms/ios/} --plugin {ścieżka do pluginu}
3.1 Przykład : plugman install --platform ios --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\ios --plugin C:\Users\Jan.Mazurek\plugins\GpsPlugin
4. Należy teraz uruchomić Xcode będąc wewnatrz projektu open ./platforms/ios/NAZWAPROJEKTU.xcworkspace/
5. w Xcode przejść do nazwaProjectu.xcodeproj. Wybrać okno targets, następnie Build Settings i w zakładce Swift Compiler - Language  zmienić Swift Language Version  na Swift 5
