package com.example.chetanmuliya.entrancetest.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetanmuliya.entrancetest.Interface.OnClickQuestionNos;
import com.example.chetanmuliya.entrancetest.R;
import com.example.chetanmuliya.entrancetest.adapter.CustomAdapterForQuestions;
import com.example.chetanmuliya.entrancetest.model.QuestionAnswerBank;
import com.example.chetanmuliya.entrancetest.model.QuestionModel;
import com.example.chetanmuliya.entrancetest.model.QuestionResponse;
import com.example.chetanmuliya.entrancetest.model.Quiz;
import com.example.chetanmuliya.entrancetest.question_list.QuestionList;
import com.example.chetanmuliya.entrancetest.rest.ApiClient;
import com.example.chetanmuliya.entrancetest.rest.ApiInterface;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.github.kexanie.library.MathView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private String mAnswer;
    private int mScore=0;
    private int mQuestionNo=0;
    private int[] questionCards;
    private ImageView imageOption1;
    private ImageView imageOption2;
    private ImageView imageOption3;
    private ImageView imageOption4;
    private List<Quiz> quizlist;
    private HashMap<Integer,Boolean> attempted;
    private ProgressDialog progressDialog;
    DrawerLayout drawer;
    RecyclerView recyclerView;
    ImageButton imageButton;
    String output;
    long seconds = 0;
    MathView choice_1,choice_2,choice_3,choice_4,question;
    String[] tex = {" $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$",
            "$$\\sum_{i=0}^n i^5 = \\frac{(n^2+n)(6n+1)}{9}$$",
            " $$\\sum_{i=0}^n i^9 = \\frac{(n^5+n)(2n+1)}{4}$$",
            "$$\\sum_{i=0}^n i^11 = \\frac{(n^2+n)(2n+1)}{9}$$"};
    String questiontext="Q.1 This come from string. You can insert inline formula:" +
            " \\(ax^2 + bx + c = 0\\) " +
            "or displayed formula:";
    int questionNo=0;
    QuestionBank questionBank;
    Context ctx;
    String[] number;
    CustomAdapterForQuestions adapter;
    CountDownTimer timmer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final TextView timmerTV=(TextView)findViewById(R.id.timeleft);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        attempted=new HashMap<>();
        progressDialog = new ProgressDialog(this);
        progressDialog.show();
        progressDialog.setMessage("preparing... questions");
        progressDialog.setCanceledOnTouchOutside(false);
        timmer=new CountDownTimer(7200000,1000) {
            @Override
            public void onTick(long l) {
                timmerTV.setText ("Time left : "+formatTime(l));

                if ( seconds == 0 )
                {
                    //Toast.makeText( getApplicationContext(), "Done", Toast.LENGTH_LONG ).show();
                }
            }

            @Override
            public void onFinish() {
                Intent intent=new Intent(MainActivity.this,StartTest.class);
                startActivity(intent);
                finish();
            }
        }.start();

        questionBank=new QuestionBank();
        quizlist = new ArrayList<>();
        getServerData();
        choice_1 = (MathView) findViewById(R.id.choice_1);
        choice_2 = (MathView) findViewById(R.id.choice_2);
        choice_3 = (MathView) findViewById(R.id.choice_3);
        choice_4 = (MathView) findViewById(R.id.choice_4);
        question = (MathView) findViewById(R.id.question);
        //imageview
        imageOption1 = (ImageView) findViewById(R.id.figure1);
        imageOption2 = (ImageView) findViewById(R.id.figure2);
        imageOption3 = (ImageView) findViewById(R.id.figure3);
        imageOption4 = (ImageView) findViewById(R.id.figure4);
        int questionno=getIntent().getIntExtra("questionNo",0);

        //ui

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        //rightButton=(ImageButton)findViewById(R.id.rightButton);
        number=getResources().getStringArray(R.array.question_no);
        questionCards = new int[quizlist.size()];
        for (int i=1;i<=quizlist.size();i++){
            questionCards[i]=i;
        }

        adapter=new CustomAdapterForQuestions(ctx, questionCards, new OnClickQuestionNos() {
            @Override
            public void onClick(View v, int position) {
                drawer.closeDrawer(GravityCompat.END);
                getQuestion(position);
                mQuestionNo = position;
            }
        });
        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
        recyclerView.setAdapter(adapter);
        //Question library
    }

    private void getServerData() {
        ApiInterface apiService= ApiClient.getClient().create(ApiInterface.class);
      Call<QuestionModel> call=apiService.getQuestionAnswer();
      call.enqueue(new Callback<QuestionModel>() {
          @Override
          public void onResponse(Call<QuestionModel> call, Response<QuestionModel> response) {
              quizlist = response.body().getQuiz();
              Log.d("*****", "quiz size: "+ quizlist.size());
              Log.d("*****", "response: "+ new Gson().toJson(response));
              updateQuestions();
              progressDialog.dismiss();
          }

          @Override
          public void onFailure(Call<QuestionModel> call, Throwable t) {
              Log.d("***RETROFIT***", "onFailure: "+t.toString());
          }
      });
    }


    public String formatTime(long millis)
    {
        output = "";
        seconds = millis / 1000;
        long minutes = seconds / 60;
        long hours=minutes/ 60;

        seconds = seconds % 60;
        minutes = minutes % 60;
        hours=hours%60;

        String secondsD = String.valueOf(seconds);
        String minutesD = String.valueOf(minutes);
        String hoursD=String.valueOf(hours);

        if (seconds < 10)
            secondsD = "0" + seconds;
        if (minutes < 10)
            minutesD = "0" + minutes;

        if (hours < 10)
            hoursD = "0" + hours;

        output = hoursD+" : "+minutesD + " : " + secondsD;

        return output;
    }
    public void updateQuestions(){


        if(questionNo<quizlist.size()){
            getQuestion(questionNo);
            questionNo++;
        }else{
            Toast.makeText(MainActivity.this,"Test Finished",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public void skipQuestion(View view) {
        updateQuestions();
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        } else {
            super.onBackPressed();
        }
    }

    public void openDrawerLayout(View view) {
        if(drawer.isDrawerOpen(GravityCompat.END)){
            drawer.closeDrawer(GravityCompat.END);
        }else{
            drawer.openDrawer(GravityCompat.END);
        }
    }

    public void optionSelected(View view) {
        if(choice_1.getText().equals(mAnswer)){
            mScore++;
        }
        if(choice_2.getText().equals(mAnswer)){
            mScore++;
        }
        if(choice_3.getText().equals(mAnswer)){
            mScore++;
        }
        if(choice_4.getText().equals(mAnswer)){
            mScore++;
        }

        updateQuestions();
    }
    void getQuestion(int questionNo){
        imageOption1.setVisibility(View.GONE);
        imageOption2.setVisibility(View.GONE);
        imageOption3.setVisibility(View.GONE);
        imageOption4.setVisibility(View.GONE);
        if(questionNo<quizlist.size()) {
            String image = null;
            Document doc = Jsoup.parse(quizlist.get(questionNo).getOptions().get(0));
            // option1
            Elements option1 = doc.getElementsByTag("p");
            if (!doc.getElementsByAttribute("src").attr("src").isEmpty()) {
                image = doc.getElementsByAttribute("src").attr("src");
                Log.d("*****", "img: " + doc.getElementsByAttribute("src").attr("src"));
                imageOption1.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(image).into(imageOption1);
            }
            // option2
            Document doc2 = Jsoup.parse(quizlist.get(questionNo).getOptions().get(1));
            Elements option2 = doc2.getElementsByTag("p");
            if (!doc2.getElementsByAttribute("src").attr("src").isEmpty()) {
                image = doc2.getElementsByAttribute("src").attr("src");
                Log.d("*****", "img: " + doc.getElementsByAttribute("src").attr("src"));
                imageOption2.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(image).into(imageOption2);
            }
            // option3
            Document doc3 = Jsoup.parse(quizlist.get(questionNo).getOptions().get(2));
            Elements option3 = doc3.getElementsByTag("p");
            if (!doc3.getElementsByAttribute("src").attr("src").isEmpty()) {
                image = doc3.getElementsByAttribute("src").attr("src");
                Log.d("*****", "img: " + doc.getElementsByAttribute("src").attr("src"));
                imageOption3.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(image).into(imageOption3);
            }
            // option 4
            Document doc4 = Jsoup.parse(quizlist.get(questionNo).getOptions().get(3));
            Elements option4 = doc4.getElementsByTag("p");
            if (!doc3.getElementsByAttribute("src").attr("src").isEmpty()) {
                image = doc4.getElementsByAttribute("src").attr("src");
                Log.d("*****", "img: " + doc.getElementsByAttribute("src").attr("src"));
                imageOption4.setVisibility(View.VISIBLE);
                Picasso.with(getApplicationContext()).load(image).into(imageOption4);
            }

            question.setText("Question no. "+(questionNo+1) + quizlist.get(questionNo).getQuestion());
            choice_1.setText(option1.text());
            choice_2.setText(option2.text());
            choice_3.setText(option3.text());
            choice_4.setText(option4.text());
            mAnswer = quizlist.get(questionNo).getAnswer();
        }
    }
}
