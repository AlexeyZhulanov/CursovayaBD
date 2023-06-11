package com.example.bd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SecondActivity extends AppCompatActivity {

    private TextView phone_price, head_price, develop_price, summ;

    private CheckBox phone_3m, phone_6m, phone_12m, head_3m, head_6m, head_12m, develop_3m, develop_6m, develop_12m;

    int phone_pr = 0, head_pr = 0, develop_pr = 0, summ_pr = 0, count_months, current_department;

    List<Integer> id_list = new ArrayList<Integer>();
    List<Integer> count_list = new ArrayList<Integer>();
    List<Integer> stock_list = new ArrayList<Integer>();

    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
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

        ListenCheck();
    }


    private void ListenCheck() {
        phone_pr = 0;
        head_pr = 0;
        develop_pr = 0;
        summ_pr = 0;
        phone_3m = findViewById(R.id.phone_3m);
        phone_6m = findViewById(R.id.phone_6m);
        phone_12m = findViewById(R.id.phone_12m);
        head_3m = findViewById(R.id.head_3m);
        head_6m = findViewById(R.id.head_6m);
        head_12m = findViewById(R.id.head_12m);
        develop_3m = findViewById(R.id.develop_3m);
        develop_6m = findViewById(R.id.develop_6m);
        develop_12m = findViewById(R.id.develop_12m);
        phone_price = findViewById(R.id.phone_price);
        head_price = findViewById(R.id.head_price);
        develop_price = findViewById(R.id.develop_price);
        summ = findViewById(R.id.summ);
        phone_3m.setOnCheckedChangeListener(
                    new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if(isChecked) {
                                current_department = 1;
                                count_months = 3;
                                int tmp = AllTasks(current_department, count_months);
                                phone_pr += tmp;
                                summ_pr += tmp;
                                phone_price.setText(String.valueOf(phone_pr));
                                summ.setText(String.valueOf(summ_pr));
                            }
                        }
                    }
        );
        phone_6m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 1;
                            count_months = 6;
                            int tmp = AllTasks(current_department, count_months);
                            phone_pr += tmp;
                            summ_pr += tmp;
                            phone_price.setText(String.valueOf(phone_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        phone_12m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 1;
                            count_months = 12;
                            int tmp = AllTasks(current_department, count_months);
                            phone_pr += tmp;
                            summ_pr += tmp;
                            phone_price.setText(String.valueOf(phone_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        head_3m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 2;
                            count_months = 3;
                            int tmp = AllTasks(current_department, count_months);
                            head_pr += tmp;
                            summ_pr += tmp;
                            head_price.setText(String.valueOf(head_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        head_6m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 2;
                            count_months = 6;
                            int tmp = AllTasks(current_department, count_months);
                            head_pr += tmp;
                            summ_pr += tmp;
                            head_price.setText(String.valueOf(head_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        head_12m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 2;
                            count_months = 12;
                            int tmp = AllTasks(current_department, count_months);
                            head_pr += tmp;
                            summ_pr += tmp;
                            head_price.setText(String.valueOf(head_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        develop_3m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 3;
                            count_months = 3;
                            int tmp = AllTasks(current_department, count_months);
                            develop_pr += tmp;
                            summ_pr += tmp;
                            develop_price.setText(String.valueOf(develop_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        develop_6m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 3;
                            count_months = 6;
                            int tmp = AllTasks(current_department, count_months);
                            develop_pr += tmp;
                            summ_pr += tmp;
                            develop_price.setText(String.valueOf(develop_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
        develop_12m.setOnCheckedChangeListener(
                new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked) {
                            current_department = 3;
                            count_months = 12;
                            int tmp = AllTasks(current_department, count_months);
                            develop_pr += tmp;
                            summ_pr += tmp;
                            develop_price.setText(String.valueOf(develop_pr));
                            summ.setText(String.valueOf(summ_pr));
                        }
                    }
                }
        );
    }

    private int AllTasks(int cur_dep, int month) {
        id_list.clear();
        count_list.clear();
        stock_list.clear();
        int temp_PurId;
        int temp_PrPurId;
        int temp_price;
        //Получаем информацию
        Cursor cursor = mDb.rawQuery("SELECT sum((s.Required_Quantity*? - s.In_Stock)*dir.Price) AS Summ FROM Department d INNER JOIN State s ON s.Id_Department = d.Id INNER JOIN Product prod ON s.Id_Product = prod.Id INNER JOIN Directory dir ON dir.Id_Product = prod.Id WHERE dir.Price IN (SELECT min(direct.Price) FROM Directory direct GROUP BY direct.Id_Product) AND d.Id = ? AND (s.Required_Quantity*? - s.In_Stock) > 0 GROUP BY d.Id", new String[]{String.valueOf(month), String.valueOf(cur_dep), String.valueOf(month)});
        if (cursor != null && cursor.moveToFirst()) {
            temp_price = cursor.getInt(0);
            cursor.close();
            Cursor cursor1 = mDb.rawQuery("SELECT max(p.Id) FROM Purchase p", null);
            cursor1.moveToFirst();
            temp_PurId = cursor1.getInt(0) + 1;
            cursor1.close();
            Cursor cursor2 = mDb.rawQuery("SELECT max(p.Id) FROM Product_Purchase p", null);
            cursor2.moveToFirst();
            temp_PrPurId = cursor2.getInt(0) + 1;
            cursor2.close();
            Cursor cursor3 = mDb.rawQuery("SELECT DISTINCT p.Id, (s.Required_Quantity*? - s.In_Stock), s.In_Stock FROM Product p INNER JOIN State s ON p.Id = s.Id_Product WHERE (s.Required_Quantity*? - s.In_Stock > 0) AND s.Id_Department = ? ORDER BY p.Id", new String[]{String.valueOf(month), String.valueOf(month), String.valueOf(cur_dep)});
            cursor3.moveToFirst();
            for (int i = 0; i < cursor3.getCount(); i++) {
                id_list.add(cursor3.getInt(0));
                count_list.add(cursor3.getInt(1));
                stock_list.add(cursor3.getInt(2));
                cursor3.moveToNext();
            }
            cursor3.close();
            //Записываем информацию
            //Сначала обновим In_Stock в State
            for (int i = 0; i < id_list.size(); i++) {
                ContentValues cv = new ContentValues();
                cv.put("In_Stock", count_list.get(i) + stock_list.get(i));
                mDb.update("State", cv, "Id_Department = ? AND Id_Product = ?", new String[]{String.valueOf(cur_dep), String.valueOf(id_list.get(i))});
            }
            //Теперь добавим строку в Purchase
            Cursor cur = mDb.rawQuery("SELECT datetime()", null);
            cur.moveToFirst();
            String tempstr = cur.getString(0);
            cur.close();
            final int random = new Random().nextInt(3) + 1;
            ContentValues cv = new ContentValues();
            cv.put("Id_Provider", random);
            cv.put("Purchase_Date", tempstr);
            mDb.insert("Purchase", null, cv);
            //Теперь Product_Purchase
            for (int i = 0; i < id_list.size(); i++) {
                ContentValues cv5 = new ContentValues();
                cv5.put("Id_Product", id_list.get(i));
                cv5.put("Id_Purchase", temp_PurId);
                cv5.put("Quantity", count_list.get(i));
                mDb.insert("Product_Purchase", null, cv5);
            }
            //Ну и напоследок Product_by_Department
            for (int i = temp_PrPurId; i < temp_PrPurId + id_list.size(); i++) {
                ContentValues cv1 = new ContentValues();
                cv1.put("Id_Department", cur_dep);
                cv1.put("Id_Product_Purchase", i);
                mDb.insert("Product_by_Department", null, cv1);
            }
            //Всё готово, теперь завершаем
            return temp_price;
        }
        else {
            return 0;
        }
    }
}