package com.example.bd;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ThirdActivity extends AppCompatActivity {

    ArrayList<DasaBannih> bases = new ArrayList<DasaBannih>();
    private DasaAdapter adapter;
    private Button change_add, change_update, change_delete;

    private LinearLayout add_layout, update_layout, delete_layout;

    private RecyclerView recyclerView;

    private GridLayoutManager manager = new GridLayoutManager(this,1);

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        mDBHelper = new DatabaseHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch(IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }

        try {
            mDb = mDBHelper.getWritableDatabase();
        } catch(SQLException mSQLException) {
            throw mSQLException;
        }

        Listen_Choise();
    }


    private void Listen_Choise() {
        change_add = findViewById(R.id.change_add);
        change_update = findViewById(R.id.change_update);
        change_delete = findViewById(R.id.change_delete);
        change_add.setBackgroundResource(R.drawable.defaultbutton);
        change_update.setBackgroundResource(R.drawable.defaultbutton);
        change_delete.setBackgroundResource(R.drawable.defaultbutton);
        add_layout = findViewById(R.id.add_layout);
        update_layout = findViewById(R.id.update_layout);
        delete_layout = findViewById(R.id.delete_layout);
        recyclerView = findViewById(R.id.table5);
        change_add.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_add.setBackgroundResource(R.drawable.defaultclickedbutton);
                        change_update.setBackgroundResource(R.drawable.defaultbutton);
                        change_delete.setBackgroundResource(R.drawable.defaultbutton);
                        update_layout.setVisibility(View.GONE);
                        delete_layout.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.GONE);
                        add_layout.setVisibility(View.VISIBLE);
                        EditText prod_name = findViewById(R.id.prod_name);
                        EditText prod_article = findViewById(R.id.prod_article);
                        EditText phone_quantity = findViewById(R.id.phone_quantity);
                        EditText phone_stock = findViewById(R.id.phone_stock);
                        EditText head_quantity = findViewById(R.id.head_quantity);
                        EditText head_stock = findViewById(R.id.head_stock);
                        EditText develop_quantity = findViewById(R.id.develop_quantity);
                        EditText develop_stock = findViewById(R.id.develop_stock);
                        EditText post1_price = findViewById(R.id.post1_price);
                        EditText post2_price = findViewById(R.id.post2_price);
                        EditText post3_price = findViewById(R.id.post3_price);
                        Button add_button = (Button)findViewById(R.id.button_add);
                        add_button.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if(prod_name.getText().length() > 0 && prod_article.getText().length() > 0) {
                                            if((phone_quantity.getText().length() > 0 && phone_stock.getText().length() > 0) || (head_quantity.getText().length() > 0 && head_stock.getText().length() > 0) || (develop_quantity.getText().length() > 0 && develop_stock.getText().length() > 0)) {
                                                if (post1_price.getText().length() > 0 || post2_price.getText().length() > 0 || post3_price.getText().length() > 0) {
                                                        Cursor cur = mDb.rawQuery("SELECT Id FROM Product WHERE Product_Name = ? AND Article_Number = ?", new String[]{String.valueOf(prod_name.getText()), String.valueOf(prod_article.getText())});
                                                        List<Integer> id_departments = new ArrayList<Integer>();
                                                        List<Integer> id_providers = new ArrayList<Integer>();
                                                        int tempid;
                                                        if(cur != null && cur.moveToFirst()) {
                                                            tempid = cur.getInt(0);
                                                            cur.close();
                                                            Cursor cur1 = mDb.rawQuery("SELECT Id_Department FROM State WHERE Id_Product = ?", new String[]{String.valueOf(tempid)});
                                                            if(cur1 != null && cur1.moveToFirst()) {
                                                                for(int i = 0; i < cur1.getCount(); i++) {
                                                                    id_departments.add(cur1.getInt(0));
                                                                    cur1.moveToNext();
                                                                }
                                                                cur1.close();
                                                            }
                                                            Cursor cur2 = mDb.rawQuery("SELECT Id_Provider FROM Directory WHERE Id_Product = ?", new String[]{String.valueOf(tempid)});
                                                            if(cur2 != null && cur2.moveToFirst()) {
                                                                for(int i = 0; i < cur2.getCount(); i++) {
                                                                    id_providers.add(cur2.getInt(0));
                                                                    cur2.moveToNext();
                                                                }
                                                                cur2.close();
                                                            }
                                                        }
                                                        else {
                                                            ContentValues cv = new ContentValues();
                                                            cv.put("Product_Name", String.valueOf(prod_name.getText()));
                                                            cv.put("Article_Number", String.valueOf(prod_article.getText()));
                                                            mDb.insert("Product", null, cv);
                                                            Cursor cursor = mDb.rawQuery("SELECT max(p.Id) FROM Product p", null);
                                                            cursor.moveToFirst();
                                                            tempid = cursor.getInt(0);
                                                            cursor.close();
                                                        }
                                                        if (phone_quantity.getText().length() > 0 && phone_stock.getText().length() > 0) {
                                                            if (!id_departments.contains(1)) {
                                                                ContentValues cv1 = new ContentValues();
                                                                cv1.put("Id_Product", tempid);
                                                                cv1.put("Id_Department", 1);
                                                                cv1.put("Required_Quantity", String.valueOf(phone_quantity.getText()));
                                                                cv1.put("In_Stock", String.valueOf(phone_stock.getText()));
                                                                mDb.insert("State", null, cv1);
                                                            }
                                                        }
                                                        if (head_quantity.getText().length() > 0 && head_stock.getText().length() > 0) {
                                                            if (!id_departments.contains(2)) {
                                                                ContentValues cv2 = new ContentValues();
                                                                cv2.put("Id_Product", tempid);
                                                                cv2.put("Id_Department", 2);
                                                                cv2.put("Required_Quantity", String.valueOf(head_quantity.getText()));
                                                                cv2.put("In_Stock", String.valueOf(head_stock.getText()));
                                                                mDb.insert("State", null, cv2);
                                                            }
                                                        }
                                                        if (develop_quantity.getText().length() > 0 && develop_stock.getText().length() > 0) {
                                                            if (!id_departments.contains(3)) {
                                                                ContentValues cv3 = new ContentValues();
                                                                cv3.put("Id_Product", tempid);
                                                                cv3.put("Id_Department", 3);
                                                                cv3.put("Required_Quantity", String.valueOf(develop_quantity.getText()));
                                                                cv3.put("In_Stock", String.valueOf(develop_stock.getText()));
                                                                mDb.insert("State", null, cv3);
                                                            }
                                                        }
                                                        if (post1_price.getText().length() > 0) {
                                                            if (!id_providers.contains(1)) {
                                                                ContentValues cv4 = new ContentValues();
                                                                cv4.put("Id_Product", tempid);
                                                                cv4.put("Id_Provider", 1);
                                                                cv4.put("Price", String.valueOf(post1_price.getText()));
                                                                mDb.insert("Directory", null, cv4);
                                                            }
                                                        }
                                                        if (post2_price.getText().length() > 0) {
                                                            if (!id_providers.contains(2)) {
                                                                ContentValues cv5 = new ContentValues();
                                                                cv5.put("Id_Product", tempid);
                                                                cv5.put("Id_Provider", 2);
                                                                cv5.put("Price", String.valueOf(post2_price.getText()));
                                                                mDb.insert("Directory", null, cv5);
                                                            }
                                                        }
                                                        if (post3_price.getText().length() > 0) {
                                                            if (!id_providers.contains(3)) {
                                                                ContentValues cv6 = new ContentValues();
                                                                cv6.put("Id_Product", tempid);
                                                                cv6.put("Id_Provider", 3);
                                                                cv6.put("Price", String.valueOf(post3_price.getText()));
                                                                mDb.insert("Directory", null, cv6);
                                                            }
                                                        }
                                                        add_layout.setVisibility(View.GONE);
                                                        post3_price.setText("");
                                                        post2_price.setText("");
                                                        post1_price.setText("");
                                                        develop_quantity.setText("");
                                                        develop_stock.setText("");
                                                        head_quantity.setText("");
                                                        head_stock.setText("");
                                                        phone_quantity.setText("");
                                                        phone_stock.setText("");
                                                        prod_name.setText("");
                                                        prod_article.setText("");
                                                }
                                            }
                                        }
                                    }
                                }
                        );
                    }
                }
        );
        change_update.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_add.setBackgroundResource(R.drawable.defaultbutton);
                        change_update.setBackgroundResource(R.drawable.defaultclickedbutton);
                        change_delete.setBackgroundResource(R.drawable.defaultbutton);
                        add_layout.setVisibility(View.GONE);
                        delete_layout.setVisibility(View.GONE);
                        update_layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        EditText update_id = findViewById(R.id.update_id);
                        EditText update_quantity = findViewById(R.id.update_quantity);
                        GetTable();
                        Button update_button = (Button)findViewById(R.id.button_update);
                        update_button.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (update_id.getText().length() > 0 && update_quantity.getText().length() > 0) {
                                            ContentValues cv = new ContentValues();
                                            cv.put("Required_Quantity", String.valueOf(update_quantity.getText()));
                                            mDb.update("State", cv, "Id = ?", new String[]{String.valueOf(update_id.getText())});
                                            GetTable();
                                            update_id.setText("");
                                            update_quantity.setText("");
                                        }
                                    }
                                }
                        );
                    }
                }
        );
        change_delete.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        change_add.setBackgroundResource(R.drawable.defaultbutton);
                        change_update.setBackgroundResource(R.drawable.defaultbutton);
                        change_delete.setBackgroundResource(R.drawable.defaultclickedbutton);
                        add_layout.setVisibility(View.GONE);
                        update_layout.setVisibility(View.GONE);
                        delete_layout.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.VISIBLE);
                        EditText delete_id = findViewById(R.id.delete_id);
                        GetTable();
                        Button delete_button = (Button)findViewById(R.id.button_delete);
                        delete_button.setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (delete_id.getText().length() > 0) {
                                            mDb.delete("State", "Id = ?", new String[]{String.valueOf(delete_id.getText())});
                                            GetTable();
                                            delete_id.setText("");
                                        }
                                    }
                                }
                        );
                    }
                }
        );
    }

    private void GetTable() {
        bases.clear();
        String t = "State";
        Cursor tmp = mDb.rawQuery("SELECT c.name FROM pragma_table_info('"+ String.valueOf(t) +"') c",null);
        tmp.moveToFirst();
        String[] tmpnames = new String[5];
        for(int i = 0; i < tmp.getCount(); i++) {
            tmpnames[i] = tmp.getString(0);
            tmp.moveToNext();
        }
        tmp.close();
        bases.add(new DasaBannih(tmpnames[0], tmpnames[1], tmpnames[2], tmpnames[3], tmpnames[4]));
        Cursor cursor = mDb.rawQuery("SELECT * FROM State",null);
        cursor.moveToFirst();
        for(int i = 0; i < cursor.getCount(); i++) {
            bases.add(new DasaBannih(cursor.getString(0),cursor.getString(1),cursor.getString(2),cursor.getString(3),cursor.getString(4)));
            cursor.moveToNext();
        }
        cursor.close();
        recyclerView.setLayoutManager(manager);
        adapter = new DasaAdapter(getBaseContext(), bases, manager);
        recyclerView.setAdapter(adapter);
    }
}