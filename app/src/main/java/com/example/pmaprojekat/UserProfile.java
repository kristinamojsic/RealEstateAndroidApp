package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Currency;

public class UserProfile extends AppCompatActivity {

    EditText email,ime,prezime,broj;

    int id;
    Button azuriraj, obrisi,promeniSifru;

    SessionManagement sessionManagement;
    DatabaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        setTitle("Izmena profila");
        email = findViewById(R.id.tvProfileEmail);
        ime = findViewById(R.id.tvProfileIme);
        prezime = findViewById(R.id.tvProfilePrezime);
        broj = findViewById(R.id.tvProfileTelefon);
        azuriraj = findViewById(R.id.btnIzmeni);
        obrisi = findViewById(R.id.btnObrisi);
        promeniSifru = findViewById(R.id.btnPromeniSifru);
        sessionManagement = new SessionManagement(UserProfile.this);
        id = sessionManagement.getSession();
        if (id != -1) {
            db = new DatabaseHelper(UserProfile.this);
            Cursor cursor = db.uzmiPodatkeOKorisniku(id);
            if (cursor.moveToFirst()) {
                email.setText(cursor.getString(3));
                ime.setText(cursor.getString(1));
                prezime.setText(cursor.getString(2));
                broj.setText(cursor.getString(4));
            }

        }
        else
        {
            Intent i = new Intent(UserProfile.this,LoginActivity.class);
            startActivity(i);
        }

        azuriraj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper db = new DatabaseHelper(UserProfile.this);
                if (db.izmeniKorisnika(id, email.getText().toString().trim(),
                        ime.getText().toString().trim(), prezime.getText().toString().trim(),
                        broj.getText().toString().trim())) {
                    new AlertDialog.Builder(UserProfile.this)
                            .setTitle("Uspešno")
                            .setIcon(R.drawable.alert_check)
                            .setMessage("Uspešno ste izmenili podatke!")
                            .show();
                    //Toast.makeText(UserProfile.this, "uspesno", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserProfile.this, "neuspesno", Toast.LENGTH_SHORT).show();
                }
            }
        });

        obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(UserProfile.this).setTitle("Upozorenje")
                        .setMessage("Da li ste sigurni da želite da obrišete nalog? Vaši oglasi će takođe biti obrisani.")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper db = new DatabaseHelper(UserProfile.this);
                                if (db.obrisiKorisnika(id)) {
                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);

//                                        startActivity(new Intent(Profile.this, MainActivity.class));
//                                        Toast.makeText(Profile.this, "Your account has deleted!", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserProfile.this, "Nemoguća operacija", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Ne", null).show();

            }
        });

        promeniSifru.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserProfile.this,PromeniSifru.class);
                startActivity(i);
            }
        });

    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        MenuItem item = menu.findItem(R.id.itemPodesavanja);
        item.setVisible(false);
        invalidateOptionsMenu();
        if(menu instanceof MenuBuilder){
            MenuBuilder m = (MenuBuilder) menu;
            m.setOptionalIconsVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch(item.getItemId())
        {

            case R.id.itemPretraga:
                Intent pretraga = new Intent(UserProfile.this,MainActivity.class);
                startActivity(pretraga);
                pretraga.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                return true;

            case R.id.itemDodajOglas:
                Intent dodaj = new Intent(UserProfile.this,AddActivity.class);
                //dodaj.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(dodaj);
                return true;
            case R.id.itemMojiOglasi:
                Intent mojiOglasi = new Intent(UserProfile.this,MojiOglasi.class);
                mojiOglasi.putExtra("idKorisnika",id);
                mojiOglasi.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mojiOglasi);
                return true;
            case R.id.itemOdjaviSe:
                Intent odjaviSe = new Intent(UserProfile.this,Logout.class);
                startActivity(odjaviSe);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}