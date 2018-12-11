package app.denode.denode;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.provider.Settings;
import android.service.notification.NotificationListenerService;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

import dbManager.bancoController;
import helpers.AlarmReceiver;

import static android.os.Build.VERSION_CODES.N;
import static app.denode.denode.R.id.boxMan;
import static app.denode.denode.R.id.boxNoi;
import static app.denode.denode.R.id.boxTar;
import static dbManager.DatabaseHandler.diasTarefa;
import static dbManager.DatabaseHandler.horaAlarme;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;
import static helpers.NotificationHelper.ALARM_TYPE_RTC;


public class config_activity extends AppCompatActivity {

    Switch notifica, diario;
    ImageButton salva, volta;
    private PendingIntent pIntent;
    TextView horario;
    TimePickerDialog timePickerDialog;
    int h, m;
    Calendar c;
    NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.config_activity);

        notifica = (Switch) findViewById(R.id.notifOnOff);
        diario = (Switch) findViewById(R.id.diarioOnOff);
        salva = (ImageButton) findViewById(R.id.salvaConfig);
        horario = (TextView) findViewById(R.id.diarioAlarmeTime);
        volta = (ImageButton) findViewById(R.id.volta);
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        c = Calendar.getInstance();
        h = 22;
        m = 00;

        //pesquisar como faz pra pegar o horário que notificação id = 0 pro textview e timepickerdialog
        horario.setText(String.valueOf(h) + ":" + String.valueOf(m));

        volta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        salva.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notifica.isChecked()) {
                    //notificações tarefa ligadas

                } else {
                    bancoController crud = new bancoController(getBaseContext());
                    Cursor cursor = crud.getTarefasNot();

                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

                        String tit = cursor.getString(cursor.getColumnIndex(titleTarefa));
                        String per = cursor.getString(cursor.getColumnIndex(periTarefa));
                        String dias = cursor.getString(cursor.getColumnIndex(diasTarefa));
                        String id = cursor.getString(cursor.getColumnIndex("_id"));
                        String hor = cursor.getString(cursor.getColumnIndex(horaAlarme));

                        //deleta o alarme de cada um dos dias
                        for(int i = 1; i < 8; i++) {
                            if (dias.indexOf(String.valueOf(i)) > -1) {
                                deletaAlarme(i, Integer.parseInt(id + String.valueOf(i)), tit, per, hor);
                            }}
                    }

                    String result = crud.setAllNotsToOff();
                    if(result.equalsIgnoreCase("Erro")){
                        Toast.makeText(getBaseContext(), "Erro ao desligar alarmes.", Toast.LENGTH_SHORT).show();
                    }

                }

                if (diario.isChecked()) {
                    createDiarioNot();
                } else {
                    deleteDiarioNot();
                }
            }
        });

        horario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(config_activity.this, TimePickerDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int selectedH, int selectedM) {

                                horario.setText(selectedH + ":" + selectedM);
                            }
                        }, h, m, true);
                timePickerDialog.show();
            }
        });
    }

    private void deleteDiarioNot() {

        Intent intentAlarm = new Intent(config_activity.this, helpers.AlarmReceiver.class);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, 0);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getDiarioNot());

        String[] time = horario.getText().toString().trim().split(":");
        //não sei se isso vai funcionar, se a pessoa muda o horário, o Intent muda também, na teoria
        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, Integer.parseInt(time[1]));
        c.set(Calendar.HOUR_OF_DAY, Integer.parseInt(time[0]));
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // create the object

        PendingIntent pendingIntent = PendingIntent.getBroadcast(config_activity.this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        //set the alarm for particular time
        AlarmManager am = (AlarmManager) config_activity.this.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(getBaseContext(), "Notificação do diário desligada.", Toast.LENGTH_SHORT).show();
    }


    private void createDiarioNot() {

        Intent intentAlarm = new Intent(config_activity.this, helpers.AlarmReceiver.class);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, 0);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getDiarioNot());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(config_activity.this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);
        Toast.makeText(getBaseContext(), "Notificação do diário ligada.", Toast.LENGTH_SHORT).show();
    }

    private Notification getDiarioNot() {

        Intent intent = new Intent(config_activity.this, diarioHabitos_activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(config_activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Denode");
        builder.setContentText("Organize quais atividades você realizou hoje.");
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_icone_app);
        builder.setColor(Color.parseColor("#6453a2"));
        return builder.build();

    }

    public void deletaAlarme(int dia, long id, String titulo, String periodo, String hora){

        Integer i = (int) id;

        Intent intentAlarm = new Intent(config_activity.this, helpers.AlarmReceiver.class);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, i);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot(i, titulo, periodo));

        String[] time = hora.split(":");
        h = Integer.parseInt(time[0]);
        m = Integer.parseInt(time[1]);

        if(dia == 1){c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);}
        if(dia == 2){c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
        if(dia == 3){c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
        if(dia == 4){c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
        if(dia == 5){c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
        if(dia == 6){c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}
        if(dia == 7){c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);}

        c.set(Calendar.MINUTE, m);
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);

        //set the alarm for particular time
        PendingIntent pendingIntent = PendingIntent.getBroadcast(config_activity.this, i, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        //set the alarm for particular time
        AlarmManager am = (AlarmManager) config_activity.this.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(getBaseContext(), "Alarm" + titulo+ " desligado.", Toast.LENGTH_SHORT).show();
    }

    private Notification getNot(int id, String titulo, String periodo){

        Intent notificationIntent = new Intent(config_activity.this, diarioHabitos_activity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Denode");
        builder.setContentText(" "+ titulo + " no período da " + periodo);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_icone_app);

        return builder.build();
    }

}