package com.example.pmaprojekat;

public class Korisnik {
    private int idKorisnika;
    private String ime, prezime, brojTelefona, email, sifra;

    public Korisnik( String ime, String prezime, String brojTelefona, String email, String sifra) {
        //this.idKorisnika = idKorisnika;
        this.ime = ime;
        this.prezime = prezime;
        this.brojTelefona = brojTelefona;
        this.email = email;
        this.sifra = sifra;
    }

    public int getIdKorisnika() {
        return idKorisnika;
    }

    public String getIme() {
        return ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public String getBrojTelefona() {
        return brojTelefona;
    }

    public String getEmail() {
        return email;
    }

    public String getSifra() {
        return sifra;
    }
}

