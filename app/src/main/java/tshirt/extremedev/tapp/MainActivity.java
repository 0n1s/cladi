package tshirt.extremedev.tapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    ListView listView;
    public RecyclerView recyclerView;
    public RecyclerView.Adapter adapter;
    private Context mContext;
    RecyclerView.LayoutManager layoutManager;
    String actvity_title;
    SharedPreferences sharedPreferences;
    public static final String MyPREFERENCES = "MyPrefs";
    String uID;
    public static JSONArray   jsonArray ;

    public static int item_id=0;
    SharedPreferences sharedpreferences;
    public List<ItemData> listitems;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        getSupportActionBar().setTitle("TApp");


        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.white)));
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetch_items();
            }
        });


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Intent intent=getIntent();
        uID=intent.getStringExtra("email");
       // Toast.makeText(this, "Welcome "+uID, Toast.LENGTH_SHORT).show();

        fetch_items();





    }

    public  void fetch_items()
    {



        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Processing your request");
        progressDialog.show();
        RequestQueue queue = Volley.newRequestQueue(this);
        //Toast.makeText(mContext, String.valueOf(url), Toast.LENGTH_SHORT).show();


        StringRequest stringRequest = new StringRequest(Request.Method.GET, URLs.main_url+"item_fetcher.php",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.cancel();

                      //  Toast.makeText(MainActivity.this, response, Toast.LENGTH_SHORT).show();

//                        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
                        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);//done here
                        layoutManager = new GridLayoutManager(mContext, 1);
                        recyclerView.setLayoutManager(layoutManager);

                        JSONObject jsonObject = null;
                        ItemData itemdata;


                        try {
                            jsonObject = new JSONObject(response);
                            JSONArray result = jsonObject.getJSONArray("result");



                            listitems = new ArrayList<>();
                            for (int i = 0; i < result.length(); i++)
                            {
                                JSONObject jo = result.getJSONObject(i);
                                String item_price=jo.getString("price");
                                String image_url=jo.getString("image");
                                String item_name=jo.getString("name");
                                String item_description=jo.getString("contents");
                                itemdata = new ItemData(item_price, image_url, item_name, item_description);
                                listitems.add(itemdata);
                            }
                            adapter = new MyAdapter(listitems,MainActivity.this);
                            recyclerView.setAdapter(adapter);



                        } catch (JSONException e) {

                            new SweetAlertDialog(MainActivity.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText(e.toString())
                                    .show();

                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.dismiss();
            }
        });

        queue.add(stringRequest);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_everywhere, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("email", "null");
            editor.putString("password", "null");
            editor.commit();
            this.finish();
            Intent intent = new Intent(MainActivity.this, SignIN.class);
            startActivity(intent);
        }



        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
  if (id == R.id.nav_camera1) 
	{

            Intent intent = new Intent(MainActivity.this, ImageEditor.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}


