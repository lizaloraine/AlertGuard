# AlertGuard: IoT-Based Door Alarm System

**AlertGuard** is an IoT-based door alarm system that integrates hardware, an Android app, and Firebase to provide a smart home security solution. This project leverages the NodeMCU ESP8266, a reed switch, and Firebase for real-time status monitoring. The alarm can be toggled between "Enabled" and "Disabled" modes via the Android app, providing real-time updates for enhanced security management.

---

## **Features**
- **Real-Time Updates**: Get real-time updates (via output in the Android app) when your door is opened or closed.
- **Firebase Integration**: Seamless storage and retrieval of door state data.
- **WiFi Connectivity**: Utilizes NodeMCU ESP8266 for communication.
- **Mobile App**: Android app (built using Android Studio) for live monitoring and management.

---

## **Project Structure**
### 1. **Arduino Code**
Located in the `ArduinoCode/` directory. Includes:
- `alertguard.ino`: The main sketch for the NodeMCU.
- `libraries/`: Required libraries

### 2. **Android App**
Located in the `Android App/` directory. Includes:
- Android Studio project for the mobile app.
- Code for fetching data from Firebase and sending notifications.

### 3. **Hardware**
Located in the `Hardware/` directory. Includes:
- `SETUP.md` (Instruction for setting up the system)
- `hardware_setup.jpg`
- `connection_image.jpg`

### 4. **Firebase**
Located in the `Firebase/` directory. Includes:
- `firebase_config.json`: Configuration file for Firebase setup.
- `DatabaseStructure.txt`: Details on the Firebase Realtime Database structure.
- `README.md`: Instructions for setting up Firebase.

---

## **How It Works**
1. The N.C. reed switch detects changes in the door state (open/closed).
2. The NodeMCU ESP8266 updates the door state in Firebase Realtime Database.
3. The Android app retrieves the door state from Firebase and sends an update to the user.

---

## **Setup Instructions**

### **Hardware**
1. Connect the NodeMCU ESP8266 to the N.C. reed switch as shown in `CircuitDiagram.png`.
2. Power the NodeMCU using a USB cable or external power source.

### **Firebase**
1. Create a Firebase project in the [Firebase Console](https://console.firebase.google.com/).
2. Enable Firebase Realtime Database and add your project's configuration file (`firebase_config.json`).
3. Set up the database structure as described in `DatabaseStructure.txt`.

### **Arduino Code**
1. Install the [Arduino IDE](https://www.arduino.cc/en/software).
2. Download the required libraries (`ESP8266WiFi`, `FirebaseESP8266`, `NTPClient`).
3. Replace placeholder Firebase credentials in the code with your project's details.
4. Upload `alertguard.ino` to the NodeMCU ESP8266.

### **Android App**
1. Open the `Android App/` directory in Android Studio.
2. Add your Firebase project configuration to the app (`google-services.json`).
3. Build and install the APK on your Android device.

---

## **Prerequisites**
- Arduino IDE installed on your computer.
- Android Studio for modifying or building the app.
- Firebase Realtime Database setup.
- Basic knowledge of electronics, Firebase, and Android development.

---

## **Acknowledgments**
- Firebase for Realtime Database and Firestore Database support.
- Arduino for the open-source IDE and community.
- NodeMCU ESP8266 community for libraries and documentation.
- Inspiration from the need for smarter home security systems.

This project, *AlertGuard*, is our final project in fulfillment of the course **CS 312 - Mobile Computing**.
Thank you ! ❤️

---

## **Contact**
For questions or feedback, reach out to **Liza Loraine** at [lizaloraine124@gmail.com].
