package br.com.cafecomcodigo.boaviagem.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BoaViagemDAO {
    private DatabaseHelper helper;
    private SQLiteDatabase db;

    public BoaViagemDAO(Context context) {
        helper = new DatabaseHelper(context);
    }

    public List<Viagem> listarViagens(){
        Cursor cursor = getDb().query(Viagem.ViagemEntry.TABELA,
                Viagem.ViagemEntry.COLUNAS,
                null, null, null, null, null);
        List<Viagem> viagens = new ArrayList<Viagem>();
        while(cursor.moveToNext()){
            Viagem viagem = criarViagem(cursor);
            viagens.add(viagem);
        }
        cursor.close();
        return viagens;
    }

    private Viagem criarViagem(Cursor cursor) {
        Viagem viagem = new Viagem(

                cursor.getLong(cursor.getColumnIndex(
                        Viagem.ViagemEntry._ID)),

                cursor.getString(cursor.getColumnIndex(
                        Viagem.ViagemEntry.DESTINO)),

                cursor.getInt(cursor.getColumnIndex(
                        Viagem.ViagemEntry.TIPO_VIAGEM)),

                new Date(cursor.getLong(cursor.getColumnIndex(
                        Viagem.ViagemEntry.DATA_CHEGADA))),

                new Date(cursor.getLong(cursor.getColumnIndex(
                        Viagem.ViagemEntry.DATA_SAIDA))),

                cursor.getDouble(cursor.getColumnIndex(
                        Viagem.ViagemEntry.ORCAMENTO)),

                cursor.getInt(cursor.getColumnIndex(
                        Viagem.ViagemEntry.QUANTIDADE_PESSOAS))
        );
        return viagem;
    }

    private SQLiteDatabase getDb() {
        if (db == null) {
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public void close(){
        helper.close();
    }
}