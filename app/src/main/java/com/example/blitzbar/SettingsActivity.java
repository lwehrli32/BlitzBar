package com.example.blitzbar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class SettingsActivity extends AppCompatActivity {

    // TODO get username
    String tempusername = "Lukas";

    static final int PICK_IMAGE = 1;
    static final long MAX_DOWNLOAD_SIZE = 1024 * 1024;
    private static final int IMAGEPICK_GALLERY_REQUEST = 300;
    private static final int IMAGE_PICKCAMERA_REQUEST = 400;
    private static final int CAMERA_REQUEST = 100;
    private static final int STORAGE_REQUEST = 200;

    private NavigationBarView bottomNavigationBarView;

    ImageCache imgCache;
    String cameraPermission[];
    String storagePermission[];
    Uri imageuri;
    ProgressDialog pd;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    StorageReference storageRef;
    boolean justUploaded;

    SharedPreferences sp;
    SwitchCompat swSounds;
    SwitchCompat swNotifications;
    SwitchCompat swLocationPublic;
    TextView userName;
    ImageView profileImage;
    TextView blitzBarScore;
    String builtNameAge;

    public void onSwitch(View v) {
        if (v.getId() == R.id.Sounds) {
            toggleMute();
        } else if (v.getId() == R.id.Notifications) {
            toggleNotifications();
        } else if (v.getId() == R.id.LocationPublic) {
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
        swSounds = findViewById(R.id.Sounds);
        swNotifications = findViewById(R.id.Notifications);
        swLocationPublic = findViewById(R.id.LocationPublic);

        swSounds.setChecked(sp.getBoolean("sounds", true));
        swNotifications.setChecked(sp.getBoolean("notifications", true));
        swLocationPublic.setChecked(sp.getBoolean("locationPublic", true));
    }

    // We will select an image from gallery
    public void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGEPICK_GALLERY_REQUEST);
    }

    public void pickFromCamera() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp_pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        imageuri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        Intent camerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        camerIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageuri);
        startActivityForResult(camerIntent, IMAGE_PICKCAMERA_REQUEST);
    }

    public Boolean checkCameraPermission() {
        boolean result = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }

    public void requestCameraPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST);
    }

    public Boolean checkStoragePermission() {
        boolean result = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }

    // requesting for storage permission
    public void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_REQUEST: {
                if (grantResults.length > 0) {
                    boolean camera_accepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageaccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accepted && writeStorageaccepted) {
                        pickFromCamera();
                    } else {
                        Toast.makeText(this, "Please Enable Camera and Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST: {
                if (grantResults.length > 0) {
                    boolean writeStorageaccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (writeStorageaccepted) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "Please Enable Storage Permissions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == IMAGEPICK_GALLERY_REQUEST) {
                imageuri = data.getData();
                upload_image(imageuri);
            }
            if (requestCode == IMAGE_PICKCAMERA_REQUEST) {
                upload_image(imageuri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void showImagePicDialog(View v) {
        String options[] = {"Camera", "Gallery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image From");

        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // if access is not given then we will request for permission
                if (which == 0) {
                    if (!checkCameraPermission()) {
                        requestCameraPermission();
                    } else {
                        pickFromCamera();
                    }
                } else if (which == 1) {
                    if (!checkStoragePermission()) {
                        requestStoragePermission();
                    } else {
                        pickFromGallery();
                    }
                }
            }
        });
        builder.create().show();
    }

    public void upload_image(final Uri uri){
        pd.show();

        justUploaded = true;

        try {
            //Uri path = Uri.parse("android.resource://com.example.blitzbar/" + R.drawable.profile_image);
            //String imgPath = path.toString();

            // TODO get user name
            StorageReference imageRef = storageRef.child("Profile_Pictures").child(tempusername);

            //Bitmap bitmap = BitmapFactory.decodeResource(this.getResources(), R.drawable.profile_image);
            //ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
            //byte[] imageByteStream = outputStream.toByteArray();

            //UploadTask uploadTask = imageRef.putBytes(imageByteStream);
            imageRef.putFile(uri).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.e("Profile_Image_Upload", "Upload to firebase failed");
                    pd.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Log.i("Profile_Image_Upload", "Profile image successfully uploaded to firebase.");
                    profileImage.setImageURI(null);
                    profileImage.setImageURI(uri);
                    SharedPreferences.Editor editor = sp.edit();
                    editor.putInt("profilePic", 1).apply();
                    editor.putBoolean("justUploaded", false).apply();

                    // TODO get username
                    imgCache.updateContext(getApplicationContext());
                    imgCache.cacheProfilePic(tempusername, profileImage);

                    pd.dismiss();
                }
            });
        }catch(Exception e){
            e.printStackTrace();
            Log.e("Error", "Upload to firebase failed");
            pd.dismiss();
        }
    }

    public void download_image(){
        pd.show();

        // TODO get username
        StorageReference imageRef = storageRef.child("Profile_Pictures").child(tempusername);

        final long image_size = MAX_DOWNLOAD_SIZE;

        imageRef.getBytes(image_size).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                SharedPreferences.Editor editor = sp.edit();
                // set image
                profileImage.setImageBitmap(bitmap);

                // TODO set username
                imgCache.updateContext(getApplicationContext());
                imgCache.cacheProfilePic(tempusername, profileImage);
                editor.putBoolean("justUploaded", true).apply();

                pd.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
                Log.e("Error", "Download from firebase failed");
                pd.dismiss();
            }
        });
    }

    public void signOutUser(View v){

        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("loggedIn", 0).apply();

        LoginActivity.loggedInUser = null;

        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private NavigationBarView.OnItemSelectedListener bottomnavFunction = new NavigationBarView.OnItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent menu_intent = new Intent();
            switch (item.getItemId()) {
                case R.id.listView:
                    menu_intent = new Intent(SettingsActivity.this, ListActivity.class);
                    break;
                case R.id.friends:
                    menu_intent = new Intent(SettingsActivity.this, FriendsActivity.class);
                    break;
                case R.id.mapView:
                    menu_intent = new Intent(SettingsActivity.this, MapsActivity.class);
                    break;
                default:
                    return false;
            }

            startActivity(menu_intent);
            return true;
        }
    };

    public void onChangeListener(boolean isChecked){
        SharedPreferences.Editor editor = sp.edit();
        if (isChecked) {
            editor.putInt("isDarkMode", 1).apply();
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            editor.putInt("isDarkMode", 0).apply();
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sp = getApplicationContext().getSharedPreferences("BlitzBar", Context.MODE_PRIVATE);

        boolean isDarkMode = sp.getInt("isDarkMode", 0) == 1;
        if (isDarkMode) {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            getDelegate().setLocalNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        SharedPreferences.Editor editor = sp.edit();
        SwitchCompat darkMode = (SwitchCompat) findViewById(R.id.DarkMode);

        if (isDarkMode)
            darkMode.setChecked(true);

        firebaseDatabase = FirebaseDatabase.getInstance();
        storageRef = FirebaseStorage.getInstance().getReference();
        databaseReference = firebaseDatabase.getReference("Profile_Pictures");
        pd = new ProgressDialog(this);
        pd.setCanceledOnTouchOutside(false);

        Button imageBtn;
        boolean isChecked = false;

        justUploaded = sp.getBoolean("justUploaded", false);
        imgCache = new ImageCache(getApplicationContext());

        blitzBarScore = (TextView) findViewById(R.id.blitzbarscore);
        userName = (TextView) findViewById(R.id.userName);
        profileImage = (ImageView) findViewById(R.id.profileImage);
        imageBtn = (Button) findViewById(R.id.select_image);
        switchState();

        bottomNavigationBarView = findViewById(R.id.bottomnav);
        bottomNavigationBarView.setOnItemSelectedListener(bottomnavFunction);

        darkMode.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                onChangeListener(isChecked);
            }
        });

        String userEmail = LoginActivity.loggedInUser.getEmail();

        if (userEmail != "") {

            boolean hasProfilePic = sp.getInt("profilePic", 0) == 1;

            if (hasProfilePic && !justUploaded) {
                download_image();
                editor.putBoolean("justUploaded", true).apply();
            }else if (hasProfilePic){
                Bitmap bmp = imgCache.getProfilePic(tempusername);
                profileImage.setImageBitmap(bmp);
            }
            int fireEmoji = 0x1F525;

            userName.setText(LoginActivity.loggedInUser.getFirst_name() + " " + LoginActivity.loggedInUser.getLast_name());
            blitzBarScore.setText(new String(Character.toChars(fireEmoji)) + " " + String.valueOf(LoginActivity.loggedInUser.getBlitz_score()));
        }
    }
}