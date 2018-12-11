package app.denode.denode;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import com.github.clans.fab.FloatingActionButton;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.RecyclerView;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.support.design.widget.BottomNavigationView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dbManager.bancoController;
import helpers.AdapterConteudo;
import helpers.AdapterHomeTarefas;
import helpers.AlarmReceiver;

import static android.R.attr.id;
import static android.R.id.empty;
import static android.app.PendingIntent.getBroadcast;
import static app.denode.denode.R.id.boxMan;
import static app.denode.denode.R.id.boxNoi;
import static app.denode.denode.R.id.boxTar;
import static app.denode.denode.R.id.toolbar;
import static app.denode.denode.R.layout.diario_habitos_activity;
import static app.denode.denode.R.layout.novacat_dialog;
import static app.denode.denode.R.mipmap.iconeroxonew;
import static dbManager.DatabaseHandler.catCont;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.dataCont;
import static dbManager.DatabaseHandler.lidoCont;
import static dbManager.DatabaseHandler.linkCont;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.salvCont;
import static dbManager.DatabaseHandler.textCont;
import static dbManager.DatabaseHandler.titleTarefa;
import static dbManager.DatabaseHandler.titulCont;
import static java.security.AccessController.getContext;

//import static app.denode.denode.R.id.bottom_menu;


public class home_activity extends AppCompatActivity {

    int idUsu, day;
    String nomeUsu;
    bancoController bancoController;
    List metas;
    ListView lista;
    BottomNavigationView bottomMenu;
    Tarefa tarefaMan, tarefaTar, tarefaNoi;
    ArrayList manhaMap, tardeMap, noiteMap;
    AlertDialog NewCatDialog;
    SharedPreferences prefs = null;
    LinearLayout ativs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        prefs = getSharedPreferences("com.denode.denode", MODE_PRIVATE);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        BottomNavigationViewEx bnve = (BottomNavigationViewEx) findViewById(R.id.bottom_navigation);
        bnve.enableAnimation(false);
        bnve.enableShiftingMode(false);
        bnve.enableItemShiftingMode(false);
        bnve.setTextVisibility(false);
        bnve.setIconSize(40, 40);

        Menu menu = bnve.getMenu();
        MenuItem item = menu.findItem(R.id.menu_home).setIcon(R.drawable.inicioicoselect);

