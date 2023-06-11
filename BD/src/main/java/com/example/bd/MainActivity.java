package com.example.bd;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.ScaleGestureDetector;
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.MotionEvent;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;

import java.io.IOException;
import java.util.Objects;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {
    ArrayList<DasaBannih> bases = new ArrayList<DasaBannih>();
    private DatabaseHelper mDBHelper;
    private SQLiteDatabase mDb;
    private GridLayoutManager manager = new GridLayoutManager(this,1);

    ScaleGestureDetector objScaleGestureDetector;


    private Button but_tables, but_apanel, but_queries;

    private LinearLayout query_layout, table1_layout, table2_layout, opisanie, panel, selected_dep, selected_prod;
    private ScrollView scroll;

    private RecyclerView recyclerView;

    private DasaAdapter adapter;
    private TextView event_text, opis, seek_text;

    private EditText text_login, text_password;

    private RadioGroup radioGroupDepartment;

    private SeekBar seek;


    String[] str_log_pass = new String[10];
    int[] levels = new int[10];
    int current_level = 1;
    int prev_lower_button = 0;
    int prev_query_button = -1;
    int prev_table_button = -1;

    int selected_department = -1;

    int selected_product = 0;

    int current_SpanCount = 1;

    String[] opis_texts = {
            "Вывести количество товара, которое нужно закупить в указанный отдел, чтобы их хватило на 1 год.",
            "Вывести название отдела, который является самым расточительным, если закупить все виды товара с запасом на 6 месяцев по минимально возможной цене у продавцов с рейтингом 3+.",
            "Вывести список отделов нуждающихся в закупке в ближайший месяц.",
            "Вывести виды товара, которые нужно закупить в ближайший месяц в указанный отдел.",
            "Хватит ли фирме 600.000 рублей, чтобы закупить все товары в указанный отдел на год по минимально возможной цене(True/False).",
            "Вывести товар, за который фирма должна будет заплатить больше всего рублей, если закупать товар по минимально возможной цене у поставщиков с рейтингом 3+, чтобы в указанном отделе был запас этого товара на 6 месяцев.",
            "Вывести виды товара, которые можно не закупать в указанный отдел в ближайшие 3 месяца.",
            "Вывести количество месяцев, на которое хватит каждого товара в указанном отделе, если докупить каждого товара по 100 штук.",
            "Вывести отдел, в который нужно закупить больше всего указанного товара, чтобы его хватило на год."
    };

    String[] table_names =  {
            "Firm",
            "Department",
            "State",
            "Product",
            "Directory",
            "Provider",
            "Purchase",
            "Product_Purchase",
            "Product_by_Department"
    };

    boolean isStart = true;
    boolean flag_tables = true, flag_apanel = false, flag_queries = false;

    boolean flag_privilege1 = true, flag_privilege2 = false, flag_privilege3 = false, flag_privilege4 = false;
    private static final int[] lower_buttons = {
            R.id.button10,
            R.id.button11,
            R.id.button16
    };
    private static final int[] table_buttons = {
            R.id.button15,
            R.id.button17,
            R.id.button18,
            R.id.button19,
            R.id.button20,
            R.id.button21,
            R.id.button22,
            R.id.button23,
            R.id.button24
    };
    private static final int[] query_buttons = {
            R.id.button,
            R.id.button2,
            R.id.button3,
            R.id.button4,
            R.id.button5,
            R.id.button6,
            R.id.button7,
            R.id.button8,
            R.id.button9
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        objScaleGestureDetector = new ScaleGestureDetector(getApplicationContext(),new PinchZoomListener());
        addListenerOnButtonTables();
        addListenerOnButtonApanel();
        addListenerOnButtonQueries();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        objScaleGestureDetector.onTouchEvent(event);
        return true;
    }

    public class PinchZoomListener extends SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            float gesturefactor = detector.getScaleFactor();

            if(gesturefactor > 1) {
                if(current_SpanCount < 3) {
                    current_SpanCount += 1;
                }
            }
            else {
                if(current_SpanCount > 1) {
                    current_SpanCount -= 1;
                }
            }
            PrintTable();
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }
    }
    private void PrintTable() {
        manager.setSpanCount(current_SpanCount);
            recyclerView = findViewById(R.id.table);
            recyclerView.setLayoutManager(manager);
            adapter = new DasaAdapter(getBaseContext(), bases, manager);
            recyclerView.setAdapter(adapter);
    }


    private void TablesListener() {
        //table button listener
        for(int i = 0; i < 9; i++) {
            final int gl_i = i;
            Button button = (Button) findViewById(table_buttons[i]);
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (gl_i == prev_table_button) {
                                bases.clear();
                                PrintTable();
                                button.setBackgroundResource(R.drawable.defaultbutton);
                                prev_table_button = -1;
                            } else {
                                button.setBackgroundResource(R.drawable.defaultclickedbutton);
                                if (prev_table_button != -1) {
                                    Button but = (Button) findViewById(table_buttons[prev_table_button]);
                                    but.setBackgroundResource(R.drawable.defaultbutton);
                                }
                                prev_table_button = gl_i;
                                bases.clear();
                                Cursor tmp = mDb.rawQuery("SELECT c.name FROM pragma_table_info('"+ String.valueOf(table_names[gl_i]) +"') c",null);
                                tmp.moveToFirst();
                                String[] tmpnames = new String[5];
                                for(int i = 0; i < tmp.getCount(); i++) {
                                    tmpnames[i] = tmp.getString(0);
                                    tmp.moveToNext();
                                }
                                if(tmp.getCount() == 3) {
                                    bases.add(new DasaBannih(tmpnames[0], tmpnames[1], tmpnames[2], null, null));
                                }
                                else if(tmp.getCount() == 4) {
                                    bases.add(new DasaBannih(tmpnames[0], tmpnames[1], tmpnames[2], tmpnames[3], null));
                                }
                                else if(tmp.getCount() == 5) {
                                    bases.add(new DasaBannih(tmpnames[0], tmpnames[1], tmpnames[2], tmpnames[3], tmpnames[4]));
                                }
                                tmp.close();
                                if(gl_i != 1) {
                                    Cursor cursor = mDb.rawQuery("SELECT * FROM " + String.valueOf(table_names[gl_i]), null);
                                    cursor.moveToFirst();
                                    if (current_level != 1) {
                                        for (int j = 0; j < cursor.getCount(); j++) {
                                            if (cursor.getColumnCount() == 3) {
                                                bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), null, null));
                                            } else if (cursor.getColumnCount() == 4) {
                                                bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null));
                                            } else if (cursor.getColumnCount() == 5) {
                                                bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                                            }
                                            cursor.moveToNext();
                                        }
                                    } else {
                                        for (int j = 0; j < cursor.getCount(); j++) {
                                            if (gl_i != 2 && gl_i != 4 && gl_i != 6 && gl_i != 7) {
                                                if (cursor.getColumnCount() == 3) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), null, null));
                                                } else if (cursor.getColumnCount() == 4) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null));
                                                } else if (cursor.getColumnCount() == 5) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                                                }
                                            } else {
                                                if (gl_i == 2) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), "*****", "*****"));
                                                } else if (gl_i == 4) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), "*****", null));
                                                } else if (gl_i == 6) {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), "*****", null, null));
                                                } else {
                                                    bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), "*****", null));
                                                }
                                            }
                                            cursor.moveToNext();
                                        }
                                    } //
                                    cursor.close();
                                }
                                else {
                                    //VIEW Spisok_Otdelov
                                    Cursor cursor = mDb.rawQuery("SELECT * FROM Spisok_Otdelov",null);
                                    cursor.moveToFirst();
                                    for(int i = 0; i < cursor.getCount(); i++) {
                                        bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2),null,null));
                                        cursor.moveToNext();
                                    }
                                    cursor.close();
                                }
                                if(bases.size() > 0) {
                                    PrintTable();
                                }
                            }
                        }
                    }
            );
        }
    }

    private void addListenerOnButtonTables() {
        but_tables = findViewById(lower_buttons[0]);
        if(isStart) {
            TablesListener();
            isStart = false;
        }
        but_tables.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!flag_tables) {
                            if(prev_lower_button == 1) {
                                Button button = (Button)findViewById(lower_buttons[1]);
                                button.setBackgroundResource(R.drawable.buttonshape);
                                recyclerView = findViewById(R.id.table);
                                recyclerView.setVisibility(View.VISIBLE);
                                panel = findViewById(R.id.panel);
                                panel.setVisibility(View.GONE);
                                scroll = findViewById(R.id.scroll);
                                scroll.setVisibility(View.VISIBLE);
                                flag_apanel = false;
                            }
                            else if(prev_lower_button == 2) {
                                Button button = (Button)findViewById(lower_buttons[2]);
                                button.setBackgroundResource(R.drawable.buttonshapebottom);
                                query_layout = findViewById(R.id.querybuttons);
                                query_layout.setVisibility(View.GONE);
                                opisanie = findViewById(R.id.opisanie);
                                opisanie.setVisibility(View.GONE);
                                selected_dep = findViewById(R.id.select_department);
                                selected_dep.setVisibility(View.GONE);
                                selected_prod = findViewById(R.id.select_product);
                                selected_prod.setVisibility(View.GONE);
                                bases.clear();
                                flag_queries = false;
                            }
                            but_tables.setBackgroundResource(R.drawable.buttonshapetopclicked);
                            flag_tables = true;
                            table1_layout = findViewById(R.id.table1);
                            table1_layout.setVisibility(View.VISIBLE);
                            table2_layout = findViewById(R.id.table2);
                            table2_layout.setVisibility(View.VISIBLE);
                            event_text = findViewById(R.id.eventtext);
                            event_text.setVisibility(View.VISIBLE);
                            event_text.setText("Tables");
                            prev_lower_button = 0;
                            if(prev_table_button != -1) {
                                Button button = (Button)findViewById(table_buttons[prev_table_button]);
                                button.setBackgroundResource(R.drawable.defaultbutton);
                                prev_table_button = -1;
                            }

                            TablesListener();
                        }
                    }
                }
        );
    }
    private void addListenerOnButtonApanel() {
        but_apanel = findViewById(lower_buttons[1]);
        isStart = false;
        but_apanel.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!flag_apanel) {
                            if(prev_lower_button == 0) {
                                Button button = (Button)findViewById(lower_buttons[0]);
                                button.setBackgroundResource(R.drawable.buttonshapetop);
                                table1_layout = findViewById(R.id.table1);
                                table1_layout.setVisibility(View.GONE);
                                table2_layout = findViewById(R.id.table2);
                                table2_layout.setVisibility(View.GONE);
                                flag_tables = false;
                            }
                            else if(prev_lower_button == 2) {
                                Button button = (Button)findViewById(lower_buttons[2]);
                                button.setBackgroundResource(R.drawable.buttonshapebottom);
                                query_layout = findViewById(R.id.querybuttons);
                                query_layout.setVisibility(View.GONE);
                                opisanie = findViewById(R.id.opisanie);
                                opisanie.setVisibility(View.GONE);
                                selected_dep = findViewById(R.id.select_department);
                                selected_dep.setVisibility(View.GONE);
                                selected_prod = findViewById(R.id.select_product);
                                selected_prod.setVisibility(View.GONE);
                                flag_queries = false;
                            }
                            but_apanel.setBackgroundResource(R.drawable.buttonshapeclicked);
                            panel = findViewById(R.id.panel);
                            panel.setVisibility(View.VISIBLE);
                            bases.clear();
                            scroll = findViewById(R.id.scroll);
                            scroll.setVisibility(View.GONE);
                            event_text = findViewById(R.id.eventtext);
                            event_text.setVisibility(View.GONE);
                            flag_apanel = true;
                            prev_lower_button = 1;
                            text_login = findViewById(R.id.text_login);
                            text_password = findViewById(R.id.text_password);
                            if(current_level >= 3) {
                                //Go to SecondActivity
                                ZakupListener();
                            }
                            if(current_level == 4) {
                                //Go to TrirdActivity
                                ChangeListener();
                            }
                            Button but_login = (Button)findViewById(R.id.button12);
                            but_login.setOnClickListener(
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TextView lev = (TextView)findViewById(R.id.textView6);
                                            TextView inf_acc = (TextView)findViewById(R.id.textView3);
                                            TextView quer_acc = (TextView)findViewById(R.id.textView4);
                                            Button but_post = (Button)findViewById(R.id.button25);
                                            Button but_change = (Button)findViewById(R.id.button13);
                                            if(text_password.length() > 0 && text_login.length() > 0) {
                                                Cursor cursor = mDb.rawQuery("SELECT * FROM Login", null);
                                                cursor.moveToFirst();
                                                for(int i = 0; i < cursor.getCount(); i++) {
                                                    str_log_pass[i] = cursor.getString(1) + ":" + cursor.getString(2);
                                                    levels[i] = cursor.getInt(3);
                                                    cursor.moveToNext();
                                                }
                                                cursor.close();
                                                int temp_level = 1;
                                                String temp = text_login.getText() + ":" + text_password.getText();
                                                for(int i = 0; i < str_log_pass.length; i++) {
                                                    if(Objects.equals(str_log_pass[i], temp)) {
                                                        temp_level = levels[i];
                                                    }
                                                }
                                                if(temp_level != current_level) {
                                                    current_level = temp_level;
                                                    text_login.setText("");
                                                    text_password.setText("");
                                                    if(current_level >= 2) {
                                                        if(current_level == 2) {
                                                            lev.setText("Модератор");
                                                            lev.setTextColor(Color.GREEN);
                                                        }
                                                        inf_acc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal1,0);
                                                        quer_acc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal1,0);
                                                        if(current_level >= 3) {
                                                            //Go to SecondActivity
                                                            ZakupListener();
                                                            if(current_level == 3) {
                                                                lev.setText("Администратор");
                                                                lev.setTextColor(Color.RED);
                                                            }
                                                            but_post.setClickable(true);
                                                            but_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal1,0);
                                                            if(current_level == 4) {
                                                                //Go to ThirdActivity
                                                                ChangeListener();
                                                                lev.setText("Гл. Администратор");
                                                                lev.setTextColor(Color.parseColor("#8B0000"));
                                                                but_change.setClickable(true);
                                                                but_change.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal1,0);
                                                            }
                                                        }
                                                    }
                                                }
                                                else {
                                                    text_login.setText("");
                                                    text_password.setText("");
                                                }
                                            }
                                            else {
                                                lev.setText("Пользователь");
                                                current_level = 1;
                                                lev.setTextColor(Color.parseColor("#FF6200EE"));
                                                inf_acc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal2,0);
                                                quer_acc.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal2,0);
                                                but_post.setClickable(false);
                                                but_post.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal2,0);
                                                but_change.setClickable(false);
                                                but_change.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.gal2,0);
                                            }
                                        }
                                    }
                            );
                        }
                    }
                }
        );
    }

    private void addListenerOnButtonQueries() {
        but_queries = findViewById(lower_buttons[2]);
        isStart = false;
        but_queries.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(!flag_queries) {
                            but_queries.setBackgroundResource(R.drawable.buttonshapebottomclicked);
                            flag_queries = true;
                            if(prev_lower_button == 0) {
                                Button button = (Button) findViewById(lower_buttons[0]);
                                button.setBackgroundResource(R.drawable.buttonshapetop);
                                table1_layout = findViewById(R.id.table1);
                                table1_layout.setVisibility(View.GONE);
                                table2_layout = findViewById(R.id.table2);
                                table2_layout.setVisibility(View.GONE);
                                bases.clear();
                                flag_tables = false;
                            }
                            else if(prev_lower_button == 1) {
                                Button button = (Button)findViewById(lower_buttons[1]);
                                button.setBackgroundResource(R.drawable.buttonshape);
                                panel = findViewById(R.id.panel);
                                panel.setVisibility(View.GONE);
                                scroll = findViewById(R.id.scroll);
                                scroll.setVisibility(View.VISIBLE);
                                flag_apanel = false;
                            }
                                query_layout = findViewById(R.id.querybuttons);
                                query_layout.setVisibility(View.VISIBLE);
                                event_text = findViewById(R.id.eventtext);
                                event_text.setVisibility(View.VISIBLE);
                                event_text.setText("Queries");
                                opisanie = findViewById(R.id.opisanie);
                                if(prev_query_button != -1) {
                                    Button button = (Button)findViewById(query_buttons[prev_query_button]);
                                    if(prev_query_button == 0) button.setBackgroundResource(R.drawable.buttonshapetop);
                                    else if(prev_query_button != 8) button.setBackgroundResource(R.drawable.buttonshape);
                                    else button.setBackgroundResource(R.drawable.buttonshapebottom);
                                    prev_query_button = -1;
                                }
                                prev_lower_button = 2;
                            //query buttons listener
                            Queries();
                        }
                    }
                }
        );
    }

    private void Queries() {
        for(int i = 0; i < 9; i++) {
            final int gl_i = i;
            Button button = (Button)findViewById(query_buttons[i]);
            button.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(gl_i == prev_query_button) {
                                bases.clear();
                                opisanie.setVisibility(View.GONE);
                                if(prev_query_button == 0) button.setBackgroundResource(R.drawable.buttonshapetop);
                                else if(prev_query_button != 8) button.setBackgroundResource(R.drawable.buttonshape);
                                else button.setBackgroundResource(R.drawable.buttonshapebottom);
                                prev_query_button = -1;
                            }
                            else {

                                if (prev_query_button != -1) {
                                    Button but = (Button) findViewById(query_buttons[prev_query_button]);
                                    if (prev_query_button == 0)
                                        but.setBackgroundResource(R.drawable.buttonshapetop);
                                    else if (prev_query_button != 8)
                                        but.setBackgroundResource(R.drawable.buttonshape);
                                    else but.setBackgroundResource(R.drawable.buttonshapebottom);
                                }
                                opisanie.setVisibility(View.VISIBLE);
                                if (gl_i == 0)
                                    button.setBackgroundResource(R.drawable.buttonshapetopclicked);
                                else if (gl_i != 8)
                                    button.setBackgroundResource(R.drawable.buttonshapeclicked);
                                else
                                    button.setBackgroundResource(R.drawable.buttonshapebottomclicked);
                                prev_query_button = gl_i;
                                //query
                                selected_prod = findViewById(R.id.select_product);
                                selected_prod.setVisibility(View.GONE);
                                radioGroupDepartment = findViewById(R.id.radio);
                                radioGroupDepartment.clearCheck();
                                opis = findViewById(R.id.opis);
                                opis.setText(opis_texts[gl_i]);
                                Quer(gl_i);
                            }
                        }
                    }
            );
        }
    }

    private void Quer(final int gl_i) {
        bases.clear();
        Cursor cursor;
        if (gl_i != 8 && gl_i != 1 && gl_i != 2) {
            selected_dep = findViewById(R.id.select_department);
            selected_dep.setVisibility(View.VISIBLE);
            RadioCheckDepartment(gl_i);
            if(selected_department != -1) {
                if (gl_i == 0) {
                    bases.add(new DasaBannih("Id_Firm", "Name", "CountForYear",null,null));
                    cursor = mDb.rawQuery("SELECT d.Id_Firm, d.Name, sum(s.Required_Quantity*12 - s.In_Stock) AS CountForYear FROM State s INNER JOIN Department d ON s.Id_Department = d.Id WHERE s.Id_Department = ? AND (s.Required_Quantity*12 - s.In_Stock) > 0", new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for (int i = 0; i < cursor.getCount(); i++) {
                        bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), null, null));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                else if(gl_i == 3) {
                    bases.add(new DasaBannih("Id_Product", "Product_Name",null,null,null));
                    cursor = mDb.rawQuery("SELECT DISTINCT p.Id as 'Id_Product', p.Product_Name FROM Product p INNER JOIN State s ON p.Id = s.Id_Product WHERE (s.Required_Quantity - s.In_Stock > 0) AND s.Id_Department = ?", new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        bases.add(new DasaBannih(cursor.getString(0),cursor.getString(1),null,null,null));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                else if(gl_i == 4) {
                    bases.add(new DasaBannih("Firm","Department","Summ","Result",null));
                    cursor = mDb.rawQuery("SELECT f.Name as 'Firm', d.Name as 'Department', sum((s.Required_Quantity*12 - s.In_Stock)*dir.Price) AS Summ, CAST(CASE WHEN sum((s.Required_Quantity*12 - s.In_Stock)*dir.Price) > 600000 THEN 'False' ELSE 'True' END AS TEXT) AS Result FROM Firm f INNER JOIN Department d ON d.Id_Firm = f.Id INNER JOIN State s ON s.Id_Department = d.Id INNER JOIN Product prod ON s.Id_Product = prod.Id INNER JOIN Directory dir ON dir.Id_Product = prod.Id WHERE dir.Price IN (SELECT min(direct.Price) FROM Directory direct GROUP BY direct.Id_Product) AND d.Id = ? AND (s.Required_Quantity*12 - s.In_Stock) > 0 GROUP BY d.Id",new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        if(current_level != 1) {
                            bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null));
                        }
                        else {
                            bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1),"*****", cursor.getString(3), null));
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                else if(gl_i == 5) {
                    bases.add(new DasaBannih("Firm", "Department","Product_Name","Price",null));
                    cursor = mDb.rawQuery("SELECT f.Name as 'Firm', d.Name as 'Department', prod.Product_Name, ((s.Required_Quantity*6 - s.In_Stock)*dir.Price) as Price FROM Firm f INNER JOIN Department d ON f.Id = d.Id_Firm INNER JOIN State s ON s.Id_Department = d.Id INNER JOIN Product prod ON s.Id_Product = prod.Id INNER JOIN Directory dir ON dir.Id_Product = prod.Id INNER JOIN Provider prov ON dir.Id_Provider = prov.Id WHERE dir.Price IN (SELECT min(direct.Price) FROM Directory direct GROUP BY direct.Id_Product) AND d.Id = ? AND prov.Rating >= 3 AND (s.Required_Quantity*6 - s.In_Stock) > 0 GROUP BY s.Id ORDER BY Price DESC LIMIT 1", new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        if(current_level != 1) {
                            bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), null));
                        }
                        else {
                            bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2),"*****", null));
                        }
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                else if(gl_i == 6) {
                    bases.add(new DasaBannih("Product_Id", "Product_Name", null,null,null));
                    cursor = mDb.rawQuery("SELECT DISTINCT p.Id as 'Product_Id', p.Product_Name FROM Product p INNER JOIN State s ON p.Id = s.Id_Product INNER JOIN Department d ON s.Id_Department = d.Id WHERE (s.Required_Quantity*3 - s.In_Stock < 0) AND s.Id_Department = ?", new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        bases.add(new DasaBannih(cursor.getString(0),cursor.getString(1),null,null,null));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
                else if(gl_i == 7) {
                    bases.add(new DasaBannih("Id_Product","Product_Name","Months",null,null));
                    cursor = mDb.rawQuery("SELECT s.Id_Product, p.Product_Name, (s.In_Stock + 100) / s.Required_Quantity AS Months FROM State s INNER JOIN Product p ON s.Id_Product = p.Id WHERE s.Id_Department = ? ORDER BY s.Id_Product", new String[]{String.valueOf(selected_department)});
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        bases.add(new DasaBannih(cursor.getString(0),cursor.getString(1),cursor.getString(2),null,null));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
        }
        else {
            selected_dep = findViewById(R.id.select_department);
            selected_dep.setVisibility(View.GONE);
            if(gl_i != 8) {
                if(gl_i == 1) {
                    bases.add(new DasaBannih("Id_Firm", "Department", "Summ",null,null));
                    cursor = mDb.rawQuery("SELECT d.Id_Firm, d.Name as 'Department', sum((s.Required_Quantity*6 - s.In_Stock)*dir.Price) as Summ FROM Department d INNER JOIN State s ON s.Id_Department = d.Id INNER JOIN Product prod ON s.Id_Product = prod.Id INNER JOIN Directory dir ON dir.Id_Product = prod.Id INNER JOIN Provider prov ON prov.Id = dir.Id_Provider WHERE dir.Price IN (SELECT min(direct.Price) FROM Directory direct GROUP BY direct.Id_Product) AND (s.Required_Quantity*6 - s.In_Stock) > 0 GROUP BY d.Id ORDER BY Summ DESC LIMIT 1",null);
                    cursor.moveToFirst();
                    if(current_level != 1) {
                        bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), null, null));
                    }
                    else {
                        bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1),"*****", null, null));
                    }
                    cursor.close();
                }
                else {
                    bases.add(new DasaBannih("Id_Firm", "Department",null,null,null));
                    cursor = mDb.rawQuery("SELECT DISTINCT d.Id_Firm, d.Name as 'Department' FROM Department d INNER JOIN State s ON d.Id = s.Id_Department WHERE s.Required_Quantity - s.In_Stock > 0",null);
                    cursor.moveToFirst();
                    for(int i = 0; i < cursor.getCount(); i++) {
                        bases.add(new DasaBannih(cursor.getString(0),cursor.getString(1),null,null,null));
                        cursor.moveToNext();
                    }
                    cursor.close();
                }
            }
            else {
                selected_prod = findViewById(R.id.select_product);
                selected_prod.setVisibility(View.VISIBLE);
                if(selected_product > 0) {
                    bases.add(new DasaBannih("Id_Firm","Department","Id_Product","Product_Name","Count"));
                    cursor = mDb.rawQuery("SELECT d.Id_Firm, d.Name as 'Department', s.Id_Product, prod.Product_Name, sum(s.Required_Quantity*12 - s.In_Stock) as 'Count' FROM Department d INNER JOIN State s ON d.Id = s.Id_Department INNER JOIN Product prod ON s.Id_Product = prod.Id WHERE prod.Id = ? AND (s.Required_Quantity*12 - s.In_Stock) > 0 GROUP BY Id_Department ORDER BY 'Count' DESC LIMIT 1", new String[]{String.valueOf(selected_product)});
                    if(cursor != null && cursor.moveToFirst()) {
                        for (int i = 0; i < cursor.getCount(); i++) {
                            bases.add(new DasaBannih(cursor.getString(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4)));
                            cursor.moveToNext();
                        }
                        cursor.close();
                    }
                    else {
                        bases.clear();
                        bases.add(new DasaBannih("Компания не нуждается в данном товаре",null,null,null,null));
                    }
                }
                else {
                    bases.clear();
                }
                CheckSeekBar(gl_i);
            }
        }
            recyclerView = findViewById(R.id.table);
            recyclerView.setLayoutManager(manager);
            adapter = new DasaAdapter(getBaseContext(), bases, manager);
            recyclerView.setAdapter(adapter);
    }


    private void CheckSeekBar(final int gl_i) {
        seek = findViewById(R.id.seekBar);
        Cursor cursor = mDb.rawQuery("SELECT Id FROM Product", null);
        seek.setMax(cursor.getCount());
        cursor.close();
        seek_text = findViewById(R.id.progress);
        seek.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        seek_text.setText(String.valueOf(progress));
                        selected_product = progress;
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) {

                    }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) {
                        Quer(gl_i);
                    }
                }
        );
    }

    private void RadioCheckDepartment(final int gl_i) {
        radioGroupDepartment = findViewById(R.id.radio);
        radioGroupDepartment.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        if (checkedId == -1) {
                            selected_department = -1;
                        } else if (checkedId == R.id.radioButton) {
                            selected_department = 1;
                            Quer(gl_i);
                        } else if (checkedId == R.id.radioButton2) {
                            selected_department = 2;
                            Quer(gl_i);
                        } else if (checkedId == R.id.radioButton3) {
                            selected_department = 3;
                            Quer(gl_i);
                        }
                    }
                }
        );
    }

    private void ZakupListener() {
        Button but_post = (Button)findViewById(R.id.button25);
        but_post.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, SecondActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }

    private void ChangeListener() {
        Button but_change = (Button)findViewById(R.id.button13);
        but_change.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, ThirdActivity.class);
                        startActivity(intent);
                    }
                }
        );
    }
}