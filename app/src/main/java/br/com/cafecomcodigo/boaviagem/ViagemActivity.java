package br.com.cafecomcodigo.boaviagem;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

import java.util.Calendar;

public class ViagemActivity extends ActionBarActivity {

    private int ano, mes, dia;
    private Button dataChegada;
    private Button dataSaida;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nova_viagem);

        Calendar calendar = Calendar.getInstance();
        ano = calendar.get(Calendar.YEAR);
        mes = calendar.get(Calendar.MONTH);
        dia = calendar.get(Calendar.DAY_OF_MONTH);
        dataChegada = (Button) findViewById(R.id.dataChegada);
        dataChegada.setText(dia + "/" + (mes+1) + "/" + ano);
        dataSaida = (Button) findViewById(R.id.dataSaida);
        dataSaida.setText(dia + "/" + (mes+1) + "/" + ano);
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
            dataChegada.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };

    private DatePickerDialog.OnDateSetListener listenerSaida = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view,
                              int year, int monthOfYear, int dayOfMonth) {
            ano = year;
            mes = monthOfYear;
            dia = dayOfMonth;
            dataSaida.setText(dia + "/" + (mes+1) + "/" + ano);
        }
    };

    public void salvarViagem(View view) {
    }
}
