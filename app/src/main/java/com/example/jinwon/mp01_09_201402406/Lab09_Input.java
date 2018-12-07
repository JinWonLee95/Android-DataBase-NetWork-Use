package com.example.jinwon.mp01_09_201402406;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Lab09_Input extends AppCompatActivity {

    dbHelper helper;
    SQLiteDatabase db;
    EditText movie_name, director, nation, rating, day;
    Button save_btn;
    String id=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lab09);

        helper = new dbHelper(this);
        try {
            db = helper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = helper.getReadableDatabase();
        }

        movie_name = findViewById(R.id.movie_name);
        day = findViewById(R.id.date);
        director = findViewById(R.id.director);
        nation = findViewById(R.id.nation);
        rating = findViewById(R.id.rating);
        save_btn = findViewById(R.id.save);

        Intent i = getIntent();
        if(i.getStringExtra("id") != null){
            save_btn.setVisibility(View.INVISIBLE);
            id = i.getStringExtra("id");
            setValue(id);
        }

    }

    public void setValue(String _id){
        Cursor cursor = db.rawQuery("Select * FROM movie where _id='"+_id+"';",null);

        cursor.moveToFirst();
        movie_name.setText(cursor.getString(1));
        day.setText(cursor.getString(2));
        director.setText(cursor.getString(3));
        rating.setText(cursor.getString(4));
        nation.setText(cursor.getString(5));

    }

    public void delete(View target){
        db.execSQL("DELETE FROM movie WHERE _id ='"+id+"'");
        Toast.makeText(getApplicationContext(), "성공적으로 삭제되었음", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), Lab09_ShowList.class);
        startActivity(i);
        finish();
    }

    public void insert(View target){
        String name = movie_name.getText().toString();
        String director = this.director.getText().toString();
        String date = day.getText().toString();
        String nation = this.nation.getText().toString();
        String rating = this.rating.getText().toString();

        db.execSQL("INSERT INTO movie VALUES (null,'"+name+"','"+date+"','"+director+"','"+rating+"','"+nation+"');");
        Toast.makeText(getApplicationContext(), "성공적으로 추가되었음", Toast.LENGTH_SHORT).show();
        movie_name.setText("");
        this.director.setText("");
        day.setText("");
        this.nation.setText("");
        this.rating.setText("");

        Intent i = new Intent(getApplicationContext(), Lab09_ShowList.class);
        startActivity(i);
        finish();
    }

    public void modify(View target){
        String name = movie_name.getText().toString();
        String director = this.director.getText().toString();
        String date = day.getText().toString();
        String nation = this.nation.getText().toString();
        String rating = this.rating.getText().toString();

        db.execSQL("UPDATE movie set movie_name ='"+name+"', director ='"+director+"', date='"+date+"', nation ='"+nation+"', rating ='"+rating+"' WHERE _id ='"+id+"';");
        Toast.makeText(getApplicationContext(), "성공적으로 수정되었음", Toast.LENGTH_SHORT).show();

        Intent i = new Intent(getApplicationContext(), Lab09_ShowList.class);
        startActivity(i);
        finish();
    }

    class dbHelper extends SQLiteOpenHelper {
        private static final String DATABASE_NAME = "myMovie.db";
        private static final int DATABASE_VERSION = 2;

        public dbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db){
            db.execSQL("CREATE TABLE movie (_id INTEGER PRIMARY KEY AUTOINCREMENT, movie_name TEXT, date TEXT, director TEXT, rating TEXT, nation TEXT)");
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
            db.execSQL("DROP TABLE IF EXISTS movie");
            onCreate(db);
        }
    }
}
