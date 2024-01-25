package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class PrikaziOglas extends AppCompatActivity {

    TextView naslov,tip,namena,povrsina,cena,adresa,grad,namestenost,brojSoba,infrastruktura,opis,imeprezime,email,broj;

    ArrayList<byte[]> slike;
    ViewPager slika;
    SessionManagement sessionManagement;
    DatabaseHelper db;
    int idOglasa,idKorisnika,idVlasnika;
    Cursor oglas;
    Cursor vlasnik;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prikazi_oglas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        naslov = findViewById(R.id.naslov);
        tip = findViewById(R.id.tip);
        namena = findViewById(R.id.namena);
        povrsina = findViewById(R.id.povrsina);
        cena = findViewById(R.id.cena);
        adresa = findViewById(R.id.adresa);
        grad = findViewById(R.id.grad);
        namestenost = findViewById(R.id.namestenost);
        brojSoba = findViewById(R.id.brojSoba);
        infrastruktura = findViewById(R.id.infrastruktura);
        opis = findViewById(R.id.opis);
        slika = findViewById(R.id.viewPager);
        imeprezime = findViewById(R.id.imePrezime);
        email = findViewById(R.id.email);
        broj = findViewById(R.id.broj);
        sessionManagement = new SessionManagement(PrikaziOglas.this);
        idKorisnika = sessionManagement.getSession();
        slike = new ArrayList<>();
        if(getIntent().getExtras()!=null)
        {
            idOglasa = getIntent().getExtras().getInt("idOglasa");
            db = new DatabaseHelper(PrikaziOglas.this);
            oglas = db.prikaziOglas(idOglasa);
            if(oglas.moveToFirst())
            {
                naslov.setText(oglas.getString(4));
                switch (oglas.getInt(2))
                {
                    case 1:
                        tip.setText("Kuća");
                        break;
                    case 2:
                        tip.setText("Stan");
                        break;
                }
                switch(oglas.getInt(3))
                {
                    case 1:
                        namena.setText("Na prodaju");
                        break;
                    case 2:
                        namena.setText("Za izdavanje");
                        break;
                }
                adresa.setText(oglas.getString(5) + ", ");
                grad.setText(oglas.getString(6));
                povrsina.setText("površina: "+String.valueOf(oglas.getDouble(7)) + " m2");
                brojSoba.setText("broj soba: " + String.valueOf(oglas.getInt(8)));
                switch(oglas.getInt(9)){
                    case 0:
                        namestenost.setText("Namešteno");
                        break;
                    case 1:
                        namestenost.setText("Nenamešteno");
                        break;
                }
                infrastruktura.setText("infrastruktura: "+oglas.getString(10));
                cena.setText("cena: " + String.valueOf(oglas.getString(11)) + "€");
                opis.setText(oglas.getString(13));
                vlasnik = db.vlasnikOglasa(idOglasa);
                if(vlasnik.moveToFirst())
                {
                    imeprezime.setText("Ime i prezime: " +vlasnik.getString(1) + " " + vlasnik.getString(2));
                    email.setText("email: " + vlasnik.getString(3));
                    broj.setText("broj: " + vlasnik.getString(4));
                }
                do {
                    slike.add(oglas.getBlob(16));
                }
                while(oglas.moveToNext());
            }

        }
        ImageAdapter imageAdapter = new ImageAdapter(PrikaziOglas.this,slike);
        slika.setAdapter(imageAdapter);
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