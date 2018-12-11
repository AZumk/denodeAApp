package dbManager;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by Ana Zumk on 18/10/2017.
 */

public class DatabaseHandler extends SQLiteOpenHelper {

    private SQLiteDatabase db;
    private DatabaseHandler denode;

    // Logcat tag
    private static final String LOG = "DatabaseHelper";

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "denode.db";

    // Table Names
    public static final String TABLE_AGENDA = "agenda_app";
    public static final String TABLE_CONTEUDO = "conteudo_app";
    public static final String TABLE_PERFUSU = "perfUsu_table";
    public static final String TABLE_TAREFASCONTROLE = "tarefas_controle";
    public static final String TABLE_CATEGORIAS = "categorias_table";

    // NOTES CONTEUDO - column nmaes
    public static final String idCont = "ID_CONT";
    public static final String titulCont = "TIT_CONT";
    public static final String catCont = "CAT_CONT";
    public static final String textCont = "TEX_CONT";
    public static final String dataCont = "DATA_CONT";
    public static final String linkCont = "LINK_CONT";
    public static final String lidoCont = "LIDO_CONT";
    public static final String salvCont = "SALV_CONT";

    // NOTES AGENDA - column nmaes
    public static final String idTarefa = "ID_AGE";
    public static final String titleTarefa = "TIT_AGE";
    public static final String catTarefa = "CAT_AGE";
    public static final String diasTarefa = "DIA_AGE";
    public static final String periTarefa = "PER_AGE";
    public static final String alarmeTarefa = "ALA_AGE";
    public static final String horaAlarme = "HOR_ALARME";

    // NOTES PERFUL USU - column nmaes
    public static final String idUsu = "ID_USU";
    public static final String nomeUsu = "NAME_USU";
    public static final String emailUsu = "EMAIL_USU";
    public static final String senhaUsu = "SENHA_USU";

    // NOTES CONTROLE TAREFAS - column nmaes
    public static final String tarefaFeita = "CHECK_TAREFA";
    public static final String dataTarefa = "DATA_TAREFA";

    //NOTES CATEGORIA - column names
    public static final String idCateg = "ID_CATEG";
    public static final String nomeCateg = "NOME_CATEG";
    public static final String corCateg = "COR_CATEG";

    // Table Create Statements
    // Todo table create statement

    static final String criarAgenda = "CREATE TABLE "
            + TABLE_AGENDA + "(" +
            idTarefa + " INTEGER PRIMARY KEY," +
            titleTarefa + " TEXT," +
            catTarefa + " TEXT," +
            diasTarefa + " TEXT," +
            periTarefa + " TEXT," +
            alarmeTarefa + " BOOLEAN," +
            horaAlarme + " TEXT"
            + ")";

    static final String criarCont = "CREATE TABLE "
            + TABLE_CONTEUDO + "(" +
            idCont + " INTEGER PRIMARY KEY, " +
            titulCont + " TEXT, " +
            catCont + " TEXT, " +
            textCont + " TEXT, " +
            dataCont + " TEXT, " +
            linkCont + " TEXT, " +
            lidoCont + " BOOLEAN, " +
            salvCont + " BOOLEAN"
            + ")";

    static final String criarPerfil = "CREATE TABLE "
            + TABLE_PERFUSU + "(" +
            idUsu + " INTEGER PRIMARY KEY," +
            nomeUsu + " TEXT," +
            emailUsu + " TEXT," +
            senhaUsu + " TEXT"
            + ")";

    static final String criarContro = "CREATE TABLE "
            + TABLE_TAREFASCONTROLE + "(" +
            idTarefa + " INTEGER, " +
            catTarefa + " TEXT, " +
            tarefaFeita + " TEXT, " +
            dataTarefa + " TEXT"
            + ")";

    static final String criarCateg = "CREATE TABLE "
            + TABLE_CATEGORIAS + "(" +
            nomeCateg + " TEXT, " +
            corCateg + " TEXT, " +
            idCateg + " INTEGER PRIMARY KEY " +
            ")";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // creating required tables
        db.execSQL(criarAgenda);
        db.execSQL(criarCont);
        db.execSQL(criarPerfil);
        db.execSQL(criarContro);
        db.execSQL(criarCateg);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // on upgrade drop older tables
        db.execSQL("DROP TABLE IF EXISTS " + criarAgenda);
        db.execSQL("DROP TABLE IF EXISTS " + criarCont);
        db.execSQL("DROP TABLE IF EXISTS " + criarPerfil);
        db.execSQL("DROP TABLE IF EXISTS " + criarContro);
        db.execSQL("DROP TABLE IF EXISTS " + criarCateg);

        // create new tables
        onCreate(db);
    }
//não mexer, isso é da api de ver o bd no device
    public ArrayList<Cursor> getData(String Query){
        //get writable database
        SQLiteDatabase sqlDB = this.getWritableDatabase();
        String[] columns = new String[] { "message" };
        //an array list of cursor to save two cursors one has results from the query
        //other cursor stores error message if any errors are triggered
        ArrayList<Cursor> alc = new ArrayList<Cursor>(2);
        MatrixCursor Cursor2= new MatrixCursor(columns);
        alc.add(null);
        alc.add(null);

        try{
            String maxQuery = Query ;
            //execute the query results will be save in Cursor c
            Cursor c = sqlDB.rawQuery(maxQuery, null);

            //add value to cursor2
            Cursor2.addRow(new Object[] { "Success" });

            alc.set(1,Cursor2);
            if (null != c && c.getCount() > 0) {

                alc.set(0,c);
                c.moveToFirst();

                return alc ;
            }
            return alc;
        } catch(SQLException sqlEx){
            Log.d("printing exception", sqlEx.getMessage());
            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+sqlEx.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        } catch(Exception ex){
            Log.d("printing exception", ex.getMessage());

            //if any exceptions are triggered save the error message to cursor an return the arraylist
            Cursor2.addRow(new Object[] { ""+ex.getMessage() });
            alc.set(1,Cursor2);
            return alc;
        }
    }




}
