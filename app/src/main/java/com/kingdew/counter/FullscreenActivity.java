package com.kingdew.counter;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.widget.ActionMenuView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.EventListener;

import io.paperdb.Paper;


public class FullscreenActivity extends AppCompatActivity {
    Button mainext;
    Runnable runnable;
    Handler handler=new Handler();
    private String EVENT_DATE_TIME = "2022-02-07 00:00:00";
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private String EVENT_NAME = "My Event";
    private TextView tv_days, tv_hour, tv_minute, tv_second,event_name;
    ProgressBar progressBar;
    ImageView edit;
    private AlertDialog.Builder builder;
    LottieAnimationView animationVie,animationVie2;

    Button contbutton;
    Button popbutton;
    String finaldate;
    TextView date;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);
        initUI();


        animationVie2.setVisibility(View.INVISIBLE);
        if (Paper.book().read("date") == null){
            getData();
        }else {
            EVENT_DATE_TIME=Paper.book().read("date");
            EVENT_NAME=Paper.book().read("event");
            event_name.setText(EVENT_NAME);
            countDownStart();
        }
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.O){

            NotificationChannel channel= new NotificationChannel("My Notification","My Notification", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager manager =getSystemService(NotificationManager.class);
            NotificationChannel channel2= new NotificationChannel("Top","Top", NotificationManager.IMPORTANCE_HIGH);



            manager.createNotificationChannel(channel);
            manager.createNotificationChannel(channel2);
        }




            mainext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog=new Dialog(FullscreenActivity.this);
                dialog.setContentView(R.layout.about_popup);
                TextView user=dialog.findViewById(R.id.uname);
                Button ok=dialog.findViewById(R.id.button2);

                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);


                user.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String url = "https://api.whatsapp.com/send?phone="+"+94764247796";
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);

                    }
                });
                ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                dialog.show();


            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getData();
            }
        });



    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(FullscreenActivity.this,Service.class));
    }

    @Override
    protected void onPause() {
        super.onPause();
        //startService(new Intent(FullscreenActivity.this,Service.class));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //startService(new Intent(FullscreenActivity.this,Service.class));
    }



    private void getData() {


        builder = new AlertDialog.Builder(this);
        View view=getLayoutInflater().inflate(R.layout.popupmsg,null);
        popbutton=view.findViewById(R.id.popup_button);
        contbutton=view.findViewById(R.id.popup_button_next);
        EditText eventName=view.findViewById(R.id.eventName);
        date=view.findViewById(R.id.date);
        builder.setView(view);
        if (Paper.book().read("event") != null){
            EVENT_NAME=Paper.book().read("event");
            eventName.setText(EVENT_NAME);
        }


        AlertDialog alertDialog= builder.create();
        alertDialog.setCancelable(true);
        contbutton.setVisibility(View.GONE);


        contbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (eventName.getText().toString().isEmpty()){
                    eventName.setError("Enter Event Name !");
                }else {

                    Toast.makeText(getApplicationContext(), ""+finaldate, Toast.LENGTH_SHORT).show();
                    EVENT_DATE_TIME=finaldate;
                    EVENT_NAME=eventName.getText().toString();
                    Paper.book().write("date",finaldate);
                    Paper.book().write("event",eventName.getText().toString());
                    countDownStart();
                    event_name.setText(EVENT_NAME);
                    animationVie.setVisibility(View.VISIBLE);
                    animationVie2.setVisibility(View.INVISIBLE);

                    alertDialog.dismiss();
                }

            }
        });

        popbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog();


//                MaterialDatePicker.Builder dateBuilder=MaterialDatePicker.Builder.datePicker();
//                dateBuilder.setTitleText("Select a Date");
//                MaterialDatePicker datePicker= dateBuilder.build();
//                datePicker.show(getSupportFragmentManager(),"MATERIAL_DATE_PICKER");
//                datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
//                    @Override
//                    public void onPositiveButtonClick(Object selection) {
//
//                        //EVENT_DATE_TIME=datePicker.getHeaderText()
//                        date.setText(datePicker.getHeaderText());
//
//
//
//                            popbutton.setVisibility(View.GONE);
//                            contbutton.setVisibility(View.VISIBLE);
//                            contbutton.setEnabled(false);
//
//                    }
//                });
//                datePicker.addOnCancelListener(new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialogInterface) {
//
//                    }
//                });
            }
        });

        alertDialog.show();

    }

    private void datePickerDialog() {

        final Calendar calendar = Calendar.getInstance();


        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);

        DatePickerDialog datePickerDialog=new DatePickerDialog(this);
        datePickerDialog=new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {


                if (String.valueOf(month).length()==1){

                    if (String.valueOf(day).length()==1) {
                        finaldate = year + "-" + "0" +(month + 1) + "-" + "0" + day + " 00:00:00";
                    }else {
                        finaldate = year + "-" + "0" +(month + 1) + "-" +day + " 00:00:00";
                    }

                }else {
                    if (String.valueOf(day).length()==1) {
                        finaldate = year + "-" +(month + 1) + "-" + "0" + day + " 00:00:00";
                    }else {
                        finaldate=year+"-"+(month+1)+"-"+day+" 00:00:00";
                    }

                }

                Toast.makeText(FullscreenActivity.this, ""+finaldate, Toast.LENGTH_SHORT).show();
                date.setText(finaldate);
                popbutton.setVisibility(View.INVISIBLE);
                contbutton.setVisibility(View.VISIBLE);

            }
        },year,month,day);
        datePickerDialog.setCancelable(true);

        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();

    }

    private void initUI() {

        tv_days = findViewById(R.id.tv_days);
        tv_hour = findViewById(R.id.tv_hour);
        tv_minute = findViewById(R.id.tv_minute);
        tv_second = findViewById(R.id.tv_second);
        mainext=findViewById(R.id.button);
        event_name=findViewById(R.id.mainText);
        progressBar=findViewById(R.id.progressBar1);
        Paper.init(getApplicationContext());
        animationVie=findViewById(R.id.animationView);
        animationVie2=findViewById(R.id.animationView2);
        Drawable draw=getDrawable(R.drawable.custom_progressbar);
        edit=findViewById(R.id.Edit);
        progressBar.setProgressDrawable(draw);
    }

    private void countDownStart() {
        runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    handler.postDelayed(this, 1000);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT);
                    Date event_date = dateFormat.parse(EVENT_DATE_TIME);
                    Date current_date = new Date();
                    if (!current_date.after(event_date)) {
                        long diff = event_date.getTime() - current_date.getTime();
                        long Days = diff / (24 * 60 * 60 * 1000);
                        long Hours = diff / (60 * 60 * 1000) % 24;
                        long Minutes = diff / (60 * 1000) % 60;
                        long Seconds = diff / 1000 % 60;
                        //
                        tv_days.setText(String.format("%02d", Days));
                        tv_hour.setText(String.format("%02d", Hours));
                        tv_minute.setText(String.format("%02d", Minutes));
                        tv_second.setText(String.format("%02d", Seconds));
                        progressBar.setMax(60);
                        progressBar.setProgress((int) Minutes);
                    } else {

                        animationVie.setVisibility(View.INVISIBLE);
                        animationVie2.setVisibility(View.VISIBLE);
                        handler.removeCallbacks(runnable);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        handler.postDelayed(runnable, 0);
    }


}