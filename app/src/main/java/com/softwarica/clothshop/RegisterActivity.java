package com.softwarica.clothshop;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.CursorLoader;

import com.softwarica.ServerResponse.ImageResponse;
import com.softwarica.ServerResponse.RegisterResponse;
import com.softwarica.clothshop.API.UsersAPI;
import com.softwarica.clothshop.Model.User;
import com.softwarica.clothshop.StrictMode.StrictModeClass;
import com.softwarica.clothshop.URL.URL;

import java.io.File;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etFullName, etEmail, etPassword, etConfirmPassword, etMobileNumber, etAddress;
    private Spinner spgender;
    private Button btnRegister,btnlogin;
    private CircleImageView imgProfile;
    String imagePath;
    private String imageName = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userregistration);
        etFullName=findViewById(R.id.etFullname);
        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        etConfirmPassword=findViewById(R.id.etCPassword);
        etMobileNumber=findViewById(R.id.etmobilenumber);
        etAddress=findViewById(R.id.etAddress);
        imgProfile=findViewById(R.id.imgProfile);
        spgender=findViewById(R.id.spgender);
        btnRegister=findViewById(R.id.btnRegister);
        btnlogin=findViewById(R.id.btnlogin);
        String gender[]={"Select Gender","Male","Female","Others"};
        ArrayAdapter adapter= new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,gender);
        spgender.setAdapter(adapter);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BrowseImage();
            }
        });
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
                    if (validate()) {
                        saveImageOnly();
                        register();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Password Donot match", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                    return;
                }
            }
        });

    }

    private void register() {
        String fullname= etFullName.getText().toString();
        String email= etEmail.getText().toString();
        String password= etPassword.getText().toString();
        String confirmpassword = etConfirmPassword.getText().toString();
        String mobilenumber = etMobileNumber.getText().toString();
        String address = etAddress.getText().toString();

        User users = new User(fullname, email, password, confirmpassword, mobilenumber, address, imageName);
        UsersAPI usersAPI= URL.getInstance().create(UsersAPI.class);
        Call<RegisterResponse> registerCall =usersAPI.registerUser(users);

        registerCall.enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {
                if (!response.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Code" + response.code(), Toast.LENGTH_SHORT).show();
                    return;
                }
                Toast.makeText(RegisterActivity.this, "Register Successfully", Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this, "Error" + t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void saveImageOnly() {
        File file = new File(imagePath);
        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        MultipartBody.Part body = MultipartBody.Part.createFormData("imageFile",
                file.getName(), requestBody);
        UsersAPI usersAPI = URL.getInstance().create(UsersAPI.class);
        Call<ImageResponse> responseBodyCall = usersAPI.uploadImage(body);

        StrictModeClass.StrictMode();

        try {
            Response<ImageResponse> imageResponse = responseBodyCall.execute();
            imageName = imageResponse.body().getFilename();
            Toast.makeText(this, "Image Inserted", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            Toast.makeText(this, "Error inserting image " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private boolean validate() {
            boolean status = true;

            if (imgProfile.getDrawable() == null) {
                Toast.makeText(this, "Please select image", Toast.LENGTH_SHORT).show();
                imgProfile.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etEmail.getText())) {
                etEmail.setError("Enter email address");
                etEmail.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etFullName.getText())) {
                etFullName.setError("Enter full name");
                etFullName.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etEmail.getText())) {
                etEmail.setError("Enter email address");
                etEmail.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etPassword.getText())) {
                etPassword.setError("Enter password");
                etPassword.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etEmail.getText())) {
                etEmail.setError("Enter email address");
                etEmail.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etMobileNumber.getText())) {
                etMobileNumber.setError("Enter mobile phone number");
                etMobileNumber.requestFocus();
                status = false;
            } else if (TextUtils.isEmpty(etAddress.getText())) {
                etAddress.setError("Enter Your Address");
                etAddress.requestFocus();
                status = false;
            } else if (etPassword.getText().toString().length() < 6) {
                etPassword.setError("Minimum 6 character");
                etPassword.requestFocus();
                status = false;
            }
            else if (etPassword.getText().toString().length() > 20) {
                etPassword.setError("Maximum 20 character");
                etPassword.requestFocus();
                status = false;
            } else if (etMobileNumber.getText().toString().length() != 10) {
                etMobileNumber.setError("Mobile Number should be 10 character");
                etMobileNumber.requestFocus();
                status = false;
            } else if (!TextUtils.isEmpty(etMobileNumber.getText())) {
                String first = etMobileNumber.getText().toString().charAt(0) + "";
                if (!first.equals("9")) {
                    etMobileNumber.setError("Mobile number should start from 9");
                    status = false;
                }
            }

            return status;
        }

    private void BrowseImage() {
        Intent intent =new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, 0);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "Please Select Image", Toast.LENGTH_SHORT).show();
            }
        }
        Uri uri = data.getData();
        imgProfile.setImageURI(uri);
        imagePath = getRealPathFromUri(uri);
    }

    private String getRealPathFromUri(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        CursorLoader loader = new CursorLoader(getApplicationContext(),
                uri, projection, null, null, null);
        Cursor cursor = loader.loadInBackground();
        int colIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String result = cursor.getString(colIndex);
        cursor.close();
        return result;
    }

}
