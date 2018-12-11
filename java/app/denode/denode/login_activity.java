package app.denode.denode;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import dbManager.bancoController;
import helpers.inputValidation;

import android.view.WindowManager;

import android.widget.TextView;

import android.view.View.OnClickListener;

import java.util.Random;

import static app.denode.denode.R.id.cor1;
import static app.denode.denode.R.id.cor2;
import static app.denode.denode.R.id.cor3;
import static app.denode.denode.R.id.cor4;
import static app.denode.denode.R.id.cor5;
import static app.denode.denode.R.id.cor6;
import static app.denode.denode.R.id.email;
import static dbManager.DatabaseHandler.idUsu;
import static dbManager.DatabaseHandler.nomeCateg;
import static dbManager.DatabaseHandler.nomeUsu;


/**
 * A login screen that offers login via email/password.
 */

public class login_activity extends Activity {
    private static final String TAG = "login_activity";
    private static final int REQUEST_SIGNUP = 0;

    String idUsu;
    EditText username, password;
    String nomeUsu, usuLogData, usuLogName;
    TextView forgot, pass;
    Button login_button, cadastro_button, button;
    String PasswordHolder, EmailHolder;
    Boolean CheckEditText ;
    inputValidation inputValidation;
    bancoController bancoController;
    Cursor usuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        username = (EditText) findViewById(R.id.email_input);
        password = (EditText) findViewById(R.id.password_input);
        login_button = (Button) findViewById(R.id.entrar_button);
        cadastro_button = (Button) findViewById(R.id.goToCadastro_button);

        forgot = (TextView)findViewById(R.id.forgotPsw);

            bancoController = new bancoController(login_activity.this);
            inputValidation = new inputValidation(login_activity.this);


        forgot.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(login_activity.this);
                LayoutInflater inflater = login_activity.this.getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.forgotpsw_dialog, null);
                dialogBuilder.setView(dialogView);

                final bancoController crud = new bancoController(getBaseContext());

                final EditText email = (EditText) dialogView.findViewById(R.id.emailforgot);

                dialogBuilder.setPositiveButton("Enviar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        String mail = email.getText().toString().trim();
                        boolean valid = crud.emailExists(mail);
                        if(valid == true){
                            sendAndRetrieveCode(mail);
                        } else {
                            Toast.makeText(login_activity.this, "Email não cadastrado. Por favor, insira um email válido.", Toast.LENGTH_LONG).show();
                        }

                    }
                });
                dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                       //
                    }
                });
                AlertDialog b = dialogBuilder.create();
                b.show();
            }
        });

        login_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    inputValidation validation = new inputValidation(getBaseContext());

                    CheckEditTextIsEmptyOrNot();

                    if(!CheckEditText){
                        Toast.makeText(login_activity.this, "Por favor, preencha todos os campos.", Toast.LENGTH_LONG).show();
                    }
                    else if(!validation.isEmail(username, true)) {
                      Toast.makeText(login_activity.this, "Por favor, preencha um email válido.", Toast.LENGTH_LONG).show();
                        return;}
                    else if (!bancoController.validaUser(EmailHolder, PasswordHolder)){
                        Toast.makeText(login_activity.this, "Email ou senha incorreto.", Toast.LENGTH_LONG).show();
                    } else {
                        goTo_home(view);
                    }
                }
            });
        }

    public void sendAndRetrieveCode(final String email){

        char[] chars = "abcdefghijklmnopqrstuvwxyz1234567890".toCharArray();
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 4; i++) {
            char c = chars[random.nextInt(chars.length)];
            sb.append(c);
        }
        final String code = sb.toString();
        Toast.makeText(login_activity.this, code, Toast.LENGTH_LONG).show();

        final EditText codeUser;
        final TextView codeMsg;
        //generate random code and send it to the email

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(login_activity.this);
        LayoutInflater inflater = login_activity.this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.newpsw_dialog, null);
        dialogBuilder.setView(dialogView);

        final bancoController crud = new bancoController(getBaseContext());

        codeUser = (EditText) dialogView.findViewById(R.id.codeInsert);
        codeMsg = (TextView)dialogView.findViewById(R.id.dialogMesg);
        final EditText novaSenha = (EditText)dialogView.findViewById(R.id.novaSenha);
        Button checkCode = dialogView.findViewById(R.id.codeChek);

        codeMsg.setText("");

        checkCode.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String isCode = codeUser.getText().toString().trim();

                if(isCode.equalsIgnoreCase(code)){
                    codeMsg.setText("Código correto.");
                    novaSenha.setEnabled(true);
                } else {
                    codeMsg.setText("Código incorreto.");
                    novaSenha.setEnabled(false);
                }
            }
        });

        dialogBuilder.setPositiveButton("Salvar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newPsw = novaSenha.getText().toString().trim();
                String result = crud.atualizarSenha(email, newPsw);
                Toast.makeText(login_activity.this, result, Toast.LENGTH_LONG).show();
            }
        });
        dialogBuilder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //
            }
        });

        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    public void CheckEditTextIsEmptyOrNot(){

        EmailHolder = username.getText().toString().trim();
        PasswordHolder = password.getText().toString().trim();

        CheckEditText = !(TextUtils.isEmpty(EmailHolder) || TextUtils.isEmpty(PasswordHolder));
    }



    public void goTo_cadastro(View view) {
        Intent intent = new Intent(this, cadastro_activity.class);
        startActivity(intent);
    }
    public void goTo_home(View view) {

        bancoController crud = new bancoController(login_activity.this);
        Cursor cursor = crud.getUsuarioLogado(EmailHolder, PasswordHolder);

        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            usuLogData = String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")));
            usuLogName = cursor.getString(cursor.getColumnIndex("NAME_USU"));
        }

        Intent intent = new Intent(this, home_activity.class);

        intent.putExtra("id", usuLogData);
        intent.putExtra("nome", usuLogName);

        startActivity(intent);
    }

}