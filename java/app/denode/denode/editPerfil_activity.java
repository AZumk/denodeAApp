package app.denode.denode;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import dbManager.bancoController;
import helpers.inputValidation;

import static android.R.attr.min;
import static app.denode.denode.R.id.addTarefa;
import static app.denode.denode.R.id.alarmeTime;
import static dbManager.DatabaseHandler.emailUsu;
import static dbManager.DatabaseHandler.horaAlarme;
import static dbManager.DatabaseHandler.idUsu;
import static dbManager.DatabaseHandler.nomeUsu;

public class editPerfil_activity extends AppCompatActivity {

    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_perfil_activity);

        ImageButton salvar = (ImageButton)findViewById(R.id.salvarPerfilEditado);
        ImageButton back = (ImageButton)findViewById(R.id.volta);
        final EditText nome = (EditText)findViewById(R.id.edperNome);
        final EditText email = (EditText)findViewById(R.id.edperEmail);
        final EditText oldSenha = (EditText)findViewById(R.id.oldPsw);
        final EditText novaSenha = (EditText)findViewById(R.id.newPsw);
        final EditText confirmaSenha = (EditText)findViewById(R.id.newPswConf);

        final bancoController crud = new bancoController(editPerfil_activity.this);
        final inputValidation validation = new inputValidation(editPerfil_activity.this);
        Cursor usuario = crud.getUsuario();

        for (usuario.moveToFirst(); !usuario.isAfterLast(); usuario.moveToNext()) {
            nome.setText(usuario.getString(usuario.getColumnIndex(nomeUsu)));
            email.setText(usuario.getString(usuario.getColumnIndex(emailUsu)));
            id = usuario.getInt(usuario.getColumnIndex("_id"));
        }

        salvar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String newName = nome.getText().toString().trim();
                String newEmail = email.getText().toString().trim();
                String senha1 = oldSenha.getText().toString().trim();
                String senha2 = novaSenha.getText().toString().trim();
                String senha3 = confirmaSenha.getText().toString().trim();

                boolean isName = validation.notEmpty(nome);
                boolean isEmail = validation.notEmpty(email);
                boolean isSenha = validation.notEmpty(novaSenha);

                //usuario não vai atualizar a senha então o banco não altera ela
                if (oldSenha.getText().toString().trim().matches("") || novaSenha.getText().toString().trim().matches("") || confirmaSenha.getText().toString().trim().matches("")){

                    if(isName && isEmail) {
                        String resultado = crud.atualizaNomeEmail(String.valueOf(id), newName, newEmail);
                        Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                    } else {
                        if(!isName){Toast.makeText(getApplicationContext(), "Nome inválido, use apenas letras.", Toast.LENGTH_LONG).show();}
                        if(!isEmail){Toast.makeText(getApplicationContext(), "Email inválido.", Toast.LENGTH_LONG).show();}
                    }
                } else {

                    //usuario vai alterar a senha então tem que ter a checagem
                    if(senha2.equals(senha3)) {

                        //nova senha e confirma senha são iguais
                        int senhaCerta = crud.senhaExists(String.valueOf(id), senha1);

                        if (senhaCerta == 1) {
                            if(isName && isEmail && isSenha) {
                                String resultado = crud.atualizarPerfilCompleto(String.valueOf(id), newName, newEmail, senha2);
                                Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                            } else {
                                if(!isName){Toast.makeText(getApplicationContext(), "Nome inválido, use apenas letras.", Toast.LENGTH_LONG).show();}
                                if(!isEmail){Toast.makeText(getApplicationContext(), "Email inválido.", Toast.LENGTH_LONG).show();}
                                if(!isSenha){Toast.makeText(getApplicationContext(), "Senha inválida, use apenas números e letras.", Toast.LENGTH_LONG).show();}
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Senha atual incorreta.", Toast.LENGTH_LONG).show();
                        }

                    } else {
                        //nova senha e confirma senha estão diferentes
                        Toast.makeText(getApplicationContext(), "A nova senha precisa ser igual nos dois campos.", Toast.LENGTH_LONG).show();

                    }
                }
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
