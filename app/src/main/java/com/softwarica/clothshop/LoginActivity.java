package com.softwarica.clothshop;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.softwarica.clothshop.API.UsersAPI;
import com.softwarica.clothshop.BLL.LoginBLL;
import com.softwarica.clothshop.Model.User;
import com.softwarica.clothshop.StrictMode.StrictModeClass;
import com.softwarica.clothshop.URL.URL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnRegister, btnLogin;
    EditText etEmail,etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail=findViewById(R.id.etEmail);
        etPassword=findViewById(R.id.etPassword);
        btnRegister =findViewById(R.id.btnRegister);
        btnLogin=findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = etEmail.getText().toString();
                String pwd = etPassword.getText().toString();

                LoginBLL loginBLL = new LoginBLL();
                StrictModeClass.StrictMode();

                if (loginBLL.checkUser(email, pwd)) {

                    UsersAPI usersAPI= URL.getInstance().create(UsersAPI.class);
                    Call<User> userCall=usersAPI.getUserDetails(URL.token);
                    userCall.enqueue(new Callback<User>() {
                        @Override
                        public void onResponse(Call<User> call, Response<User> response) {
                            if (!response.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                String admin=response.body().getAdmin();

                                String token = URL.token;
                                SharedPreferences preferences = getSharedPreferences("token", Context.MODE_PRIVATE);
                                preferences.edit().putString("TOKEN",token).apply();

                                if (admin=="false")
                                {
                                    Toast.makeText(LoginActivity.this, "Login Sucessfully "+admin, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                                else
                                {
                                    Toast.makeText(LoginActivity.this, "Login Sucessfully "+admin, Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, AdminDashboardActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<User> call, Throwable t) {
                            Toast.makeText(LoginActivity.this, "error"+t.getLocalizedMessage(), Toast.LENGTH_SHORT).show();

                        }
                    });



                } else {
                    Toast.makeText(LoginActivity.this, "Email or password donot match", Toast.LENGTH_SHORT).show();
                    etEmail.setText("");
                    etPassword.setText("");
                    etEmail.requestFocus();
                }

            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });


    }
}