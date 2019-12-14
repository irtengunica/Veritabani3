package com.example.okul.veritabani;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by okul on 10.9.2015.
 */
public class MyDbHelper extends SQLiteOpenHelper{

    public static  final String DATABASE_NAME="aksaray.db";
    public static  final int DB_VERSION=1;//veritaban�nda yap�sal de�i�iklik oldu�unda versiyonu art�r�p tekrar �al��t�r.
    public static  final String TBL_ADI="arabalar";
    public static  final String _ID="_id";
    public static  final String MARKA="marka";
    public static  final String KATEGORI="kategori";
    public static  final String RESIM="resim";
    private Context ctx;
    static MyDbHelper dbOrnegi;//static metod i�erisinde static olmayan di�ardaki bir de�i�kene ula�amazs�n�z.
    SQLiteDatabase vt;

    public static MyDbHelper VtOrnekGetir(Context ctx){
        if (dbOrnegi==null)
        {
            dbOrnegi=new MyDbHelper(ctx);
        }
        return dbOrnegi;
    }

    public MyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DB_VERSION);
        this.ctx=context;

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create Table "+TBL_ADI+ " ( "+_ID+" integer primary key autoincrement, "+
        MARKA+" text, "+KATEGORI+" text, "+RESIM+" integer);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//mevcut yedek al�n�r
        db.execSQL("Drop Table If exists "+TBL_ADI+";");
        onCreate(db);
        //yedekten veriler tekrar y�klenir.
    }
    public long KayitEkle(String marka, String kategori, int resim ){
        long id;
        vt=getWritableDatabase();//yazmak i�in veritaban�n� a�
        //vt.execSQL("insert..."); direk yaz�labilir.
        ContentValues cv=new ContentValues();
        cv.put(MARKA,marka);
        cv.put(KATEGORI,kategori);
        cv.put(RESIM,resim);
        id=vt.insert(TBL_ADI,null,cv);
        vt.close();
        return id;
    }
    public long KayitEkle(ContentValues cv){
        long id;
        vt=getWritableDatabase();//yazmak i�in veritaban�n� a�
        //vt.execSQL("insert..."); direk yaz�labilir.
        id=vt.insert(TBL_ADI,null,cv);
        vt.close();
        return id;
    }
    public ArrayList<String> AraclarGetir(){
        ArrayList<String> lstArac=null;//adres tutar
        vt=getReadableDatabase();
        Cursor cr; //veritaban�ndaki kay�tlar� tutar
        String sql="Select * from "+TBL_ADI;
        cr=vt.rawQuery(sql, null);
        if(cr!=null){
            lstArac=new ArrayList<String>();//y���n� olu�turur adrese atar
            while (cr.moveToNext()){
                lstArac.add(cr.getString(cr.getColumnIndex(MARKA)).toString());
            }

        }
        vt.close();
        return lstArac;
    }

    public Cursor KayitGetir(String kayitAdi) {
        vt=getReadableDatabase();
        String sql="Select * from "+TBL_ADI+" where "+MARKA+"='"+kayitAdi+"' ;";
        Cursor cr=vt.rawQuery(sql,null);
        /*2. yol
        String[] Kosullar=new  String[] {kayitAdi,"oto"};
        String[] Kolonlar=new String[]{_ID,MARKA,KATEGORI,RESIM};
        Cursor cr=vt.query(TBL_ADI,Kolonlar," "+ MARKA +" =? and "+KATEGORI+" =? ",Kosullar,null,null,null);
        */
        return cr;
    }

    public int Guncelle(int kayitId, String marka, String kategori, int resim) {
        vt=getWritableDatabase();
        ContentValues cv=new ContentValues();
        cv.put(MARKA,marka);
        cv.put(KATEGORI, kategori);
        cv.put(RESIM, resim);
        cv.put(_ID, kayitId);
        String sorgu=_ID+ "=?";
        String[] params=new String[]{String.valueOf(kayitId)};
        int sonuc=vt.update(TBL_ADI, cv, sorgu, params);
        vt.close();
        return sonuc;
    }

    public int Sil(int kayitId) {
        vt=getWritableDatabase();
        String sorgu=_ID+ "=?";
        String[] params=new String[]{String.valueOf(kayitId)};
        int sonuc=vt.delete(TBL_ADI,sorgu,params);
        vt.close();
        return sonuc;
    }
}
