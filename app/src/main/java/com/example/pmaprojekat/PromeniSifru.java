package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PromeniSifru extends AppCompatActivity {

    EditText novaLozinka, potvrdiLozinku;
    Button potvrdi;
    DatabaseHelper db;
    SessionManagement sessionManagement;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promeni_sifru);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        novaLozinka = findViewById(R.id.tvNovaLozinka);
        potvrdiLozinku = findViewById(R.id.tvPotvrdiLozinku);
        potvrdi = findViewById(R.id.btnPotvrdi);
        sessionManagement = new SessionManagement(PromeniSifru.this);
        id = sessionManagement.getSession();
        if (id == -1)
        {
            Intent i = new Intent(PromeniSifru.this,LoginActivity.class);
            startActivity(i);
        }
        potvrdi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (novaLozinka.getText().toString().trim().isEmpty())
                {
                    Toast.makeText(PromeniSifru.this,"Popunite polja",Toast.LENGTH_SHORT).show();
                }

                else if (! (novaLozinka.getText().toString().trim().equals(potvrdiLozinku.getText().toString().trim()))){
                    Toast.makeText(PromeniSifru.this,"Šifre se ne poklapaju!",Toast.LENGTH_SHORT).show();
                }
                else {
                    db = new DatabaseHelper(PromeniSifru.this);
                    if (db.promeniSifru(id, novaLozinka.getText().toString().trim())) {
                        new AlertDialog.Builder(PromeniSifru.this)
                                .setTitle("Uspešno")
                                .setIcon(R.drawable.alert_check)
                                .setMessage("Uspešno ste izmenili podatke!")
                                .show();
                    } else
                    {
                        Toast.makeText(PromeniSifru.this,"Neuspešno!",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}