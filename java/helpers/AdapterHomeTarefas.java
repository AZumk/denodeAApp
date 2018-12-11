package helpers;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import app.denode.denode.Tarefa;
import app.denode.denode.R;
import dbManager.bancoController;

import static android.R.attr.id;
import static dbManager.DatabaseHandler.corCateg;

/**
 * Created by Ana Zumk on 25/10/2017.
 */

public class AdapterHomeTarefas  extends BaseAdapter{

    private Context context; //context
    private ArrayList<Tarefa> tarefa; //data source of the list adapter

    //public constructor
    public AdapterHomeTarefas(Context context, ArrayList<Tarefa> items) {
        this.context = context;
        this.tarefa = items;
    }
    @Override
    public int getCount() {
        return tarefa.size();
    }

    @Override
    public Object getItem(int position) {
        return tarefa.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // inflate the layout for each list row
        if (convertView == null ) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listahome_tarefas, parent, false);
        }

        // get current item to be displayed
        Tarefa currentItem = (Tarefa) getItem(position);

        // get the TextView for item name and item description
        TextView textViewItemName = (TextView) convertView.findViewById(R.id.titleTarefa);
        TextView textViewItemDescription = (TextView) convertView.findViewById(R.id.catTarefa);
        ImageView tagCat = (ImageView)convertView.findViewById(R.id.tag);

        //sets the text for item name and item description from the current item object
        textViewItemName.setText(currentItem.getTitulo());
        textViewItemDescription.setText(currentItem.getCategoria());

        String cat = currentItem.getCategoria();
        if(cat.equalsIgnoreCase("Exercício")){ tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Verde));}
        else if(cat.equalsIgnoreCase("Relaxamento")){tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Laranja));}
        else if(cat.equalsIgnoreCase("Alimentação")){tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Rosa));}
        else if(cat.equalsIgnoreCase("Estudos")){tagCat.setColorFilter(ContextCompat.getColor(context, R.color.RoxoClaro));}
        else if(cat.equalsIgnoreCase("Cuidados pessoais")){tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Amarelo));}
        else {
            String cor = "";
            bancoController crud = new bancoController(context);
            Cursor color = crud.getCategColor(cat);
            for (color.moveToFirst(); !color.isAfterLast(); color.moveToNext()) {
                cor = color.getString(color.getColumnIndex(corCateg));
            }
            if (cor.equalsIgnoreCase("VerdeMusgo")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.VerdeMusgo));
            } else if (cor.equalsIgnoreCase("RoxoEscuro")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.RoxoEscuro));
            } else if (cor.equalsIgnoreCase("Laranja")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Laranja));
            } else if (cor.equalsIgnoreCase("Verde")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Verde));
            } else if (cor.equalsIgnoreCase("Rosa")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Rosa));
            } else if (cor.equalsIgnoreCase("Amarelo")) {
                tagCat.setColorFilter(ContextCompat.getColor(context, R.color.Amarelo));
            }

        }
            // returns the view for the current row
        return convertView;
    }

}
