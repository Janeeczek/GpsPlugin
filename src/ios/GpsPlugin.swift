
import CoreLocation
import UserNotifications

@objc(GpsPlugin)
class GpsPlugin : CDVPlugin,CLLocationManagerDelegate {
    var locationManager: CLLocationManager?
    let notificationCenter = UNUserNotificationCenter.current()
    let options: UNAuthorizationOptions = [.alert, .sound, .badge]
    var latitude: Double = 0.0
    var longitude: Double = 0.0
    var lastAccouracies = [Double]()
    var desired = 0;
    var now = 0;
    var counter = 0;
    var timer1: Timer?
    var timer2: Timer?
    var runCount = 0
    
    
    override func pluginInitialize() {
        super.pluginInitialize()
        print("GpsPlugin :: Inizjalizacja")
        lastAccouracies = [Double]()
        //Dodanie listenera na event zakończenia działania aplikacji
        //W momencie kiedy aplikacja zostanie wyłączona to zaczyna się pobieranie lokalizacji
        NotificationCenter.default.addObserver(self, selector: #selector(terminateCall), name: UIApplication.willTerminateNotification, object: nil)
        NotificationCenter.default.addObserver(self, selector: #selector(foregroundCall), name: UIApplication.willEnterForegroundNotification, object: nil)
        //initializeLocationBest()
        //initializeLocationWorse()
        inistializeNotification()
        self.timer1 = Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(timerRequestLocation), userInfo: nil, repeats: true)
        timer1?.tolerance = 0.2
        self.lastAccouracies.append(0.00)
        sendNotification(title: "GpsPlugin", body: "Inicjalizacja poprawna")
    }
    //receiver który odbiera event zakończenia działania aplikacji
    @objc func terminateCall() {
        //sendNotification(title: "App", body: "Uruchamiam lokalizacje w tle")
        print("GpsPlugin :: Aplikacja zakończona.")
        //stopUpdateLocation()
        self.timer1 = Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(timerRequestLocation), userInfo: nil, repeats: true)
        timer1?.tolerance = 0.2
        sendNotification(title: "GpsPlugin", body: "Awaryjne pobieranie lokalizacji włączone")
    }
    @objc func foregroundCall() {
        //sendNotification(title: "App", body: "Uruchamiam lokalizacje w tle")
        print("GpsPlugin :: Aplikacja przywrocona.")
        //stopUpdateLocation()
        timer1?.invalidate()
        timer2?.invalidate()
    }
    
