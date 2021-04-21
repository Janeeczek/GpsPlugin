# StartFromFCM_plugin
Ściągnij cały projekt pluginu z githuba https://github.com/Janeeczek/StartFromFCM_plugin/archive/refs/heads/master.zip 
Rozpakuj go


0. instalacja plugman z npm: npm install -g plugman

1. instalacja pluginu komendą : plugman install --platform android --project <scieżka do folderu \platoforms\android w projekcie do którego ma być wgrany plugin> --plugin <ścieżka do folderu z pluginem>
2. Przykład : plugman install --platform android --project C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android --plugin C:\Users\Jan.Mazurek\plugins\autostart

3. Należy wgrać plik google-services.json do folderu \app projektu aplikacji np. C:\Users\Jan.Mazurek\CordovaProjects\test\platforms\android\app
4. Należy edytować plik MyFirebaseMessagingService.java (\platforms\android\app\src\main\java\com\example\cordova\plugin\FCM) importując w nim klasę MainActivity zależnie od pakietu aplikacji. (Wiecej info po otworzeniu tego pliku)
5. Należy edytować plik index.js (\platforms\android\app\src\main\assets\www\js) dodając na początku funkcji onDeviceReady() : 
```javascript
      window.plugins.autostart.show('test', 'long', function() {
          console.log('test!');
        }, function(err) {
          console.log(' ' + err);
        });
```

6. Należy uruchomić aplikacje i sprawdzić logi podczas *run*, aby wyłapać token dla firebase. przykładowy token: f0eJSQcKTsytOOxY2o7egN:APA91bGdAqw1jEsfda8NAWBHFuA00iJ1DEyPS5lRzzf1vECZV1CqOisS-Ri_m_ikS52kpMwiKKmG1KQVMX-pp40eUK-501_Bqe3YaLvtrRM3AYdAQ71iSJVsWVB0qVNbO9v0o8NXVeaW
7. Token generuje się tylko podczas pierwszego uruchomienia po ponowym zainstalowaniu aplikcji. Tak więc podczas testów wystarczy go sprawdzić tylko raz
8. W apce zaakceptować pozwolenia i uruchomić ją ponownie
9. Należy uruchomić https://www.postman.com/ wersję przeglądarkową
10. Stworzyć nowy request
11. W Headers dodać parametr key = Authorization | value = key={Server key ze strony projektu firebase}
12. przykładowy value = key=AAAASCEBOlU:APA91bEiuZNuU4pW1Tqa04RWs5vsv7dbgBoojpjRLjZwuymOOGjy8cWNNMzjl8k20Cr296PNsdEtobY-w0VQxko2qfymdluNvz4RDekLL4nSf7HD-IhRGEwLNAJfKVH-A16OO7kh59OB
13.  nie zapomnij przed wartościa server key dodać *key=*!!!!!
14. W Body , podzakładka *raw* wkleić :
```javascript
      {
      "to" : "token",
      "collapse_key" : "type_a",
      "data" : {
            "START" : "sta"
      }
      }
```
15. przykładowy raw:
```javascript
      {
      "to" : "f0eJSQcKTsytOOxY2o7egN:APA91bGdAqw1jEsfda8NAWBHFuA00iJ1DEyPS5lRzzf1vECZV1CqOisS-Ri_m_ikS52kpMwiKKmG1KQVMX-pp40eUK-                501_Bqe3YaLvtrRM3AYdAQ71iSJVsWVB0qVNbO9v0o8NXVeaW",
      "collapse_key" : "type_a",
      "data" : {
            "START" : "sta"
      }
      }
```




16. teraz można wyłączyć apke i w PostManie wcisnąc SEND
17. Aplikacja powinna się sama uruchomić
