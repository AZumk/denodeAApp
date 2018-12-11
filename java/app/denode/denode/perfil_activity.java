package app.denode.denode;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

import org.w3c.dom.Text;

import dbManager.bancoController;

import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.emailUsu;
import static dbManager.DatabaseHandler.nomeCateg;
import static dbManager.DatabaseHandler.nomeUsu;
import static dbManager.DatabaseHandler.periTarefa;

public class perfil_activity extends AppCompatActivity {

    int tot1, fei1, tot2, fei2, tot3, fei3, tot4, fei4, tot5, fei5, tot6, fei6 = 0;
    TextView counter1, counter2, counter3, counter4, counter5, counter6;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.perfil_activity);

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
        MenuItem item = menu.findItem(R.id.menu_perfil).setIcon(R.drawable.perfilicoselect);

        TextView atividades = (TextView)findViewById(R.id.totalAtividades);
        TextView posts = (TextView)findViewById(R.id.totalPosts);
        TextView usuNome = (TextView)findViewById(R.id.nome);
        TextView usuEmail = (TextView)findViewById(R.id.email);
        TextView todasAtividades = (TextView)findViewById(R.id.todasAtividadesCounter);

        bancoController crud = new bancoController(perfil_activity.this);
        Cursor usuario = crud.getUsuario();
        Cursor totalLidos = crud.getLidosPosts();
        Cursor categorias = crud.getCategorias();
        Cursor tarefasFeitas = crud.totalAtivFeitas();
        Cursor tarefasTotal = crud.getAllControle();

        posts.setText(String.valueOf(totalLidos.getCount()));

        for (usuario.moveToFirst(); !usuario.isAfterLast(); usuario.moveToNext()) {
            usuNome.setText(usuario.getString(usuario.getColumnIndex(nomeUsu)));
            usuEmail.setText(usuario.getString(usuario.getColumnIndex(emailUsu)));
        }

        atividades.setText(String.valueOf(tarefasFeitas.getCount()));

        todasAtividades.setText(String.valueOf(tarefasFeitas.getCount()) + "/" + String.valueOf(tarefasTotal.getCount()));

        BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);


        for (tarefasTotal.moveToFirst(); !tarefasTotal.isAfterLast(); tarefasTotal.moveToNext()) {
            String ctg = tarefasTotal.getString(tarefasTotal.getColumnIndex(catTarefa));
            if(ctg.equalsIgnoreCase("Exercício")){tot1++;}
            else if(ctg.equalsIgnoreCase("Relaxamento")){tot2++;}
            else if(ctg.equalsIgnoreCase("Alimentação")){tot3++;}
            else if(ctg.equalsIgnoreCase("Estudos")){tot4++;}
            else if(ctg.equalsIgnoreCase("Cuidados Pessoais")){tot5++;}
            else {tot6++;}
        }

        for (tarefasFeitas.moveToFirst(); !tarefasFeitas.isAfterLast(); tarefasFeitas.moveToNext()) {
            String ctg = tarefasFeitas.getString(tarefasFeitas.getColumnIndex(catTarefa));
            if(ctg.equalsIgnoreCase("Exercício")){fei1++;}
            else if(ctg.equalsIgnoreCase("Relaxamento")){fei2++;}
            else if(ctg.equalsIgnoreCase("Alimentação")){fei3++;}
            else if(ctg.equalsIgnoreCase("Estudos")){fei4++;}
            else if(ctg.equalsIgnoreCase("Cuidados Pessoais")){fei5++;}
            else {fei6++;}
        }

        counter1 = (TextView)findViewById(R.id.exerCounter);
        counter2 = (TextView)findViewById(R.id.relaxCounter);
        counter3 = (TextView)findViewById(R.id.alimCounter);
        counter4 = (TextView)findViewById(R.id.estuCounter);
        counter5 = (TextView)findViewById(R.id.cuidCounter);
        counter6 = (TextView)findViewById(R.id.outrCounter);

        counter1.setText(String.valueOf(fei1)+"/"+String.valueOf(tot1));
        counter2.setText(String.valueOf(fei2)+"/"+String.valueOf(tot2));
        counter3.setText(String.valueOf(fei3)+"/"+String.valueOf(tot3));
        counter4.setText(String.valueOf(fei4)+"/"+String.valueOf(tot4));
        counter5.setText(String.valueOf(fei5)+"/"+String.valueOf(tot5));
        counter6.setText(String.valueOf(fei6)+"/"+String.valueOf(tot6));

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu_home:

                                Intent goToHome = new Intent(perfil_activity.this, home_activity.class);
                                startActivity(goToHome);
                                break;
                            case R.id.menu_agenda:

                                Intent goToAgenda = new Intent(perfil_activity.this, agenda_activity.class);
                                startActivity(goToAgenda);
                                break;
                            case R.id.menu_diario:

                                Intent goToDiario = new Intent(perfil_activity.this, diarioHabitos_activity.class);
                                startActivity(goToDiario);
                                break;
                            case R.id.menu_conteudo:

                                Intent goToConteudo = new Intent(perfil_activity.this, conteudo_activity.class);
                                startActivity(goToConteudo);
                                break;
                            case R.id.menu_perfil:

                                Intent goToPerfil = new Intent(perfil_activity.this, perfil_activity.class);
                                startActivity(goToPerfil);
                                break;
                        }
                        return true;
                    }
                });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.perfil_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.perfil_editar:
                Intent edit = new Intent(perfil_activity.this, editPerfil_activity.class);
                startActivity(edit);
                break;

            case R.id.perfil_config:
                Intent config = new Intent(perfil_activity.this, config_activity.class);
                startActivity(config);
                break;

            case R.id.perfil_sair:
                Intent logout = new Intent(perfil_activity.this, login_activity.class);
                startActivity(logout);
                break;
        }
        return true;

    }

}
