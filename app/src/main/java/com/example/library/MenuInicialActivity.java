package com.example.library;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MenuInicialActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_inicial);
        setTitle("Saude Box");
    }

    public void btnSocial(View view) {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        homeActivity.putExtra("saude", "social");
        startActivity(homeActivity);
    }

    public void btnFisica(View view) {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        homeActivity.putExtra("saude", "fisica");
        startActivity(homeActivity);
    }

    public void btnMental(View view) {
        Intent homeActivity = new Intent(getApplicationContext(), HomeActivity.class);
        homeActivity.putExtra("saude", "mental");
        startActivity(homeActivity);
    }
}
