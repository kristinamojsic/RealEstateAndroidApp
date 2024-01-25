package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IzmeniOglas extends AppCompatActivity {

    EditText naziv,adresa,grad,povrsina,brojSoba,infrastruktura,cena,opis;
    int namestenost,tip,namena;
    RadioButton namestenostDa,namestenostNe;
    RadioButton tipKuca,tipStan,namenaProdaja,namenaIzdavanje;


    //boolean namestenost;
    ImageSwitcher imageSwitcher;
    ViewPager slika;
    Button izmeni,dodajSlike,prethodna,sledeca,ukloni;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    DatabaseHelper db;

    ArrayList<Uri> mArrayUri;
    ArrayList<byte[]> slike;
    ArrayList<Bitmap> bitmaps;
    ArrayList<byte[]> nizSlikaZaUbacivanje;

    int position = 0;
    List<String> imagesEncodedList;
    int idNekretnine;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izmeni_oglas);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Izmena oglasa");
        naziv = findViewById(R.id.etNaziv);
        adresa = findViewById(R.id.etAdresa);
        grad = findViewById(R.id.etGrad);
        povrsina = findViewById(R.id.etPovrsina);
        brojSoba = findViewById(R.id.etbrojSoba);
        infrastruktura = findViewById(R.id.etInfrastruktura);
        namestenostDa = findViewById(R.id.radioDa);
        namestenostNe = findViewById(R.id.radioNe);
        tipKuca = findViewById(R.id.izmeniTipKuce);
        tipStan = findViewById(R.id.izmeniTipStan);
        namenaProdaja = findViewById(R.id.izmeniProdaja);
        namenaIzdavanje = findViewById(R.id.izmeniIzdavanje);
        cena = findViewById(R.id.etCena);
        opis = findViewById(R.id.etOpis);
        imageSwitcher = findViewById(R.id.imageSwitcher);
        slika = findViewById(R.id.viewPager);
        izmeni = findViewById(R.id.Izmeni);
        ukloni = findViewById(R.id.btnObrisiSveSlike);
        dodajSlike = findViewById(R.id.btnDodajSlike);
        prethodna = findViewById(R.id.prethodna);
        sledeca = findViewById(R.id.sledeca);
        mArrayUri = new ArrayList<>();
        slike = new ArrayList<>();
        bitmaps = new ArrayList<>();
        nizSlikaZaUbacivanje = new ArrayList<>();
        db = new DatabaseHelper(IzmeniOglas.this);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            idNekretnine = extras.getInt("idNekretnine");
            Cursor oglas = db.prikaziOglas(idNekretnine);
            naziv.setText(extras.getString("naziv"));
            adresa.setText(extras.getString("adresa"));
            grad.setText(extras.getString("grad"));
            povrsina.setText(String.valueOf(extras.getDouble("povrsina")));
            brojSoba.setText(String.valueOf(extras.getInt("brojSoba")));
            namestenost = Integer.parseInt(extras.getString("namestenost"));
            tip = extras.getInt("tip");
            namena = extras.getInt("namena");
            /*if(namestenost==1)
                namestenostDa.setChecked(true);
            else
                namestenostNe.setChecked(true);
            if(tip==1)*/
            namestenostDa.setChecked(namestenost==1);
            namestenostNe.setChecked(namestenost==0);
            tipKuca.setChecked(tip==1);
            tipStan.setChecked(tip==2);
            namenaProdaja.setChecked(namena==1);
            namenaIzdavanje.setChecked(namena==2);
            //Log.d("PROVERA", "onCreate: " + extras.getString("namestenost"));
            infrastruktura.setText(extras.getString("infrastruktura"));
            cena.setText(String.valueOf(extras.getDouble("cena")));

            opis.setText(extras.getString("opis"));

            if(oglas.moveToFirst())
            {
                do {
                    slike.add(oglas.getBlob(16));
                }
                while(oglas.moveToNext());
            }



        }
        ImageAdapter imageAdapter = new ImageAdapter(IzmeniOglas.this,slike);
        slika.setAdapter(imageAdapter);

        imageSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView imageView1 = new ImageView(getApplicationContext());
                return imageView1;
            }
        });

        sledeca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position < mArrayUri.size() - 1) {
                    // increase the position by 1
                    position++;
                    imageSwitcher.setImageURI(mArrayUri.get(position));
                } else {
                    Toast.makeText(IzmeniOglas.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
                }
            }

        });
        prethodna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (position > 0) {
                    // decrease the position by 1
                    position--;
                    imageSwitcher.setImageURI(mArrayUri.get(position));
                }

            }
        });

        dodajSlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // initialising intent
                Intent intent = new Intent();

                // setting type to select to be image
                intent.setType("image/*");

                // allowing multiple image to be selected
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //startActivityForResult(intent,1);
                //resultLauncher.launch(intent);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_MULTIPLE);
            }
        });
        izmeni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("tina", "onClick: " + nizSlikaZaUbacivanje.size());
                if(db.izmeniOglas(idNekretnine,tip,namena,naziv.getText().toString().trim(),adresa.getText().toString().trim(),grad.getText().toString().trim(),
                        Double.parseDouble(povrsina.getText().toString().trim()),Integer.parseInt(brojSoba.getText().toString().trim()),
                        namestenost,infrastruktura.getText().toString().trim(),Double.parseDouble(cena.getText().toString().trim()),
                        opis.getText().toString().trim()))
                {

                    if(nizSlikaZaUbacivanje.size()!=0)
                    {
                        if(db.ubaciSlike(nizSlikaZaUbacivanje,idNekretnine))
                        {
                            new AlertDialog.Builder(IzmeniOglas.this)
                                    .setTitle("Uspešno")
                                    .setIcon(R.drawable.alert_check)
                                    .setMessage("Uspešno ste izmenili podatke!")
                                    .show();
                        }
                        else
                        {
                            Toast.makeText(IzmeniOglas.this, "Neke slike nisu dodate!", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        new AlertDialog.Builder(IzmeniOglas.this)
                                .setTitle("Uspešno")
                                .setIcon(R.drawable.alert_check)
                                .setMessage("Uspešno ste izmenili podatke!")
                                .show();
                    }

                }
                else
                {
                    Toast.makeText(IzmeniOglas.this, "neuspesno", Toast.LENGTH_SHORT).show();
                }

            }
        });
        ukloni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(IzmeniOglas.this).setMessage("Da li želite da uklonite sve slike?").setPositiveButton("Da", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!db.obrisiSlike(idNekretnine))
                        {
                            Toast.makeText(IzmeniOglas.this, "Nemoguće", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).setNegativeButton("Ne", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_MULTIPLE && resultCode == RESULT_OK && null != data) {

            // Get the Image from data
            if (data.getClipData() != null) {


                ClipData mClipData = data.getClipData();
                int cout = data.getClipData().getItemCount();
                Log.d("tina", "onActivityResult: cout" + cout);
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mArrayUri.add(imageurl);
                    try {
                        Bitmap bitmap = MediaStore.Images.Media.getBitmap((getApplicationContext().getContentResolver()),imageurl);
                        bitmaps.add(bitmap);
                        Log.d("tina", "onActivityResult:urisize " + mArrayUri.size());
                        Log.d("tina", "onActivityResult:bitmapssize " + bitmaps.size());
                    } catch (IOException e) {
                        e.printStackTrace();
                        Log.d("tina", "onActivityResult: " + e);
                    }

                }
                // setting 1st selected image into image switcher
                imageSwitcher.setImageURI(mArrayUri.get(0));
                position = 0;
            } else {
                Uri imageurl = data.getData();
                mArrayUri.add(imageurl);
                imageSwitcher.setImageURI(mArrayUri.get(0));
                position = 0;
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap((getApplicationContext().getContentResolver()),imageurl);
                    bitmaps.add(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        } else {
            // show this if no image is selected
            Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
        }
        nizSlikaZaUbacivanje = getBytesFromBitmap(bitmaps);
    }


    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioDa:
                if (checked)
                    namestenost = 1;
                break;
            case R.id.radioNe:
                if (checked)
                    namestenost = 0;
                break;
        }
    }
    public void onRadioButtonTipClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.izmeniTipKuce:
                if (checked)
                    tip = 1;
                break;
            case R.id.izmeniTipStan:
                if (checked)
                    tip = 2;
                break;
        }
    }
    public void onRadioButtonNamenaClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.izmeniProdaja:
                if (checked)
                    namena = 1;
                break;
            case R.id.izmeniIzdavanje:
                if (checked)
                    namena = 2;
                break;
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
    public static ArrayList<byte[]> getBytesFromBitmap(ArrayList<Bitmap> bitmaps) {
        ArrayList<byte[]> blobovi=new ArrayList<>();
        for (int i = 0;i<bitmaps.size();i++)
        {
            if (bitmaps.get(i)!=null) {
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmaps.get(i).compress(Bitmap.CompressFormat.JPEG, 70, stream);
                blobovi.add(stream.toByteArray());
            }
        }
        return blobovi;
    }
}