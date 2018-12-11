package helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import app.denode.denode.Tarefa;
import dbManager.bancoController;

import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.titleTarefa;

/**
 * Created by Ana Zumk on 23/11/2017.
 */

public class reportCheck extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        bancoController crud = new bancoController(context);

        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -7);
        DateFormat dateFormat = new SimpleDateFormat("dd");
        DateFormat weekday = new SimpleDateFormat("EEEE");
        String lastWD = dateFormat.format(c.getTime());
        String dayOfWeek = weekday.format(c.getTime());

        int exists = crud.existeRegistroControle(lastWD);

        if(exists > 0){
            //stops intent
            String result = crud.updateControle("222",
                    false, lastWD, "teste mtf");
        } else {

            int day = 0;
            if(dayOfWeek == "Segunda"){day = 2;}
            if(dayOfWeek == "Terça"){day = 3;}
            if(dayOfWeek == "Quarta"){day = 4;}
            if(dayOfWeek == "Quinta"){day = 5;}
            if(dayOfWeek == "Sexta"){day = 6;}
            if(dayOfWeek == "Sábado"){day = 7;}
            if(dayOfWeek == "Domingo"){day = 1;}

            Cursor cursor = crud.getTarefasDia(day);
            int qtdTarefas = cursor.getCount();

            ArrayList<Tarefa> manhaMap = new ArrayList();

            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                Tarefa tarefaMan = new Tarefa(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                        cursor.getString(cursor.getColumnIndex(titleTarefa)),
                        cursor.getString(cursor.getColumnIndex(catTarefa)),
                        false
                );
                manhaMap.add(tarefaMan);
            }

            for (int i = 0; i < qtdTarefas; i++){
                Tarefa tarefa = manhaMap.get(i);
                String result = crud.updateControle(String.valueOf(tarefa.getId()),
                        false, lastWD, tarefa.getCategoria());
            }
        }
        Toast.makeText(context, "weeklyreport", Toast.LENGTH_LONG).show();
    }
}
