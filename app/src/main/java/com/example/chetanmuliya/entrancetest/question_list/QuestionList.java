package com.example.chetanmuliya.entrancetest.question_list;

import android.content.Context;


import com.example.chetanmuliya.entrancetest.database.MyDatabaseHelper;
import com.example.chetanmuliya.entrancetest.model.Question;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Abhishek on 11/13/2017.
 */

public class QuestionList {

    List<Question> list=new ArrayList<>();
    MyDatabaseHelper myDatabaseHelper;

    public int getLength(){
        return list.size();
    }
    public String getQuestion(int a){
      return  list.get(a).getQuestion();
    }
    public String getChoice(int index,int num){
        return list.get(index).getChoice(num-1);
    }
    public String getCorrectAnswer(int a){
        return list.get(a).getAnswer();
    }

    public void initQuestions(Context context){
        myDatabaseHelper=new MyDatabaseHelper(context);
        list=myDatabaseHelper.getAllQuestionList();

        if(list.isEmpty()){
            myDatabaseHelper.addInitialQuestion(new Question("Q.1 When did google acquire Android?",
                    new String[]{"2001","2006","2008","2003"},"2003"));
            myDatabaseHelper.addInitialQuestion(new Question("Q.2 Which of the following is the indeﬁnite integral of    \\(x^2 + 7\\) ?",
                    new String[]{"$$\\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6}$$","$$\\sum_{i=0}^n i^7 = \\frac{(n^6+n)(7n+1)}{9}$$"," $$\\sum_{i=0}^n i^6 = \\frac{(n^6+n)(3n+1)}{4}$$","$$\\sum_{i=0}^n i^11 = \\frac{(n^3+n)(4n+1)}{12}$$"},"$$\\sum_{i=0}^n i^7 = \\frac{(n^6+n)(7n+1)}{9}$$"));
            myDatabaseHelper.addInitialQuestion(new Question("Q.3 Who is the president of America ?",
                    new String[]{"Modi","Obama","Trump","Washington"},"Trump"));
            myDatabaseHelper.addInitialQuestion(new Question("Q.4 Where is taj mahal located ?",
                    new String[]{"pakistan","india","germany","Australia"},"india"));
            myDatabaseHelper.addInitialQuestion(new Question("Q.5 On which Year India got his Independence ?",
                    new String[]{"1947","1950","1970","1955"},"1947"));

            list=myDatabaseHelper.getAllQuestionList();
        }
    }
}
