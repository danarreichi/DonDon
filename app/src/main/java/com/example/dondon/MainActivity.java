package com.example.dondon;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    String[] daftar;
    protected Cursor cursor;
    ListView list_view_history;
    DataHelper dbcenter;
    ArrayList<HashMap<String, String>> transaksiList;
    Button button_income, button_expense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set variable
        list_view_history = findViewById(R.id.list_view_history);
        button_expense = findViewById(R.id.button_expense);
        button_income = findViewById(R.id.button_income);
        list_view_history = (ListView) findViewById(R.id.list_view_history);

        list_view_history.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selection = transaksiList.get(position).get("ID_TRANSAKSI");
//                System.out.println(selection);
                final CharSequence[] dialogitem = {"Edit Transaksi", "Hapus Transaksi"};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Pilihan");
                builder.setItems(dialogitem, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch (item) {
                            case 1:
                                SQLiteDatabase db = dbcenter.getWritableDatabase();
                                db.execSQL("DELETE FROM TRANSAKSI WHERE ID_TRANSAKSI = " + selection);
                                refreshList();
                                break;
                            case 0:
                                String get_type = transaksiList.get(position).get("NOMINAL").substring(1, 2);
                                if (get_type.matches("\\+")) {
                                    Intent pindah = new Intent(MainActivity.this, edit_transaksi_income.class);
                                    pindah.putExtra("ID", selection);
                                    startActivity(pindah);
                                } else if (get_type.matches("-")) {
                                    Intent pindah = new Intent(MainActivity.this, edit_transaksi_expense.class);
                                    pindah.putExtra("ID", selection);
                                    startActivity(pindah);
                                }
                        }
                    }
                });
                builder.create().show();
            }
        });

        button_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah;
            }
        });

        button_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(MainActivity.this, tambah_transaksi_income.class);
                startActivity(pindah);
            }
        });


        button_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pindah = new Intent(MainActivity.this, tambah_transaksi_expense.class);
                startActivity(pindah);
            }
        });
    }

    private void refreshList() {
        dbcenter = new DataHelper(this);
        transaksiList = dbcenter.GetUsers();
        ListAdapter adapter = new SimpleAdapter(MainActivity.this, transaksiList, R.layout.listview,
                new String[]{"KETERANGAN", "NOMINAL", "NAMA_KATEGORI", "TANGGAL_TRANSAKSI"},
                new int[]{R.id.keterangan_transaksi, R.id.nominal_transaksi, R.id.kategori_transaksi, R.id.tanggal_transaksi});
        list_view_history.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        refreshList();
    }
}