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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chetanmuliya.entrancetest.Interface.OnClickQuestionNos;
import com.example.chetanmuliya.entrancetest.R;
import com.example.chetanmuliya.entrancetest.adapter.CustomAdapterForQuestions;
import com.example.chetanmuliya.entrancetest.helper.SessionManager;
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

import java.lang.reflect.Array;
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
    private int currentQuestionPosition;
    private boolean clickedSkipbtn=false;
    private ArrayList<Integer> skipQuestionArray;
    private int[] questionCards;
    private List<Boolean> answered;
    private List<String> status;
    private ImageView imageOption1;
    private ImageView imageOption2;
    private ImageView imageOption3;
    private ImageView imageOption4;
    private LinearLayout option1,option2,option3,option4;
    private List<Quiz> quizlist;
    private ProgressDialog progressDialog;
    DrawerLayout drawer;
    RecyclerView recyclerView;
    ImageButton imageButton;
    private SessionManager session;
    String output;
    long seconds = 0;
    MathView choice_1,choice_2,choice_3,choice_4,question;
    String[] tex = {" $$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$",
            "$$\\sum_{i=0}^n i^5 = \\frac{(n^2+n)(6n+1)}{9}$$",
            " $$\\sum_{i=0}^n i^9 = \\frac{(n^5+n)(2n+1)}{4}$$",
            "$$\\sum_{i=0}^n i^11 = \\frac{(n^2+n)(2n+1)}{9}$$"};
    int questionNo=0;
    QuestionBank questionBank;
    Context ctx;
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
        answered=new ArrayList<>();
        status=new ArrayList<>();
        session=new SessionManager(getApplicationContext());
        getServerData();
        choice_1 = (MathView) findViewById(R.id.choice_1);
        choice_2 = (MathView) findViewById(R.id.choice_2);
        choice_3 = (MathView) findViewById(R.id.choice_3);
        choice_4 = (MathView) findViewById(R.id.choice_4);
        question = (MathView) findViewById(R.id.question);
        //Options
        option1 =   (LinearLayout)findViewById(R.id.option1);
        option2 =   (LinearLayout)findViewById(R.id.option2);
        option3 =   (LinearLayout)findViewById(R.id.option3);
        option4 =   (LinearLayout)findViewById(R.id.option4);
        //imageview
        imageOption1 = (ImageView) findViewById(R.id.figure1);
        imageOption2 = (ImageView) findViewById(R.id.figure2);
        imageOption3 = (ImageView) findViewById(R.id.figure3);
        imageOption4 = (ImageView) findViewById(R.id.figure4);
        //ui
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);


        recyclerView=(RecyclerView)findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,5));
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
              questionCards = new int[quizlist.size()];
              for (int i=0;i<quizlist.size();i++){
                  questionCards[i]=i+1;
              }
              adapter=new CustomAdapterForQuestions(ctx,answered,status, questionCards, new OnClickQuestionNos() {
                  @Override
                  public void onClick(View v, int position) {
                      if(answered.get(position).equals(true)){
                          Toast.makeText(getApplicationContext(),"question already attempted",Toast.LENGTH_LONG).show();
                          return;
                      }
                      if(answered.get(position).equals(false)){
                          questionNo = position;
                          currentQuestionPosition = position;
                          Log.d("********", "onClick:getQuestion No:"+questionNo);
                      }
                      drawer.closeDrawer(GravityCompat.END);
                      getQuestion(position);
                  }
              });
              recyclerView.setAdapter(adapter);
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
            if(!answered.get(questionNo+1).equals(true)){
                option1.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option2.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option3.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option4.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            }else{
                option1.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option2.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option3.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                option4.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
                Toast.makeText(getApplicationContext(),"already answered",Toast.LENGTH_LONG).show();

                Log.d("*****", "updateQuestions: option "+(questionNo+1)+" answered :"+session.getSelectedOption(questionNo+1));
                if(session.getSelectedOption(questionNo+1)==1){
                    option1.setBackground(getDrawable(R.drawable.choice_selection));
                    Log.d("*****", "updateQuestions: option "+session.getSelectedOption(questionNo+1));
                }
                if(session.getSelectedOption(questionNo+1)==2){
                    option2.setBackground(getDrawable(R.drawable.choice_selection));
                    Log.d("*****", "updateQuestions: option "+session.getSelectedOption(questionNo+1));
                }
                if(session.getSelectedOption(questionNo+1)==3){
                    option3.setBackground(getDrawable(R.drawable.choice_selection));
                    Log.d("*****", "updateQuestions: option "+session.getSelectedOption(questionNo+1));
                }
                if(session.getSelectedOption(questionNo+1)==4){
                    option4.setBackground(getDrawable(R.drawable.choice_selection));
                    Log.d("*****", "updateQuestions: option "+session.getSelectedOption(questionNo+1));
                }
            }
            getQuestion(questionNo);
            questionNo++;
            Log.d("*******", "updateQuestions: NO "+questionNo);
        }else{
            Toast.makeText(MainActivity.this,"Test Finished",Toast.LENGTH_LONG).show();
            Intent intent=new Intent(MainActivity.this,Dashboard.class);
            startActivity(intent);
            finish();
        }
    }

    public void skipQuestion(View view) {
        Log.d("color", "skipQuestion: "+(questionNo-1));
        clickedSkipbtn =  true;
        adapter.setStatus(currentQuestionPosition,"yellow");
        adapter.notifyDataSetChanged();
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
         if(option1.isPressed()){
             option1.setBackground(getDrawable(R.drawable.option_background));
             option2.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
             option3.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
             option4.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
             session.setAnsweredOptionC(questionNo,1);
             Log.d("*****", "updateQuestions: option "+(questionNo)+" f "+session.getSelectedOption(questionNo));
         }
        if(option2.isPressed()){
            option2.setBackground(getDrawable(R.drawable.option_background));
            option1.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option3.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option4.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            session.setAnsweredOptionC(questionNo,2);
            Log.d("*****", "updateQuestions: option "+(questionNo)+" f "+session.getSelectedOption(questionNo));
        }
        if(option3.isPressed()){
            option3.setBackground(getDrawable(R.drawable.option_background));
            option2.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option1.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option4.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            session.setAnsweredOptionC(questionNo,3);
            Log.d("*****", "updateQuestions: option "+(questionNo)+" f "+session.getSelectedOption(questionNo));
        }
        if(option4.isPressed()){
            option4.setBackground(getDrawable(R.drawable.option_background));
            option2.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option3.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            option1.setBackground(getDrawable(R.drawable.backgroundbuttoncolor));
            session.setAnsweredOptionC(questionNo,4);
            Log.d("*****", "updateQuestions: option "+(questionNo)+" f "+session.getSelectedOption(questionNo));
        }
    }
    void getQuestion(int questionNo){
        Log.d("*********", "getQuestion: Question NO :"+questionNo);
        currentQuestionPosition=questionNo;
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

    public void saveNext(View view) {

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
        Log.d("********", "save answer: "+(currentQuestionPosition));
        adapter.setAnswered(currentQuestionPosition, true);
        adapter.setStatus(currentQuestionPosition,"green");
        adapter.notifyDataSetChanged();
        updateQuestions();
    }
}
