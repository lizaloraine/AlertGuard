package com.example.iotbaseddooralarmsystem;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AlertGuard extends AppCompatActivity {

    private Button btnToggleAlarm;
    private TextView tvDoorStatus, tvCurrentAlarmStatus;
    private LinearLayout notificationsContainer;
    private View circle1, circle2, circle3;
    private ImageView btnLogout;


    private FirebaseFirestore firestore;
    private DatabaseReference mDatabase;
    private boolean isAlarmEnabled = false;
    private String lastAddedDate = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alertguard);

        btnToggleAlarm = findViewById(R.id.btnToggleAlarm);
        tvDoorStatus = findViewById(R.id.tvDoorStatus);
        tvCurrentAlarmStatus = findViewById(R.id.tvCurrentAlarmStatus);
        notificationsContainer = findViewById(R.id.notificationsContainer);
        circle1 = findViewById(R.id.circle1);
        circle2 = findViewById(R.id.circle2);
        circle3 = findViewById(R.id.circle3);
        btnLogout = findViewById(R.id.btnLogout);

        mDatabase = FirebaseDatabase.getInstance().getReference("doorStatus");
        firestore = FirebaseFirestore.getInstance();

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = currentUser.getEmail();
            Log.d("FirebaseUID", "User UID: " + uid + " | Email: " + email); // Debugging
            loadUserNotificationHistory(uid);

        } else {
            Log.d("FirebaseAuth", "No user is currently logged in");
            Toast.makeText(AlertGuard.this, "No user logged in", Toast.LENGTH_SHORT).show();
        }

        updateAlarmStatusUI();

        btnLogout.setOnClickListener(v -> {
            if (currentUser != null) {
                String currentUid = currentUser.getUid();
            }

            FirebaseAuth.getInstance().signOut();

            Toast.makeText(AlertGuard.this, "User logged out", Toast.LENGTH_SHORT).show();

            finish();
        });

        btnToggleAlarm.setOnClickListener(v -> {
            isAlarmEnabled = !isAlarmEnabled;
            updateAlarmStatusUI();

            if (isAlarmEnabled) {
                startAlarmAnimation();
            } else {
                disableAlarmAnimation();
            }

            addNotification(isAlarmEnabled ? "Alarm Enabled" : "Alarm Disabled");
            Toast.makeText(AlertGuard.this, isAlarmEnabled ? "Alarm Enabled" : "Alarm Disabled", Toast.LENGTH_SHORT).show();
        });



        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!isAlarmEnabled) {
                    tvDoorStatus.setText("Door Status: Alarm is Disabled");
                } else {
                    tvDoorStatus.setText("Door Status: Alarm Activated");

                    String doorStatus = dataSnapshot.child("status").getValue(String.class);
                    if (doorStatus != null) {
                        tvDoorStatus.setText("Door Status: " + doorStatus);
                    } else {
                        tvDoorStatus.setText("Door Status: Unknown");
                    }

                    String notification = dataSnapshot.child("notification").getValue(String.class);
                    if (notification != null) {
                        addNotification(notification);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError error) {
                Toast.makeText(AlertGuard.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void updateAlarmStatusUI() {
        tvCurrentAlarmStatus.setText("Current Alarm Status: " + (isAlarmEnabled ? "Enabled" : "Disabled"));
        tvDoorStatus.setText("Door Status: " + (isAlarmEnabled? "Alarm Activated" : "Alarm is Disabled"));
        btnToggleAlarm.setText(isAlarmEnabled ? "Disable Alarm" : "Enable Alarm");

    }

    private Typeface getPoppinsFont() {
        return ResourcesCompat.getFont(this, R.font.poppins_regular);
    }

    private void loadUserNotificationHistory(String uid) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            String userId = currentUser.getUid();
            String userEmail = currentUser.getEmail();

            firestore.collection("notifications")
                    .document(userId)
                    .collection("history")
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (var document : queryDocumentSnapshots) {
                                String event = document.getString("event");
                                String date = document.getString("date");
                                String time = document.getString("time");
                                addNotificationToUI(event, date, time);
                            }
                        } else {
                            Toast.makeText(this, "No previous notifications found.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Failed to load history: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "User not logged in. Please log in to view notifications.", Toast.LENGTH_SHORT).show();
        }
    }


    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("MMMM dd", Locale.getDefault());
        return sdf.format(new Date());
    }


    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a", Locale.getDefault());
        return sdf.format(new Date());
    }


    private void addNotification(String eventText) {
        String currentDate = getCurrentDate();
        String currentTime = getCurrentTime();

        if (!currentDate.equals(lastAddedDate)) {
            TextView dateHeader = new TextView(AlertGuard.this);
            dateHeader.setText(currentDate);
            dateHeader.setTextSize(22);
            dateHeader.setPadding(0, 10, 0, 10);
            dateHeader.setTextColor(getResources().getColor(android.R.color.black));
            dateHeader.setTypeface(getPoppinsFont(), Typeface.BOLD);
            notificationsContainer.addView(dateHeader);

            lastAddedDate = currentDate;
        }

        LinearLayout notificationLayout = new LinearLayout(AlertGuard.this);
        notificationLayout.setOrientation(LinearLayout.HORIZONTAL);
        notificationLayout.setPadding(0, 5, 0, 5);

        TextView icon = new TextView(AlertGuard.this);
        icon.setTextSize(16);
        icon.setPadding(0, 0, 10, 0);
        icon.setTypeface(getPoppinsFont());

        if (eventText.contains("Enabled")) {
            icon.setText("🔔");
        } else if (eventText.contains("Disabled")) {
            icon.setText("🔕");
        } else if (eventText.contains("Opened")) {
            icon.setText("🚪");
        } else if (eventText.contains("Closed")) {
            icon.setText("🚪");
        }

        TextView newEvent = new TextView(AlertGuard.this);
        newEvent.setText(String.format("%s at %s", eventText, currentTime));
        newEvent.setTextSize(16);
        newEvent.setTypeface(getPoppinsFont());

        notificationLayout.addView(icon);
        notificationLayout.addView(newEvent);

        notificationsContainer.addView(notificationLayout);

        if (isAlarmEnabled) {
            saveNotificationToFirestore(eventText, currentDate, currentTime);
        }
    }

    private void saveNotificationToFirestore(String eventText, String date, String time) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(this, "Error: User not logged in", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        String documentId = String.valueOf(System.currentTimeMillis());

        Map<String, Object> notificationData = new HashMap<>();
        notificationData.put("event", eventText);
        notificationData.put("date", date);
        notificationData.put("time", time);

        firestore.collection("notifications")
                .document(userId) // UID is the document
                .collection("history")
                .document(documentId) // Set a meaningful document ID
                .set(notificationData);
    }

    private void addNotificationToUI(String eventText, String date, String time) {
        if (!date.equals(lastAddedDate)) {
            TextView dateHeader = new TextView(this);
            dateHeader.setText(date);
            dateHeader.setTextSize(22);
            dateHeader.setPadding(0, 10, 0, 10);
            dateHeader.setTextColor(getResources().getColor(android.R.color.black));
            dateHeader.setTypeface(getPoppinsFont(), Typeface.BOLD);
            notificationsContainer.addView(dateHeader);

            lastAddedDate = date;
        }

        LinearLayout notificationLayout = new LinearLayout(this);
        notificationLayout.setOrientation(LinearLayout.HORIZONTAL);
        notificationLayout.setPadding(0, 5, 0, 5);

        TextView icon = new TextView(this);
        icon.setTextSize(16);
        icon.setPadding(0, 0, 10, 0);
        icon.setTypeface(getPoppinsFont());


        if (eventText.contains("Enabled")) {
            icon.setText("🔔");
        } else if (eventText.contains("Disabled")) {
            icon.setText("🔕");
        } else if (eventText.contains("Opened") || eventText.contains("Closed")) {
            icon.setText("🚪");
        }

        TextView newEvent = new TextView(this);
        newEvent.setText(String.format("%s at %s", eventText, time));
        newEvent.setTextSize(16);
        newEvent.setTypeface(getPoppinsFont());

        notificationLayout.addView(icon);
        notificationLayout.addView(newEvent);
        notificationsContainer.addView(notificationLayout);
    }

    private void startAlarmAnimation() {
        resetCircles();

        ObjectAnimator scaleX1 = ObjectAnimator.ofFloat(circle1, "scaleX", 1f, 3f);
        ObjectAnimator scaleY1 = ObjectAnimator.ofFloat(circle1, "scaleY", 1f, 3f);
        ObjectAnimator alpha1 = ObjectAnimator.ofFloat(circle1, "alpha", 1f, 0f);

        ObjectAnimator scaleX2 = ObjectAnimator.ofFloat(circle2, "scaleX", 1f, 3f);
        ObjectAnimator scaleY2 = ObjectAnimator.ofFloat(circle2, "scaleY", 1f, 3f);
        ObjectAnimator alpha2 = ObjectAnimator.ofFloat(circle2, "alpha", 1f, 0f);

        ObjectAnimator scaleX3 = ObjectAnimator.ofFloat(circle3, "scaleX", 1f, 3f);
        ObjectAnimator scaleY3 = ObjectAnimator.ofFloat(circle3, "scaleY", 1f, 3f);
        ObjectAnimator alpha3 = ObjectAnimator.ofFloat(circle3, "alpha", 1f, 0f);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX1, scaleY1, alpha1, scaleX2, scaleY2, alpha2, scaleX3, scaleY3, alpha3);
        animatorSet.setDuration(1500);
        animatorSet.setInterpolator(new AccelerateInterpolator());
        animatorSet.start();

        animatorSet.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (isAlarmEnabled) {
                    startAlarmAnimation();
                }
            }
        });
    }

    private void resetCircles() {
        circle1.setScaleX(1f);
        circle1.setScaleY(1f);
        circle1.setAlpha(1f);

        circle2.setScaleX(1f);
        circle2.setScaleY(1f);
        circle2.setAlpha(1f);

        circle3.setScaleX(1f);
        circle3.setScaleY(1f);
        circle3.setAlpha(1f);
    }

    private void disableAlarmAnimation() {
        resetCircles();
    }

}