package app.denode.denode;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.AlertDialogLayout;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import dbManager.bancoController;
import helpers.AlarmReceiver;
import helpers.inputValidation;

import static android.R.attr.id;
import static android.app.AlertDialog.THEME_HOLO_LIGHT;
import static app.denode.denode.R.id.addTarefa;
import static app.denode.denode.R.id.inputTitulo;
import static app.denode.denode.R.id.volta;
import static dbManager.DatabaseHandler.corCateg;
import static dbManager.DatabaseHandler.nomeCateg;
import static dbManager.DatabaseHandler.periTarefa;

public class addTarefa_activity extends Activity implements AdapterView.OnItemSelectedListener {

    EditText inputTit;
    CheckBox boxSeg, boxTer, boxQua, boxQui, boxSex, boxSab, boxDom;
    int day1, day2, day3, day4, day5, day6, day7, per1, per2, per3, hour, min, h, m;
    Spinner inputCategoria;
    Switch alarme;
    String tarTit, d, periodos, horaAlarme, tarCat;
    Boolean tarAla;
    ImageButton addTarefa, tabMan, voltar;
    RadioButton  boxMan, boxTar, boxNoi;
    RadioGroup per;
    ImageView tag;
    TextView alarmeTime;
    TimePickerDialog timePickerDialog;
    Calendar c;

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Remove title bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

//Remove notification bar
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//set content view AFTER ABOVE sequence (to avoid crash)
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_tarefa_activity);

        c = Calendar.getInstance();
        h = c.get(Calendar.HOUR_OF_DAY);
        m = c.get(Calendar.MINUTE);

        inputTit = findViewById(inputTitulo);
        inputCategoria = findViewById(R.id.inputCategorias);

        tag = findViewById(R.id.tag);
        voltar = findViewById(volta);
        boxSeg = findViewById(R.id.boxSeg);
        boxTer = findViewById(R.id.boxTer);
        boxQua = findViewById(R.id.boxQua);
        boxQui = findViewById(R.id.boxQui);
        boxSex = findViewById(R.id.boxSex);
        boxSab = findViewById(R.id.boxSab);
        boxDom = findViewById(R.id.boxDom);

        boxMan = findViewById(R.id.boxMan);
        boxTar = findViewById(R.id.boxTar);
        boxNoi = findViewById(R.id.boxNoi);

        per = findViewById(R.id.periodos);

        alarme = findViewById(R.id.inputAlarme);
        alarmeTime = findViewById(R.id.inputAlTime);

        addTarefa = findViewById(R.id.addTarefa);
        tabMan = findViewById(R.id.tableManager);

        alarmeTime.setText(String.valueOf(h) + ":" + String.valueOf(m));

        bancoController crud = new bancoController(getBaseContext());
        inputValidation validation = new inputValidation(getBaseContext());

        Cursor categorias = crud.getCategorias();
        ArrayList<String> ctg = new ArrayList<>();
        ctg.add("Exercício");
        ctg.add("Relaxamento");
        ctg.add("Alimentação");
        ctg.add("Estudos");
        ctg.add("Cuidados pessoais");

        for (categorias.moveToFirst(); !categorias.isAfterLast(); categorias.moveToNext()) {
            ctg.add(categorias.getString(categorias.getColumnIndex(nomeCateg)));
        }

        inputCategoria = findViewById(R.id.inputCategorias);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(addTarefa_activity.this,
                android.R.layout.simple_spinner_item, ctg);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputCategoria.setAdapter(adapter);
        inputCategoria.setOnItemSelectedListener(this);
        tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.Verde));


        alarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // The toggle is disabled
                tarAla = isChecked;
                if(isChecked){
                    alarmeTime.setTextColor(ContextCompat.getColor(addTarefa_activity.this, R.color.RoxoClaro));
                } else {
                    alarmeTime.setTextColor(ContextCompat.getColor(addTarefa_activity.this, R.color.Cinza));
                }
            }
        });


        voltar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        alarmeTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(addTarefa_activity.this, TimePickerDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int selectedH, int selectedM) {

                                alarmeTime.setText(selectedH + ":" + selectedM);
                            }
                        }, h, m, true);
                timePickerDialog.show();
            }
        });


        addTarefa.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


                StringBuilder dias = new StringBuilder();
                if(boxSeg.isChecked()){dias.append("2");}
                if(boxTer.isChecked()){dias.append("3");}
                if(boxQua.isChecked()){dias.append("4");}
                if(boxQui.isChecked()){dias.append("5");}
                if(boxSex.isChecked()){dias.append("6");}
                if(boxSab.isChecked()){dias.append("7");}
                if(boxDom.isChecked()){dias.append("1");}
                d = dias.toString();

                boolean validDia;
                if(dias.length() < 1){ validDia = false;} else {validDia = true;}
                boolean validTit = inputValidation.notEmpty(inputTit);
                boolean valPer = inputValidation.isRadioChecked(per);

                if(validDia && validTit && valPer) {

                    horaAlarme = alarmeTime.getText().toString().trim();
                    tarTit = inputTit.getText().toString().trim();
                    if (boxMan.isChecked()) {
                        periodos = "M";
                    } else if (boxTar.isChecked()) {
                        periodos = "T";
                    } else if (boxNoi.isChecked()) {
                        periodos = "N";
                    }


                    if (tarAla == null) {
                        tarAla = false;
                    }
                    bancoController crud = new bancoController(getBaseContext());

                    String resultado;
                    long id = crud.novaTarefa(tarTit, tarCat, d, periodos, tarAla, horaAlarme);
                    if (tarAla == true) {

                        for (int i = 1; i < 8; i++) {
                            if (d.indexOf(String.valueOf(i)) > -1) {
                                criaAlarme(i, id);
                            }
                        }
                    }
                    Intent goToAgenda = new Intent(addTarefa_activity.this, agenda_activity.class);
                    startActivity(goToAgenda);

                } else {
                    if(!validDia){Toast.makeText(getBaseContext(), "Escolha os dias para essa tarefa.", Toast.LENGTH_SHORT).show();}
                    if(!validTit){Toast.makeText(getBaseContext(), "Adicione um título a essa tarefa.", Toast.LENGTH_SHORT).show();}
                    if(!valPer){Toast.makeText(getBaseContext(), "Escolha um período para essa tarefa.", Toast.LENGTH_SHORT).show();}
                }
                }

        });

    }

    public void criaAlarme(int dia, long id){

        Integer i = (int) id;

        Intent intentAlarm = new Intent(addTarefa_activity.this, helpers.AlarmReceiver.class);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, i);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot(i));

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
        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                24 * 7 * 60 * 60 * 1000, PendingIntent.getBroadcast(addTarefa_activity.this, i,
                        intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));
    }

    private Notification getNot(int id){
        String periodo = "";

        Intent notificationIntent = new Intent(addTarefa_activity.this, diarioHabitos_activity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), id, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        if(boxMan.isChecked()){periodo = "Manhã";}
        else if(boxTar.isChecked()){periodo = "Tarde";}
        else if(boxNoi.isChecked()){periodo = "Noite";}

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Denode");
        builder.setContentText(" "+ tarTit + " no período da " + periodo);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_icone_app);

        return builder.build();
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        tarCat = parent.getItemAtPosition(position).toString();
        if (tarCat.equalsIgnoreCase("Exercício")) {
            tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.Verde));
        } else if (tarCat.equalsIgnoreCase("Relaxamento")) {
            tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.Laranja));
        } else if (tarCat.equalsIgnoreCase("Alimentação")) {
            tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.Rosa));
        } else if (tarCat.equalsIgnoreCase("Estudos")) {
            tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.RoxoClaro));
        } else if (tarCat.equalsIgnoreCase("Cuidados pessoais")) {
            tag.setColorFilter(ContextCompat.getColor(addTarefa_activity.this, R.color.Amarelo));
        } else {
            String cor = "";
            bancoController crud = new bancoController(getBaseContext());
            Cursor color = crud.getCategColor(tarCat);
            for (color.moveToFirst(); !color.isAfterLast(); color.moveToNext()) {
                cor = color.getString(color.getColumnIndex(corCateg));
            }
            if (cor.equalsIgnoreCase("VerdeMusgo")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.VerdeMusgo));
            } else if (cor.equalsIgnoreCase("RoxoEscuro")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.RoxoEscuro));
            } else if (cor.equalsIgnoreCase("Laranja")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.Laranja));
            } else if (cor.equalsIgnoreCase("Verde")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.Verde));
            } else if (cor.equalsIgnoreCase("Rosa")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.Rosa));
            } else if (cor.equalsIgnoreCase("Amarelo")) {
                tag.setColorFilter(ContextCompat.getColor(getBaseContext(), R.color.Amarelo));
            }

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

}
