package com.example.turkish_english_pronunciation_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.ml.common.modeldownload.FirebaseModelDownloadConditions;
import com.google.firebase.ml.naturallanguage.FirebaseNaturalLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslateLanguage;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslator;
import com.google.firebase.ml.naturallanguage.translate.FirebaseTranslatorOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class Pronunciation extends AppCompatActivity {

    private TextInputEditText sourceEdt;
    private ImageView micIV;
    private TextView textView1;
    private TextView textView2;
    private TextView textView3;
    private Button button;
    private ImageView volume;

    private TextToSpeech textToSpeech;

    private static final int REQUEST_PERMISSION_CODE = 1;
    int languageCode, fromLanguageCode, toLanguageCode = 0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pronunciation);

        sourceEdt = findViewById(R.id.edtSource);
        micIV = findViewById(R.id.mic);
        textView1 = findViewById(R.id.textView1);
        textView2 =findViewById(R.id.textView2);
        textView3 =findViewById(R.id.textView3);
        button=findViewById(R.id.button);
        volume=findViewById(R.id.volume);

        RandomWordGenerator wordGenerator = new RandomWordGenerator();
        textView1.setText(wordGenerator.generateRandomWord());

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text");
                try{
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Pronunciation.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                PackageManager packageManager = getPackageManager();
                if (i.resolveActivity(packageManager) != null) {
                    startActivityForResult(i, REQUEST_PERMISSION_CODE);
                } else {
                    Toast.makeText(Pronunciation.this, "No app available to handle speech recognition.", Toast.LENGTH_SHORT).show();
                }
                textView2.setText("");
                textView3.setText("");

            }
        });

        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = textView1.getText().toString();
                pronounceWord(word);
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewWord();
            }
        });

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech is initialized successfully
                    int result = textToSpeech.setLanguage(Locale.US);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Pronunciation.this, "Language not supported", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(Pronunciation.this, "Failed to initialize TextToSpeech", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }




    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQUEST_PERMISSION_CODE) {
            if(resultCode == RESULT_OK && data != null){
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                sourceEdt.setText(result.get(0));

                String recognizedText = result.get(0);
                String expectedText = textView1.getText().toString();

                if (expectedText.equalsIgnoreCase(recognizedText)) {
                    textView2.setText("Correct!");
                } else {
                    textView3.setText("Incorrect!");
                }
            }
        }
    }

    public class RandomWordGenerator {
        private List<String> words;

        public RandomWordGenerator() {
            words = new ArrayList<>();
            words.add("apple");
            words.add("book");
            words.add("table");
            words.add("pen");
            words.add("house");
            words.add("car");
            words.add("street");
            words.add("phone");
            words.add("door");
            words.add("food");
            words.add("water");
            words.add("chair");
            words.add("shoes");
            words.add("television");
            words.add("music");
            words.add("computer");
            words.add("clock");
            words.add("mirror");
            words.add("glasses");
            words.add("park");
            words.add("room");
            words.add("sport");
            words.add("writing");
            words.add("year");
            words.add("tourist");
            words.add("history");
            words.add("picture");
            words.add("sky");
            words.add("holiday");
            words.add("money");
            words.add("vegetable");
            words.add("fruit");
            words.add("color");
            words.add("family");
            words.add("love");
            words.add("health");
            words.add("friend");
            words.add("dream");
            words.add("city");
            words.add("peace");
            words.add("weather");
            words.add("student");
            words.add("dance");
            words.add("cinema");
            words.add("letter");
            words.add("dessert");
            words.add("lake");
            words.add("gift");
            words.add("fish");
            words.add("luck");
        }

        public String generateRandomWord() {
            Random random = new Random();
            int index = random.nextInt(words.size());
            return words.get(index);
        }
    }

    private void generateNewWord() {
        RandomWordGenerator wordGenerator = new RandomWordGenerator();
        String newWord = wordGenerator.generateRandomWord();
        textView1.setText(newWord);
        textView2.setText("");
        textView3.setText("");

        pronounceWord(newWord);
    }

    private void pronounceWord(String word) {
        if (textToSpeech != null) {
            textToSpeech.speak(word, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }



}