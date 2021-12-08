package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SettingsActivity extends AppCompatActivity {
    static final int PICK_IMAGE = 1;
    static final long MAX_DOWNLOAD_SIZE = 1024 * 1024; // 1 megabyte

    SharedPreferences sp;
    SwitchCompat swDarkMode;
    SwitchCompat swSounds;
    SwitchCompat swNotifications;
    SwitchCompat swLocationPublic;
    TextView userName;
    ImageView profileImage;

    public void onClick(View v) {
        if(v.getId() == R.id.backButton || v.getId() == R.id.backButtonDark) {
            goToLastActivity();
        }
    }

    public void onSwitch(View v) {
        if(v.getId() == R.id.DarkMode || v.getId() == R.id.DarkModeDark) {
            toggleDarkMode();
            restartActivity();
        } else if (v.getId() == R.id.Sounds || v.getId() == R.id.SoundsDark) {
            toggleMute();
        } else if (v.getId() == R.id.Notifications || v.getId() == R.id.NotificationsDark) {
            toggleNotifications();
        } else if (v.getId() == R.id.LocationPublic || v.getId() == R.id.LocationPublicDark) {
            toggleLocationPublic();
        }
    }

    //TODO return to the previous default screen
    private void goToLastActivity() {
        super.onBackPressed();
    }

    private void restartActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    private void toggleDarkMode() {
        boolean darkMode = sp.getBoolean("darkMode", false);
        darkMode = !darkMode;
        sp.edit().putBoolean("darkMode", darkMode).apply();
    }

    private void toggleMute() {
        boolean mute = sp.getBoolean("sounds", true);
        mute = !mute;
        sp.edit().putBoolean("sounds", mute).apply();
    }

    //TODO BLOCK NOTIFICATIONS WHEN notifications is false
    private void toggleNotifications() {
        boolean notifications = sp.getBoolean("notifications", true);
        notifications = !notifications;
        sp.edit().putBoolean("notifications", notifications).apply();
    }

    //TODO Figure out what we want LocationPublic to exactly do
    private void toggleLocationPublic() {
        boolean locationPublic = sp.getBoolean("locationPublic", true);
        locationPublic = !locationPublic;
        sp.edit().putBoolean("locationPublic", locationPublic).apply();
    }

    private void switchState() {
        swDarkMode = findViewById(R.id.DarkMode);
        swSounds = findViewById(R.id.Sounds);
        swNotifications = findViewById(R.id.Notifications);
        swLocationPublic = findViewById(R.id.LocationPublic);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", true));
        swNotifications.setChecked(sp.getBoolean("notifications", true));
        swLocationPublic.setChecked(sp.getBoolean("locationPublic", true));
    }

    private void switchStateDark() {
        swDarkMode = findViewById(R.id.DarkModeDark);
        swSounds = findViewById(R.id.SoundsDark);
        swNotifications = findViewById(R.id.NotificationsDark);
        swLocationPublic = findViewById(R.id.LocationPublicDark);

        swDarkMode.setChecked(sp.getBoolean("darkMode", false));
        swSounds.setChecked(sp.getBoolean("sounds", true));
        swNotifications.setChecked(sp.getBoolean("notifications", true));
        swLocationPublic.setChecked(sp.getBoolean("locationPublic", true));
    }

    void selectImage(View v){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            // compare the resultCode with the
            // SELECT_PICTURE constant
            if (requestCode == PICK_IMAGE) {
                // Get the url of the image from data
                System.out.println("Found image");
                Uri selectedImageUri = data.getData();
                if (null != selectedImageUri) {
                    System.out.println("Found image");
                }
            }
        }
    }

    public void uploadFile(){
        try {
            Uri path = Uri.parse("android.resource://com.example.blitzbar/" + R.drawable.default_account_image);
            String imgPath = path.toString();

            StorageReference storageRef = FirebaseStorage.getInstance().getReference();
            StorageReference imageRef = storageRef.child(imgPath);

            Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.default_account_image);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            byte[] imageByteStream = outputStream.toByteArray();

            UploadTask uploadTask = imageRef.putBytes(imageByteStream);
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Profile_Image_Upload", "Upload to firebase failed");
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Profile_Image_Upload", "Profile image successfully uploaded to firebase.");
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            Log.e("Error", "Upload to firebase failed");
        }
    }

    public void download_image(){

        // ***************************************
        // TODO this code may need some touching up before use
        // ***************************************

        Uri path = Uri.parse("android.resource://com.example.blitzbar/" + R.drawable.default_account_image);
        String imgPath = path.toString();

        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference imageRef = storageRef.child(imgPath);

        final ImageView imageView = findViewById(R.id.profileImage);
        final long image_size = MAX_DOWNLOAD_SIZE;

        imageRef.getBytes(image_size).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

                // set image
                imageView.setImageBitmap(bitmap);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e("Error", "Download from firebase failed");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);

        Button imageBtn;

        if (sp.getBoolean("darkMode", false)) {
            setContentView(R.layout.activity_settings_dark);
            userName = (TextView) findViewById(R.id.userNameDark);
            profileImage = (ImageView) findViewById(R.id.profileImageDark);
            imageBtn = (Button)findViewById(R.id.select_image_dark);
            switchStateDark();
        } else {
            setContentView(R.layout.activity_settings);
            userName = (TextView) findViewById(R.id.userName);
            profileImage = (ImageView) findViewById(R.id.profileImage);
            imageBtn = (Button)findViewById(R.id.select_image);
            switchState();
        }

        imageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage(v);
            }
        });

        String userEmail = sp.getString("userEmail", "");

        if (userEmail != "") {
            Context context = getApplicationContext();
            SQLiteDatabase sqLiteDatabase = context.openOrCreateDatabase("BlitzBar", Context.MODE_PRIVATE, null);
            DBHelper dbHelper = new DBHelper(sqLiteDatabase);

            User user = dbHelper.getUser(userEmail);

            sqLiteDatabase.close();

            userName.setText(user.getFirst_name() + " " + user.getLast_name());
        }
    }
}