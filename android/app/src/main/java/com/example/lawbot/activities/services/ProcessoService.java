package com.example.lawbot.activities.services;

import com.example.lawbot.activities.dtos.ProcessosResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ProcessoService {
    @GET("processo/cliente/{id}")
    Call<List<ProcessosResponse>> getProcess(@Path("id") Long id);
}
