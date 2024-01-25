package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class SviOglasi extends AppCompatActivity {

    RecyclerView rv;

    ArrayList<String> naslovi,adrese;
    ArrayList<Double> cene, povrsine;
    ArrayList<Integer> namene,id_niz;
    ArrayList<byte[]> slike;
    DatabaseHelper db;
    Cursor cursor;
    int namena,tip;
    String grad;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_svi_oglasi);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("");
        rv = findViewById(R.id.rvSviOglasi);
        db = new DatabaseHelper(SviOglasi.this);

        if(getIntent().getExtras()!=null)
        {

            if(getIntent().getExtras().getInt("namena")!=0) {
                namena = getIntent().getExtras().getInt("namena");
                if (getIntent().getExtras().getInt("tip")!=0)
                {
                    tip = getIntent().getExtras().getInt("tip");
                    if(getIntent().getExtras().getString("grad").length()!=0)
                    {
                        grad = getIntent().getExtras().getString("grad");
                        cursor = db.prikaziOglaseTipNamenaGrad(tip,namena,grad);
                    }
                    else
                    {
                        cursor = db.prikaziOglaseTipNamena(tip,namena);
                    }

                }
                else if (getIntent().getExtras().getString("grad").length()!=0)
                {
                    grad = getIntent().getExtras().getString("grad");
                    cursor = db.prikaziOglaseNamenaGrad(namena,grad);
                }
                else
                {

                    cursor = db.prikaziOglaseNamena(namena);
                }
            }
            else if(getIntent().getExtras().getInt("tip")!=0)
            {
                tip = getIntent().getExtras().getInt("tip");
                if(getIntent().getExtras().getString("grad").length()!=0)
                {
                    grad = getIntent().getExtras().getString("grad");
                    cursor = db.prikaziOglaseTipGrad(tip,grad);
                }
                else
                {
                    cursor = db.prikaziOglaseTip(tip);
                }

            }
            else if (getIntent().getExtras().getString("grad").length()!=0)
            {

                grad = getIntent().getExtras().getString("grad");
                cursor = db.prikaziOglaseGrad(grad);

            }
            else
            {

                cursor = db.prikaziSveOglase();
            }

        }



        naslovi = new ArrayList<>();
        adrese = new ArrayList<>();
        cene = new ArrayList<>();
        namene = new ArrayList<>();
        povrsine = new ArrayList<>();
        id_niz = new ArrayList<>();
        slike = new ArrayList<>();
        podaciIzBaze();


        CustomAdapter customAdapter = new CustomAdapter(this,naslovi,namene,adrese,cene,povrsine,SviOglasi.this,id_niz,slike);
        layoutManager = new LinearLayoutManager(SviOglasi.this);
        rv.setLayoutManager(layoutManager);
        rv.setAdapter(customAdapter);
    }

    void podaciIzBaze() {


        if (cursor.getCount() == 0) {
            Toast.makeText(SviOglasi.this, "Nema oglasa u datoj pretrazi", Toast.LENGTH_LONG).show();
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