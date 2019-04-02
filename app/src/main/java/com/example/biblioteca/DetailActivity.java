package com.example.biblioteca;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity {
    private static final String BASE_URL_CUBIERTA = "http://covers.openlibrary.org/b/id/";

    String cubiertaURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        ImageView cubierta = (ImageView) findViewById(R.id.cubieta);

        String cubiertaID = this.getIntent().getExtras().getString("cubiertaID");
        if (cubiertaID.length() > 0) {
            cubiertaURL = BASE_URL_CUBIERTA + cubiertaID + "-L.jpg";
            Picasso.with(this).load(cubiertaURL)
                    .placeholder(R.drawable.wait)
                    .into(cubierta);
        }

        TextView titulo = (TextView) findViewById(R.id.tituloD);
        TextView fPublicacion = (TextView) findViewById(R.id.publicacion);
        TextView fEditorial = (TextView) findViewById(R.id.idioma);
        TextView fLenguaje = (TextView) findViewById(R.id.generos);

        String tituloID = this.getIntent().getExtras().getString("tituloID");
        String publicacion = this.getIntent().getExtras().getString("publicacion");
        String editorial = this.getIntent().getExtras().getString("editorial");
        String lenguaje = this.getIntent().getExtras().getString("lenguaje");

        titulo.setText(tituloID);
        fPublicacion.setText(publicacion);
        fEditorial.setText(editorial);
        fLenguaje.setText(lenguaje);


    }

}
/*
publish_date
language
subject
 */