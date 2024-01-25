package com.example.pmaprojekat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String db_name = "nekretnine.db";
    private static final String table_korisnik = "korisnik";
    private static final String table_nekretnina = "nekretnina";
    private static final String table_nekretnina_slike = "nekretnina_slike";
    private Context context;

    public DatabaseHelper(@Nullable Context context)
    {
        super(context, db_name, null, 5);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_korisnik = "CREATE TABLE " + table_korisnik + "( idKorisnika INTEGER PRIMARY KEY AUTOINCREMENT," +
                "ime TEXT, prezime TEXT, email TEXT, broj TEXT, sifra TEXT)";
        String create_nekretnina = "CREATE TABLE " + table_nekretnina + "( idNekretnine INTEGER PRIMARY KEY AUTOINCREMENT," +
                "idKorisnika INTEGER, tip INTEGER, namena INTEGER, naziv TEXT, adresa TEXT, grad TEXT, povrsina DOUBLE, brojSoba INTEGER, namestenost BOOLEAN, infrastruktura TEXT, cena DOUBLE, slika BLOB, opis TEXT)";
        String create_nekretnina_slike = "CREATE TABLE " + table_nekretnina_slike + "( id_ INTEGER PRIMARY KEY AUTOINCREMENT, idNekretnine INTEGER, " +
                "slika BLOB)";
        db.execSQL(create_korisnik);
        db.execSQL(create_nekretnina);
        db.execSQL(create_nekretnina_slike);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String drop_korisnik = "DROP TABLE IF EXISTS " + table_korisnik;
        String drop_nekretnina = "DROP TABLE IF EXISTS " + table_nekretnina;
        String drop_nekretninaslika = "DROP TABLE IF EXISTS " + table_nekretnina_slike;
        db.execSQL(drop_korisnik);
        db.execSQL(drop_nekretnina);
        db.execSQL(drop_nekretninaslika);
        onCreate(db);
    }

    public boolean dodajKorisnika(Korisnik korisnik) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put("ime", korisnik.getIme());
        cv.put("prezime", korisnik.getPrezime());
        cv.put("email", korisnik.getEmail());
        cv.put("broj", korisnik.getBrojTelefona());
        cv.put("sifra", korisnik.getSifra());
        long insert = db.insert(table_korisnik, null, cv);
        db.close();
        return (insert != -1);


    }

    public Cursor proveriKorisnika(String email, String sifra) {

        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_korisnik + " WHERE email = ? AND sifra = ?";
        Cursor cursor = db.rawQuery(sql, new String[]{email, sifra});
        return cursor;

    }

    public Cursor uzmiPodatkeOKorisniku(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_korisnik + " WHERE idKorisnika = ? ";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(id)});
        return cursor;
    }

    public boolean izmeniKorisnika(int id, String email, String ime, String prezime, String telefon) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("ime", ime);
        contentValues.put("prezime", prezime);
        contentValues.put("email", email);
        contentValues.put("broj", telefon);

        long res = db.update(table_korisnik, contentValues, "idKorisnika=?", new String[]{String.valueOf(id)});
        return (res != -1);

    }

    public boolean obrisiKorisnika(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table_korisnik, "idKorisnika=?", new String[]{String.valueOf(id)});

        if (res != -1)
        {
            long res2 = db.delete(table_nekretnina,"idKorisnika=?",new String[]{String.valueOf(id)});
            return (res2!=-1);
        }
        else return false;

    }

    public boolean postaviOglas(int idKorisnika, int tip, int namena, String naziv, String adresa, String grad, double povrsina, int brojSoba, boolean namestenost, String infrastruktura, double cena, String opis) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("idKorisnika", idKorisnika);
        cv.put("tip", tip);
        cv.put("namena", namena);
        cv.put("naziv", naziv);
        cv.put("adresa", adresa);
        cv.put("grad", grad);
        cv.put("povrsina", povrsina);
        cv.put("brojSoba", brojSoba);
        cv.put("infrastruktura", infrastruktura);
        cv.put("namestenost", namestenost);
        cv.put("cena", cena);
        cv.put("opis", opis);
        long res = db.insert(table_nekretnina, null, cv);
        return (res != -1);
    }

    public Cursor prikaziSveOglase() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public Cursor prikaziOglaseNamena(int namena)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE namena=?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(namena)});
        return cursor;
    }

    public Cursor prikaziOglaseTip(int tip)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE tip=?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(tip)});
        return cursor;
    }
    public Cursor prikaziOglaseGrad(String grad)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE grad LIKE ?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql, new String[]{grad});
        return cursor;
    }


    public Cursor prikaziOglaseTipNamena(int tip, int namena)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE tip=? AND namena=?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(tip),String.valueOf(namena)});
        return cursor;
    }


    public Cursor prikaziOglaseTipGrad(int tip, String grad)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE tip=? AND grad LIKE ?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(tip),grad});
        return cursor;
    }

    public Cursor prikaziOglaseNamenaGrad(int namena, String grad)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE namena=? AND grad LIKE ?" + " GROUP BY n.idNekretnine";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(namena),grad});
        return cursor;
    }

    public Cursor prikaziOglaseTipNamenaGrad(int tip,int namena, String grad)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE tip=? AND namena=? AND grad LIKE ?" + " GROUP BY n.idNekretnine ORDER BY idNekretnine DESC";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(tip),String.valueOf(namena),grad});
        return cursor;
    }

    public Cursor prikaziMojeOglase(int idKorisnika)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE idKorisnika=?" + " GROUP BY n.idNekretnine ORDER BY n.idNekretnine DESC";
        //String sql = "SELECT * FROM " + table_nekretnina + " WHERE idKorisnika=?";
        Cursor cursor = db.rawQuery(sql, new String[]{String.valueOf(idKorisnika)});
        return cursor;
    }



    public boolean obrisiOglas(int idNekretnine) {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table_nekretnina, "idNekretnine=?", new String[]{String.valueOf(idNekretnine)});
        return (res != -1);
    }

    public Cursor prikaziOglas(int idNekretnine)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_nekretnina + " n LEFT JOIN " + table_nekretnina_slike + " ns ON n.idNekretnine = ns.idNekretnine" + " WHERE n.idNekretnine=?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idNekretnine)});
        return cursor;

    }

    public boolean izmeniOglas(int idNekretnine, int tip, int namena, String naziv, String adresa, String grad, Double povrsina, int brojSoba, int namestenost, String infrastruktura, double cena, String opis)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put("tip", tip);
        contentValues.put("namena", namena);
        contentValues.put("naziv", naziv);
        contentValues.put("adresa", adresa);
        contentValues.put("grad", grad);
        contentValues.put("povrsina", povrsina);
        contentValues.put("brojSoba", brojSoba);
        contentValues.put("namestenost", namestenost);
        contentValues.put("infrastruktura", infrastruktura);
        contentValues.put("cena", cena);
        contentValues.put("opis", opis);

        long res = db.update(table_nekretnina, contentValues, "idNekretnine=?", new String[]{String.valueOf(idNekretnine)});
        return (res != -1);
    }

    public Cursor vlasnikOglasa(int idOglasa)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + table_korisnik + " k INNER JOIN " + table_nekretnina + " n ON k.idKorisnika = n.idKorisnika WHERE idNekretnine=?";
        Cursor cursor = db.rawQuery(sql,new String[]{String.valueOf(idOglasa)});
        return cursor;
    }

    public Cursor lastID()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT idNekretnine FROM " + table_nekretnina;
        Cursor cursor = db.rawQuery(sql,null,null);
        cursor.moveToLast();
        return cursor;
    }

    public boolean ubaciSlike(ArrayList<byte[]> slike, int idNekretnine)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        boolean greska = false;
        for(int i = 0;i<slike.size();i++)
        {
            cv.put("idNekretnine",idNekretnine);
            cv.put("slika",slike.get(i));
            long res = db.insert(table_nekretnina_slike,null,cv);
            if(res==-1) greska = true;
        }
        return (!greska);
    }

    public boolean obrisiSlike(int idNekretnine)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        long res = db.delete(table_nekretnina_slike,"idNekretnine=?",new String[]{String.valueOf(idNekretnine)});
        return (res!=-1);
    }

    public boolean promeniSifru(int idKorisnika,String novaSifra)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("sifra", novaSifra);
        long res = db.update(table_korisnik,cv,"idKorisnika=?",new String[]{String.valueOf(idKorisnika)});
        return (res!=-1);
    }



}
