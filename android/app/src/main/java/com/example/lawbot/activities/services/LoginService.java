package com.example.lawbot.activities.services;

import com.example.lawbot.activities.dtos.LoginRequest;
import com.example.lawbot.activities.dtos.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("cliente/login")
    Call<LoginResponse> userLogin(@Body LoginRequest loginRequest);
}
