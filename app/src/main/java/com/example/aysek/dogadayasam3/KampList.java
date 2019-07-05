package com.example.aysek.dogadayasam3;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Quoc Nguyen on 13-Dec-16.
 */

public class KampList extends AppCompatActivity {

    GridView gridView;
    ArrayList<Kamp> list;
    KampListAdapter adapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kamp_list_activity);

        gridView = (GridView) findViewById(R.id.gridView);
        list = new ArrayList<>();
        adapter = new KampListAdapter(this, R.layout.kamp_items, list);
        gridView.setAdapter(adapter);

        // get all data from sqlite
        Cursor cursor = Yenikayit.sqLiteHelper.getData("SELECT * FROM KAMP");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String baslik = cursor.getString(1);
            String metin = cursor.getString(2);
            String ksayisi=cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Kamp(baslik, metin,ksayisi,image, id));
        }
        adapter.notifyDataSetChanged();

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                CharSequence[] items = {"Güncelle", "Sil"};
                AlertDialog.Builder dialog = new AlertDialog.Builder(KampList.this);

                dialog.setTitle("Bir işlem seçin.");
                dialog.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (item == 0) {
                            // update
                            Cursor c = Yenikayit.sqLiteHelper.getData("SELECT id FROM KAMP");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            // show dialog update at here
                            showDialogUpdate(KampList.this, arrID.get(position));

                        } else {
                            // delete
                            Cursor c = Yenikayit.sqLiteHelper.getData("SELECT id FROM KAMP");
                            ArrayList<Integer> arrID = new ArrayList<Integer>();
                            while (c.moveToNext()){
                                arrID.add(c.getInt(0));
                            }
                            showDialogDelete(arrID.get(position));
                        }
                    }
                });
                dialog.show();
                return true;
            }
        });
    }

    ImageView imageViewKamp;
    private void showDialogUpdate(Activity activity, final int position){

        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.update_kamp_activity);
        dialog.setTitle("Güncelleme");

        imageViewKamp = (ImageView) dialog.findViewById(R.id.imageViewKamp);
        final EditText edtbaslik = (EditText) dialog.findViewById(R.id.edtbaslik);
        final EditText edtmetin = (EditText) dialog.findViewById(R.id.edtmetin);
        final EditText edtksayisi = (EditText) dialog.findViewById(R.id.edtksayisi);
        Button btnUpdate = (Button) dialog.findViewById(R.id.btnUpdate);

        // set width for dialog
        int width = (int) (activity.getResources().getDisplayMetrics().widthPixels * 0.95);
        // set height for dialog
        int height = (int) (activity.getResources().getDisplayMetrics().heightPixels * 0.7);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

        imageViewKamp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // request photo library
                ActivityCompat.requestPermissions(
                        KampList.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        888
                );
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Yenikayit.sqLiteHelper.updateData(
                            edtbaslik.getText().toString().trim(),
                            edtmetin.getText().toString().trim(),
                            edtksayisi.getText().toString().trim(),
                            Yenikayit.imageViewToByte(imageViewKamp),
                            position
                    );
                    dialog.dismiss();
                    Toast.makeText(getApplicationContext(), "Güncelleme Başarılı!!!",Toast.LENGTH_SHORT).show();
                }
                catch (Exception error) {
                    Log.e("Güncelleme Başarısız", error.getMessage());
                }
                updateKampList();
            }
        });
    }

    private void showDialogDelete(final int idKamp){
        final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(KampList.this);

        dialogDelete.setTitle("Uyarı!!");
        dialogDelete.setMessage("Silmek istediğinizden emin misiniz?");
        dialogDelete.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                try {
                    Yenikayit.sqLiteHelper.deleteData(idKamp);
                    Toast.makeText(getApplicationContext(), "Silme işlemi başarılı!!!",Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    Log.e("error", e.getMessage());
                }
                updateKampList();
            }
        });

        dialogDelete.setNegativeButton("İptal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialogDelete.show();
    }

    private void updateKampList(){
        // get all data from sqlite
        Cursor cursor = Yenikayit.sqLiteHelper.getData("SELECT * FROM KAMP");
        list.clear();
        while (cursor.moveToNext()) {
            int id = cursor.getInt(0);
            String baslik = cursor.getString(1);
            String metin = cursor.getString(2);
            String ksayisi = cursor.getString(3);
            byte[] image = cursor.getBlob(4);

            list.add(new Kamp(baslik, metin,ksayisi, image, id));
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == 888){
            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 888);
            }
            else {
                Toast.makeText(getApplicationContext(), "Dosyaya erişme izniniz yok!", Toast.LENGTH_SHORT).show();
            }
            return;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode == 888 && resultCode == RESULT_OK && data != null){
            Uri uri = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageViewKamp.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}