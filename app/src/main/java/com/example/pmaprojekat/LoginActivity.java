package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    EditText etEmail, etSifra;
    Button prijava;
    TextView registracija;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Prijava");
        etEmail = findViewById(R.id.etEmail);
        etSifra = findViewById(R.id.etSifra);
        prijava = findViewById(R.id.btnPrijava);
        registracija = findViewById(R.id.tvRegistracija);


        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                db = new DatabaseHelper(LoginActivity.this);
                String email, sifra;
                email = etEmail.getText().toString().trim();
                sifra = etSifra.getText().toString().trim();
                Cursor cursor = db.proveriKorisnika(email, sifra);
                if (cursor.moveToFirst()) {
                    //Log.d("tag", "onClick: " + cursor.getInt(0));
                    //Toast.makeText(LoginActivity.this, cursor.getCount(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(LoginActivity.this,"Postoji",Toast.LENGTH_SHORT).show();
                    SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
                    sessionManagement.saveSession(Integer.parseInt(String.valueOf(cursor.getInt(0))));
                  //  Log.d("TAG", "onClick: " + sessionManagement.getSession());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    /*i.putExtra("id", String.valueOf(cursor.getInt(0)));
                    i.putExtra("ime", cursor.getString(1));
                    i.putExtra("prezime", cursor.getString(2));
                    i.putExtra("email", cursor.getString(3));
                    i.putExtra("broj", cursor.getString(4));*/


                    startActivity(i);
                } else {
                    Toast.makeText(LoginActivity.this, "Ne postoji korisnik sa unetim mejlom ili šifrom", Toast.LENGTH_SHORT).show();
                }

            }

        });

        registracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
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

    @Override
    protected void onStart() {
        super.onStart();
        SessionManagement sessionManagement = new SessionManagement(LoginActivity.this);
      //  Log.d("TAG", "onStart: " + sessionManagement.getSession());
       if(sessionManagement.getSession()!=-1)
        {
            Intent intent = new Intent(LoginActivity.this,MainActivity.class);

            startActivity(intent);
        }
    }
}