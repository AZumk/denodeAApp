package helpers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.denode.denode.Conteudo;
import app.denode.denode.R;
import app.denode.denode.Tarefa;

/**
 * Created by Ana Zumk on 01/11/2017.
 */

public class AdapterConteudo  extends BaseAdapter {

    private Context context; //context
    private ArrayList<Conteudo> conteudo; //data source of the list adapter

    //public constructor
    public AdapterConteudo(Context context, ArrayList<Conteudo> items) {
        this.context = context;
        this.conteudo = items;
    }
    @Override
    public int getCount() {
        return conteudo.size();
    }

    @Override
    public Object getItem(int position) {
        return conteudo.get(position);
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
            convertView = inflater.inflate(R.layout.listapost_conteudo, parent, false);
        }

        // get current item to be displayed
        Conteudo currentItem = (Conteudo) getItem(position);

        // get the TextView for item name and item description
        TextView tituloPost = (TextView) convertView.findViewById(R.id.titlePost);
        TextView categPost = (TextView) convertView.findViewById(R.id.categPost);
        TextView dataPost = (TextView) convertView.findViewById(R.id.dataPost);

        //sets the text for item name and item description from the current item object
        tituloPost.setText(currentItem.getTitulo());
        categPost.setText(currentItem.getCategoria());
        dataPost.setText(currentItem.getData());

        // returns the view for the current row
        return convertView;
    }

}