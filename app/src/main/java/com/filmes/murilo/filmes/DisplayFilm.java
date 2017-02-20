package com.filmes.murilo.filmes;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * Created by root on 17/02/17.
 */

public class DisplayFilm extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.film_details);
        TextView title = (TextView)findViewById(R.id.details_title);
        TextView genre = (TextView)findViewById(R.id.details_genre);
        TextView number = (TextView)findViewById(R.id.details_number);
        CheckBox net = (CheckBox) findViewById(R.id.details_net);
        CheckBox athome = (CheckBox) findViewById(R.id.details_athome);
        title.setText(getIntent().getStringExtra("title"));
        genre.setText(getIntent().getStringExtra("genre"));
        number.setText(getIntent().getStringExtra("number"));
        net.setChecked(getIntent().getStringExtra("net").equals("true"));
        athome.setChecked(getIntent().getStringExtra("athome").equals("true"));
    }
}
