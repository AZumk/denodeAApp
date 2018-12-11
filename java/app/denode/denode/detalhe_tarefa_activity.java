package app.denode.denode;

import android.content.Intent;
import android.database.Cursor;
import android.support.v4.content.ContextCompat;
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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import org.w3c.dom.Text;

import dbManager.bancoController;

import static android.R.attr.value;
import static app.denode.denode.R.array.dias;
import static app.denode.denode.R.id.alarmeTime;
import static app.denode.denode.R.id.detalhe_tarefa_categoria;
import static app.denode.denode.R.id.detalhe_tarefa_dias;
import static app.denode.denode.R.id.detalhe_tarefa_horaAlarme;
import static app.denode.denode.R.id.detalhe_tarefa_notificacoes;
import static app.denode.denode.R.id.detalhe_tarefa_periodo;
import static app.denode.denode.R.id.detalhe_tarefa_titulo;
import static app.denode.denode.R.id.horaAlarmeOnOff;
import static app.denode.denode.R.id.refresh;
import static dbManager.DatabaseHandler.alarmeTarefa;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.diasTarefa;
import static dbManager.DatabaseHandler.horaAlarme;
import static dbManager.DatabaseHandler.idTarefa;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;

public class detalhe_tarefa_activity extends AppCompatActivity {

    public String tarefaSelecionada;
    ImageView tag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_tarefa_activity);

        TextView titulo = (TextView) findViewById(detalhe_tarefa_titulo);
        TextView categoria = (TextView) findViewById(detalhe_tarefa_categoria);
        TextView dias = (TextView) findViewById(detalhe_tarefa_dias);
        TextView periodo = (TextView) findViewById(detalhe_tarefa_periodo);
        TextView hasAlarm = (TextView) findViewById(detalhe_tarefa_notificacoes);
        TextView ifAlarmExists = (TextView) findViewById(detalhe_tarefa_horaAlarme);
        TextView textoAlarme = (TextView) findViewById(horaAlarmeOnOff);

        tag = (ImageView)findViewById(R.id.tag);
        ImageButton voltar = (ImageButton)findViewById(R.id.volta);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        tarefaSelecionada = getIntent().getStringExtra("idDetalhe");

        bancoController crud = new bancoController(detalhe_tarefa_activity.this);
        Cursor cursor = crud.getTarefa(tarefaSelecionada);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            titulo.setText(cursor.getString(cursor.getColumnIndex(titleTarefa)));
            categoria.setText(cursor.getString(cursor.getColumnIndex(catTarefa)));
            String tarCat = cursor.getString(cursor.getColumnIndex(catTarefa));
            if(tarCat.equalsIgnoreCase("Exercício")){ tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.Verde));}
            else if(tarCat.equalsIgnoreCase("Relaxamento")){tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.Laranja));}
            else if(tarCat.equalsIgnoreCase("Alimentação")){tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.Rosa));}
            else if(tarCat.equalsIgnoreCase("Estudos")){tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.RoxoClaro));}
            else if(tarCat.equalsIgnoreCase("Cuidados pessoais")){tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.Amarelo));}
            else {tag.setColorFilter(ContextCompat.getColor(detalhe_tarefa_activity.this, R.color.RoxoEscuro));}
            String diasDaTarefa = cursor.getString(cursor.getColumnIndex(diasTarefa));

            StringBuilder sb = new StringBuilder();
            if(diasDaTarefa.indexOf("2") > -1){sb.append("Segunda ");}
            if(diasDaTarefa.indexOf("3") > -1){sb.append("Terça ");}
            if(diasDaTarefa.indexOf("4") > -1){sb.append("Quarta ");}
            if(diasDaTarefa.indexOf("5") > -1){sb.append("Quinta ");}
            if(diasDaTarefa.indexOf("6") > -1){sb.append("Sexta ");}
            if(diasDaTarefa.indexOf("7") > -1){sb.append("Sábado ");}
            if(diasDaTarefa.indexOf("1") > -1){sb.append("Domingo ");}

            String dias_tarefa = sb.toString();
            dias.setText(dias_tarefa);
            periodo.setText(cursor.getString(cursor.getColumnIndex(periTarefa)));
            if(cursor.getString(cursor.getColumnIndex(alarmeTarefa)).equalsIgnoreCase("0")) {
                hasAlarm.setText("Off");
                textoAlarme.setVisibility(View.INVISIBLE);
                ifAlarmExists.setVisibility(View.INVISIBLE);
            } else if (cursor.getString(cursor.getColumnIndex(alarmeTarefa)).equalsIgnoreCase("1")){
                hasAlarm.setText("On");
                textoAlarme.setText("Horário");
                ifAlarmExists.setText(cursor.getString(cursor.getColumnIndex(horaAlarme)));
            }
        }

        voltar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        super.onOptionsItemSelected(item);

        switch(item.getItemId()){
            case R.id.detalhe_tarefa_editar:
                Intent edit = new Intent(detalhe_tarefa_activity.this, editTarefa_activity.class);
                edit.putExtra("idDetalhe", tarefaSelecionada);
                startActivity(edit);
                break;

            case R.id.detalhe_tarefa_deletar:
                bancoController crud = new bancoController(detalhe_tarefa_activity.this);
                Toast.makeText(getBaseContext(), "Atividade deletada.", Toast.LENGTH_SHORT).show();
                crud.deletarTarefa(tarefaSelecionada);
                finish();
                break;
        }
        return true;

    }
}
