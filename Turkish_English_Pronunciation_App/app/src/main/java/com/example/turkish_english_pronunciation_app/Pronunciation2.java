package com.example.turkish_english_pronunciation_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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

public class Pronunciation2 extends AppCompatActivity {

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
        setContentView(R.layout.activity_pronunciation2);

        sourceEdt = findViewById(R.id.edtSource);
        micIV = findViewById(R.id.mic);
        textView1 = findViewById(R.id.textView1);
        textView2 =findViewById(R.id.textView2);
        textView3 =findViewById(R.id.textView3);
        button=findViewById(R.id.button);
        volume=findViewById(R.id.volume);

        Pronunciation2.RandomWordGenerator wordGenerator = new Pronunciation2.RandomWordGenerator();
        textView1.setText(wordGenerator.generateRandomWord());

        micIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                i.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr");
                i.putExtra(RecognizerIntent.EXTRA_PROMPT,"Speak to convert into text");
                try{
                    startActivityForResult(i,REQUEST_PERMISSION_CODE);
                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(Pronunciation2.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

                PackageManager packageManager = getPackageManager();
                if (i.resolveActivity(packageManager) != null) {
                    startActivityForResult(i, REQUEST_PERMISSION_CODE);
                } else {
                    Toast.makeText(Pronunciation2.this, "No app available to handle speech recognition.", Toast.LENGTH_SHORT).show();
                }

                textView2.setText("");
                textView3.setText("");

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generateNewWord();
            }
        });

        volume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = textView1.getText().toString();
                pronounceWord(word);
            }
        });

        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if (status == TextToSpeech.SUCCESS) {
                    // TextToSpeech is initialized successfully
                    Locale locale = new Locale("tr", "TR");
                    int result = textToSpeech.setLanguage(locale);
                    if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                        Toast.makeText(Pronunciation2.this, "Language not supported", Toast.LENGTH_SHORT).show();


                    }
                } else {
                    Toast.makeText(Pronunciation2.this, "Failed to initialize TextToSpeech", Toast.LENGTH_SHORT).show();
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
                    textView2.setText("DOĞRU!");
                } else {
                    textView3.setText("YANLIŞ!");
                }
            }
        }
    }

    public class RandomWordGenerator {
        private List<String> words;

        public RandomWordGenerator() {
            words = new ArrayList<>();
            words.add("elma");
            words.add("kitap");
            words.add("masa");
            words.add("kalem");
            words.add("ev");
            words.add("araba");
            words.add("sokak");
            words.add("telefon");
            words.add("kapı");
            words.add("yemek");
            words.add("su");
            words.add("sandalye");
            words.add("ayakkabı");
            words.add("televizyon");
            words.add("müzik");
            words.add("bilgisayar");
            words.add("saat");
            words.add("ayna");
            words.add("gözlük");
            words.add("park");
            words.add("oda");
            words.add("spor");
            words.add("yazı");
            words.add("yıl");
            words.add("turist");
            words.add("tarih");
            words.add("resim");
            words.add("gökyüzü");
            words.add("tatil");
            words.add("para");
            words.add("sebze");
            words.add("meyve");
            words.add("renk");
            words.add("aile");
            words.add("sevgi");
            words.add("sağlık");
            words.add("arkadaş");
            words.add("rüya");
            words.add("şehir");
            words.add("barış");
            words.add("hava");
            words.add("öğrenci");
            words.add("dans");
            words.add("sinema");
            words.add("mektup");
            words.add("tatlı");
            words.add("göl");
            words.add("hediye");
            words.add("balık");
            words.add("şans");
        }

        public String generateRandomWord() {
            Random random = new Random();
            int index = random.nextInt(words.size());
            return words.get(index);
        }
    }

    private void generateNewWord() {
        Pronunciation2.RandomWordGenerator wordGenerator = new Pronunciation2.RandomWordGenerator();
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