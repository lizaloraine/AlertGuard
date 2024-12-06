# AlertGuard - Firebase Setup

This guide will walk you through setting up Firebase for the **AlertGuard** project. This project uses **Firebase Realtime Database** to manage the current door status and **Firebase Firestore** to store the history of each user's door status and notification.

---

## **1. Setting Up Firebase in Your Android Project**

### **Create a Firebase Project:**
- Go to the [Firebase Console](https://console.firebase.google.com/).
- Click on "Add Project" and follow the steps to create a new project.
- Once your project is created, click on "Add App" and select Android.
- Follow the instructions to register your app with Firebase. This will involve adding the `google-services.json` file to your project. Make sure the file is placed in the `app/` directory.

### **Add Firebase SDK to Your Android App:**
- Open your project’s `build.gradle` files (both project-level and app-level).
- In the **project-level `build.gradle`** file, ensure you have the following in the `dependencies` section:
  ```gradle
  classpath 'com.google.gms:google-services:4.3.15'  // Check for the latest version

- In the **app-level `build.gradle`** file, add the following dependencies:

  ```gradle
  implementation 'com.google.firebase:firebase-database:20.0.5'  // Firebase Realtime Database
  implementation 'com.google.firebase:firebase-firestore:24.4.0'  // Firebase Firestore
  implementation 'com.google.firebase:firebase-auth:21.0.5'  // Firebase Authentication (if using)

- At the bottom of the **app-level `build.gradle`** file, add:

  ```gradle
  apply plugin: 'com.google.gms.google-services'

- Sync your project to ensure all dependencies are downloaded.

---

## **2. Setting Up Firebase Realtime Database**

### **Enable Realtime Database:**
- In the Firebase Console, go to **Realtime Database** under the **Build** section.
- Click **Create Database** and select **Start in test mode** (you can set more specific rules later).

### **Database Structure for Realtime Database:**
The Realtime Database will store the following structure:

  ```plaintext
  {
    "doorStatus": {
      "status": "closed"  // Possible values: "closed" or "open"
    },
    "notification": "The door is closed"  // Message reflecting the door status
  }

- This is the database structure, and this will be the one who will detect changes or events from the Node MCU.

---

## **3. Setting Up Firestore Database**

### **Enable Firestore:**
- In the Firebase Console, go to **Firestore Database** under the **Build** section.
- Click **Create Database** and select **Start in test mode**.

### **Database Structure for Firestore:**
Firestore will store the user's door status and notification history. For each user, the structure will look as follows:

```plaintext
/notifications
  /{userId}
    /history
      /{documentId}
        - event: "The door is closed"
        - date: "2024-12-06"
        - time: "12:34:56"
---

## **4. Integrating Firebase in Your Android Project**

Once Firebase is set up, the project can store and retrieve data from both the Firebase Realtime Database and Firestore:

- **Realtime Database** will be used to store the current door status and notification message.
- **Firestore** will store the history of door status changes for each user, along with the event's timestamp.

---

## **5. Firebase Documentation**

For more details on Firebase Realtime Database and Firestore setup, refer to the official documentation:

- [Realtime Database Documentation](https://firebase.google.com/docs/database)
- [Firestore Documentation](https://firebase.google.com/docs/firestore)

---

This `README.md` file provides the setup instructions for Firebase in your project, focusing on the Realtime Database and Firestore aspects.
