package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.menu.MenuBuilder;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
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
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    EditText naziv,adresa,grad,povrsina,brojSoba,infrastruktura,cena,opis;
    String naziv2,adresa2,grad2,povrsina2,brojSoba2,infrastruktura2,cena2;
    int tip,namena;
    boolean namestenost;
    ImageSwitcher imageSwitcher;
    ArrayList<byte[]> nizSlika;
    long slika;
    Button postavi,dodajSlike,prethodna,sledeca;
    int PICK_IMAGE_MULTIPLE = 1;
    String imageEncoded;
    //ActivityResultLauncher<Intent> resultLauncher;
    ArrayList<Uri> mArrayUri;
    ArrayList<Bitmap> bitmaps;
    int position = 0;
    List<String> imagesEncodedList;
    int idKorisnika;
    SessionManagement sessionManagement;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        setTitle("Dodavanje nekretnine");
        sessionManagement = new SessionManagement(AddActivity.this);
        naziv = findViewById(R.id.etNaziv);
        adresa = findViewById(R.id.etAdresa);
        grad = findViewById(R.id.etGrad);
        povrsina = findViewById(R.id.etPovrsina);
        brojSoba = findViewById(R.id.etbrojSoba);
        infrastruktura = findViewById(R.id.etInfrastruktura);
        cena = findViewById(R.id.etCena);

        opis = findViewById(R.id.etOpis);
        imageSwitcher = findViewById(R.id.imageSwitcher);
        postavi = findViewById(R.id.btnPostavi);
        dodajSlike = findViewById(R.id.btnDodajSlike);
        prethodna = findViewById(R.id.prethodna);
        sledeca = findViewById(R.id.sledeca);
        mArrayUri = new ArrayList<>();
        bitmaps = new ArrayList<>();
        nizSlika = new ArrayList<>();
        SessionManagement sessionManagement = new SessionManagement(AddActivity.this);
        idKorisnika = sessionManagement.getSession();
        if(idKorisnika==-1)
        {
            Intent intent = new Intent(AddActivity.this,LoginActivity.class);
            startActivity(intent);
        }
        /*resultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Log.d("tina", "onActivityResult: ");
                if ( result.getResultCode() == RESULT_OK && null != result.getData()) {
                    // Get the Image from data
                    if (result.getData().getClipData() != null) {
                        ClipData mClipData = result.getData().getClipData();
                        int cout = result.getData().getClipData().getItemCount();
                        for (int i = 0; i < cout; i++) {
                            // adding imageuri in array
                            Uri imageurl = result.getData().getClipData().getItemAt(i).getUri();
                            mArrayUri.add(imageurl);
                        }
                        // setting 1st selected image into image switcher
                        imageSwitcher.setImageURI(mArrayUri.get(0));
                        position = 0;
                    } else {
                        Uri imageurl = result.getData().getData();
                        mArrayUri.add(imageurl);
                        imageSwitcher.setImageURI(mArrayUri.get(0));
                        position = 0;
                    }
                } else {
                    // show this if no image is selected
                    //Toast.makeText(this, "You haven't picked Image", Toast.LENGTH_LONG).show();
                }
            }
        });*/
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
                    Toast.makeText(AddActivity.this, "Last Image Already Shown", Toast.LENGTH_SHORT).show();
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
        postavi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseHelper db = new DatabaseHelper(AddActivity.this);
                naziv2 = naziv.getText().toString().trim();
                adresa2 = adresa.getText().toString().trim();
                grad2 = grad.getText().toString().trim();
                povrsina2 = povrsina.getText().toString().trim();
                brojSoba2 = brojSoba.getText().toString().trim();
                cena2 = cena.getText().toString().trim();
                infrastruktura2 = infrastruktura.getText().toString().trim();
                Intent intent = new Intent(AddActivity.this,MojiOglasi.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                finish();

                if(naziv2.isEmpty() || adresa2.isEmpty() || grad2.isEmpty() || povrsina2.isEmpty() || brojSoba2.isEmpty() || cena2.isEmpty() || infrastruktura2.isEmpty() || tip == 0 || namena==0)
                {
                    Toast.makeText(AddActivity.this, "Niste popunili sva neophodna polja!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(db.postaviOglas(idKorisnika,tip,namena,naziv2,adresa2,grad2,Double.parseDouble(povrsina2),
                            Integer.parseInt(brojSoba2),namestenost,infrastruktura2, Double.parseDouble(cena2),
                            opis.getText().toString().trim()))
                    {
                        int lastID = db.lastID().getInt(0);
                        if(nizSlika.size()!=0)
                        {
                            if(db.ubaciSlike(nizSlika,lastID))
                            {
                                Toast.makeText(AddActivity.this, "Uspesno ste dodali oglas", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(AddActivity.this, "Neke slike nisu dodate!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(AddActivity.this, "Uspesno ste dodali oglas", Toast.LENGTH_SHORT).show();
                            startActivity(intent);
                        }

                    }
                    else
                    {
                        Toast.makeText(AddActivity.this, "Nemoguće postaviti oglas!", Toast.LENGTH_SHORT).show();
                    }
                }


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
        nizSlika = getBytesFromBitmap(bitmaps);
        Log.d("tina", "onActivityResult: " + nizSlika.size());

    }
    public void onRadioButtonClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.radioDa:
                if (checked)
                    namestenost = true;
                    break;
            case R.id.radioNe:
                if (checked)
                    namestenost = false;
                    break;
        }
    }

    public void TipClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.kuca:
                if (checked)
                    tip = 1;
                break;
            case R.id.stan:
                if (checked)
                    tip = 2;
                break;
        }
    }
    public void NamenaClicked(View view) {

        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.prodaja:
                if (checked)
                    namena = 1;
                break;
            case R.id.izdavanje:
                if (checked)
                    namena = 2;
                break;
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.user_menu,menu);
        MenuItem item = menu.findItem(R.id.itemDodajOglas);

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
                Intent pretraga = new Intent(AddActivity.this,MainActivity.class);
                pretraga.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                //pretraga.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(pretraga);
                finish();
                return true;
            case R.id.itemPodesavanja:
                Intent podesavanja = new Intent(AddActivity.this, UserProfile.class);
                podesavanja.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);

                startActivity(podesavanja);
                finish();
                return true;

            case R.id.itemMojiOglasi:
                Intent mojiOglasi = new Intent(AddActivity.this, MojiOglasi.class);
                mojiOglasi.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(mojiOglasi);
                finish();
                return true;
            case R.id.itemOdjaviSe:
                Intent odjavi = new Intent(AddActivity.this,Logout.class);
                startActivity(odjavi);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
