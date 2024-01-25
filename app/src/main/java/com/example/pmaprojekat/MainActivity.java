package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.se.omapi.Session;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    EditText mesto;
    TextView prijava,textView,dobrodosli;
    RadioButton iznajmljivanje, kupovina, stanovi, kuce;
    RadioGroup radioNamena,radioTip;
    Button pretraga;
    DatabaseHelper db;
    int namena, tip;
    String grad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        dobrodosli = findViewById(R.id.dobrodosli);
        textView = findViewById(R.id.textView);
        mesto = findViewById(R.id.etMesto);
        prijava = findViewById(R.id.tvPrijava);
        iznajmljivanje = findViewById(R.id.radioIznajmljivanje);
        kupovina = findViewById(R.id.radioKupovina);
        stanovi = findViewById(R.id.radioStanovi);
        kuce = findViewById(R.id.radioKuce);
        pretraga = findViewById(R.id.btnPretraga);
        radioNamena = findViewById(R.id.radioNamena);
        radioTip = findViewById(R.id.radioTip);

        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        if(sessionManagement.getSession()!=-1)
        {
            prijava.setVisibility(View.INVISIBLE);
            textView.setVisibility(View.INVISIBLE);
        }

        prijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
               if(sessionManagement.getSession()==-1)
                {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
            }
        });

        pretraga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,SviOglasi.class);
                grad = mesto.getText().toString().trim();
                i.putExtra("tip",tip);
                i.putExtra("namena",namena);
                i.putExtra("grad",grad);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        stanovi.setChecked(false);
        kuce.setChecked(false);
        iznajmljivanje.setChecked(false);
        kupovina.setChecked(false);
        tip = 0;
        namena = 0;
        grad = "";
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        if(sessionManagement.getSession()!=-1)
        {
            db = new DatabaseHelper(MainActivity.this);
            Cursor cursor = db.uzmiPodatkeOKorisniku(sessionManagement.getSession());
            cursor.moveToFirst();
            dobrodosli.setText("Dobrodošli, " + cursor.getString(1));
        }
    }

    public void onRadioButtonNamenaClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioIznajmljivanje:
                if (checked)
                    namena=2;

                break;
            case R.id.radioKupovina:
                if (checked)
                    namena=1;
                break;
        }
    }
    public void onRadioButtonTipClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.radioKuce:
                if (checked)
                    tip=1;

                break;
            case R.id.radioStanovi:
                if (checked)
                    tip=2;
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);

        if(sessionManagement.getSession()!=-1)
        {
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.user_menu,menu);
            MenuItem item = menu.findItem(R.id.itemPretraga);
            item.setVisible(false);
            invalidateOptionsMenu();
            if(menu instanceof MenuBuilder){
                MenuBuilder m = (MenuBuilder) menu;
                m.setOptionalIconsVisible(true);}
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        SessionManagement sessionManagement = new SessionManagement(MainActivity.this);
        switch(item.getItemId())
        {
            case R.id.itemPretraga:
                return true;
            case R.id.itemPodesavanja:
                Intent podesavanja = new Intent(MainActivity.this,UserProfile.class);
                podesavanja.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(podesavanja);
                return true;

            case R.id.itemDodajOglas:
                Intent dodaj = new Intent(MainActivity.this,AddActivity.class);
                //dodaj.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(dodaj);
                return true;
            case R.id.itemMojiOglasi:
                Intent mojiOglasi = new Intent(MainActivity.this,MojiOglasi.class);
                mojiOglasi.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mojiOglasi);
                return true;
            case R.id.itemOdjaviSe:
                Intent odjaviSe = new Intent(MainActivity.this,Logout.class);
                startActivity(odjaviSe);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}