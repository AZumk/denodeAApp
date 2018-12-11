package dbManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.renderscript.Sampler;
import android.util.ArrayMap;
import android.util.Log;
import dbManager.perfilUsu_table;
import dbManager.agenda_app;
import dbManager.conteudo_app;
import dbManager.tarefas_controle;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import dbManager.DatabaseHandler;

import static android.R.attr.data;
import static android.R.attr.id;
import static android.R.attr.password;
import static android.R.string.no;
import static android.provider.BaseColumns._ID;
import static app.denode.denode.R.array.dias;
import static app.denode.denode.R.id.alarmeTime;
import static app.denode.denode.R.id.email;
import static app.denode.denode.R.id.nome;


/**
 * Created by Ana Zumk on 19/10/2017.
 */

public class bancoController extends DatabaseHandler {
    private SQLiteDatabase db;
    private DatabaseHandler denode;

    public bancoController(Context context) {
        super(context);
        denode = new DatabaseHandler(context);
    }

    //INSERT
    public String novoPerfil(String nome, String email, String senha) {
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(nomeUsu, nome);
        values.put(emailUsu, email);
        values.put(senhaUsu, senha);

        resultado = db.insert("perfUsu_table", null, values);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "Registro inserido com sucesso";

    }

    public long novaTarefa(String titulo, String categ, String dias, String perio, Boolean alarme, String horalarme) {
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(titleTarefa, titulo);
        values.put(catTarefa, categ);
        values.put(diasTarefa, dias);
        values.put(periTarefa, perio);
        values.put(alarmeTarefa, alarme);
        values.put(horaAlarme, horalarme);

        resultado = db.insert("agenda_app", null, values);
        db.close();
        return resultado;
    }

    public String novaCategoria(String nome, String cor) {
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(nomeCateg, nome);
        values.put(corCateg, cor);

        resultado = db.insert(TABLE_CATEGORIAS, null, values);
        db.close();

        if (resultado == -1)
            return "Erro ao registrar categoria";
        else
            return "Categoria criada!";

    }

    public String novoControle(int idage, String categ, boolean feita, String data) {
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(idTarefa, idage);
        values.put(catTarefa, categ);
        values.put(tarefaFeita, feita);
        values.put(dataTarefa, data);

        resultado = db.insert("tarefas_controle", null, values);
        db.close();

        if (resultado == -1)
            return "Erro ao inserir registro";
        else
            return "O diário foi atualizado com as tarefas de hoje";

    }

    //SELECT ALL
    //alterar para ao invés de selecionar todos os perfis, select all tarefas, controle e conteudo
    public List<perfilUsu_table> getAllPerf() {
        String[] columns = {
                idUsu, nomeUsu, emailUsu, senhaUsu
        };
        String sortOrder = emailUsu + " ASC";
        List<perfilUsu_table> userList = new ArrayList<perfilUsu_table>();
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("perfUsu_table", //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order
        if (cursor.moveToFirst()) {
            do {
                perfilUsu_table user = new perfilUsu_table();
                user.setID_USU(Integer.parseInt(cursor.getString(cursor.getColumnIndex(idUsu))));
                user.setNOME_USU(cursor.getString(cursor.getColumnIndex(nomeUsu)));
                user.setEMAIL_USU(cursor.getString(cursor.getColumnIndex(emailUsu)));
                user.setSENHA_USU(cursor.getString(cursor.getColumnIndex(senhaUsu)));
                // Adding user record to list
                userList.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return userList;
    }

    public boolean emailExists(String email) {

        // array of columns to fetch
        String[] columns = {
                idUsu
        };
        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = emailUsu + " = ?";

        // selection argument
        String[] selectionArgs = {email};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query("perfUsu_table", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            //email já registrado
            return true;
        }
        //email nao registrado ainda
        return false;
    }

    public int existeRegistroControle(String date) {

        // array of columns to fetch
        String[] columns = {
                idTarefa
        };

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = dataTarefa + " = ?";

        // selection argument
        String[] selectionArgs = {date};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com';
         */
        Cursor cursor = db.query(TABLE_TAREFASCONTROLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            //email já registrado
            return 1;
        }
        //email nao registrado ainda
        return 0;
    }

    public int isTarefaFeita(int id, String date) {

        int feita = 0;
        
        String[] columns = {
                tarefaFeita
        };
        // array of columns to fetch
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = dataTarefa + " = ?" + " AND " + idTarefa + " = ? ";

        // selection argument
        String[] selectionArgs = {date, String.valueOf(id)};

        Cursor cursor = db.query(TABLE_TAREFASCONTROLE, //Table to query
                null,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);
        //The sort order
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(tarefaFeita))) > 0){
                feita = 1;
            } else {
                feita = 0;
            }
        }
        cursor.close();
        db.close();

