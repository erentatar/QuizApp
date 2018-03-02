package com.example.android.quizapp;

import java.util.List;

class Question {

    private QuestionType questionType;
    private String questionText;
    private String imageName;
    private List<String> choices;
    private List<String> answers;

    Question(QuestionType questionType, String questionText, String imageName, List<String> choices, List<String> answers) {
        this.questionType = questionType;
        this.questionText = questionText;
        this.imageName = imageName;
        this.choices = choices;
        this.answers = answers;
    }

    QuestionType getQuestionType() {
        return questionType;
    }

    String getQuestionText() {
        return questionText;
    }

    String getImageName() {
        return imageName;
    }

    List<String> getChoices() {
        return choices;
    }

    List<String> getAnswers() {
        return answers;
    }
}
