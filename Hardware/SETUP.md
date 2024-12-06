
# Hardware Setup for AlertGuard

This guide will walk you through the process of setting up the hardware for the AlertGuard IoT-based door alarm system.

---

## **Components Needed**
1. **NodeMCU ESP8266** (Version 1.0, ESP-12E)
2. **N.C. Reed Switch Door Sensor**
3. **Breadboard**
4. **Power Bank**
5. **USB Cable** (to connect the NodeMCU to the power bank)

---

## **Circuit Overview**
The system uses a reed switch sensor to detect door status (open/closed). The NodeMCU communicates this status over WiFi to the Firebase database. 

- The reed switch has two wires:
  - **Wire 1** aligns with the **D2 pin** on the NodeMCU.
  - **Wire 2** aligns with the **GND (Ground)** pin on the NodeMCU.

---

## **Step-by-Step Setup**

### **Step 1: Place the NodeMCU on the Breadboard**
1. Place the NodeMCU securely on the breadboard. Align its pins with the breadboard rows to stabilize it and allow for easy connections.
2. Ensure that the micro-USB port on the NodeMCU is easily accessible for power.

### **Step 2: Connect the Reed Switch Wires**
1. Align the reed switch wires directly to the corresponding pins on the NodeMCU:
   - **Wire 1** of the reed switch should align with the **D2 pin** on the NodeMCU.
   - **Wire 2** of the reed switch should align with the **GND pin** on the NodeMCU.
2. Ensure the wires make firm contact with the pins. The breadboard will help stabilize these connections.

### **Step 3: Position the Reed Switch on the Door**
1. Mount the reed switch securely on the door frame.
2. Attach the accompanying magnet to the door itself, ensuring the two are aligned.
3. The switch should close when the door is closed and open when the door is opened.

### **Step 4: Power the NodeMCU**
1. Connect the NodeMCU to the power bank using the USB cable.
2. Place the power bank near the NodeMCU for stable power delivery during operation.

---

## **Final Checks**
1. Verify that the connections are secure:
   - **Wire 1 of Reed Switch → D2 on NodeMCU**
   - **Wire 2 of Reed Switch → GND on NodeMCU**
2. Ensure the reed switch and magnet are correctly aligned to detect door status changes.
3. Power on the NodeMCU using the USB cable and test the setup.

---

## **Circuit Diagram**
Refer to the `connection_image.jpg` file in this folder for a visual representation of the connections.

---

## **Tips for a Successful Setup**
- Ensure the reed switch wires are properly aligned and making good contact with the NodeMCU pins.
- Position the reed switch and magnet carefully to ensure accurate door status detection.
- Keep the power bank charged to maintain continuous operation.

---

Now, you're ready to upload the code to the NodeMCU and test the system.