        //nie dziala
    func locationManager(_ manager: CLLocationManager, didChangeAuthorization status: CLAuthorizationStatus) {
        switch status {
        case .denied: // Setting option: Never
            print("GpsPlugin :: Ma zakaz na odczyt lokalizacji. Wysyłam prośbę użytkownikowi o dostęp ciągły.")
            locationManager?.requestAlwaysAuthorization()
        case .notDetermined: // Setting option: Ask Next Time
            print("GpsPlugin :: Nie ma określonego pozwolenia na odczyt lokalizacji. Wysyłam prośbę użytkownikowi o dostęp ciągły.")
            locationManager?.requestAlwaysAuthorization()
        case .authorizedWhenInUse: // Setting option: While Using the App
            print("GpsPlugin :: Ma zezwolenie na używanie lokalizacji podczas działania aplikacji. Wysyłam prośbę użytkownikowi o dostęp ciągły.")
            locationManager?.requestAlwaysAuthorization()
        case .authorizedAlways: // Setting option: Always
            print("GpsPlugin :: Ma zezwolenie na odczyt ciągły lokalizacji. Sprawdzam czy odbiór lokalizacji działa.")
            locationManager?.requestLocation()
        case .restricted: // Restricted by parental control
            print("GpsPlugin :: Lokalizacja zablokowania kontrolą rodzicelską. Nie robię nic.")
        default:
            print("GpsPlugin :: Nie udało mi się odczytać statusu pozwolenia lokalizacji.")
      }
    }
    @objc func timerRequestLocation() {
        print("timerRequestLocation fired! rc:\(runCount)")
        initializeLocationBest()
        runCount+=1
        if(runCount > 3) {
            timer1?.invalidate()
            runCount = 0
            self.timer2 = Timer.scheduledTimer(timeInterval: 50.0, target: self, selector: #selector(timerStartLocation), userInfo: nil, repeats: false)
            self.timer2?.tolerance = 0.2
        }
    }
    @objc func timerStartLocation() {
        print("timerStartLocation fired!")
        self.timer1 = Timer.scheduledTimer(timeInterval: 5.0, target: self, selector: #selector(timerRequestLocation), userInfo: nil, repeats: true)
        
        
        
    }
    func locationManager(_ manager: CLLocationManager, didUpdateLocations locations: [CLLocation]) {
        
        sendNotification(title: "Dokladnosc", body: "\(locations.last?.horizontalAccuracy)")
        
        
        
        print("GpsPlugin :: Aktualizacja lokalizacji: liczba lokalizacji: \(locations.count)")
        let dateFormatter = DateFormatter()
        dateFormatter.dateFormat = "yyyy-MM-dd HH:mm:ss"
    
        locations.forEach { (location) in
          print("GpsPlugin :: Aktualizacja lokalizacji: \(dateFormatter.string(from: location.timestamp)); \(location.coordinate.latitude), \(location.coordinate.longitude)")
        }
        
      }
      
    func locationManager(_ manager: CLLocationManager, didFailWithError error: Error) {
        print("GpsPlugin :: Wykryto błąd lokalizacji \(error.localizedDescription)")
        if let error = error as? CLError, error.code == .denied {
            // Location updates are not authorized.
            // To prevent forever looping of `didFailWithError` callback
            locationManager?.stopMonitoringSignificantLocationChanges()
            return
        }
    }
    
    
    
    func updateLocation() {
        self.locationManager?.stopUpdatingLocation()
        self.locationManager?.startUpdatingLocation()
        
        
    }
    func stopUpdateLocation() {
        self.locationManager?.stopUpdatingLocation()
    }
    func startUpdateLocation() {
        locationManager?.startUpdatingLocation()
    }
    func initializeLocationBest() {
        self.locationManager = CLLocationManager()
        self.locationManager?.delegate = self
        self.locationManager?.desiredAccuracy = kCLLocationAccuracyNearestTenMeters
        self.locationManager?.activityType = CLActivityType.fitness
        self.locationManager?.distanceFilter = 10
        self.locationManager?.requestAlwaysAuthorization()
        self.locationManager?.allowsBackgroundLocationUpdates = true
        self.locationManager?.pausesLocationUpdatesAutomatically = false
        locationManager?.startMonitoringSignificantLocationChanges()
        self.locationManager?.startUpdatingLocation()
        
    }
    func inistializeNotification() {
        UNUserNotificationCenter.current().requestAuthorization(options: [.alert, .badge, .sound]) { success, error in
            if success {
                print("GpsPlugin :: Powiadomienia ustawione poprawnie!!")
            } else if let error = error {
                print(error.localizedDescription)
            }
        }
        notificationCenter.getNotificationSettings { (settings) in
          if settings.authorizationStatus != .authorized {
            print("GpsPlugin :: Powiadomienia zakazane przez użytkownika")
          }
        }
        
    }
    func sendNotification(title: String,body:String) {
        let content = UNMutableNotificationContent()
        content.title = title
        content.subtitle = body
        content.sound = UNNotificationSound.default

        // show this notification five seconds from now
        let trigger = UNTimeIntervalNotificationTrigger(timeInterval: 5, repeats: false)

        // choose a random identifier
        let request = UNNotificationRequest(identifier: UUID().uuidString, content: content, trigger: trigger)

        // add our notification request
        UNUserNotificationCenter.current().add(request)
        
    }
    
    @objc(test:)
    func test( command: CDVInvokedUrlCommand) {

        print("GpsPlugin :: test is called")
        
        
            
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
    @objc(start:)
    func start( command: CDVInvokedUrlCommand) {

        print("GpsPlugin :: start is called")
        
        
        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
    @objc(stop:)
    func stop( command: CDVInvokedUrlCommand) {

        print("GpsPlugin :: stop is called")

        let pluginResult = CDVPluginResult(status: CDVCommandStatus_OK)
        commandDelegate.send(pluginResult, callbackId:command.callbackId)
    }
    
}