        ativs = (LinearLayout)findViewById(R.id.ativFeita);
        ativs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detalhe = new Intent(home_activity.this, diarioHabitos_activity.class);
                startActivity(detalhe);
            }
        });

        //chama id do usuario logado
        Intent intent = getIntent();
        idUsu = intent.getIntExtra("identidade", 2);

        //get week day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        //calls bancoControle e função das tarefas do dia
        bancoController crud = new bancoController(home_activity.this);
        Cursor cursor = crud.getTarefasDia(day);

        //retorna o total de tarefas que tem pra hoje
        int qtdTarefas = cursor.getCount();

        //retorna o total de tarefas de hoje já feitas/cadastradas
        SimpleDateFormat banco = new SimpleDateFormat("dd");
        String dataBanco = banco.format(calendar.getTime()).toString();
        Cursor feitas = crud.tarefasFeitasHoje(dataBanco);
        int qtdFeitas = feitas.getCount();

        TextView hoje = (TextView)findViewById(R.id.qtdTarefas);
        hoje.setText(String.valueOf(qtdFeitas) + "/" + String.valueOf(qtdTarefas));

        Cursor posts = crud.getLastPosts();

        final ArrayList<Conteudo> postsCont = new ArrayList();

        for (posts.moveToFirst(); !posts.isAfterLast(); posts.moveToNext()) {

            Conteudo post = new Conteudo(posts.getInt(posts.getColumnIndex("_id")),
                    posts.getString(posts.getColumnIndex(titulCont)),
                    posts.getString(posts.getColumnIndex(catCont)),
                    posts.getString(posts.getColumnIndex(textCont)),
                    posts.getString(posts.getColumnIndex(dataCont)),
                    posts.getString(posts.getColumnIndex(linkCont)),
                    posts.getInt(posts.getColumnIndex(lidoCont)),
                    posts.getInt(posts.getColumnIndex(salvCont)));
            postsCont.add(post);

        } // fim da criação de listas de tarefa

        ListView ultimosPosts = (ListView)findViewById(R.id.ultimosPostsHome) ;

        AdapterConteudo totalPosts = new AdapterConteudo(home_activity.this, postsCont);
        ultimosPosts.setAdapter(totalPosts);
        ultimosPosts.setChoiceMode(ultimosPosts.CHOICE_MODE_SINGLE);

        ultimosPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(home_activity.this, detalhePost_activity.class);
                Conteudo post = postsCont.get(position);
                int idPost = post.getId();
                String idString = String.valueOf(idPost);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });


        //methods do menu de navegação inferior
        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:

                                Intent goToHome = new Intent(home_activity.this, home_activity.class);
                                startActivity(goToHome);
                                break;
                            case R.id.menu_agenda:

                                Intent goToAgenda = new Intent(home_activity.this, agenda_activity.class);
                                startActivity(goToAgenda);
                                break;
                            case R.id.menu_diario:

                                Intent goToDiario = new Intent(home_activity.this, diarioHabitos_activity.class);
                                startActivity(goToDiario);
                                break;
                            case R.id.menu_conteudo:

                                Intent goToConteudo = new Intent(home_activity.this, conteudo_activity.class);
                                startActivity(goToConteudo);
                                break;
                            case R.id.menu_perfil:

                                Intent goToPerfil = new Intent(home_activity.this, perfil_activity.class);
                                startActivity(goToPerfil);
                                break;
                        }
                        return true;
                    }
                });

        createDiarioNot();
        weeklyReport();
    }

    private void weeklyReport() {

        Intent intent = new Intent(this, helpers.reportCheck.class);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this.getApplicationContext(), 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmMgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
// Set the alarm to start at approximately 4:00 p.m.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 16);
        alarmMgr.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, calendar.getTimeInMillis(),
                1000*60*60*24, pendingIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            // Do first run stuff here then set 'firstrun' as false
            // using the following line to edit/commit prefs

            Intent intentAlarm = new Intent(home_activity.this, helpers.AlarmReceiver.class);
            intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, 0);
            intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot());

            Calendar c = Calendar.getInstance();
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.HOUR_OF_DAY, 22);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            // create the object
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            //set the alarm for particular time
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), 24 * 60 * 60 * 1000, getBroadcast(home_activity.this, 1, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT));

            prefs.edit().putBoolean("firstrun", false).commit();

            Intent intro = new Intent(home_activity.this, intro_activity.class);
            startActivity(intro);
        }
    }

    private void createDiarioNot() {

        Intent intentAlarm = new Intent(home_activity.this, helpers.AlarmReceiver.class);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION_ID, 0);
        intentAlarm.putExtra(AlarmReceiver.NOTIFICATION, getNot());

        Calendar c = Calendar.getInstance();
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.HOUR_OF_DAY, 22);
        c.set(Calendar.SECOND, 0);
        c.set(Calendar.MILLISECOND, 0);
        // create the object
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(home_activity.this, 0, intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);
        //set the alarm for particular time
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), alarmManager.INTERVAL_DAY, pendingIntent);

    }

    private Notification getNot(){

        Intent intent = new Intent(home_activity.this, diarioHabitos_activity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(home_activity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentTitle("Denode");
        builder.setContentText("Organize quais atividades você realizou hoje.");
        builder.setContentIntent(pendingIntent);
        builder.setSmallIcon(R.drawable.ic_icone_app);
        builder.setColor(Color.parseColor("#6453a2"));
        return builder.build();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detalhe_tarefa_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.home_config:
                Intent config = new Intent(home_activity.this, config_activity.class);
                startActivity(config);
                break;

            case R.id.home_logout:
                Intent intent = new Intent(home_activity.this, login_activity.class);
                startActivity(intent);
                break;

            case R.id.home_intro:
                Intent intro = new Intent(home_activity.this, intro_activity.class);
                startActivity(intro);
                break;
        }
        return true;

    }

}


