package com.example.pmaprojekat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MojiOglasiAdapter extends RecyclerView.Adapter<MojiOglasiAdapter.MyViewHolder> {

    Context context;
    ArrayList<String> naslovi,adrese;
    ArrayList<Double> cene, povrsine;
    ArrayList<Integer> namene,id_niz;
    ArrayList<byte[]> slike;

    Activity activity;

    public MojiOglasiAdapter(Context context, ArrayList<String> naslovi, ArrayList<String> adrese, ArrayList<Double> cene, ArrayList<Double> povrsine, ArrayList<Integer> namene,ArrayList<Integer> id_niz, Activity activity,ArrayList<byte[]> slike) {
        this.context = context;
        this.naslovi = naslovi;
        this.adrese = adrese;
        this.cene = cene;
        this.povrsine = povrsine;
        this.namene = namene;
        this.id_niz = id_niz;
        this.activity = activity;
        this.slike = slike;
    }

    @NonNull
    @Override
    public MojiOglasiAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.mojioglasired, parent, false);

        return new MojiOglasiAdapter.MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MojiOglasiAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.naslov.setText(String.valueOf(naslovi.get(position)));
        holder.cena.setText(String.valueOf(cene.get(position)) + " €");
        if(namene.get(position)==2) holder.mesecno.setVisibility(View.VISIBLE);
        else holder.mesecno.setVisibility(View.INVISIBLE);
        holder.povrsina.setText(String.valueOf(povrsine.get(position)) + " m2");
        if(slike.get(position)!=null)
        {
            Bitmap bm = BitmapFactory.decodeByteArray(slike.get(position), 0 ,slike.get(position).length);
            holder.slika.setImageBitmap(bm);
        }

        if(namene.get(position)==1)
        {
            holder.namena.setText("PRODAJA");
        }
        else
        {
            holder.namena.setText("IZDAVANJE");
        }
        holder.adresa.setText(String.valueOf(adrese.get(position)));

        holder.izmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(v.getContext());
                Cursor cursor = db.prikaziOglas(id_niz.get(position));
                cursor.moveToFirst();
                Intent i = new Intent(v.getContext(),IzmeniOglas.class);
              //  Log.d("PROVERA", "onClick: " + cursor.getInt(2));
                i.putExtra("idNekretnine",id_niz.get(position));
             //   i.putExtra("idKorisnika",String.valueOf(cursor.getInt(1)));
                i.putExtra("tip", cursor.getInt(2));
                i.putExtra("namena",cursor.getInt(3));
                i.putExtra("naziv", cursor.getString(4));
                i.putExtra("adresa",cursor.getString(5));
                i.putExtra("grad",cursor.getString(6));
                i.putExtra("povrsina",cursor.getDouble(7));
                i.putExtra("brojSoba",cursor.getInt(8));
                i.putExtra("namestenost",cursor.getString(9));
                i.putExtra("infrastruktura",cursor.getString(10));
                i.putExtra("cena",cursor.getDouble(11));
                i.putExtra("opis",cursor.getString(13));


                v.getContext().startActivity(i);
            }
        });

        holder.obrisi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(v.getContext()).setTitle("Upozorenje")
                        .setMessage("Da li ste sigurni da želite da obrišete oglas?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("Da", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                DatabaseHelper db = new DatabaseHelper(v.getContext());
                                if (db.obrisiOglas(id_niz.get(position))) {
                                    Intent intent = new Intent(v.getContext(), MojiOglasi.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    v.getContext().startActivity(intent);

//
                                } else {
                                    Toast.makeText(v.getContext(), "Nemoguća operacija", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }).setNegativeButton("Ne", null).show();

            }
        });

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(),PrikaziOglas.class);
                intent.putExtra("idOglasa",id_niz.get(position));
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return naslovi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView naslov,cena,povrsina,namena,adresa,mesecno;
        Button obrisi,izmeni;
        ImageView slika;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            naslov = itemView.findViewById(R.id.tvNaslov);
            cena = itemView.findViewById(R.id.tvCena);
            povrsina = itemView.findViewById(R.id.tvPovrsina);
            namena = itemView.findViewById(R.id.tvNamena);
            adresa = itemView.findViewById(R.id.tvAdresa);
            slika = itemView.findViewById(R.id.ivHouse);
            obrisi = itemView.findViewById(R.id.btnObrisi);
            izmeni = itemView.findViewById(R.id.btnIzmeni);
            mesecno = itemView.findViewById(R.id.tvMesecno);
            mainLayout = itemView.findViewById(R.id.mainLayout);


        }
    }
}
