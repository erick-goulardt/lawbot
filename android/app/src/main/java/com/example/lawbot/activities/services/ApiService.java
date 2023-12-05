package com.example.lawbot.activities.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiService {
    private static final String URL = "http://192.168.1.13:8080/";
    private static Retrofit getRetrofit(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    public static LoginService getUserService(){
        LoginService userService = getRetrofit().create(LoginService.class);
        return userService;
    }

    public static ProcessoService getListProcess() {
        ProcessoService processoService = getRetrofit().create(ProcessoService.class);
        return processoService;
    }
}
