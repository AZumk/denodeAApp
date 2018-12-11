package app.denode.denode;

import android.annotation.TargetApi;
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
import android.media.Image;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
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

import dbManager.bancoController;
import helpers.AlarmReceiver;
import helpers.inputValidation;

import static android.R.attr.button;
import static android.R.attr.id;
import static android.R.attr.min;
import static android.R.id.edit;
import static app.denode.denode.AndroidDatabaseManager.indexInfo.index;
import static app.denode.denode.R.array.dias;
import static app.denode.denode.R.id.alarmeTime;
import static app.denode.denode.R.id.boxDom;
import static app.denode.denode.R.id.boxMan;
import static app.denode.denode.R.id.boxNoi;
import static app.denode.denode.R.id.boxQua;
import static app.denode.denode.R.id.boxQui;
import static app.denode.denode.R.id.boxSab;
import static app.denode.denode.R.id.boxSeg;
import static app.denode.denode.R.id.boxSex;
import static app.denode.denode.R.id.boxTar;
import static app.denode.denode.R.id.boxTer;
import static app.denode.denode.R.id.periodoEscolhido;
import static dbManager.DatabaseHandler.alarmeTarefa;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.corCateg;
import static dbManager.DatabaseHandler.diasTarefa;
import static dbManager.DatabaseHandler.horaAlarme;
import static dbManager.DatabaseHandler.nomeCateg;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;