        //email nao registrado ainda
        return feita;
    }

    public Cursor tarefasFeitasHoje(String date) {

        int feitas = 0;

        String[] columns = {
                tarefaFeita
        };

        // array of columns to fetch
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = dataTarefa + " = ?" + " AND " + tarefaFeita + " = ? ";

        // selection argument
        String[] selectionArgs = {date, "1"};

        Cursor cursor = db.query(TABLE_TAREFASCONTROLE, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getTarefasNot() {

        String[] columns = {
                idTarefa + " AS _id ",
                titleTarefa, diasTarefa, periTarefa, horaAlarme
        };

        // array of columns to fetch
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = alarmeTarefa + " = 1";

        Cursor cursor = db.query(TABLE_AGENDA, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                null,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getCategColor(String categ) {
        String color;
        String[] columns = {
                corCateg
        };

        // array of columns to fetch
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = nomeCateg + " = ?";
        String[] selectionArgs = {categ};

        Cursor cursor = db.query(TABLE_CATEGORIAS, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }
        // return user list
        return cursor;
    }


    public boolean validaUser(String email, String password) {

        // array of columns to fetch
        String[] columns = {
                idUsu, nomeUsu
        };
        SQLiteDatabase db = this.getReadableDatabase();
        // selection criteria
        String selection = emailUsu + " = ?" + " AND " + senhaUsu + " = ?";

        // selection arguments
        String[] selectionArgs = {email, password};
        List<perfilUsu_table> usuario = new ArrayList<perfilUsu_table>();
        // query user table with conditions
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'jack@androidtutorialshub.com' AND user_password = 'qwerty';
         */
        Cursor cursor = db.query("perfUsu_table", //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                       //filter by row groups
                null);                      //The sort order

        int cursorCount = cursor.getCount();
        cursor.moveToFirst();

        cursor.close();
        db.close();

        //validado
//não validado
        return cursorCount > 0;
    }

    public Cursor getTarefasDia(int weekDay) {

        String[] columns = {idTarefa + " AS _id", titleTarefa, catTarefa, diasTarefa, periTarefa
        };
        String sortOrder = idTarefa + " ASC";

        String wkDay = String.valueOf(weekDay);
        String selection = diasTarefa + " LIKE " + "'%" + wkDay + "%'";

        // selection arguments
        String[] selectionArgs = {"'%" + weekDay + "%'"};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AGENDA, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order

        Log.v("WEEK DAY: ", String.valueOf(weekDay));
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getTarefaId() {

        String[] columns = {idTarefa + " AS _id"};

       String order = " ID_AGE DESC LIMIT 1";

        // selection arguments

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AGENDA, //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                order); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getPosts() {

        db = this.getReadableDatabase();
        String[] columns = {"ID_CONT AS _id", "TIT_CONT", "CAT_CONT",
                "TEX_CONT", "DATA_CONT", "LINK_CONT", "LIDO_CONT", "SALV_CONT"};
        String sortOrder = "ID_CONT" + " DESC";

        Cursor cursor = db.query(TABLE_CONTEUDO, //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getLastPosts() {

        db = this.getReadableDatabase();
        String[] columns = {idCont + " AS _id ", titulCont, catCont, textCont, dataCont, linkCont, lidoCont, salvCont};
        String sortOrder = "ID_CONT DESC LIMIT 2";

        Cursor cursor = db.query(TABLE_CONTEUDO, //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getCategorias() {

        db = this.getReadableDatabase();
        String[] columns = { idCateg + " AS _id", nomeCateg, corCateg};

        Cursor cursor = db.query(TABLE_CATEGORIAS, //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getLidosPosts() {

        db = this.getReadableDatabase();
        String[] columns = {"ID_CONT AS _id", "TIT_CONT", "CAT_CONT",
                "TEX_CONT", "DATA_CONT", "LINK_CONT", "LIDO_CONT", "SALV_CONT"};
        String sortOrder = "ID_CONT" + " DESC";

        String selection = "LIDO_CONT = 1";

        Cursor cursor = db.query(TABLE_CONTEUDO, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor totalAtivFeitas() {

        db = this.getReadableDatabase();
        String[] columns = {idTarefa, catTarefa};

        String selection = tarefaFeita + " = 1";

        Cursor cursor = db.query(TABLE_TAREFASCONTROLE, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getAllControle() {

        db = this.getReadableDatabase();
        String[] columns = {catTarefa};

        Cursor cursor = db.query(TABLE_TAREFASCONTROLE, //table da query
                columns, // colunas que retornam
                null, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getSalvosPosts() {

        db = this.getReadableDatabase();
        String[] columns = {"ID_CONT AS _id", "TIT_CONT", "CAT_CONT",
                "TEX_CONT", "DATA_CONT", "LINK_CONT", "LIDO_CONT", "SALV_CONT"};
        String sortOrder = "ID_CONT" + " DESC";

        String selection = "SALV_CONT = 1";

        Cursor cursor = db.query(TABLE_CONTEUDO, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                null, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                sortOrder); //sort the order

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getUsuarioLogado(String email, String password) {

        String[] campos = {"ID_USU AS _id", nomeUsu};
        // selection criteria
        String selection = emailUsu + " = ?" + " AND " + senhaUsu + " = ?";
        // selection arguments
        String[] selectionArgs = {email, password};

        db = denode.getReadableDatabase();
        Cursor cursor = db.query("perfUsu_table",
                campos,
                selection,
                selectionArgs,
                null,
                null,
                null,
                null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getUsuario() {

        String[] campos = {idUsu+ " AS _id", nomeUsu, emailUsu};
        // selection criteria
        // selection arguments
        db = denode.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PERFUSU,
                campos,
                null,
                null,
                null,
                null,
                null,
                null);

        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public Cursor getTarefa(String id) {
        String[] columns = {idTarefa + " AS _id", titleTarefa, catTarefa, diasTarefa, periTarefa, alarmeTarefa, horaAlarme
        };

        String selection = idTarefa + " = ?";

        // selection arguments
        String[] selectionArgs = {id};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_AGENDA, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                selectionArgs, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }

    public int senhaExists(String id, String senha) {
        String[] columns = {idUsu + " AS _id"};

        String selection = idUsu + " = ?" + " AND " + senhaUsu + " = ?";

        // selection arguments
        String[] selectionArgs = {id, senha};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_PERFUSU, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                selectionArgs, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order

        if(cursor.getCount() > 0){
            return 1;
        } else {
            return 0;
        }
    }

    public Cursor getPost(String id) {

        String[] columns = {"ID_CONT AS _id", "TIT_CONT", "CAT_CONT",
                "DATA_CONT", "TEX_CONT", "LINK_CONT", "LIDO_CONT", "SALV_CONT"};
        String selection =  "ID_CONT = ?";

        // selection arguments
        String[] selectionArgs = {id};

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CONTEUDO, //table da query
                columns, // colunas que retornam
                selection, // WHERE clause
                selectionArgs, // values for the WHERE
                null, // ROWS clause
                null, //filter by the rows
                null); //sort the order
        try {
            if (cursor.moveToFirst()) {
                do {
                    // Adding user record to list
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null) {

            }
        }

        // return user list
        return cursor;
    }



    public Cursor getControle() {
        Cursor cursor;
        String[] campos = {titleTarefa, catTarefa, diasTarefa, periTarefa, alarmeTarefa, horaAlarme};
        db = denode.getReadableDatabase();
        cursor = db.query("agenda_app", campos, null, null, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }


    public String updateTarefa(String id, String titulo, String categoria, String dias, String periodo, String alarme, String horaalarme) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(titleTarefa, titulo);
        values.put(catTarefa, categoria);
        values.put(diasTarefa, dias);
        values.put(periTarefa, periodo);
        values.put(alarmeTarefa, alarme);
        values.put(horaAlarme, horaalarme);

        resultado = db.update("agenda_app", values, idTarefa + " = ?",new String[] { id });

        if (resultado == -1)
            return "Erro ao atualizar registro";
        else
            return "Registro atualizado com sucesso";

    }

    public String atualizaNomeEmail(String id, String nome, String email) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(nomeUsu, nome);
        values.put(emailUsu, email);

        resultado = db.update(TABLE_PERFUSU, values, idUsu + " = ?",new String[] { id });

        if (resultado == -1)
            return "Erro ao atualizar nome e email.";
        else
            return "Nome e email atualizados com sucesso.";

    }

    public String setAllNotsToOff() {

        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(alarmeTarefa, "0");
        values.put(horaAlarme, "00:00");

        resultado = db.update(TABLE_AGENDA, values, null, null);

        if (resultado == -1)
            return "Erro";
        else
            return "Suc";

    }


    public String atualizarPerfilCompleto(String id, String nome, String email, String senha) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(nomeUsu, nome);
        values.put(emailUsu, email);
        values.put(senhaUsu, senha);

        resultado = db.update(TABLE_PERFUSU, values, idUsu + " = ?",new String[] { id });

        if (resultado == -1)
            return "Erro ao atualizar perfil.";
        else
            return "Perfil atualizado com sucesso.";

    }

    public String atualizarSenha(String email, String senha) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(senhaUsu, senha);

        resultado = db.update(TABLE_PERFUSU, values, emailUsu + " = ?",new String[] { email });

        if (resultado == -1)
            return "Erro ao atualizar senha.";
        else
            return "Senha atualizada com sucesso.";

    }

    public String updateControle(String idage, boolean feita, String data, String titCateg){

        db = denode.getWritableDatabase();
        ContentValues values, newTar;
        int resultado;
        String finalresult = "";

        // selection criteria
        String selection = idTarefa + " = ?" + " AND " + dataTarefa + " = ?";
        // selection arguments
        String[] selectionArgs = {idage, data};

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put(tarefaFeita, feita);

        newTar = new ContentValues();
        newTar.put(idTarefa, idage);
        newTar.put(catTarefa, titCateg);
        newTar.put(tarefaFeita, feita);
        newTar.put(dataTarefa, data);

        resultado = db.update(TABLE_TAREFASCONTROLE, values, selection, selectionArgs);

        if (resultado == -1) {
            finalresult =  "Erro ao atualizar registro";
        } else if (resultado == 0) {
            long newInsert = db.insert(TABLE_TAREFASCONTROLE, null, newTar);
            if (newInsert == -1) {
                finalresult = "Erro ao atualizar registro";
            } else {
                finalresult = "Diário atualizado com as tarefas de hoje";
            }
        } else if (resultado > 0){
            finalresult = "Diário atualizado com as tarefas de hoje";
        }
        return finalresult;
    }

    public String favoritaPost(String id, boolean salvo) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put("SALV_CONT", salvo);

        resultado = db.update(TABLE_CONTEUDO, values, "ID_CONT = ?",new String[] { id });

        if (resultado == -1)
            return "Erro ao favoritar.";
        else
            return "Post favoritado!";

    }

    public String arquivaPost(String id, boolean lido) {
        db = denode.getWritableDatabase();
        ContentValues values;
        long resultado;

        db = denode.getWritableDatabase();
        values = new ContentValues();
        values.put("LIDO_CONT", lido);

        resultado = db.update(TABLE_CONTEUDO, values, "ID_CONT = ?",new String[] { id });

        if (resultado == -1)
            return "Erro ao arquivar.";
        else
            return "Post arquivado!";
    }

    public String deletarTarefa(String id) {
        db = denode.getWritableDatabase();
        int resultado = db.delete("agenda_app", idTarefa + " = "+ id, null);

        if (resultado == -1)
            return "Erro ao deletar meta.";
        else
            return "Meta deletada com sucesso";

    }

    public String deletaCategoria(String id) {
        db = denode.getWritableDatabase();
        int resultado = db.delete(TABLE_CATEGORIAS, idCateg + " = "+ id, null);

        if (resultado == -1)
            return "Erro ao deletar categoria.";
        else
            return "Categoria deletada.";

    }
}


