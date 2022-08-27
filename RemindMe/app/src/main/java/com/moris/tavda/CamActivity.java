package com.moris.tavda;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.moris.tavda.Data.NetworkClient;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CamActivity extends AppCompatActivity {
    //    Uri selectedImage ;
    Bitmap thumbnailBitmap;
    private ImageView imageView;
    private Button buttSend;
    private EditText editText;
    String currentPhotoPath;

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cam);
        imageView = findViewById(R.id.imageView3);
        buttSend = findViewById(R.id.button2);
        editText = findViewById(R.id.editText2);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == RESULT_OK) {
            // Фотка сделана, извлекаем картинку
            //selectedImage = data.getExtras().get("data");
            int targetW = imageView.getWidth();
            int targetH = imageView.getHeight();
            BitmapFactory.Options bmOptions = new BitmapFactory.Options();
            bmOptions.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(currentPhotoPath, bmOptions);

            int photoW = bmOptions.outWidth;
            int photoH = bmOptions.outHeight;

            // Determine how much to scale down the image
           // int scaleFactor = Math.max(1, Math.min(photoW/targetW, photoH/targetH));
            int scaleFactor = 1;
            // Decode the image file into a Bitmap sized to fill the View
            bmOptions.inJustDecodeBounds = false;
            bmOptions.inSampleSize = scaleFactor;
            bmOptions.inPurgeable = true;
            thumbnailBitmap = BitmapFactory.decodeFile(currentPhotoPath, bmOptions);
            imageView.setImageBitmap(thumbnailBitmap);

//            Bundle extras = data.getExtras();
//            thumbnailBitmap = (Bitmap) extras.get("data");
//            Bitmap scaledImage = scaleBitmap(thumbnailBitmap, 460, 230);
//            imageView.setImageBitmap(thumbnailBitmap);

            //thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            //imageView.setImageBitmap(thumbnailBitmap);

        }
    }

    //    http://tavda.ru:8070/index.php?action=insert&text=dcgfdgdgf
    @SuppressLint("QueryPermissionsNeeded")
    public void onClick(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String[] permissionRequest = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
            requestPermissions(permissionRequest, 2323);
        }
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            //if (cameraIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this.getApplication(),
                        "com.moris.tavda.fileprovider",
                        photoFile);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(cameraIntent, 0);
            }
        }
    }

    private void uploadToServer() {
        Retrofit retrofit = NetworkClient.getRetrofitClient(this);
        NetworkClient.UploadAPIs uploadAPIs = retrofit.create(NetworkClient.UploadAPIs.class);
        //Create a file object using file path
//        File file = new File());
        // Create a request body with file and image media type
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            thumbnailBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bitmapdata = baos.toByteArray();
        RequestBody fileReqBody = RequestBody.create(MediaType.parse("image/*"), bitmapdata);
        // Create MultipartBody.Part using file request-body,file name and part name
        MultipartBody.Part part = MultipartBody.Part.createFormData("upload", "name", fileReqBody);
        //Create request body with text description and text media type
//        RequestBody description = RequestBody.create(MediaType.parse("text/plain"), "image-type");
        //
//        Call call = uploadAPIs.uploadImage(part, description);
        RequestBody action = RequestBody.create(MediaType.parse("multipart/form-data"), "insert");
        RequestBody text = RequestBody.create(MediaType.parse("multipart/form-data"), editText.getText().toString());
        Call call = uploadAPIs.uploadImage(action, text, part);

        call.enqueue(new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                int kod = response.code();
                buttSend.setText(Integer.toString(kod));
                if (response.isSuccessful()) {
                    buttSend.setText("ОК");
                    buttSend.setEnabled(false);
                }

            }

            @Override
            public void onFailure(Call call, Throwable t) {

            }
        });
    }

    public void onClickPost(View v) {
        uploadToServer();
    }
}
