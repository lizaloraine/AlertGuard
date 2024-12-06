#include <ESP8266WiFi.h>
#include <ESP8266HTTPClient.h>
#include <WiFiClientSecure.h> 

#define WIFI_SSID "WiFi_Name_Here_2.4Ghz"
#define WIFI_PASSWORD "WiFi_Password_Here"

#define FIREBASE_HOST "iotbaseddooralarmapp-default-rtdb.asia-southeast1.firebasedatabase.app"
#define FIREBASE_SECRET "apiKey_Here" 

const int reedSwitchPin = D2;   
const int ledPin = LED_BUILTIN; 

WiFiClientSecure wifiClient;

int lastDoorState = HIGH; 
int currentDoorState = HIGH;

void setup() {
  pinMode(reedSwitchPin, INPUT_PULLUP); 
  pinMode(ledPin, OUTPUT);
  Serial.begin(115200); 
  digitalWrite(ledPin, LOW); 

 
  Serial.println("Connecting to Wi-Fi...");
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  while (WiFi.status() != WL_CONNECTED) {
    delay(1000);
    Serial.println("Connecting...");
  }
  Serial.println("Connected to Wi-Fi");

  wifiClient.setInsecure(); 

  sendDoorStatusToFirebase("closed", "The door is closed");
}

void loop() {
  currentDoorState = digitalRead(reedSwitchPin);

  if (currentDoorState != lastDoorState) {
    if (currentDoorState == LOW) { 
      Serial.println("Door is closed");
      sendDoorStatusToFirebase("closed", "The door is closed");
      digitalWrite(ledPin, HIGH);
    } else { 
      Serial.println("Door is open");
      sendDoorStatusToFirebase("open", "The door is open");
      digitalWrite(ledPin, LOW);  
    }
    lastDoorState = currentDoorState;
  }
}

void sendDoorStatusToFirebase(String status, String notification) {
  if (WiFi.status() == WL_CONNECTED) { 
    HTTPClient http;
    String firebaseURL = String("https://") + FIREBASE_HOST + "/doorStatus.json?auth=" + FIREBASE_SECRET;

    String jsonPayload = "{\"status\":\"" + status + "\",\"notification\":\"" + notification + "\"}";

    http.begin(wifiClient, firebaseURL); 
    http.addHeader("Content-Type", "application/json"); 
    int httpResponseCode = http.PUT(jsonPayload); 

    if (httpResponseCode > 0) {
      Serial.print("Firebase response: ");
      Serial.println(httpResponseCode);
      Serial.println(http.getString());
    } else {
      Serial.print("Error sending data: ");
      Serial.println(httpResponseCode);
    }

    http.end(); 
  } else {
    Serial.println("Wi-Fi disconnected, unable to send data.");
  }
}
