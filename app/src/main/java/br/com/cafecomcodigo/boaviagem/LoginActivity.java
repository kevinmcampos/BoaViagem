package br.com.cafecomcodigo.boaviagem;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class LoginActivity extends ActionBarActivity {

    private EditText usuario;
    private EditText senha;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usuario = (EditText) findViewById(R.id.usuario);
        senha = (EditText) findViewById(R.id.senha);
    }

    public void entrarOnClick(View view) {

        String usuarioInformado = usuario.getText().toString();
        String senhaInformada = senha.getText().toString();
        if("".equals(usuarioInformado) &&
                "".equals(senhaInformada)) {

            startActivity(new Intent(this, DashboardActivity.class));

        } else{

            String mensagemErro = getString(R.string.erro_autenticacao);
            Toast toast = Toast.makeText(this, mensagemErro,
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
