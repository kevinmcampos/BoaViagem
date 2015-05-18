package br.com.cafecomcodigo.boaviagem;

import android.app.ListActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GastoListActivity extends ListActivity implements AdapterView.OnItemClickListener {

    private List<Map<String, Object>> gastos;
    private String dataAnterior = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String[] de = { "data", "descricao", "valor", "categoria" };
        int[] para = { R.id.data, R.id.descricao, R.id.valor, R.id.categoria };
        SimpleAdapter adapter = new SimpleAdapter(this,
                listarGastos(), R.layout.lista_gasto, de, para);
        adapter.setViewBinder(new GastoViewBinder());
        setListAdapter(adapter);
        getListView().setOnItemClickListener(this);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getMenuInflater().inflate(R.menu.menu_gasto, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.remover_gasto) {
            AdapterViewCompat.AdapterContextMenuInfo info = (AdapterViewCompat.AdapterContextMenuInfo) item
                    .getMenuInfo();
            gastos.remove(info.position);
            getListView().invalidateViews();
            dataAnterior = "";
            // remover do banco de dados
            return true;
        }

        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> map = gastos.get(position);
        String descricao = (String) map.get("descricao");
        String mensagem = "Gasto selecionada: " + descricao;
        Toast.makeText(this, mensagem, Toast.LENGTH_SHORT).show();
    }

    private List<Map<String, Object>> listarGastos() {

        gastos = new ArrayList<>();

        Map<String, Object> item = new HashMap<>();
        item.put("data", "04/02/2012");
        item.put("descricao", "Diária Hotel");
        item.put("valor", "R$ 260,00");
        item.put("categoria", R.color.categoria_hospedagem);
        gastos.add(item);

        item = new HashMap<>();
        item.put("data", "05/02/2012");
        item.put("descricao", "Continental Breakfast");
        item.put("valor", "R$ 310,00");
        item.put("categoria", R.color.categoria_alimentacao);
        gastos.add(item);

        return gastos;
    }

    private class GastoViewBinder implements SimpleAdapter.ViewBinder {

        @Override
        public boolean setViewValue(View view, Object data,
                                    String textRepresentation) {

            if(view.getId() == R.id.data){

                if(dataAnterior.equals(data)){
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.VISIBLE);
                    TextView textView = (TextView) view;
                    textView.setText(textRepresentation);
                    dataAnterior = textRepresentation;
                }
                return true;

            } else if(view.getId() == R.id.categoria){

                Integer id = (Integer) data;
                view.setBackgroundColor(getResources().getColor(id));
                return true;
            }
            return false;
        }
    }
}
