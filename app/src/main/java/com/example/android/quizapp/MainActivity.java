package com.example.android.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    LinearLayout layoutChoice;
    QuestionLibrary questionLibrary = new QuestionLibrary();
    Question question;

    private int score = 0;
    private int questionNum = 1;
    private int numOfChoices = 0;
    private int questionCount = questionLibrary.getQuestionCount();

    private Boolean calculated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayQuestion(questionNum);
    }

    private void displayQuestion(int num) {
        setContentView(R.layout.activity_main);

        layoutChoice = (LinearLayout) findViewById(R.id.layoutChoice);
        question = questionLibrary.getQuestionList().get(num - 1);
        numOfChoices = question.choices.size();
        calculated = false;

        TextView txtQuestionNumber = (TextView) findViewById(R.id.txtQuestionNumber);
        txtQuestionNumber.setText("Question " + num + " of " + questionCount);

        TextView txtQuestionText = (TextView) findViewById(R.id.txtQuestionText);
        txtQuestionText.setText(question.questionText);

        ImageView imgQuestion = (ImageView) findViewById(R.id.imgQuestion);
        int resourceId = getResources().getIdentifier(question.imageName, "drawable", getPackageName());
        imgQuestion.setImageResource(resourceId);

        if (question.questionType == QuestionType.MultipleChoice) {
            addListView();
        } else if (question.questionType == QuestionType.SingleChoice) {
            addRadioGroup();
        } else if (question.questionType == QuestionType.Text) {
            addEditText();
        } else if (question.questionType == QuestionType.YesNo) {
            addRadioGroup();
        }
    }

    private void addListView() {
        ListView listView = new ListView(this);
        listView.setId(R.id.listView_Id);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, question.choices);
        listView.setAdapter(arrayAdapter);
        layoutChoice.addView(listView);
    }

    private void addRadioGroup() {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setId(R.id.radioGroup_Id);

        for (int i = 0; i < numOfChoices; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(1000 + i);
            radioButton.setText(question.choices.get(i));
            radioGroup.addView(radioButton);
        }
        layoutChoice.addView(radioGroup);
    }

    private void addEditText() {
        EditText editText = new EditText(this);
        editText.setId(R.id.editText_Id);
        LayoutParams editTextParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        editText.setLayoutParams(editTextParams);
        editText.setInputType(InputType.TYPE_TEXT_FLAG_CAP_WORDS);
        editText.setHint("Write the city's name here.");
        layoutChoice.addView(editText);
    }

    public void btnSubmit_Click(View view) {
        if (!calculated) {
            calculateScore();
            if (questionNum != questionCount && calculated)
                displayQuestion(++questionNum);
        }

        if (calculated && questionNum == questionCount)
            Toast.makeText(this, String.format("Finished with %s correct answer(s)!", score), Toast.LENGTH_SHORT).show();
    }

    private void calculateScore() {
        if (question.questionType == QuestionType.MultipleChoice) {
            ListView listView = (ListView) findViewById(R.id.listView_Id);
            SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
            if (sparseBooleanArray.size() == 0) return;
            int numOfCorrectAnswers = 0;

            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                int key = sparseBooleanArray.keyAt(i);
                if (sparseBooleanArray.get(key))
                    if (question.answers.contains(listView.getItemAtPosition(key).toString()))
                        numOfCorrectAnswers++;
            }

            if (numOfCorrectAnswers == question.answers.size())
                score++;

        } else if (question.questionType == QuestionType.SingleChoice) {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_Id);
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) return;
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
            String answer = radioButton.getText().toString();
            if (question.answers.contains(answer))
                score++;

        } else if (question.questionType == QuestionType.Text) {
            EditText editText = (EditText) findViewById(R.id.editText_Id);
            String answer = editText.getText().toString();
            if (answer == null || answer.trim().isEmpty()) return;
            if (question.answers.contains(answer))
                score++;

        } else if (question.questionType == QuestionType.YesNo) {
            RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radioGroup_Id);
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) return;
            RadioButton radioButton = (RadioButton) radioGroup.findViewById(checkedId);
            String answer = radioButton.getText().toString();
            if (question.answers.contains(answer))
                score++;
        }

        calculated = true;
    }
}
