package br.com.cafecomcodigo.boaviagem;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ProgressBar;
import android.widget.SimpleAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.cafecomcodigo.boaviagem.data.DatabaseHelper;

public class ViagemListActivity extends ListActivity implements AdapterView.OnItemClickListener,
        DialogInterface.OnClickListener {

    private List<Map<String, Object>> viagens;
    private AlertDialog alertDialog;
    private AlertDialog confirmDialog;
    private int viagemSelecionada;
    private DatabaseHelper databaseHelper;
    private SimpleDateFormat dateFormat;
    private Double valorLimite;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        databaseHelper = new DatabaseHelper(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");

        SharedPreferences preferencias =
                PreferenceManager.getDefaultSharedPreferences(this);
        String valor = preferencias.getString("valor_limite", "-1");
        valorLimite = Double.valueOf(valor);

        String[] de = {"imagem", "destino", "data", "total", "barraProgresso"};
        int[] para = {R.id.tipoViagem, R.id.destino, R.id.data, R.id.valor, R.id.barraProgresso};
        SimpleAdapter adapter =
                new SimpleAdapter(this, listarViagens(),
                        R.layout.lista_viagem, de, para);
        adapter.setViewBinder(new ViagemViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);

        alertDialog = criaAlertDialog();
        confirmDialog = criaDialogConfirmacao();
    }

    private List<Map<String, Object>> listarViagens() {

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        Cursor cursor =
                db.rawQuery("SELECT _id, tipo_viagem, destino, " +
                                "data_chegada, data_saida, orcamento FROM viagem",
                        null);

        viagens = new ArrayList<>();

        while (cursor.moveToNext()) {
            Map<String, Object> item = new HashMap<>();

            String id = cursor.getString(0);
            int tipoViagem = cursor.getInt(1);
            String destino = cursor.getString(2);
            long dataChegada = cursor.getLong(3);
            long dataSaida = cursor.getLong(4);
            double orcamento = cursor.getDouble(5);

            item.put("id", id);
            if (tipoViagem == Constantes.VIAGEM_LAZER) {
                item.put("imagem", R.drawable.lazer);
            } else {
                item.put("imagem", R.drawable.negocios);
            }
            item.put("destino", destino);
            Date dataChegadaDate = new Date(dataChegada);
            Date dataSaidaDate = new Date(dataSaida);
            String periodo = dateFormat.format(dataChegadaDate) + " a "
                    + dateFormat.format(dataSaidaDate);
            item.put("data", periodo);
            double totalGasto = calcularTotalGasto(db, id);
            item.put("total", "Gasto total R$ " + totalGasto);

            double alerta = orcamento * valorLimite / 100;
            Double [] valores =
                    new Double[] { orcamento, alerta, totalGasto };
            item.put("barraProgresso", valores);

            viagens.add(item);
        }

        cursor.close();

        return viagens;
    }

    private double calcularTotalGasto(SQLiteDatabase db, String id) {
        Cursor cursor = db.rawQuery(
                "SELECT SUM(valor) FROM gasto WHERE viagem_id = ?",
                new String[]{id});
        cursor.moveToFirst();
        double total = cursor.getDouble(0);
        cursor.close();

        return total;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        this.viagemSelecionada = position;
        alertDialog.show();


        /*Map<String, Object> map = viagens.get(position);
        String destino = (String) map.get("destino");
        String mensagem = "Viagem selecionada: "+ destino;
        Toast.makeText(getApplicationContext(), mensagem,
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GastoListActivity.class));*/

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

        Intent intent;
        String id = (String) viagens.get(viagemSelecionada).get("id");

        switch (which) {
            case 0:
                intent = new Intent(this, ViagemActivity.class);
                intent.putExtra(Constantes.VIAGEM_ID, id);
                startActivity(intent);
                break;
            case 1:
                startActivity(new Intent(this, GastoActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, GastoListActivity.class));
                break;
            case 3:
                confirmDialog.show();
                break;
            case DialogInterface.BUTTON_POSITIVE:
                viagens.remove(viagemSelecionada);
                removerViagem(id);
                getListView().invalidateViews();
                break;
            case DialogInterface.BUTTON_NEGATIVE:
                confirmDialog.dismiss();
        }

    }

    private void removerViagem(String id) {
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        String where []  = new String[]{ id };
        db.delete("gasto", "viagem_id = ?", where);
        db.delete("viagem", "_id = ?", where);
    }

    private AlertDialog criaAlertDialog() {
        final CharSequence[] items = {
                getString(R.string.editar_viagem),
                getString(R.string.novo_gasto),
                getString(R.string.lista_gastos),
                getString(R.string.remover_viagem) };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.opcoes);
        builder.setItems(items, this);
        return builder.create();
    }

    private AlertDialog criaDialogConfirmacao() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.confirmacao_exclusao_viagem);
        builder.setPositiveButton(getString(R.string.sim), this);
        builder.setNegativeButton(getString(R.string.nao), this);
        return builder.create();
    }

    private class ViagemViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {

            if (view.getId() == R.id.barraProgresso) {
                Double valores[] = (Double[]) data;
                ProgressBar progressBar = (ProgressBar) view;
                progressBar.setMax(valores[0].intValue());
                progressBar.setSecondaryProgress(valores[1].intValue());
                progressBar.setProgress(valores[2].intValue());
                return true;
            }
            return false;
        }
    }
}
