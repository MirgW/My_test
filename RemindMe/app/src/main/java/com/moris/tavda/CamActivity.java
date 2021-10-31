package com.moris.tavda;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.moris.tavda.Data.NetworkClient;

import java.io.ByteArrayOutputStream;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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
            thumbnailBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(thumbnailBitmap);

        }
    }

    //    http://tavda.ru:8070/index.php?action=insert&text=dcgfdgdgf
    public void onClick(View v) {
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(cameraIntent, 0);
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
