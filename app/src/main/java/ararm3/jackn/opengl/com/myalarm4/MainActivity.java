package ararm3.jackn.opengl.com.myalarm4;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ararm3.jackn.opengl.com.myalarm4.util.PreferenceUtil;

public class MainActivity extends AppCompatActivity {
    public static final String ALARM_TIME = "alarm_time";

    Button button;
    ToggleButton alarmButton;
    Calendar alarmCalendar = Calendar.getInstance();
    PreferenceUtil pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button sendButton = findViewById(R.id.nextButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SubActivity.class);
                startActivity(intent);
            }
        });
        pref = new PreferenceUtil(this);
        setupViews();
        setListeners();
    }

    private void setupViews() {
        button = (Button) findViewById(R.id.button);
        alarmButton = (ToggleButton) findViewById(R.id.AlarmButton);

        long alarmTime = pref.getLong(ALARM_TIME);
        if (alarmTime != 0) {
            DateFormat df = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
            Date date = new Date(alarmTime);
            button.setText(df.format(date));
            alarmButton.setChecked(true);
        }
    }

    private void setListeners() {
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int monthOfYear = calendar.get(Calendar.MONTH);
                final int dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
                final int hour = calendar.get(Calendar.HOUR_OF_DAY);
                final int minute = calendar.get(Calendar.MINUTE);
                DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, final int y, final int m, final int d) {
                        TimePickerDialog timePickerDialog = new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                alarmCalendar.set(Calendar.YEAR, y);
                                alarmCalendar.set(Calendar.MONTH, m);
                                alarmCalendar.set(Calendar.DAY_OF_MONTH, d);
                                alarmCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                alarmCalendar.set(Calendar.MINUTE, minute);
                                alarmCalendar.set(Calendar.SECOND, 0);

                                DateFormat df = new SimpleDateFormat("yyyy年MM月dd日  HH:mm");
                                button.setText(df.format(alarmCalendar.getTime()));
                                Toast.makeText(MainActivity.this, "アラームをセットしました", Toast.LENGTH_LONG).show();
                            }
                        }, hour, minute, true);
                        timePickerDialog.show();
                    }
                }, year, monthOfYear, dayOfMonth);
                datePickerDialog.show();


            }
        });

        alarmButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    register(alarmCalendar.getTimeInMillis());
                    Toast.makeText(MainActivity.this, "アラームをONしました", Toast.LENGTH_LONG).show();
                } else {
                    unregister();
                    Toast.makeText(MainActivity.this, "アラームをOFFしました", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    // 登録
    private void register(long alarmTimeMillis) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent = getPendingIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            alarmManager.setAlarmClock(new AlarmManager.AlarmClockInfo(alarmTimeMillis, null), pendingIntent);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTimeMillis, pendingIntent);
        }
        // 保存
        pref.setLong(ALARM_TIME, alarmTimeMillis);
    }

    // 解除
    private void unregister() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent());
        pref.delete(ALARM_TIME);
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(this, ararm3.jackn.opengl.com.myalarm4.AlarmCheckActivity.class);
        intent.setClass(this, ararm3.jackn.opengl.com.myalarm4.AlarmCheckActivity.class);
        // 複数のアラームを登録する場合はPendingIntent.getBroadcastの第二引数を変更する
        // 第二引数が同じで第四引数にFLAG_CANCEL_CURRENTがセットされている場合、2回以上呼び出されたときは
        // あとからのものが上書きされる
        return PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}
