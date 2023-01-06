package com.example.dondon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
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

public class tambah_transaksi_income extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    TextView datePicker;
    EditText keterangan, uang;
    Spinner spinner_data_kategori;
    Cursor cursor;
    String[] jenis;
    Button button_tambah_income, pilih_tanggal;
    DataHelper dbcenter;
    int temp_ID;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_income);
        datePicker = findViewById(R.id.date_picker_income);
        keterangan = findViewById(R.id.keterangan);
        uang = findViewById(R.id.uang);
        spinner_data_kategori = findViewById(R.id.spinner_data_kategori);
        pilih_tanggal = findViewById(R.id.pilih_tanggal_income);
        button_tambah_income = findViewById(R.id.button_tambah_income);


        button_tambah_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tambah = spinner_data_kategori.getSelectedItem().toString();
                String tanggal_text = datePicker.getText().toString();
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
                    db = dbcenter.getReadableDatabase();
                    cursor = db.rawQuery("SELECT max(ID_TRANSAKSI) FROM TRANSAKSI", null);
                    for (int cc = 0; cc < cursor.getCount(); cc++) {
                        cursor.moveToPosition(cc);
                        temp_ID = cursor.getInt(0);
                    }
                    temp_ID = temp_ID + 1;
                    db = dbcenter.getWritableDatabase();
                    db.execSQL("INSERT INTO TRANSAKSI VALUES (" + temp_ID + ", " + id_kategori + ", '" + keterangan_text + "', " + uang_text + ", 'income', '" + tanggal_text + "')");
                    finish();
                }
            }
        });

        spinner_data_kategori.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String tambah = spinner_data_kategori.getSelectedItem().toString();
                if (tambah == "Tambah Kategori...") {
                    Intent pindah = new Intent(tambah_transaksi_income.this, tambah_kategori_income.class);
                    startActivity(pindah);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        pilih_tanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        refresh_data();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh_data();
    }

    private void refresh_data() {
        dbcenter = new DataHelper(this);
        SQLiteDatabase db = dbcenter.getReadableDatabase();
        cursor = db.rawQuery("SELECT * FROM MASTER_KATEGORI WHERE JENIS_KATEGORI = 'masuk'", null);
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
        datePicker.setText(date);
    }
}