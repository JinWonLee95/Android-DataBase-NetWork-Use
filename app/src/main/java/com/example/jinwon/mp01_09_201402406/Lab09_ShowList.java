package com.example.jinwon.mp01_09_201402406;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Lab09_ShowList extends AppCompatActivity {

    dbHelper helper;
    SQLiteDatabase db;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab09_show_list);

        helper = new dbHelper(this);
        db = helper.getWritableDatabase();
        final Cursor cursor = db.rawQuery("SELECT * FROM movie", null);

        findViewById(R.id.addMovie).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), Lab09_Input.class);
                startActivity(intent);
            }
        });

        ListViewAdapter adapter = new ListViewAdapter();

        final ListView list = findViewById(R.id.list);
        list.setAdapter(adapter);

        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            adapter.addItem(Integer.toString(i + 1), cursor.getString(1));
            cursor.moveToNext();
        }

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView parent, View view, int position, long id) {
                cursor.moveToPosition(position);
                String t = String.valueOf(cursor.getInt(0));

                Intent intent = new Intent(getApplicationContext(), Lab09_Input.class);
                intent.putExtra("id", t);

                startActivity(intent);
                finish();

            }
        });

    }

    class dbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myMovie.db";
        private static final int DATABASE_VERSION = 2;

        public dbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE movie (_id INTEGER PRIMARY KEY AUTOINCREMENT, movie_name TEXT, date TEXT, director TEXT, rating TEXT, nation TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS movie");
            onCreate(db);
        }
    }

    class ListViewAdapter extends BaseAdapter {
        // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
        private ArrayList<ListViewItem> listViewItemList = new ArrayList<ListViewItem>();

        // ListViewAdapter의 생성자
        public ListViewAdapter() {
        }

        // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
        @Override
        public int getCount() {
            return listViewItemList.size();
        }

        // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final int pos = position;
            final Context context = parent.getContext();

            // "listview_item" Layout을 inflate하여 convertView 참조 획득.
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_item, parent, false);
            }

            // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득
            TextView indexView = (TextView) convertView.findViewById(R.id.index);
            TextView movieNameView = (TextView) convertView.findViewById(R.id.movie_name);

            // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
            ListViewItem listViewItem = listViewItemList.get(position);

            // 아이템 내 각 위젯에 데이터 반영
            indexView.setText(listViewItem.getIndex());
            movieNameView.setText(listViewItem.getMovie_name());

            return convertView;
        }

        // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
        @Override
        public long getItemId(int position) {
            return position;
        }

        // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
        @Override
        public Object getItem(int position) {
            return listViewItemList.get(position);
        }

        // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
        public void addItem(String index, String moName) {
            ListViewItem item = new ListViewItem();

            item.setIndex(index);
            item.setMovie_name(moName);

            listViewItemList.add(item);
        }

    }
}
