package app.denode.denode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Image;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.Calendar;

import dbManager.bancoController;
import helpers.AdapterHomeTarefas;
import app.denode.denode.Tarefa;

import static android.R.attr.data;
import static android.R.id.empty;
import static app.denode.denode.R.color.RoxoEscuro;
import static app.denode.denode.R.id.boxMan;
import static app.denode.denode.R.id.boxNoi;
import static app.denode.denode.R.id.boxTer;
import static app.denode.denode.R.id.cor1;
import static app.denode.denode.R.id.cor2;
import static app.denode.denode.R.id.cor3;
import static app.denode.denode.R.id.cor4;
import static app.denode.denode.R.id.cor5;
import static app.denode.denode.R.id.cor6;
import static app.denode.denode.R.id.empty1;
import static app.denode.denode.R.id.empty2;
import static app.denode.denode.R.id.empty3;
import static app.denode.denode.R.id.refresh;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.nomeCateg;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;

public class agenda_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    int outroDia;
    Spinner diaSemana;
    String diaSelecionado;
    String hoje;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agenda_activity);
        setTheme(R.style.AppTheme);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(40, 40);

        Menu menu = bnve.getMenu();
        MenuItem item = menu.findItem(R.id.menu_agenda).setIcon(R.drawable.agendaicoselect);

        //Toolbar functions
        diaSemana = (Spinner) findViewById(R.id.diasSpinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.dias, android.R.layout.simple_spinner_dropdown_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        diaSemana.setAdapter(adapter);
        diaSemana.setOnItemSelectedListener(this);

        ImageButton refresh = (ImageButton) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agenda_activity.this.recreate();
            }
        });


        //get week day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        //calls bancoControle e função das tarefas do dia
        bancoController crud = new bancoController(agenda_activity.this);
        Cursor cursor = crud.getTarefasDia(day);
        if (day == 1){ hoje = "Domingo";}
        if (day == 2){ hoje = "Segunda";}
        if (day == 3){ hoje = "Terça";}
        if (day == 4){ hoje = "Quarta";}
        if (day == 5){ hoje = "Quinta";}
        if (day == 6){ hoje = "Sexta";}
        if (day == 7){ hoje = "Sábado";}

        int spinnerpos = adapter.getPosition(hoje);
        diaSemana.setSelection(spinnerpos);

        //checa se está retornando algo
        int qtdTarefas = cursor.getCount();

        //vai colocar cada tarefa na lista de seu respectivo periodo
        final ArrayList<Tarefa> manhaMap = new ArrayList();
        final ArrayList<Tarefa> tardeMap = new ArrayList();
        final ArrayList<Tarefa> noiteMap = new ArrayList();


        //String[] arrayColumns = new String[]{titleTarefa, catTarefa};
        //int[] arrayViewsID = new int[]{R.id.titleTarefa, R.id.catTarefa};

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("M")) {
                Tarefa tarefaMan = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                manhaMap.add(tarefaMan);

                //adiciona para ArrayList tarefas do periodo da tarde
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("T")) {
                Tarefa tarefaTar = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                tardeMap.add(tarefaTar);

                //adiciona para ArrayList tarefas da noite
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("N")) {
                Tarefa tarefaNoi = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                noiteMap.add(tarefaNoi);

            }

        } // fim da criação de listas de tarefa

        //criação de listviews para cada tarefa
        ListView manhaTasks = (ListView) findViewById(R.id.listManha);
        ListView tardeTasks = (ListView) findViewById(R.id.listTarde);
        ListView noiteTasks = (ListView) findViewById(R.id.listNoite);
        TextView empty1 = (TextView) findViewById(R.id.empty1);
        TextView empty2 = (TextView) findViewById(R.id.empty2);
        TextView empty3 = (TextView) findViewById(R.id.empty3);

        if (manhaMap.size() == 0) {
            empty1.setText("Você não tem nenhuma meta para hoje a manhã.");
        }
        if (tardeMap.size() == 0) {
            empty2.setText("Você não tem nenhuma meta para hoje a tarde.");
        }
        if (noiteMap.size() == 0) {
            empty3.setText("Você não tem nenhuma meta para hoje a noite.");
        }

        AdapterHomeTarefas todayMTask = new AdapterHomeTarefas(this, manhaMap);
        manhaTasks.setAdapter(todayMTask);

        AdapterHomeTarefas todayTTask = new AdapterHomeTarefas(this, tardeMap);
        tardeTasks.setAdapter(todayTTask);

        AdapterHomeTarefas todayNTask = new AdapterHomeTarefas(this, noiteMap);
        noiteTasks.setAdapter(todayNTask);

        manhaTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                    Tarefa tarefa = manhaMap.get(position);
                    int idTarefa = tarefa.getId();
                    String idString = String.valueOf(idTarefa);
                    detalhe.putExtra("idDetalhe", idString);
                    startActivity(detalhe);
            }
        });

        tardeTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                Tarefa tarefa = tardeMap.get(position);
                int idTarefa = tarefa.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });

        noiteTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                Tarefa tarefa = noiteMap.get(position);
                int idTarefa = tarefa.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
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

                                Intent goToHome = new Intent(agenda_activity.this, home_activity.class);
                                startActivity(goToHome);
                                break;
                            case R.id.menu_agenda:

                                Intent goToAgenda = new Intent(agenda_activity.this, agenda_activity.class);
                                startActivity(goToAgenda);
                                break;
                            case R.id.menu_diario:

                                Intent goToDiario = new Intent(agenda_activity.this, diarioHabitos_activity.class);
                                startActivity(goToDiario);
                                break;
                            case R.id.menu_conteudo:

                                Intent goToConteudo = new Intent(agenda_activity.this, conteudo_activity.class);
                                startActivity(goToConteudo);
                                break;
                            case R.id.menu_perfil:

                                Intent goToPerfil = new Intent(agenda_activity.this, perfil_activity.class);
                                startActivity(goToPerfil);
                                break;
                        }
                        return true;
                    }
                });

        //method do floating action menu
        com.github.clans.fab.FloatingActionMenu menu1;
        com.github.clans.fab.FloatingActionButton menu2, menu3, menu4;
        menu1 = (FloatingActionMenu) findViewById(R.id.fabMenu);
        menu2 = (FloatingActionButton) findViewById(R.id.fabItem1);
        menu3 = (FloatingActionButton) findViewById(R.id.fabItem2);
        menu4 = (FloatingActionButton) findViewById(R.id.fabItem3);

        menu1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menu2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewCatDialog();

            }
        });

        menu3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //NOVA ATIVIDADE/META
                Intent intent = new Intent(agenda_activity.this, addTarefa_activity.class);
                startActivity(intent);

            }
        });

        menu4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DelCatDialog();

            }
        });

    }

    //method para abrir dialogo de nova categoria
    public void NewCatDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.novacat_dialog, null);
        dialogBuilder.setView(dialogView);

        final bancoController crud = new bancoController(getBaseContext());

        final EditText nomeCateg = (EditText) dialogView.findViewById(R.id.newCatNome);
        final RadioGroup cores = (RadioGroup) dialogView.findViewById(R.id.groupCor);

        final RadioButton cor1 = (RadioButton) dialogView.findViewById(R.id.cor1);
        final RadioButton cor2 = (RadioButton) dialogView.findViewById(R.id.cor2);
        final RadioButton cor3 = (RadioButton) dialogView.findViewById(R.id.cor3);
        final RadioButton cor4 = (RadioButton) dialogView.findViewById(R.id.cor4);
        final RadioButton cor5 = (RadioButton) dialogView.findViewById(R.id.cor5);
        final RadioButton cor6 = (RadioButton) dialogView.findViewById(R.id.cor6);

        int selectedId = cores.getCheckedRadioButtonId();

        dialogBuilder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String newCateg = nomeCateg.getText().toString().trim();
                String cor = "RoxoEscuro";
                if(cor1.isChecked()){cor = "RoxoEscuro";}
                if(cor2.isChecked()){cor = "Laranja";}
                if(cor3.isChecked()){cor = "Verde";}
                if(cor4.isChecked()){cor = "Rosa";}
                if(cor5.isChecked()){cor = "VerdeMusgo";}
                if(cor6.isChecked()){cor = "Amarelo";}

                String resultado = crud.novaCategoria(newCateg, cor);
                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void DelCatDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delcat_dialog, null);
        dialogBuilder.setView(dialogView);

        final bancoController crud = new bancoController(getBaseContext());
        final Cursor categorias = crud.getCategorias();
        final int[] delId = {0};

        final ArrayList<String> customCategs = new ArrayList<>();
        for (categorias.moveToFirst(); !categorias.isAfterLast(); categorias.moveToNext()) {
            customCategs.add(categorias.getString(categorias.getColumnIndex(nomeCateg)));
        }
        String[] from = new String[]{"NOME_CATEG"};
        int[] to = new int[]{R.id.customCategs};

        ListView customCatList = (ListView) dialogView.findViewById(R.id.customCategs);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(agenda_activity.this,
                android.R.layout.simple_list_item_1, customCategs);

        customCatList.setAdapter(adapter);

        customCatList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                categorias.moveToPosition(position);
                view.setSelected(true);
                view.setBackgroundResource(RoxoEscuro);
                int rowId = categorias.getInt(categorias.getColumnIndexOrThrow("_id"));
                delId[0] = rowId;
            }
        });
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            String deleta = crud.deletaCategoria(String.valueOf(delId[0]));
                Toast.makeText(getApplicationContext(), deleta, Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        diaSelecionado = parent.getItemAtPosition(position).toString();

        if (diaSelecionado.equalsIgnoreCase("Segunda")) {
            outroDia = 2;
        } else if (diaSelecionado.equalsIgnoreCase("Terça")) {
            outroDia = 3;
        } else if (diaSelecionado.equalsIgnoreCase("Quarta")) {
            outroDia = 4;
        } else if (diaSelecionado.equalsIgnoreCase("Quinta")) {
            outroDia = 5;
        } else if (diaSelecionado.equalsIgnoreCase("Sexta")) {
            outroDia = 6;
        } else if (diaSelecionado.equalsIgnoreCase("Sábado")) {
            outroDia = 7;
        } else if (diaSelecionado.equalsIgnoreCase("Domingo")) {
            outroDia = 1;
        }

        bancoController crud = new bancoController(agenda_activity.this);
        Cursor cursor = crud.getTarefasDia(outroDia);

        final ArrayList<Tarefa> manhaMap = new ArrayList();
        final ArrayList<Tarefa> tardeMap = new ArrayList();
        final ArrayList<Tarefa> noiteMap = new ArrayList();


        //String[] arrayColumns = new String[]{titleTarefa, catTarefa};
        //int[] arrayViewsID = new int[]{R.id.titleTarefa, R.id.catTarefa};

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            //adiciona para uma ArrayList todas as tarefas marcadas para o período da manhã
            //preciso pensar como fazer se não houver nenhuma
            if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("M")) {
                Tarefa tarefaMan = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                manhaMap.add(tarefaMan);

                //adiciona para ArrayList tarefas do periodo da tarde
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("T")) {
                Tarefa tarefaTar = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                tardeMap.add(tarefaTar);

                //adiciona para ArrayList tarefas da noite
            } else if (cursor.getString(cursor.getColumnIndex(periTarefa)).equalsIgnoreCase("N")) {
                Tarefa tarefaNoi = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)), false);
                noiteMap.add(tarefaNoi);

            }

        } // fim da criação de listas de tarefa

        //criação de listviews para cada tarefa
        ListView manhaTasks = (ListView) findViewById(R.id.listManha);
        ListView tardeTasks = (ListView) findViewById(R.id.listTarde);
        ListView noiteTasks = (ListView) findViewById(R.id.listNoite);

        TextView empty1 = (TextView) findViewById(R.id.empty1);
        TextView empty2 = (TextView) findViewById(R.id.empty2);
        TextView empty3 = (TextView) findViewById(R.id.empty3);

        if (manhaMap.size() == 0) {
            empty1.setText("Você não tem nenhuma meta para hoje a manhã.");
        } else { empty1.setText("");}
        if (tardeMap.size() == 0) {
            empty2.setText("Você não tem nenhuma meta para hoje a tarde.");
        }else { empty2.setText("");}
        if (noiteMap.size() == 0) {
            empty3.setText("Você não tem nenhuma meta para hoje a noite.");
        }else { empty3.setText("");}

        AdapterHomeTarefas todayMTask = new AdapterHomeTarefas(this, manhaMap);
        manhaTasks.setAdapter(todayMTask);

        AdapterHomeTarefas todayTTask = new AdapterHomeTarefas(this, tardeMap);
        tardeTasks.setAdapter(todayTTask);

        AdapterHomeTarefas todayNTask = new AdapterHomeTarefas(this, noiteMap);
        noiteTasks.setAdapter(todayNTask);

        manhaTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                Tarefa tarefa = manhaMap.get(position);
                int idTarefa = tarefa.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });

        tardeTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                Tarefa tarefa = tardeMap.get(position);
                int idTarefa = tarefa.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });

        noiteTasks.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(agenda_activity.this, detalhe_tarefa_activity.class);
                Tarefa tarefa = noiteMap.get(position);
                int idTarefa = tarefa.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });


    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
