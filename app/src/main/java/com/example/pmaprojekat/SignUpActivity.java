package com.example.pmaprojekat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class SignUpActivity extends AppCompatActivity {

    EditText etIme, etPrezime, etEmail, etSifra, etPotvrdiSifru, etTelefon;
    Button btnRegistracija;
    TextView tvPrijava;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Registracija");
        etIme = findViewById(R.id.etIme);
        etPrezime = findViewById(R.id.etPrezime);
        etEmail = findViewById(R.id.editTextEmail);
        etSifra = findViewById(R.id.editTextSifra);
        etPotvrdiSifru = findViewById(R.id.etPotvrdiSifru);
        etTelefon = findViewById(R.id.etTelefon);
        btnRegistracija = findViewById(R.id.btnRegistracija);
        tvPrijava = findViewById(R.id.tvPrijava);

        btnRegistracija.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ime,prezime,email,sifra,potvrdiSifru,telefon;
                ime = etIme.getText().toString().trim();
                prezime = etPrezime.getText().toString().trim();
                email = etEmail.getText().toString().trim();
                sifra = etSifra.getText().toString().trim();
                potvrdiSifru = etPotvrdiSifru.getText().toString().trim();
                telefon = etTelefon.getText().toString().trim();

                if (ime.isEmpty() || email.isEmpty() || email.isEmpty() || sifra.isEmpty() || potvrdiSifru.isEmpty() || telefon.isEmpty()) {

                    Toast.makeText(SignUpActivity.this, "Morate popuniti sva polja", Toast.LENGTH_SHORT).show();}
                else if(!sifra.equals(potvrdiSifru))
                {
                    Toast.makeText(SignUpActivity.this,"Šifre se ne poklapaju!",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Korisnik korisnik = new Korisnik(ime,prezime,telefon,email,sifra);

                    DatabaseHelper db = new DatabaseHelper(SignUpActivity.this);
                    if(db.dodajKorisnika(korisnik))
                    {
                        Intent i = new Intent(SignUpActivity.this,MainActivity.class);
                        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK );
                        startActivity(i);
                    }
                    else
                    {
                        Toast.makeText(SignUpActivity.this,"Neuspešna registracija",Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

        tvPrijava.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignUpActivity.this,LoginActivity.class);
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
}