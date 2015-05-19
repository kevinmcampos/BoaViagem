package br.com.cafecomcodigo.boaviagem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

import br.com.cafecomcodigo.boaviagem.data.DatabaseHelper;

public class ViagemActivity extends ActionBarActivity {

    private int ano, mes, dia;
    private Date dataChegada, dataSaida;
    private Button dataChegadaButton;
    private Button dataSaidaButton;
    DatabaseHelper databaseHelper;
    private EditText destino, quantidadePessoas, orcamento;
    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dataChegadaButton = (Button) findViewById(R.id.dataChegada);
        dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        dataSaidaButton = (Button) findViewById(R.id.dataSaida);
        dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);


        destino = (EditText) findViewById(R.id.destino);
        quantidadePessoas = (EditText) findViewById(R.id.quantidadePessoas);
        orcamento = (EditText) findViewById(R.id.orcamento);
        radioGroup = (RadioGroup) findViewById(R.id.tipoViagem);
        // prepara acesso ao banco de dados
        databaseHelper = new DatabaseHelper(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_viagem, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.novo_gasto:
                //
            case R.id.remover_viagem:
                //
        }

        return false;
    }

    // TODO Utilizar DialogFragment com FragmentManager
    public void selecionarData(View view) {
        showDialog(view.getId());
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case R.id.dataChegada:
                return new DatePickerDialog(this, listenerChegada, ano, mes, dia);
            case R.id.dataSaida:
                return new DatePickerDialog(this, listenerSaida, ano, mes, dia);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener listenerChegada = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataChegadaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerSaida = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataSaidaButton.setText(dia + "/" + (mes + 1) + "/" + ano);
        }
    };

    public void salvarViagem(View view) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("destino", destino.getText().toString());
        values.put("data_chegada", dataChegada.getTime());
        values.put("data_saida", dataSaida.getTime());
        values.put("orcamento", orcamento.getText().toString());
        values.put("quantidade_pessoas",
                quantidadePessoas.getText().toString());
        int tipo = radioGroup.getCheckedRadioButtonId();
        if(tipo == R.id.lazer) {
            values.put("tipo_viagem", Constantes.VIAGEM_LAZER);
        } else {
            values.put("tipo_viagem", Constantes.VIAGEM_NEGOCIOS);
        }


        long resultado = db.insert("viagem", null, values);
        if(resultado != -1 ){
            Toast.makeText(this, getString(R.string.registro_salvo),
                    Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, getString(R.string.erro_salvar),
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }
}
