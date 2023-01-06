package com.example.dondon;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DataHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "data_transaksi.db";
    private static final int DATABASE_VERSION = 1;

    public DataHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub

        String sql_master_kategori = "CREATE TABLE MASTER_KATEGORI(ID_KATEGORI INTEGER PRIMARY KEY, NAMA_KATEGORI VARCHAR, JENIS_KATEGORI VARCHAR);";
        String sql_transaksi = "CREATE TABLE TRANSAKSI(ID_TRANSAKSI INTEGER PRIMARY KEY, ID_KATEGORI INTEGER, KETERANGAN VARCHAR, NOMINAL INTEGER, JENIS_TRANSAKSI VARCHAR, TANGGAL_TRANSAKSI DATE, FOREIGN KEY(ID_KATEGORI) REFERENCES MASTER_KATEGORI(ID_KATEGORI));";

        db.execSQL(sql_master_kategori);
        db.execSQL(sql_transaksi);
        String sql_insert_data_kat_masuk1 = "INSERT INTO MASTER_KATEGORI VALUES(1, 'Gaji', 'masuk');";
        String sql_insert_data_kat_masuk2 = "INSERT INTO MASTER_KATEGORI VALUES(2, 'Piutang', 'masuk');";
        String sql_insert_data_kat_keluar1 = "INSERT INTO MASTER_KATEGORI VALUES(3, 'Belanja', 'keluar');";
        db.execSQL(sql_insert_data_kat_masuk1);
        db.execSQL(sql_insert_data_kat_masuk2);
        db.execSQL(sql_insert_data_kat_keluar1);
    }

    public ArrayList<HashMap<String, String>> GetUsers() {
        SQLiteDatabase db = this.getReadableDatabase();
        ArrayList<HashMap<String, String>> transaksiList = new ArrayList<>();
        String query = "SELECT ID_TRANSAKSI, '('||M.NAMA_KATEGORI||')' AS NAMA_KATEGORI, T.KETERANGAN, T.NOMINAL, T.JENIS_TRANSAKSI, T.TANGGAL_TRANSAKSI FROM TRANSAKSI T JOIN MASTER_KATEGORI M ON T.ID_KATEGORI = M.ID_KATEGORI ORDER BY ID_TRANSAKSI ASC";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            HashMap<String, String> transaksi = new HashMap<>();
            transaksi.put("ID_TRANSAKSI", cursor.getString(cursor.getColumnIndex("ID_TRANSAKSI")));
            transaksi.put("KETERANGAN", cursor.getString(cursor.getColumnIndex("KETERANGAN")));
            transaksi.put("NAMA_KATEGORI", cursor.getString(cursor.getColumnIndex("NAMA_KATEGORI")));
            if (cursor.getString(cursor.getColumnIndex("JENIS_TRANSAKSI")).matches("income")) {
                transaksi.put("NOMINAL", "(+) Rp" + cursor.getString(cursor.getColumnIndex("NOMINAL")));
            } else {
                transaksi.put("NOMINAL", "(-) Rp" + cursor.getString(cursor.getColumnIndex("NOMINAL")));
            }
            transaksi.put("TANGGAL_TRANSAKSI", cursor.getString(cursor.getColumnIndex("TANGGAL_TRANSAKSI")));
            transaksiList.add(transaksi);
        }
        return transaksiList;
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

}
