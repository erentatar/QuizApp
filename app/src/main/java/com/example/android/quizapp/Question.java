package com.example.android.quizapp;

import java.util.List;

/**
 * Created by eren.tatar on 27.02.2018.
 */

public class Question {

    public QuestionType questionType;
    public String questionText;
    public String imageName;
    public List<String> choices;
    public List<String> answers;

    public Question(QuestionType questionType, String questionText, String imageName, List<String> choices, List<String> answers) {
        this.questionType = questionType;
        this.questionText = questionText;
        this.imageName = imageName;
        this.choices = choices;
        this.answers = answers;
    }

}
