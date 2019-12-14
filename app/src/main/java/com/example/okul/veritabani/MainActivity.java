package com.example.okul.veritabani;

import android.content.ContentValues;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {
    EditText txtMarka, txtKategori, txtResim;
    Button btnEkle, btnListele;
    ListView lst;
    String KayitAdi;
    ArrayList<String> kayitlar;
    int kayitId;

    @Override
    protected void onResume() {
        super.onResume();
        KayitAdi = "Ekle";
        kayitId = 0;
        btnEkle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDbHelper dbHelper = MyDbHelper.VtOrnekGetir(MainActivity.this);
                String marka = txtMarka.getText().toString();
                String kategori = txtKategori.getText().toString();
                int resim = Integer.parseInt((txtResim.getText().toString()));
                if (kayitId > 0) {//deðiþtir yapacak
                    int snc = dbHelper.Guncelle(kayitId, marka, kategori, resim);
                    if (snc > 0) {
                        Toast.makeText(MainActivity.this, "Kayýt Güncellendi ID:" + snc, Toast.LENGTH_SHORT).show();
                        txtMarka.setText("");
                        txtKategori.setText("");
                        txtResim.setText("");
                        kayitId = 0;
                        btnEkle.setText("Ekle");
                        btnListele.setText("Listele");
                        KayitDoldur();

                    } else {
                        Toast.makeText(MainActivity.this, "Kayýt Güncellenemedi:" + snc, Toast.LENGTH_SHORT).show();
                    }


                } else {//kayýt ekle yapýcak
                    long sonuc = dbHelper.KayitEkle(marka, kategori, resim);
                    if (sonuc > 0) {
                        Toast.makeText(MainActivity.this, "Kayýt Eklendi ID:" + sonuc, Toast.LENGTH_SHORT).show();
                        txtMarka.setText("");
                        txtKategori.setText("");
                        txtResim.setText("");
                        KayitDoldur();
                    } else {
                        Toast.makeText(MainActivity.this, "Kayýt eklenmedi:" + sonuc, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        btnListele.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (kayitId > 0) {//deðiþtir yapacak
                    int snc = MyDbHelper.VtOrnekGetir(MainActivity.this).Sil(kayitId);
                    if (snc > 0) {
                        Toast.makeText(MainActivity.this, "Kayýt Silindi ID:" + snc, Toast.LENGTH_SHORT).show();
                        txtMarka.setText("");
                        txtKategori.setText("");
                        txtResim.setText("");
                        kayitId = 0;
                        btnEkle.setText("Ekle");
                        btnListele.setText("Listele");
                    }
                }
                KayitDoldur();
            }
        });
        lst.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                KayitAdi = kayitlar.get(position);
                MyDbHelper dbHelper = MyDbHelper.VtOrnekGetir(MainActivity.this);
                Cursor cr = dbHelper.KayitGetir(KayitAdi);
                if (cr != null) {
                    cr.moveToFirst();
                    String markaa = cr.getString(cr.getColumnIndex(MyDbHelper.MARKA));
                    String kategorii = cr.getString(cr.getColumnIndex(MyDbHelper.KATEGORI));
                    int resimm = cr.getInt(cr.getColumnIndex(MyDbHelper.RESIM));
                    kayitId = cr.getInt(cr.getColumnIndex(MyDbHelper._ID));
                    txtMarka.setText(markaa);
                    txtKategori.setText(kategorii);
                    txtResim.setText(String.valueOf(resimm));
                    btnEkle.setText("Güncelle");
                    btnListele.setText("Sil");

                }
            }
        });

        /*
       ContentValues cv=new ContentValues();
        cv.put(MyDbHelper.MARKA,"Fort");
        cv.put(MyDbHelper.KATEGORI, "oto");
        cv.put(MyDbHelper.RESIM, 1);
        long sonuc=MyDbHelper.VtOrnekGetir(this).KayitEkle(cv);
        if(sonuc>0){
            Toast.makeText(this,"Kayýt Eklendi ID:"+sonuc,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Kayýt eklenmedi:"+sonuc,Toast.LENGTH_SHORT).show();
        }*/

    }

    private void KayitDoldur() {
        kayitlar = MyDbHelper.VtOrnekGetir(MainActivity.this).AraclarGetir();
        ArrayAdapter<String> adp = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_list_item_1, kayitlar);
        lst.setAdapter(adp);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtKategori = (EditText) findViewById(R.id.txtKategori);
        txtMarka = (EditText) findViewById(R.id.txtMarka);
        txtResim = (EditText) findViewById(R.id.txtResim);
        btnEkle = (Button) findViewById(R.id.btnEkle);
        btnListele = (Button) findViewById(R.id.btnListe);
        lst = (ListView) findViewById(R.id.lst);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
