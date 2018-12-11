package app.denode.denode;

import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.hardware.SensorManager;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dbManager.bancoController;
import helpers.AdapterConteudo;

import static android.R.attr.id;
import static android.R.attr.mode;
import static android.R.id.list;
import static app.denode.denode.R.id.lidoButton;
import static app.denode.denode.R.id.listMode;
import static app.denode.denode.R.id.listaPosts;
import static app.denode.denode.R.id.postArch;
import static app.denode.denode.R.id.postFav;
import static app.denode.denode.R.id.postShare;
import static app.denode.denode.R.id.salvoButton;
import static app.denode.denode.R.id.shareButton;
import static dbManager.DatabaseHandler.idTarefa;
import static dbManager.DatabaseHandler.linkCont;

public class allPosts extends Fragment {

    TextView empty, msg;
    ListView listPosts;
    ActionMode mActionMode;
    public String postSelecionadoCAB,resultado, link;
    MenuItem lido, salvo, share;
    boolean isRead, isSaved;

    public allPosts() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_all_posts, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        listPosts = (ListView) getView().findViewById(listaPosts);
        empty = (TextView) getView().findViewById(R.id.emptyPosts1);
        msg = (TextView) getView().findViewById(R.id.emptyMsg1);

        bancoController crud = new bancoController(getContext());
        Cursor cursor = crud.getPosts();

        //checa se está retornando algo
        int qtdTarefas = cursor.getCount();

        //vai colocar cada tarefa na lista de seu respectivo periodo
        final ArrayList<Conteudo> postsCont = new ArrayList();

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {

            Conteudo post = new Conteudo(Integer.parseInt(cursor.getString(cursor.getColumnIndex("_id"))),
                    cursor.getString(cursor.getColumnIndex("TIT_CONT")),
                    cursor.getString(cursor.getColumnIndex("CAT_CONT")),
                    cursor.getString(cursor.getColumnIndex("TEX_CONT")),
                    cursor.getString(cursor.getColumnIndex("DATA_CONT")),
                    cursor.getString(cursor.getColumnIndex("LINK_CONT")),
                    cursor.getInt(cursor.getColumnIndex("LIDO_CONT")),
                    cursor.getInt(cursor.getColumnIndex("SALV_CONT")));
            postsCont.add(post);

        } // fim da criação de listas de tarefa

        if (postsCont.size() == 0) {
            empty.setText("Nada encontrado");
            msg.setText("Não foram encontrados posts nesta sessão.");
        } else {
        }

        AdapterConteudo totalPosts = new AdapterConteudo(getActivity().getApplicationContext(), postsCont);
        listPosts.setAdapter(totalPosts);

        listPosts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent detalhe = new Intent(getContext(), detalhePost_activity.class);
                Conteudo post = postsCont.get(position);
                int idTarefa = post.getId();
                String idString = String.valueOf(idTarefa);
                detalhe.putExtra("idDetalhe", idString);
                startActivity(detalhe);
            }
        });


        listPosts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick (AdapterView parent, View view,
                                            int position, long id) {
                if (mActionMode == null) {
                    Conteudo post = postsCont.get(position);
                    postSelecionadoCAB = String.valueOf(post.getId());
                    mActionMode = getActivity().startActionMode(mActionModeCallback);
                    view.setSelected(true);
                }
                return true;
            }
        });
    }

    ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            // Inflate a menu resource providing context menu items
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.postcontext_menu, menu);

            salvo = menu.findItem(R.id.postFav);
            lido = menu.findItem(R.id.postArch);
            share = menu.findItem(R.id.postShare);

            final bancoController crud = new bancoController(getActivity());
            Cursor cursor = crud.getPost(postSelecionadoCAB);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                isRead = (cursor.getInt(cursor.getColumnIndex("LIDO_CONT")) > 0);
                isSaved = (cursor.getInt(cursor.getColumnIndex("SALV_CONT")) > 0);
                link = (cursor.getString(cursor.getColumnIndex(linkCont)));

                if (isRead == false) {
                    lido.setIcon(R.drawable.ico_excluir_);
                } else {
                    lido.setIcon(R.drawable.ico_marcarlido_);
                }
                if (isSaved == false) {
                    salvo.setIcon(R.drawable.ico_favoritar_);
                } else {
                    salvo.setIcon(R.drawable.btn_jalido_2x);
                }

            }
            return true;
        }
        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            String id = postSelecionadoCAB;
            final bancoController crud = new bancoController(getActivity());
            Cursor cursor = crud.getPost(postSelecionadoCAB);
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                isRead = (cursor.getInt(cursor.getColumnIndex("LIDO_CONT")) > 0);
                isSaved = (cursor.getInt(cursor.getColumnIndex("SALV_CONT")) > 0);
            }

            switch (item.getItemId()) {
                case R.id.postFav:
                    if(isSaved==false){
                        isSaved = true;
                        salvo.setIcon(R.drawable.favor_icon);
                    }
                    else if (isSaved==true) {
                        isSaved = false;
                        salvo.setIcon(R.drawable.favoritado_icon);
                    }

                    resultado = crud.favoritaPost(postSelecionadoCAB, isSaved);
                    Toast.makeText(getContext(), resultado, Toast.LENGTH_SHORT).show();
                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.postArch:
                    if(isRead==false){
                        isRead = true;
                        lido.setIcon(R.drawable.x_icon);
                    }
                    else {
                        isRead = false;
                        lido.setIcon(R.drawable.salvar_icon);
                    }

                    resultado = crud.arquivaPost(postSelecionadoCAB, isRead);

                    mode.finish(); // Action picked, so close the CAB
                    return true;

                case R.id.postShare:
                    Toast.makeText(getContext(), "Link do post copiado!", Toast.LENGTH_LONG).show();
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("link",link);
                    clipboard.setPrimaryClip(clip);
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            mActionMode = null;
            postSelecionadoCAB = null;
            getActivity().recreate();
        }
    };
}
