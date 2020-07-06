package com.softwarica.clothshop.BLL;

import com.softwarica.clothshop.API.UsersAPI;
import com.softwarica.clothshop.Model.User;
import com.softwarica.clothshop.ServerResponse.RegisterResponse;
import com.softwarica.clothshop.URL.URL;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class RegisterBLL {
    boolean isSuccess=false;


    public boolean registerUser(String fullname, String email, String password, String confirmpassword, String mobilenumber,String address , String image){
        UsersAPI usersAPI= URL.getInstance().create(UsersAPI.class);

        User users = new User(fullname, email, password,confirmpassword, mobilenumber, address,image);

        Call<RegisterResponse> usersCall=usersAPI.registerUser(users);

        try {
            Response<RegisterResponse> registerResponse=usersCall.execute();
            if (registerResponse.isSuccessful() && registerResponse.body().getStatus().equals("Registration successful!")){

                URL.token += registerResponse.body().getToken();

                isSuccess=true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
