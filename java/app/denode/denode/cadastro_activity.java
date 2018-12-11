package app.denode.denode;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.content.Intent;
import android.text.TextUtils;

import dbManager.bancoController;
import helpers.inputValidation;


import android.widget.TextView;
import android.widget.Toast;

import static app.denode.denode.R.id.email_cadastro;

public class cadastro_activity extends Activity {

    EditText email, nome_cadastro, senha_cadastro;
    Button botao_cadastro;
    String retorno, nomeHolder, emailHolder, senhaHolder;
    Boolean CheckEditText, checkValidation;
    inputValidation validation;
    TextView backTo_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cadastro_activity);

        email = (EditText) findViewById(email_cadastro);
        nome_cadastro = (EditText) findViewById(R.id.nome_cadastro);
        senha_cadastro = (EditText) findViewById(R.id.senha_cadastro);
        botao_cadastro = (Button) findViewById(R.id.botao_cadastro);
        backTo_login = (TextView) findViewById(R.id.backTo_login);

        //Adding Click Listener on button.
        botao_cadastro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean isName = validation.notEmpty(nome_cadastro);
                boolean isEmail = validation.notEmpty(email);
                boolean isSenha = validation.notEmpty(senha_cadastro);

                if(isName && isEmail && isSenha){
                    // If EditText is not empty and CheckEditText = True then this block will execute.
                    bancoController crud = new bancoController(getBaseContext());
                    String resultado;
                    resultado = crud.novoPerfil(nomeHolder, emailHolder, senhaHolder);
                    Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    if(resultado == "Registro inserido com sucesso") {
                    goToLogin(view);
                    }
                } else {
                    if(!isName){Toast.makeText(getApplicationContext(), "Nome inválido, use apenas letras.", Toast.LENGTH_LONG).show();}
                    if(!isEmail){Toast.makeText(getApplicationContext(), "Email inválido.", Toast.LENGTH_LONG).show();}
                    if(!isSenha){Toast.makeText(getApplicationContext(), "Senha inválido, use apenas letras e números.", Toast.LENGTH_LONG).show();}
                }
            }
        });

        backTo_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(cadastro_activity.this,login_activity.class);
                startActivity(intent);

            }
        });

    }

    private void goToLogin(View view) {
        Intent intent = new Intent(this, login_activity.class);
        startActivity(intent);
    }

    public void CheckEditTextIsEmptyOrNot(){

        nomeHolder = nome_cadastro.getText().toString().trim();
        emailHolder = email.getText().toString().trim();
        senhaHolder = senha_cadastro.getText().toString().trim();

        CheckEditText = !(TextUtils.isEmpty(nomeHolder) || TextUtils.isEmpty(emailHolder) || TextUtils.isEmpty(senhaHolder));

    }

    public boolean checkIfValid(){
        checkValidation = helpers.inputValidation.isValidText(nome_cadastro, true);
        return checkValidation;
    }

    public void backTo_login(View view) {
        Intent intent = new Intent(this, login_activity.class);
        startActivity(intent);
    }
}