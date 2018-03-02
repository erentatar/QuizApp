package com.example.android.quizapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by eren.tatar on 27.02.2018.
 */

public class QuestionLibrary {

    private ArrayList<Question> questionList = new ArrayList<Question>();

    public QuestionLibrary() {
        questionList.add(new Question(QuestionType.SingleChoice,
                "To which country does this flag belong?",
                "flag",
                Arrays.asList("China", "Turkey", "Canada", "Japan"),
                Arrays.asList("Turkey")));

        questionList.add(new Question(QuestionType.Text,
                "In which city is Atatürk's Mausoleum, Anıtkabir situated?",
                "anitkabir",
                Arrays.asList(""),
                Arrays.asList("Ankara")));

        questionList.add(new Question(QuestionType.MultipleChoice,
                "Which of the following are seen in the picture?",
                "food",
                Arrays.asList("Türk kahvesi", "Baklava", "Çay", "Lokum"),
                Arrays.asList("Türk kahvesi", "Lokum")));

        questionList.add(new Question(QuestionType.SingleChoice,
                "What is the name of that meal?",
                "meal",
                Arrays.asList("Menemen", "Lahmacun", "Mantı", "Kebap"),
                Arrays.asList("Kebap")));

        questionList.add(new Question(QuestionType.SingleChoice,
                "What is the name of that dessert?",
                "dessert",
                Arrays.asList("Helva", "Lokum", "Baklava", "Kadayıf"),
                Arrays.asList("Baklava")));

        questionList.add(new Question(QuestionType.YesNo,
                "The drink in the picture is called Turkish Tea, Çay.",
                "drink",
                Arrays.asList("Yes", "No"),
                Arrays.asList("Yes")));
    }

    public List<Question> getQuestionList(){
        return questionList;
    }

    public int getQuestionCount(){
        return questionList.size();
    }
}
