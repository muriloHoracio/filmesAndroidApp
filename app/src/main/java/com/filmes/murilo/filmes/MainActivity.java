package com.filmes.murilo.filmes;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;


public class MainActivity extends AppCompatActivity {
    private final String SERVER = "http://192.168.0.10:8000/Filmes/ListaFilmes?";
    private final int LIMIT = 500;
    private ListView listView1 = null;
    private ProgressBar progressBarCyclic = null;
    private RequestQueue queue;
    private int offset = 0;
    private FilmDBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        listView1 = (ListView)findViewById(R.id.listView1);
        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView txtTitle = (TextView) view.findViewById(R.id.txtTitle);
                TextView txtNumber = (TextView) view.findViewById(R.id.txtNumber);
                TextView txtGenre = (TextView) view.findViewById(R.id.txtGenre);
                CheckBox cbNet = (CheckBox) view.findViewById(R.id.cbNet);
                CheckBox cbAtHome = (CheckBox) view.findViewById(R.id.cbAtHome);
                Intent i =  new Intent(MainActivity.this,DisplayFilm.class);
                i.putExtra("title",txtTitle.getText());
                i.putExtra("genre",txtGenre.getText());
                i.putExtra("number",txtNumber.getText());
                i.putExtra("net",Boolean.toString(cbNet.isChecked()));
                i.putExtra("athome",Boolean.toString(cbAtHome.isChecked()));
                startActivity(i);
            }
        });
        listView1.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                if(visibility == View.VISIBLE) {
                    listView1.setAdapter(new FilmeAdapter(MainActivity.this, R.layout.listview_item_row, mydb.getAllFilms()));
                }
            }
        });
        progressBarCyclic = (ProgressBar)findViewById(R.id.progress_bar_cyclic);

        mydb = new FilmDBHelper(this);
        queue = Volley.newRequestQueue(this);

        listView1.setAdapter(new FilmeAdapter(this,R.layout.listview_item_row,mydb.getAllFilms()));
    }

    @Override
    public void onNewIntent(Intent intent){
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu,menu);
        SearchManager searchManager = (SearchManager)getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()){
            case R.id.updateDB:
                listView1.setVisibility(View.GONE);
                progressBarCyclic.setVisibility(View.VISIBLE);
                mydb.deleteAllFilms();
                fetchDataFromServer(SERVER);
                break;
            case R.id.search:
                listView1.setAdapter(new FilmeAdapter(MainActivity.this,R.layout.listview_item_row,mydb.getAllFilms()));
                break;
        }
        return true;
    }

    private void fetchDataFromServer(final String server){
        StringRequest request = new StringRequest(
                Request.Method.GET,
                server+"reqType=app_data&limit="+LIMIT+"&offset="+offset,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        if(response.length()>0) {
                            mydb.restoreBackup(response);
                            offset += LIMIT;
                            fetchDataFromServer(server);
                        } else {
                            offset = 0;
                            progressBarCyclic.setVisibility(View.GONE);
                            listView1.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener(){
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        Log.d("error_volley","erro ao requisitar backup de dados");
                    }
                });
        queue.add(request);

    }

    private void handleIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())){
            String query = intent.getStringExtra(SearchManager.QUERY);
            FilmeAdapter adapter = new FilmeAdapter(MainActivity.this,R.layout.listview_item_row,mydb.getData(query));
            listView1.setAdapter(adapter);
        }
    }
}


/*listView1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int id_to_search = position + 1;
                Bundle dataBundle = new Bundle();
                dataBundle.putInt("id",id_to_search);
                Intent intent = new Intent(getApplicationContext(),DisplayFilm.class);
                intent.putExtras(dataBundle);
                startActivity(intent);
            }
        });*/


/*

JSON ARRAY REQUEST: handling an json array response into a ArrayList<Object>

        JsonArrayRequest req = new JsonArrayRequest(server+"reqType=app_data",
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject filme = (JSONObject) response.get(i);
                                    film_data_array.add(new Filme(filme.getString("titulo"), filme.getInt("numero"), filme.getString("genero"), filme.getBoolean("net"), filme.getBoolean("emcasa")));

                                }
                                updateListViewContent(film_data_array);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(req);
*/

/*

THREAD FOR updating the progress bar status

OBS: following object needs to be declared in the activity:
private Handler handler = new Handler();

        Thread t = new Thread(){
            public void run(){
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(progressBarCyclic.getVisibility() == View.VISIBLE){
                            progressBarCyclic.setVisibility(View.GONE);
                        }
                    }
                });
                for(Filme f : film_data_array){
                    mydb.insertFilm(f);
                    progress_status += 1;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progress_status);
                            if(progress_status>=data_count){
                                progressBar.setProgress(0);
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                    });
                }
            }
        };
        t.start();
*/

/*

GETTING THE TOTAL DATA COUNT FROM SERVER FOR SETTING PROGRESS BAR MAX


    private void getTotalDataCountFromServer(final String server){
        StringRequest request = new StringRequest(
            Request.Method.GET,
            server+"reqType=app_data_count",
            new Response.Listener<String>(){
                @Override
                public void onResponse(String response) {
                    data_count = Integer.parseInt(response);
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setMax(data_count);
                }
            },
            new Response.ErrorListener(){
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Log.d("error_volley","erro ao requisitar quantidade total de dados");
                }
        });
        queue.add(request);
    }
*/