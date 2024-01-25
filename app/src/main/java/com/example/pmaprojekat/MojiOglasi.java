package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

public class MojiOglasi extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<String> naslovi,adrese;
    ArrayList<Double> cene, povrsine;
    ArrayList<Integer> namene,id_niz;
    ArrayList<byte[]> slike;
    DatabaseHelper db;
    SessionManagement sessionManagement;
    int idKorisnika;
    Cursor cursor;
    int namena,tip;
    private RecyclerView.LayoutManager layoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moji_oglasi);
        setTitle("Moji oglasi");
        rv = findViewById(R.id.rvMojiOglasi);
        db = new DatabaseHelper(MojiOglasi.this);
        sessionManagement = new SessionManagement(MojiOglasi.this);
        idKorisnika = sessionManagement.getSession();
        if(idKorisnika!=-1)
        {
            cursor = db.prikaziMojeOglase(idKorisnika);
            naslovi = new ArrayList<>();
            adrese = new ArrayList<>();
            cene = new ArrayList<>();
            namene = new ArrayList<>();
            povrsine = new ArrayList<>();
            id_niz = new ArrayList<>();
            slike = new ArrayList<>();
            podaciIzBaze();

            MojiOglasiAdapter mojiOglasiAdapter = new MojiOglasiAdapter(this,naslovi,adrese,cene,povrsine,namene,id_niz,MojiOglasi.this,slike);
            layoutManager = new LinearLayoutManager(MojiOglasi.this);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(mojiOglasiAdapter);
        }
        else
        {
            Intent i = new Intent(MojiOglasi.this,LoginActivity.class);
            startActivity(i);
        }
    }

    public void podaciIzBaze()
    {
        if (cursor.getCount() == 0) {
            Toast.makeText(MojiOglasi.this, "Još uvek niste postavili nijedan oglas", Toast.LENGTH_LONG).show();
        } else {
            // ako postoje podaci u bazi citamo clan po clan dok postoje zapisi
            while (cursor.moveToNext()) {


                id_niz.add(cursor.getInt(0));
                namene.add(cursor.getInt(3));
                naslovi.add(cursor.getString(4));
                adrese.add(cursor.getString(5));
                povrsine.add(cursor.getDouble(7));
                cene.add(cursor.getDouble(11));
                slike.add(cursor.getBlob(16));

            }
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        MenuItem item = menu.findItem(R.id.itemMojiOglasi);
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
        switch (item.getItemId()) {

            case R.id.itemPretraga:
                Intent pretraga = new Intent(MojiOglasi.this,MainActivity.class);
                pretraga.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(pretraga);
                return true;
            case R.id.itemPodesavanja:
                Intent podesavanja = new Intent(MojiOglasi.this, UserProfile.class);
                podesavanja.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(podesavanja);
                return true;

            case R.id.itemDodajOglas:

                Intent dodaj = new Intent(MojiOglasi.this,AddActivity.class);
                startActivity(dodaj);
                return true;

            case R.id.itemOdjaviSe:
                Intent odjavi = new Intent(MojiOglasi.this,Logout.class);
                startActivity(odjavi);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}