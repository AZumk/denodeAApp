package app.denode.denode;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import dbManager.bancoController;

import static app.denode.denode.R.array.dias;
import static app.denode.denode.R.id.detalhe_tarefa_categoria;
import static app.denode.denode.R.id.detalhe_tarefa_dias;
import static app.denode.denode.R.id.detalhe_tarefa_horaAlarme;
import static app.denode.denode.R.id.detalhe_tarefa_notificacoes;
import static app.denode.denode.R.id.detalhe_tarefa_periodo;
import static app.denode.denode.R.id.detalhe_tarefa_titulo;
import static app.denode.denode.R.id.horaAlarmeOnOff;
import static app.denode.denode.R.id.lidoButton;
import static app.denode.denode.R.id.postCategoria;
import static app.denode.denode.R.id.postData;
import static app.denode.denode.R.id.postTexto;
import static app.denode.denode.R.id.postTitulo;
import static app.denode.denode.R.id.salva;
import static app.denode.denode.R.id.salvoButton;
import static app.denode.denode.R.id.shareButton;
import static app.denode.denode.R.id.volta;
import static dbManager.DatabaseHandler.alarmeTarefa;
import static dbManager.DatabaseHandler.catTarefa;
import static dbManager.DatabaseHandler.diasTarefa;
import static dbManager.DatabaseHandler.horaAlarme;
import static dbManager.DatabaseHandler.periTarefa;
import static dbManager.DatabaseHandler.titleTarefa;
import static java.security.AccessController.getContext;

public class detalhePost_activity extends AppCompatActivity {
    public String postSelecionado, link;
    boolean isRead, isSaved;
    public ImageButton lido, salvo, share, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detalhe_post_activity);

        TextView titulo = (TextView) findViewById(postTitulo);
        TextView categoria = (TextView) findViewById(postCategoria);
        TextView data = (TextView) findViewById(postData);
        TextView texto = (TextView) findViewById(postTexto);

        back = (ImageButton) findViewById(volta);
        lido = (ImageButton) findViewById(lidoButton);
        salvo = (ImageButton) findViewById(salvoButton);
        share = (ImageButton) findViewById(shareButton);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        postSelecionado = getIntent().getStringExtra("idDetalhe");

        final bancoController crud = new bancoController(detalhePost_activity.this);
        Cursor cursor = crud.getPost(postSelecionado);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            titulo.setText(cursor.getString(cursor.getColumnIndex("TIT_CONT")));
            categoria.setText(cursor.getString(cursor.getColumnIndex("CAT_CONT")));
            data.setText(cursor.getString(cursor.getColumnIndex("DATA_CONT")));
            texto.setText(cursor.getString(cursor.getColumnIndex("TEX_CONT")));
            link = (cursor.getString(cursor.getColumnIndex("LINK_CONT")));

            isRead = (cursor.getInt(cursor.getColumnIndex("LIDO_CONT")) > 0);
            isSaved = (cursor.getInt(cursor.getColumnIndex("SALV_CONT")) > 0);

            if (isRead == false) {
                lido.setImageResource(R.drawable.x_icon);
            } else if (isRead == true) {
                lido.setImageResource(R.drawable.salvar_icon);
            }
            if (isSaved == false) {
                salvo.setImageResource(R.drawable.favor_icon);
            } else if (isSaved == true) {
                salvo.setImageResource(R.drawable.favoritado_icon);
            }
        }

        lido.setOnClickListener(new View.OnClickListener() {
            @Override
            //NÃO ESTÃO MUDANDO PRA FALSE
            public void onClick(View v) {
                if (isRead == false) {
                    isRead = true;
                    lido.setImageResource(R.drawable.salvar_icon);
                } else {
                    isRead = false;
                    lido.setImageResource(R.drawable.x_icon);
                }

                String resultado = crud.arquivaPost(postSelecionado, isRead);
            }
        });

        salvo.setOnClickListener(new View.OnClickListener() {
            @Override
            //NÃO ESTÃO MUDANDO PRA FALSE
            public void onClick(View v) {
                if (isSaved == false) {
                    isSaved = true;
                    salvo.setImageResource(R.drawable.favoritado_icon);
                } else {
                    isSaved = false;
                    salvo.setImageResource(R.drawable.favor_icon);
                }

                String resultado = crud.arquivaPost(postSelecionado, isSaved);
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(detalhePost_activity.this, "Link do post copiado!", Toast.LENGTH_LONG).show();
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("linkl",link);
                clipboard.setPrimaryClip(clip);
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}



