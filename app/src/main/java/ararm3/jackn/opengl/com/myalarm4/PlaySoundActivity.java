package ararm3.jackn.opengl.com.myalarm4;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;
import orz.kassy.shakegesture.ShakeGestureManager;
import ararm3.jackn.opengl.com.myalarm4.util.PreferenceUtil;

public class PlaySoundActivity extends AppCompatActivity{
    Button stop;
    int randnum;
    TextView textView;
    EditText editText;
    int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD |
                WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON |
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.activity_play_sound);

        ShakeGestureManager mGestureManager = new ShakeGestureManager(this, mListener);
        mGestureManager.startSensing();

        startService(new Intent(this, PlaySoundService.class));
        Random rand = new Random();
        randnum = rand.nextInt(10);
        textView = findViewById(R.id.textView2);
        editText=findViewById(R.id.editText);
        if(randnum==0){
            textView.setText("１１×１１");
        }else if(randnum==1){
            textView.setText("９×８＋６");
        }else if(randnum==2){
            textView.setText("１２×１２");
        }else if(randnum==3){
            textView.setText("６０÷３×５");
        }else if(randnum==4){
            textView.setText("１２８÷４×２");
        }else if(randnum==5){
            textView.setText("１＋２＋３＋４＋５＋６＋７＋８＋９");
        }else if(randnum==6){
            textView.setText("５６×２＋２３");
        }else if(randnum==7){
            textView.setText("９９×(８＋２８７－２２)×０");
        }else if(randnum==8){
            textView.setText("１１×１１÷１２１");
        }else if(randnum==9){
            textView.setText("５０×３÷２");
        }

        if(count==10){
            stopsound();
        }
        stop = (Button) findViewById(R.id.stop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(randnum==0&&editText.getText().toString().equals("121")){
                    stopsound();
                }else if(randnum==2&&editText.getText().toString().equals("144")){
                    stopsound();
                }else if(randnum==3&&editText.getText().toString().equals("100")){
                    stopsound();
                }else if(randnum==4&&editText.getText().toString().equals("64")){
                    stopsound();
                }else if(randnum==5&&editText.getText().toString().equals("45")){
                    stopsound();
                }else if(randnum==1&&editText.getText().toString().equals("78")){
                    stopsound();
                }else if(randnum==6&&editText.getText().toString().equals("135")){
                    stopsound();
                }else if(randnum==7&&editText.getText().toString().equals("0")){
                    stopsound();
                }else if(randnum==8&&editText.getText().toString().equals("1")){
                    stopsound();
                }else if(randnum==9&&editText.getText().toString().equals("75")){
                    stopsound();
                }
            }
        });

    }
    public void stopsound(){
        stopService(new Intent(PlaySoundActivity.this, PlaySoundService.class));
        PreferenceUtil pref = new PreferenceUtil(PlaySoundActivity.this);
        pref.delete(ararm3.jackn.opengl.com.myalarm4.MainActivity.ALARM_TIME);
    }
    private ShakeGestureManager.GestureListener mListener = new ShakeGestureManager.GestureListener() {
        @Override
        public void onGestureDetected(int gestureType, int gestureCount) {
            // ジェスチャーを認識したらここが呼ばれる
            System.out.println("認識");
            count++;
        }

        @Override
        public void onMessage(String s) {

        }
    };
}
