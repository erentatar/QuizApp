package com.example.android.quizapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.SparseBooleanArray;
import android.view.MotionEvent;
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

    private int score;
    private int numOfChoices;
    private int questionNum = 1;
    private int questionCount = questionLibrary.getQuestionCount();

    private Boolean calculated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        displayQuestion(questionNum);
    }

    private void displayQuestion(int num) {
        setContentView(R.layout.activity_main);

        layoutChoice = findViewById(R.id.layoutChoice);
        question = questionLibrary.getQuestionList().get(num - 1);
        numOfChoices = question.getChoices().size();
        calculated = false;

        TextView txtQuestionNumber = findViewById(R.id.txtQuestionNumber);
        txtQuestionNumber.setText(String.format("Question %1$d of %2$d", num, questionCount));

        TextView txtQuestionText = findViewById(R.id.txtQuestionText);
        txtQuestionText.setText(question.getQuestionText());

        ImageView imgQuestion = findViewById(R.id.imgQuestion);
        int resourceId = getResources().getIdentifier(question.getImageName(), "drawable", getPackageName());
        imgQuestion.setImageResource(resourceId);

        if (question.getQuestionType() == QuestionType.MultipleChoice) {
            addListView();
        } else if (question.getQuestionType() == QuestionType.SingleChoice) {
            addRadioGroup();
        } else if (question.getQuestionType() == QuestionType.Text) {
            addEditText();
        } else if (question.getQuestionType() == QuestionType.YesNo) {
            addRadioGroup();
        }
    }

    private void addListView() {
        ListView listView = new ListView(this);
        listView.setId(R.id.listView_Id);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_multiple_choice, question.getChoices());
        listView.setAdapter(arrayAdapter);

        //To achieve scrolling functionality properly in the case that nested scrolling situation happens.
        listView.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;
                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });

        layoutChoice.addView(listView);
    }

    private void addRadioGroup() {
        RadioGroup radioGroup = new RadioGroup(this);
        radioGroup.setOrientation(LinearLayout.VERTICAL);
        radioGroup.setId(R.id.radioGroup_Id);

        for (int i = 0; i < numOfChoices; i++) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setId(1000 + i);
            radioButton.setText(question.getChoices().get(i));
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
            Toast.makeText(this, String.format("Your score: %1$d out of %2$d", score, questionCount), Toast.LENGTH_LONG).show();
    }

    private void calculateScore() {
        if (question.getQuestionType() == QuestionType.MultipleChoice) {
            ListView listView = findViewById(R.id.listView_Id);
            SparseBooleanArray sparseBooleanArray = listView.getCheckedItemPositions();
            if (sparseBooleanArray.size() == 0) return;
            int numOfCorrectAnswers = 0;

            for (int i = 0; i < sparseBooleanArray.size(); i++) {
                int key = sparseBooleanArray.keyAt(i);
                if (sparseBooleanArray.get(key))
                    if (question.getAnswers().contains(listView.getItemAtPosition(key).toString()))
                        numOfCorrectAnswers++;
                    else {
                        //if there exists at least one checked incorrect answer
                        numOfCorrectAnswers = 0;
                        break;
                    }
            }

            if (numOfCorrectAnswers == question.getAnswers().size())
                score++;

        } else if (question.getQuestionType() == QuestionType.SingleChoice) {
            RadioGroup radioGroup = findViewById(R.id.radioGroup_Id);
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) return;
            RadioButton radioButton = radioGroup.findViewById(checkedId);
            String answer = radioButton.getText().toString();
            if (question.getAnswers().contains(answer))
                score++;

        } else if (question.getQuestionType() == QuestionType.Text) {
            EditText editText = findViewById(R.id.editText_Id);
            String answer = editText.getText().toString().trim();
            if (answer.isEmpty()) return;
            if (question.getAnswers().get(0).equalsIgnoreCase(answer))
                score++;

        } else if (question.getQuestionType() == QuestionType.YesNo) {
            RadioGroup radioGroup = findViewById(R.id.radioGroup_Id);
            int checkedId = radioGroup.getCheckedRadioButtonId();
            if (checkedId == -1) return;
            RadioButton radioButton = radioGroup.findViewById(checkedId);
            String answer = radioButton.getText().toString();
            if (question.getAnswers().contains(answer))
                score++;
        }

        calculated = true;
    }
}
