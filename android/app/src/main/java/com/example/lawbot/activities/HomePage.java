package com.example.lawbot.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lawbot.R;
import com.example.lawbot.activities.adapter.ProcessosAdapter;
import com.example.lawbot.activities.dtos.LoginResponse;
import com.example.lawbot.activities.dtos.ProcessosResponse;
import com.example.lawbot.activities.services.ApiService;
import com.example.lawbot.activities.services.ProcessoService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomePage extends AppCompatActivity {

    RecyclerView recyclerView;
    SearchView searchView;
    ProcessosAdapter processoAdapter;
    List<ProcessosResponse> processosList;
    ImageButton button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        Intent intent = getIntent();
        Long valorLong = intent.getLongExtra("data", -1);
        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        searchView = findViewById(R.id.searchview);
        button = findViewById(R.id.logoutButton);
        ProcessoService processoService = ApiService.getListProcess();
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return true;
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                redirectToLogin();
            }
        });

        Call<List<ProcessosResponse>> call = processoService.getProcess(valorLong);
        call.enqueue(new Callback<List<ProcessosResponse>>() {
            @Override
            public void onResponse(Call<List<ProcessosResponse>> call, Response<List<ProcessosResponse>> response) {
                processosList = response.body();
                Log.d("LISTA", processosList.toString());
                updateRecyclerView(processosList);
            }

            @Override
            public void onFailure(Call<List<ProcessosResponse>> call, Throwable t) {
                Log.e("HomePage", "Erro na chamada: " + t.getMessage());
            }
        });
    }

    private void filterList(String newText) {
        List<ProcessosResponse> filteredList = new ArrayList<>();
        for (ProcessosResponse processo : processosList) {
            if (processo.getNumProcesso().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(processo);
            }
        }
        updateRecyclerView(filteredList);
    }

    private void updateRecyclerView(List<ProcessosResponse> processosList) {
        processoAdapter = new ProcessosAdapter(this, processosList);
        recyclerView.setAdapter(processoAdapter);
    }

    private void redirectToLogin() {
        Intent intent = new Intent(HomePage.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}