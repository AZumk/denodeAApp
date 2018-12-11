package app.denode.denode;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import dbManager.bancoController;
import helpers.AdapterDiario;
import helpers.AdapterHomeTarefas;

import static android.R.attr.format;
import static android.R.id.empty;
import static app.denode.denode.R.id.diarioListaCheck;
import static app.denode.denode.R.id.empty1;
import static app.denode.denode.R.id.empty2;
import static app.denode.denode.R.id.empty3;
import static app.denode.denode.R.id.refresh;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.tarefaFeita;
import static dbManager.DatabaseHandler.titleTarefa;
import static helpers.AdapterDiario.getRelatorio;

public class diarioHabitos_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int dayofyear, year, day;
    Spinner ultimaSemanaSpinner;
    ImageButton salvar;
    TextView empty1, empty2, empty3;

    String[] ultimaSemana = new String[7];
    String[] dataBanco = new String[7];
    String[] diaDaSemana = new String[7];
    ListView manhaTasks, tardeTasks, noiteTasks;
    ArrayList<Tarefa> manhaMap, tardeMap, noiteMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.diario_habitos_activity);

        salvar = (ImageButton)findViewById(R.id.salvaDiario);
        ultimaSemanaSpinner = (Spinner)findViewById(R.id.ultimaSemanaSpinner);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(40, 40);

        Menu menu = bnve.getMenu();
        MenuItem item = menu.findItem(R.id.menu_diario).setIcon(R.drawable.diarioicoselect);

        final Calendar cal = GregorianCalendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, d MMM");
        SimpleDateFormat banco = new SimpleDateFormat("dd");
        SimpleDateFormat weekDayFormat = new SimpleDateFormat("EEEE");

        //cal.setTime(new Date());

        for(int i = 0; i < 7; i++) {
            ultimaSemana[i] = sdf.format(cal.getTime()).toString();
            dataBanco[i] = banco.format(cal.getTime()).toString();
            diaDaSemana[i] = weekDayFormat.format(cal.getTime()).toString();
            cal.roll(Calendar.DAY_OF_YEAR, false);
        }

        ArrayAdapter<String> semana= new ArrayAdapter<String>(diarioHabitos_activity.this,
                android.R.layout.simple_spinner_item, ultimaSemana);
        semana.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ultimaSemanaSpinner.setAdapter(semana);
        ultimaSemanaSpinner.setOnItemSelectedListener(this);

            int posi = ultimaSemanaSpinner.getSelectedItemPosition();

            if (diaDaSemana[posi].equalsIgnoreCase("segunda")){day = 2;}
            if (diaDaSemana[posi].equalsIgnoreCase("terça")){day = 3;}
            if (diaDaSemana[posi].equalsIgnoreCase("quarta")){day = 4;}
            if (diaDaSemana[posi].equalsIgnoreCase("quinta")){day = 5;}
            if (diaDaSemana[posi].equalsIgnoreCase("sexta")){day = 6;}
            if (diaDaSemana[posi].equalsIgnoreCase("sábado")){day = 7;}
            if (diaDaSemana[posi].equalsIgnoreCase("domingo")){day = 1;}

        final bancoController crud = new bancoController(diarioHabitos_activity.this);
        Cursor cursor = crud.getTarefasDia(day);

        //checa se está retornando algo
        int qtdTarefas = cursor.getCount();

        //vai colocar cada tarefa na lista de seu respectivo periodo
        manhaMap = new ArrayList();
        tardeMap = new ArrayList();
        noiteMap = new ArrayList();


        //String[] arrayColumns = new String[]{titleTarefa, catTarefa};
        //int[] arrayViewsID = new int[]{R.id.titleTarefa, R.id.catTarefa};

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            //adiciona para uma ArrayList todas as tarefas marcadas para o período da manhã
            //preciso pensar como fazer se não houver nenhuma
            int isFeita = crud.isTarefaFeita(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                     dataBanco[posi]);

            boolean value = isFeita > 0;

            if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("M")) {
                Tarefa tarefaMan = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)),
                        value
                );
                manhaMap.add(tarefaMan);

                //adiciona para ArrayList tarefas do periodo da tarde
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("T")) {
                Tarefa tarefaTar = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), value);
                tardeMap.add(tarefaTar);

                //adiciona para ArrayList tarefas da noite
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("N")) {
                Tarefa tarefaNoi = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), value);
                noiteMap.add(tarefaNoi);

            }

        } // fim da criação de listas de tarefa

        //criação de listviews para cada tarefa
        manhaTasks = (ListView) findViewById(R.id.listManha);
        tardeTasks = (ListView) findViewById(R.id.listTarde);
        noiteTasks = (ListView) findViewById(R.id.listNoite);
        empty1 = (TextView) findViewById(R.id.empty1);
        empty2 = (TextView) findViewById(R.id.empty2);
        empty3 = (TextView) findViewById(R.id.empty3);


        if (manhaMap.size() == 0) {
            empty1.setText("Você não tem nenhuma meta para hoje a manhã.");
            ViewGroup.LayoutParams params = empty2.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty2.setLayoutParams(params);
        }
        if (tardeMap.size() == 0) {
            empty2.setText("Você não tem nenhuma meta para hoje a tarde.");
            ViewGroup.LayoutParams params = empty2.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty2.setLayoutParams(params);
        }
        if (noiteMap.size() < 1) {
            empty3.setText("Você não tem nenhuma meta para hoje a tarde.");
            ViewGroup.LayoutParams params = empty3.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty3.setLayoutParams(params);
        }

        AdapterDiario todayMTask = new AdapterDiario(this, manhaMap);
        manhaTasks.setAdapter(todayMTask);

        AdapterDiario todayTTask = new AdapterDiario(this, tardeMap);
        tardeTasks.setAdapter(todayTTask);

        AdapterDiario todayNTask = new AdapterDiario(this, noiteMap);
        noiteTasks.setAdapter(todayNTask);

        manhaTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view = manhaTasks.getChildAt(position);
                Tarefa tarefa = manhaMap.get(position);
                CheckBox cb = (CheckBox) view.findViewById(diarioListaCheck);
                if(tarefa.getFeita() == false) {
                    tarefa.setFeita(true);
                    cb.setChecked(true);
                } else {
                    tarefa.setFeita(false);
                    cb.setChecked(false);
                }
            }
        });

        tardeTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view = tardeTasks.getChildAt(position);
                Tarefa tarefa = tardeMap.get(position);
                CheckBox cb = (CheckBox) view.findViewById(diarioListaCheck);
                if(tarefa.getFeita() == false) {
                    tarefa.setFeita(true);
                    cb.setChecked(true);
                } else {
                    tarefa.setFeita(false);
                    cb.setChecked(false);
                }
            }
        });

        noiteTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                view = noiteTasks.getChildAt(position);
                Tarefa tarefa = noiteMap.get(position);
                CheckBox cb = (CheckBox) view.findViewById(diarioListaCheck);
                if(tarefa.getFeita() == false) {
                    tarefa.setFeita(true);
                    cb.setChecked(true);
                } else {
                    tarefa.setFeita(false);
                    cb.setChecked(false);
                }
            }
        });

        salvar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<Tarefa, Boolean> checklist = new HashMap<Tarefa, Boolean>();

                //pega data do spinner e usa a mesma data no formato certo da outra array
                int pos = ultimaSemanaSpinner.getSelectedItemPosition();
                String date = dataBanco[pos];

                    for (int i = 0; i < manhaTasks.getCount(); i++) {

                        v = manhaTasks.getChildAt(i);
                        Tarefa tarefa = manhaMap.get(i);
                        CheckBox cb = (CheckBox) v.findViewById(diarioListaCheck);

                        if (cb.isChecked()) {
                            checklist.put(tarefa, true);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    true, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(), result,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            checklist.put(tarefa, false);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    false, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }

                    for (int i = 0; i < tardeTasks.getCount(); i++) {

                        v = tardeTasks.getChildAt(i);
                        Tarefa tarefa = tardeMap.get(i);
                        CheckBox cb = (CheckBox) v.findViewById(diarioListaCheck);

                        if (cb.isChecked()) {
                            checklist.put(tarefa, true);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    true, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(), result,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            checklist.put(tarefa, false);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    false, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(), result, Toast.LENGTH_SHORT).show();
                        }
                    }
                    for (int i = 0; i < noiteTasks.getCount(); i++) {

                        v = noiteTasks.getChildAt(i);
                        Tarefa tarefa = noiteMap.get(i);
                        CheckBox cb = (CheckBox) v.findViewById(diarioListaCheck);

                        if (cb.isChecked()) {
                            checklist.put(tarefa, true);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    true, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(), result,
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            checklist.put(tarefa, false);
                            String result = crud.updateControle(String.valueOf(tarefa.getId()),
                                    false, date, tarefa.getCategoria());

                            Toast.makeText(getApplicationContext(),result, Toast.LENGTH_SHORT).show();
                        }
                    }

            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:

                                Intent goToHome = new Intent(diarioHabitos_activity.this, home_activity.class);
                                startActivity(goToHome);
                                break;
                            case R.id.menu_agenda:

                                Intent goToAgenda = new Intent(diarioHabitos_activity.this, agenda_activity.class);
                                startActivity(goToAgenda);
                                break;
                            case R.id.menu_diario:

                                Intent goToDiario = new Intent(diarioHabitos_activity.this, diarioHabitos_activity.class);
                                startActivity(goToDiario);
                                break;
                            case R.id.menu_conteudo:

                                Intent goToConteudo = new Intent(diarioHabitos_activity.this, conteudo_activity.class);
                                startActivity(goToConteudo);
                                break;
                            case R.id.menu_perfil:

                                Intent goToPerfil = new Intent(diarioHabitos_activity.this, perfil_activity.class);
                                startActivity(goToPerfil);
                                break;
                        }
                        return true;
                    }
                });
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        int posi = ultimaSemanaSpinner.getSelectedItemPosition();

        if (diaDaSemana[posi].equalsIgnoreCase("segunda")){day = 2;}
        if (diaDaSemana[posi].equalsIgnoreCase("terça")){day = 3;}
        if (diaDaSemana[posi].equalsIgnoreCase("quarta")){day = 4;}
        if (diaDaSemana[posi].equalsIgnoreCase("quinta")){day = 5;}
        if (diaDaSemana[posi].equalsIgnoreCase("sexta")){day = 6;}
        if (diaDaSemana[posi].equalsIgnoreCase("sábado")){day = 7;}
        if (diaDaSemana[posi].equalsIgnoreCase("domingo")){day = 1;}

        final bancoController crud = new bancoController(diarioHabitos_activity.this);
        Cursor cursor = crud.getTarefasDia(day);

        //checa se está retornando algo
        int qtdTarefas = cursor.getCount();

        //vai colocar cada tarefa na lista de seu respectivo periodo
        manhaMap = new ArrayList();
        tardeMap = new ArrayList();
        noiteMap = new ArrayList();


        //String[] arrayColumns = new String[]{titleTarefa, catTarefa};
        //int[] arrayViewsID = new int[]{R.id.titleTarefa, R.id.catTarefa};

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            int isFeita = crud.isTarefaFeita(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                    dataBanco[posi]);

            boolean value = isFeita > 0;

            if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("M")) {
                Tarefa tarefaMan = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), value
                );
                manhaMap.add(tarefaMan);

                //adiciona para ArrayList tarefas do periodo da tarde
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("T")) {
                Tarefa tarefaTar = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), value);
                tardeMap.add(tarefaTar);

                //adiciona para ArrayList tarefas da noite
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("N")) {
                Tarefa tarefaNoi = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), value);
                noiteMap.add(tarefaNoi);

            }

        } // fim da criação de listas de tarefa

        //criação de listviews para cada tarefa
        manhaTasks = (ListView) findViewById(R.id.listManha);
        tardeTasks = (ListView) findViewById(R.id.listTarde);
        noiteTasks = (ListView) findViewById(R.id.listNoite);
        empty1 = (TextView) findViewById(R.id.empty1);
        empty2 = (TextView) findViewById(R.id.empty2);
        empty3 = (TextView) findViewById(R.id.empty3);

        if (manhaMap.size() == 0) {
            empty1.setText("Você não tem nenhuma meta para hoje a manhã.");
            ViewGroup.LayoutParams params = empty1.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty1.setLayoutParams(params);
        } else {
            empty1.setText("");
            ViewGroup.LayoutParams params = empty1.getLayoutParams();
            params.height = 0;
            empty1.setLayoutParams(params);
        }
        if (tardeMap.size() == 0) {
            empty2.setText("Você não tem nenhuma meta para hoje a tarde.");
            ViewGroup.LayoutParams params = empty2.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty2.setLayoutParams(params);
        }  else {
            empty2.setText("");
            ViewGroup.LayoutParams params = empty2.getLayoutParams();
            params.height = 0;
            empty2.setLayoutParams(params);
        }
        if (noiteMap.size() == 0) {
            empty3.setText("Você não tem nenhuma meta para hoje a tarde.");
            ViewGroup.LayoutParams params = empty3.getLayoutParams();
            params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            empty3.setLayoutParams(params);
        } else {
            empty3.setText("");
            ViewGroup.LayoutParams params = empty3.getLayoutParams();
            params.height = 0;
            empty3.setLayoutParams(params);
        }

        AdapterDiario todayMTask = new AdapterDiario(this, manhaMap);
        manhaTasks.setAdapter(todayMTask);

        AdapterDiario todayTTask = new AdapterDiario(this, tardeMap);
        tardeTasks.setAdapter(todayTTask);

        AdapterDiario todayNTask = new AdapterDiario(this, noiteMap);
        noiteTasks.setAdapter(todayNTask);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}