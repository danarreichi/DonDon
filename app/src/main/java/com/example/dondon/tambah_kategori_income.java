package com.example.dondon;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class tambah_kategori_income extends AppCompatActivity {

    EditText edit_text_kategori;
    Button button_tambah_kategori;
    DataHelper dbcenter;
    int temp_ID = 0;
    Cursor cursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_kategori_income);

        edit_text_kategori = findViewById(R.id.edit_text_kategori);
        button_tambah_kategori = findViewById(R.id.button_tambah_kategori);

        dbcenter = new DataHelper(this);

        button_tambah_kategori.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit_text_kategori.getText().toString().matches("")) {
                    Toast.makeText(getApplicationContext(), "Isian Kategori Tidak Boleh Kosong",
                            Toast.LENGTH_SHORT).show();
                } else {
                    SQLiteDatabase db = dbcenter.getReadableDatabase();
                    cursor = db.rawQuery("SELECT max(ID_KATEGORI) FROM MASTER_KATEGORI", null);
                    for (int cc = 0; cc < cursor.getCount(); cc++) {
                        cursor.moveToPosition(cc);
                        temp_ID = cursor.getInt(0);
                    }
                    temp_ID = temp_ID + 1;
                    db = dbcenter.getWritableDatabase();
                    db.execSQL("INSERT INTO MASTER_KATEGORI VALUES(" + temp_ID + ", '" + edit_text_kategori.getText().toString() + "', 'masuk')");
                    finish();
                }
            }
        });

    }
}