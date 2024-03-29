package com.example.toharu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.toharu.API.API_Advice;
import com.example.toharu.API.API_Diary;
import com.example.toharu.Model.Advice;
import com.example.toharu.Model.Diary;
import com.example.toharu.Utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WriteActivity extends AppCompatActivity {

    //----------------------------------------------------------------------------------
    // 변수 선언
    //----------------------------------------------------------------------------------
    private final boolean       D = true;
    private final String        TAG = "WriteActivity";

    private ImageButton         back_Write_BTN;
    private ImageButton         save_Write_BTN;
    private Button              finish_dialog_BTN;
    private EditText            diaryArea_Write_ETXT;
    private Dialog              customDialog;

    private TextView            advice_dialog_TXT;

    public  String              getdate;
    private String              selected_emotion;
    private ImageView           emotion_Write_IMG;

    private List<Advice>        adviceList = new ArrayList<>();
    private Random              ran = new Random();
    //----------------------------------------------------------------------------------

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        getdate = getIntent().getStringExtra("mDate2");
        selected_emotion = getIntent().getStringExtra("emotion_img");
        Log.i(TAG, "dd"+getdate);
        init();
    }

    //----------------------------------------------------------------------------------
    // 초기화
    //----------------------------------------------------------------------------------
    public void init() {
        back_Write_BTN = findViewById(R.id.back_Write_BTN);
        save_Write_BTN = findViewById(R.id.save_Write_BTN);
        diaryArea_Write_ETXT = findViewById(R.id.diaryArea_Write_ETXT);
        emotion_Write_IMG = findViewById(R.id.emotion_Write_IMG);
        customDialog = new Dialog(this);
        //----------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------
        // 변수 값 초기화
        //----------------------------------------------------------------------------------
        int img_id = Utils.getImageByName(selected_emotion, getApplicationContext());
        emotion_Write_IMG.setImageResource(img_id);
        //----------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------
        // Back 버튼 이벤트
        //----------------------------------------------------------------------------------
        back_Write_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(WriteActivity.this, CalendarActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                // 중간에 뒤로 갈건지 여부 묻기
               // startActivity(intent);
                finish();
            }
        });
        //----------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------
        // Save 버튼 이벤트
        //----------------------------------------------------------------------------------
        save_Write_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Show();
//                Intent intent = new Intent(WriteActivity.this, CalendarActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
            }
        });
    }
    //----------------------------------------------------------------------------------


    //----------------------------------------------------------------------------------
    // 팝업창 띄우는 함수
    //----------------------------------------------------------------------------------
    public void Show(){

        customDialog.setContentView(R.layout.custom_dialog);
        finish_dialog_BTN = (Button) customDialog.findViewById(R.id.finish_dialog_BTN);
        advice_dialog_TXT = customDialog.findViewById(R.id.advice_dialog_TXT);
        //----------------------------------------------------------------------------------


        //selected_emotion = getIntent().getStringExtra("emotion_img");
        //customDialog.setContentView(R.layout.custom_dialog);
        //btnAccept = (Button) customDialog.findViewById(R.id.diaBTN);
        //adviceTXT = customDialog.findViewById(R.id.adviceTXT);


        //----------------------------------------------------------------------------------
        // 팝업창에서 응원의 말 화면에 세팅
        //----------------------------------------------------------------------------------
        API_Advice.fetchAdvice(selected_emotion, new OnCompletion() {
            @Override
            public void onCompletion(Object object) {
                adviceList = (List<Advice>) object;
                Log.d(Utils.TAG, "adviceList size: " + adviceList.size());
                advice_dialog_TXT.setText(adviceList.get(ran.nextInt((adviceList.size()))).getMsg());
            }
        });
        //----------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------
        // 팝업창에서 "고마워" 버튼 이벤트
        //----------------------------------------------------------------------------------
        finish_dialog_BTN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diary newDiary = new Diary(selected_emotion, getdate, diaryArea_Write_ETXT.getText().toString());
                API_Diary.writeDiaryToDB(newDiary, WriteActivity.this);
                customDialog.dismiss();
                finish();
            }
        });
        //----------------------------------------------------------------------------------


        //----------------------------------------------------------------------------------
        // 팝업창 색 지정 및 화면에 띄우기
        //----------------------------------------------------------------------------------
        customDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        customDialog.show();
        //----------------------------------------------------------------------------------

    }
    //----------------------------------------------------------------------------------

}