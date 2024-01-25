package com.example.pmaprojekat;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {
    Context context;
    ArrayList<String> naslovi,adrese;
    ArrayList<Double> cene, povrsine;
    ArrayList<Integer> namene,id_niz;
    ArrayList<byte[]> slike;

    Activity activity;
    private final static String SQUARE = "&#xB2;";
    public CustomAdapter(Context context, ArrayList<String> naslovi, ArrayList<Integer> namene, ArrayList<String> adrese, ArrayList<Double> cene, ArrayList<Double> povrsine, Activity activity,ArrayList<Integer> id_niz,ArrayList<byte[]> slike) {
        this.context = context;
        this.naslovi = naslovi;
        this.namene = namene;
        this.adrese = adrese;
        this.cene = cene;
        this.povrsine = povrsine;
        this.activity = activity;
        this.id_niz = id_niz;
        this.slike = slike;
    }

    @NonNull
    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_house, parent, false);

        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull CustomAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.naslov.setText(String.valueOf(naslovi.get(position)));
        holder.cena.setText(String.valueOf(cene.get(position)) + "€");
        if(namene.get(position)==2) holder.mesecno.setVisibility(View.VISIBLE);
        else holder.mesecno.setVisibility(View.INVISIBLE);

        holder.povrsina.setText(String.valueOf(povrsine.get(position)) + "m2");
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
            mesecno = itemView.findViewById(R.id.tvMesecno);
            mainLayout = itemView.findViewById(R.id.mainLayout);

        }
    }
}
