package com.example.chetanmuliya.entrancetest.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import com.example.chetanmuliya.entrancetest.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 11/13/2017.
 */

public class MyDatabaseHelper extends SQLiteOpenHelper {

    //Database Name
    public static String DATABASE_NAME="questionbank.db";
    private static final int DATABASE_VER=1;
    public static String DATABASE_TABLE="question";

    //all fields
    private static final String KEY_ID="id";
    private static final String QUESTION="question";
    private static final String CHOICE1="choice1";
    private static final String CHOICE2="choice2";
    private static final String CHOICE3="choice3";
    private static final String CHOICE4="choice4";
    private static final String ANSWER="answer";
    private static final String ATTEMPTED = "attempted";

    //create table
    private static final String CREATE_TABLE_QUESTION="CREATE TABLE "
            + DATABASE_TABLE + "("
            + KEY_ID +" INTEGER PRIMARY KEY AUTOINCREMENT,"
            + QUESTION +" TEXT, "
            + CHOICE1 +" TEXT, "
            + CHOICE2 +" TEXT, "
            + CHOICE3 +" TEXT, "
            + CHOICE4 +" TEXT, "
            + ANSWER +" TEXT);";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_TABLE, null, DATABASE_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
         sqLiteDatabase.execSQL(CREATE_TABLE_QUESTION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+CREATE_TABLE_QUESTION);
        onCreate(sqLiteDatabase);
    }

    public long addInitialQuestion(Question question){
        SQLiteDatabase db=this.getWritableDatabase();

        ContentValues values=new ContentValues();
        values.put(QUESTION,question.getQuestion());
        values.put(CHOICE1,question.getChoice(0));
        values.put(CHOICE2,question.getChoice(1));
        values.put(CHOICE3,question.getChoice(2));
        values.put(CHOICE4,question.getChoice(3));
        values.put(ANSWER,question.getAnswer());
        //insert row in question table
        long insert=db.insert(DATABASE_TABLE,null,values);
        return insert;
    }

    public List<Question> getAllQuestionList(){
        List<Question> questionArrayList=new ArrayList<>();
        String selectQuery="SELECT * FROM "+ DATABASE_TABLE;

        SQLiteDatabase db=this.getReadableDatabase();
        Cursor c=db.rawQuery(selectQuery,null);
        if(c.moveToFirst()){
            do{
                Question question=new Question();
                String questionText=c.getString(c.getColumnIndex(QUESTION));
                question.setQuestion(questionText);

                String choice1=c.getString(c.getColumnIndex(CHOICE1));
                question.setChoice(0,choice1);

                String choice2=c.getString(c.getColumnIndex(CHOICE2));
                question.setChoice(1,choice2);

                String choice3=c.getString(c.getColumnIndex(CHOICE3));
                question.setChoice(2,choice3);

                String choice4=c.getString(c.getColumnIndex(CHOICE4));
                question.setChoice(3,choice4);

                String answerText=c.getString(c.getColumnIndex(ANSWER));
                question.setAnswer(answerText);

                questionArrayList.add(question);
            }while (c.moveToNext());
        }
        return questionArrayList;
    }
}
