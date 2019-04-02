package com.example.biblioteca;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import cz.msebera.android.httpclient.Header;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

        ShareActionProvider vShareActionProvider;

        public boolean onCreateOptionsMenu(Menu menu){
            getMenuInflater().inflate(R.menu.menu, menu);
            MenuItem compartirItem = menu.findItem(R.id.menu_compartir);
            if(compartirItem != null) {
                vShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(compartirItem);
            }

            setShareIntent();
            return super.onCreateOptionsMenu(menu);

        }

    private void setShareIntent() {
            if(vShareActionProvider!=null){
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Buscador de libros");
                shareIntent.putExtra(Intent.EXTRA_TEXT,"-Compartir-");

                vShareActionProvider.setShareIntent(shareIntent);
            }
    }

    private static final String QUERY_URL= "http://openlibrary.org/search.json?q=";
        EditText Frase;
        Button btnBuscar;
        ListView lstLista;
        AdaptadorJSON adaptador;
        FrameLayout barraContenedor;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);

            Toolbar menu = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(menu);

            Frase = (EditText) this.findViewById(R.id.txtFrase);
            btnBuscar = this.findViewById(R.id.btnBuscar);
            btnBuscar.setOnClickListener(new View.OnClickListener(){

                public void onClick(View view){
                    String frase = Frase.getText().toString();
                    queryBooks(frase);
                }
            });

            adaptador = new AdaptadorJSON(this,getLayoutInflater());
            lstLista = (ListView) findViewById(R.id.lista);
            lstLista.setAdapter(adaptador);
            lstLista.setOnItemClickListener(this);
        }
        private void queryBooks (String frase){
        String url = "";
        try{
            url = URLEncoder.encode(frase, "utf-8");
        }
        catch (UnsupportedEncodingException e){
            e.printStackTrace();
            Toast.makeText(this,"Error:"+ e.getMessage(),Toast.LENGTH_LONG).show();
        }
        barraContenedor = (FrameLayout) findViewById(R.id.barraProgresoContenedor);
        barraContenedor.setVisibility(View.VISIBLE);
        AsyncHttpClient cliente = new AsyncHttpClient();
        cliente.get( QUERY_URL + url,new JsonHttpResponseHandler(){
            public void onSuccess(int statusCode,Header[] headers, JSONObject response) {
                Toast.makeText(getApplicationContext(), "OK!", Toast.LENGTH_LONG).show();
                adaptador.updateData(response.optJSONArray("docs"));
                barraContenedor.setVisibility(View.GONE);

            }
            public void onFairule(int statusCode,Header[] headers, Throwable throwable, JSONObject errorResponse){
                Toast.makeText(getApplicationContext(), "ERROR:"+ statusCode+""+throwable.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("Error",statusCode+""+throwable.getMessage());
                barraContenedor.setVisibility(View.GONE);
            }
        });

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int i, long l) {
        JSONObject objeto = (JSONObject)adaptador.getItem(i);
        String cubiertaID = objeto.optString("cover_i","");
        String tituloID = objeto.optString("title","");
        String publicacion = objeto.optString("publish_date");

        String editorial = objeto.optString("publisher","");
        String lenguaje = objeto.optString("author_name","");

        Intent detailIntent = new Intent(this,DetailActivity.class);
        detailIntent.putExtra("cubiertaID", cubiertaID);
        detailIntent.putExtra("tituloID", tituloID);
        detailIntent.putExtra("publicacion",publicacion);
        detailIntent.putExtra("editorial", editorial);
        detailIntent.putExtra("lenguaje", lenguaje);
        startActivity(detailIntent);
    }

}
