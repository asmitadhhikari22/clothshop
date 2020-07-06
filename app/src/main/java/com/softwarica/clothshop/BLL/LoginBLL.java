package com.softwarica.clothshop.BLL;

import com.softwarica.clothshop.ServerResponse.RegisterResponse;
import com.softwarica.clothshop.API.UsersAPI;
import com.softwarica.clothshop.URL.URL;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Response;

public class LoginBLL {
    boolean isSuccess=false;
    public boolean checkUser(String email,String password){
        UsersAPI usersAPI= URL.getInstance().create(UsersAPI.class);

        Call<RegisterResponse> usersCall=usersAPI.checkUser(email,password);

        try {
            Response<RegisterResponse> loginResponse=usersCall.execute();
            if (loginResponse.isSuccessful() && loginResponse.body().getStatus().equals("Login success!")){

                URL.token += loginResponse.body().getToken();

                isSuccess=true;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return isSuccess;
    }
}