public class editTarefa_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    public String tarefaSelecionada, oldCateg;
    public String newTitulo, newCateg, newDias, newPeriodo, newAlarme, newAlarmeTime;
    TimePickerDialog timePickerDialog;
    int h, m;
    CheckBox boxSeg, boxTer, boxQua, boxQui, boxSex, boxSab, boxDom;
    RadioGroup periodos;
    RadioButton boxMan, boxTar, boxNoi;
    TextView horario;
    ImageView tag;
    Spinner inputCategoria;
    Calendar c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_tarefa_activity);

        tarefaSelecionada = getIntent().getStringExtra("idDetalhe");

        final EditText titulo = (EditText) findViewById(R.id.edit_tarefaTitulo);
        Spinner categoria = (Spinner) findViewById(R.id.editCategoria);

        c = Calendar.getInstance();

        boxSeg = (CheckBox)findViewById(R.id.editBoxSeg);
        boxTer = (CheckBox)findViewById(R.id.editBoxTer);
        boxQua = (CheckBox)findViewById(R.id.editBoxQua);
        boxQui = (CheckBox)findViewById(R.id.editBoxQui);
        boxSex = (CheckBox)findViewById(R.id.editBoxSex);
        boxSab = (CheckBox)findViewById(R.id.editBoxSab);
        boxDom = (CheckBox)findViewById(R.id.editBoxDom);

        tag = (ImageView)findViewById(R.id.tag);
        periodos = (RadioGroup) findViewById(periodoEscolhido);
        boxMan = (RadioButton) findViewById(R.id.editBoxMan);
        boxTar = (RadioButton) findViewById(R.id.editBoxTar);
        boxNoi = (RadioButton) findViewById(R.id.editBoxNoi);

        horario = (TextView)findViewById(R.id.editAlTime);
        final Switch alarme = (Switch) findViewById(R.id.editAlarme);
        //final TimePicker alarmeTime = (TimePicker)findViewById(R.id.editAlameTime);

        ImageButton salvar = (ImageButton) findViewById(R.id.salva);
        ImageButton voltar = (ImageButton) findViewById(R.id.volta);
        final bancoController crud = new bancoController(editTarefa_activity.this);
        Cursor cursor = crud.getTarefa(tarefaSelecionada);
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

        inputCategoria = (Spinner) findViewById(R.id.editCategoria);
        // Create an ArrayAdapter using the string array and a default spinner layout

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(editTarefa_activity.this,
                android.R.layout.simple_spinner_item, ctg);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        inputCategoria.setAdapter(adapter);
        inputCategoria.setOnItemSelectedListener(this);
        categoria.setOnItemSelectedListener(this);


        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            titulo.setText(cursor.getString(cursor.getColumnIndex(titleTarefa)));
            categoria.setSelection(getIndex(categoria, cursor.getString(cursor.getColumnIndex(catTarefa))));

            oldCateg = cursor.getString(cursor.getColumnIndex(catTarefa));
            if(oldCateg.equalsIgnoreCase("Exercício")){ tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Verde));}
            else if(oldCateg.equalsIgnoreCase("Relaxamento")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Laranja));}
            else if(oldCateg.equalsIgnoreCase("Alimentação")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Rosa));}
            else if(oldCateg.equalsIgnoreCase("Estudos")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoClaro));}
            else if(oldCateg.equalsIgnoreCase("Cuidados pessoais")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Amarelo));}
            else {
                String cor = "";
                Cursor color = crud.getCategColor(oldCateg);
                for (color.moveToFirst(); !color.isAfterLast(); color.moveToNext()){
                    cor = color.getString(color.getColumnIndex(corCateg));
                }
                if(cor.equalsIgnoreCase("VerdeMusgo")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.VerdeMusgo));}
                else if(cor.equalsIgnoreCase("RoxoEscuro")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoEscuro));}
                else if(cor.equalsIgnoreCase("Laranja")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Laranja));}
                else if(cor.equalsIgnoreCase("Verde")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Verde));}
                else if(cor.equalsIgnoreCase("Rosa")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Rosa));}
                else if(cor.equalsIgnoreCase("Amarelo")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Amarelo));}

                }
                    String diasDaTarefa = cursor.getString(cursor.getColumnIndex(diasTarefa));

                    if(diasDaTarefa.indexOf("2") > -1){boxSeg.setChecked(true);}
                    if(diasDaTarefa.indexOf("3") > -1){boxTer.setChecked(true);}
                    if(diasDaTarefa.indexOf("4") > -1){boxQua.setChecked(true);}
                    if(diasDaTarefa.indexOf("5") > -1){boxQui.setChecked(true);}
                    if(diasDaTarefa.indexOf("6") > -1){boxSex.setChecked(true);}
                    if(diasDaTarefa.indexOf("7") > -1){boxSab.setChecked(true);}
                    if(diasDaTarefa.indexOf("1") > -1){boxDom.setChecked(true);}

                    if ((cursor.getString(cursor.getColumnIndex(periTarefa))).equalsIgnoreCase("M")) {
                        periodos.check(R.id.editBoxMan);
                    } else if ((cursor.getString(cursor.getColumnIndex(periTarefa))).equalsIgnoreCase("T")) {
                        periodos.check(R.id.editBoxTar);
                    } else if ((cursor.getString(cursor.getColumnIndex(periTarefa))).equalsIgnoreCase("N")) {
                        periodos.check(R.id.editBoxNoi);
                    }

                    if(cursor.getString(cursor.getColumnIndex(alarmeTarefa)).equalsIgnoreCase("0")) {
                        alarme.setChecked(false);
                       horario.setTextColor(ContextCompat.getColor(editTarefa_activity.this, R.color.Cinza));

                    } else if (cursor.getString(cursor.getColumnIndex(alarmeTarefa)).equalsIgnoreCase("1")){
                        alarme.setChecked(true);
                        horario.setTextColor(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoClaro));
                    }

            String horadoalarme = cursor.getString(cursor.getColumnIndex(horaAlarme));
            horario.setText(horadoalarme);
            String[] time = horadoalarme.split(":");
            h = Integer.parseInt(time[0]);
            m = Integer.parseInt(time[1]);

            }

            alarme.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        horario.setTextColor(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoClaro));
                    } else {
                        horario.setTextColor(ContextCompat.getColor(editTarefa_activity.this, R.color.Cinza));
                    }
                }
            });

            horario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Launch Time Picker Dialog
                timePickerDialog = new TimePickerDialog(editTarefa_activity.this, TimePickerDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int selectedH, int selectedM) {

                                horario.setText(selectedH + ":" + selectedM);
                            }
                        }, h, m, true);
                timePickerDialog.show();
            }
        });

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            public void onClick(View v) {
                StringBuilder dias = new StringBuilder();
                if(boxSeg.isChecked()){dias.append("2");}
                if(boxTer.isChecked()){dias.append("3");}
                if(boxQua.isChecked()){dias.append("4");}
                if(boxQui.isChecked()){dias.append("5");}
                if(boxSex.isChecked()){dias.append("6");}
                if(boxSab.isChecked()){dias.append("7");}
                if(boxDom.isChecked()){dias.append("1");}
                newDias = dias.toString();

                boolean validDia;
                if(dias.length() < 1){ validDia = false;} else {validDia = true;}
                boolean validTit = inputValidation.notEmpty(titulo);
                boolean valPer = inputValidation.isRadioChecked(periodos);

                if(validDia && validTit && valPer) {

                    newTitulo = titulo.getText().toString().trim();

                    if (boxMan.isChecked()) {
                        newPeriodo = "M";
                    } else if (boxTer.isChecked()) {
                        newPeriodo = "T";
                    } else if (boxNoi.isChecked()) {
                        newPeriodo = "N";
                    } else {
                        newPeriodo = "T";
                    }

                    if (alarme.isChecked()) {
                        newAlarme = "1";
                        newAlarmeTime = horario.getText().toString().trim();

                        Cursor cursor = crud.getTarefaId();
                        for (int i = 1; i < 8; i++) {
                            if (newDias.indexOf(String.valueOf(i)) > -1) {
                                modAlarme(i, Integer.parseInt(tarefaSelecionada + String.valueOf(i)));
                            }
                        }
                    } else {
                        for (int i = 1; i < 8; i++) {
                            if (newDias.indexOf(String.valueOf(i)) > -1) {
                                disableNot(i, Integer.parseInt(tarefaSelecionada + String.valueOf(i)));
                            }
                            newAlarme = "0";
                            newAlarmeTime = "00:00";
                        }
                    }

                    String resultado = crud.updateTarefa(tarefaSelecionada, newTitulo, newCateg, newDias, newPeriodo, newAlarme, newAlarmeTime);
                    Toast.makeText(getBaseContext(), resultado, Toast.LENGTH_SHORT).show();
                } else {
                    if(!validDia){Toast.makeText(getBaseContext(), "Escolha os dias para essa tarefa.", Toast.LENGTH_SHORT).show();}
                    if(!validTit){Toast.makeText(getBaseContext(), "Adicione um título a essa tarefa.", Toast.LENGTH_SHORT).show();}
                    if(!valPer){Toast.makeText(getBaseContext(), "Escolha um período para essa tarefa.", Toast.LENGTH_SHORT).show();}

                }
            }
        });
    }


    private int getIndex(Spinner spinner, String oldcateg){
        int index = 0;
        for (int i = 0; i < spinner.getCount(); i++){
            if(spinner.getItemAtPosition(i).toString().equalsIgnoreCase(oldcateg)){
                index = i;
                break;
            }
        }
        return index;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        newCateg = parent.getItemAtPosition(position).toString();
        if(newCateg.equalsIgnoreCase("Exercício")){ tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Verde));}
        else if(newCateg.equalsIgnoreCase("Relaxamento")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Laranja));}
        else if(newCateg.equalsIgnoreCase("Alimentação")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Rosa));}
        else if(newCateg.equalsIgnoreCase("Estudos")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoClaro));}
        else if(newCateg.equalsIgnoreCase("Cuidados pessoais")){tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Amarelo));}
        else {
            bancoController crud = new bancoController(getBaseContext());
            String cor = "";
            Cursor color = crud.getCategColor(oldCateg);
            for (color.moveToFirst(); !color.isAfterLast(); color.moveToNext()){
                cor = color.getString(color.getColumnIndex(corCateg));
            }
            if(cor.equalsIgnoreCase("VerdeMusgo")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.VerdeMusgo));}
            else if(cor.equalsIgnoreCase("RoxoEscuro")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.RoxoEscuro));}
            else if(cor.equalsIgnoreCase("Laranja")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Laranja));}
            else if(cor.equalsIgnoreCase("Verde")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Verde));}
            else if(cor.equalsIgnoreCase("Rosa")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Rosa));}
            else if(cor.equalsIgnoreCase("Amarelo")) {tag.setColorFilter(ContextCompat.getColor(editTarefa_activity.this, R.color.Amarelo));}

        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        newCateg = oldCateg;
    }

    public void onCheckboxClicked(View view) {
    }

    private void disableNot(int dia, int id) {

        Intent intentAlarm = new Intent(editTarefa_activity.this, helpers.AlarmReceiver.class);

        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, id);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot(id));

        Calendar c = Calendar.getInstance();

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

        PendingIntent pendingIntent = PendingIntent.getBroadcast(editTarefa_activity.this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager am = (AlarmManager) editTarefa_activity.this.getSystemService(ALARM_SERVICE);
        am.cancel(pendingIntent);
        pendingIntent.cancel();
        Toast.makeText(getBaseContext(), "Alarme desligado.", Toast.LENGTH_SHORT).show();
    }


    public void onCheckedChanged(RadioGroup group, int checkedId) {
        // checkedId is the RadioButton selected
    }

    private void modAlarme(int dia, int id) {

        //helpers.AlarmReceiver é a Broadcast Receiver
        Intent intentAlarm = new Intent(editTarefa_activity.this, helpers.AlarmReceiver.class);

        //coloca a notificação no Intent sendo a ID a id da tarefa + o número do dia Ex: 35 (id tarefa = 3, dia = 5)
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, id);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot(id));

        Calendar c = Calendar.getInstance();

        //para cada dia criei uma notificação distinta, que é repetida após uma semana

        if(dia == 1){c.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);}
        if(dia == 2){c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);}
        if(dia == 3){c.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);}
        if(dia == 4){c.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);}
        if(dia == 5){c.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);}
        if(dia == 6){c.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);}
        if(dia == 7){c.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);}

        //valores do TimePickerDialog h  e m
        c.set(Calendar.MINUTE, m);
        c.set(Calendar.HOUR_OF_DAY, h);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // create the object


        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //activity atual/context : editTarefa
        PendingIntent pendingIntent = PendingIntent.getBroadcast(editTarefa_activity.this, id, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 7 * 60 * 60 * 24 * 1000, pendingIntent);

    }

    private Notification getNot(int id){

        String periodo = "";

        if (boxMan.isChecked()) {
            periodo = "Manhã";
        } else if (boxTar.isChecked()) {
            periodo = "Tarde";
        } else if (boxNoi.isChecked()) {
            periodo = "Noite";
        }

        //esse intent faz o click na notificação levar a activity de diário

        Intent intent = new Intent(editTarefa_activity.this, diarioHabitos_activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(editTarefa_activity.this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Denode");
        builder.setContentText(" " + newTitulo + " no período da " + periodo);
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_icone_app);
        builder.setColor(Color.parseColor("#6453a2"));
        return builder.build();

    }

}
