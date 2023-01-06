package com.example.dondon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class edit_transaksi_expense extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    Spinner spinner_data_kategori;
    TextView date_picker;
    DataHelper dbcenter;
    Cursor cursor;
    Intent intent;
    String ID, kat_id;
    String[] jenis;
    Button pilih_tanggal, button_tambah;
    EditText keterangan, uang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_transaksi_expense);

        date_picker = findViewById(R.id.date_picker);
        keterangan = findViewById(R.id.keterangan);
        uang = findViewById(R.id.uang);
        spinner_data_kategori = findViewById(R.id.spinner_data_kategori);
        button_tambah = findViewById(R.id.button_tambah);
        pilih_tanggal = findViewById(R.id.pilih_tanggal);

        button_tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tambah = spinner_data_kategori.getSelectedItem().toString();
                String tanggal_text = date_picker.getText().toString();
                String keterangan_text = keterangan.getText().toString();

                if (tanggal_text.matches("") || keterangan_text.matches("") || uang.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Isian Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                } else {
                    int uang_text = Integer.parseInt(uang.getText().toString());
                    SQLiteDatabase db = dbcenter.getReadableDatabase();
                    cursor = db.rawQuery("SELECT ID_KATEGORI FROM MASTER_KATEGORI WHERE NAMA_KATEGORI = '" + tambah + "' ORDER BY ID_KATEGORI ASC LIMIT 1", null);
                    cursor.moveToPosition(0);
                    int id_kategori = cursor.getInt(0);
                    db = dbcenter.getWritableDatabase();
                    db.execSQL("UPDATE TRANSAKSI SET ID_KATEGORI = " + id_kategori + ", KETERANGAN = '" + keterangan_text + "', NOMINAL = " + uang_text + ", TANGGAL_TRANSAKSI = '" + tanggal_text + "' WHERE ID_TRANSAKSI = " + ID);
                    finish();
                }
            }
        });

        pilih_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

        spinner_data_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tambah = spinner_data_kategori.getSelectedItem().toString();
                if (tambah == "Tambah Kategori...") {
                    Intent pindah = new Intent(edit_transaksi_expense.this, tambah_kategori_expense.class);
                    startActivity(pindah);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        intent = getIntent();
        ID = intent.getStringExtra("ID");
        cursor = db.rawQuery("SELECT * FROM TRANSAKSI WHERE ID_TRANSAKSI = " + ID, null);
        cursor.moveToPosition(0);
        kat_id = cursor.getString(cursor.getColumnIndex("ID_KATEGORI"));
        date_picker.setText(cursor.getString(cursor.getColumnIndex("TANGGAL_TRANSAKSI")));
        keterangan.setText(cursor.getString(cursor.getColumnIndex("KETERANGAN")));
        uang.setText(cursor.getString(cursor.getColumnIndex("NOMINAL")));
        refresh_data();
    }

    private void refresh_data() {
        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM MASTER_KATEGORI WHERE JENIS_KATEGORI = 'keluar'", null);
        cursor.moveToFirst();
        jenis = new String[cursor.getCount() + 1];
        for (int cc = 0; cc < cursor.getCount(); cc++) {
            cursor.moveToPosition(cc);
            jenis[cc] = cursor.getString(1);
        }
        jenis[jenis.length - 1] = "Tambah Kategori...";

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, jenis);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_data_kategori.setAdapter(adapter);
        String compareValue = getKategoriNAMA(kat_id);
        System.out.println(compareValue);
        int spinnerPosition = adapter.getPosition(compareValue);
        spinner_data_kategori.setSelection(spinnerPosition);
    }

    public String getKategoriNAMA(String id) {
        String nama_generated;
        Cursor ss;
        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        ss = db.rawQuery("SELECT NAMA_KATEGORI FROM MASTER_KATEGORI WHERE ID_KATEGORI = " + id, null);
        ss.moveToPosition(0);
        nama_generated = ss.getString(0);
        return nama_generated;
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = year + "-" + (month + 1) + "-" + dayOfMonth;
        date_picker.setText(date);
    }
}