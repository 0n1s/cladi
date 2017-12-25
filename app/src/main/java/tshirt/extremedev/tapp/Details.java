package tshirt.extremedev.tapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapLabel;
import com.beardedhen.androidbootstrap.TypefaceProvider;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import us.feras.mdv.MarkdownView;

import static tshirt.extremedev.tapp.MainActivity.MyPREFERENCES;
import static tshirt.extremedev.tapp.MainActivity.item_id;
import static tshirt.extremedev.tapp.MainActivity.jsonArray;
import static tshirt.extremedev.tapp.SignIN.useruser;
import static tshirt.extremedev.tapp.URLs.img_url;


import static tshirt.extremedev.tapp.ImageEditor.back;
import static tshirt.extremedev.tapp.ImageEditor.front;

public class Details extends AppCompatActivity {


    MarkdownView markdownView;
    BootstrapLabel name;
    ImageView image;
    SharedPreferences sharedPreferences;
    BootstrapButton btn;
    TextView price;
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home)
        {
            finish();
        }
        else
            startActivity(new Intent(Details.this, Cart.class));


        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TypefaceProvider.registerDefaultIconSets();
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        sharedPreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        markdownView = (MarkdownView) findViewById(R.id.markdownView);
        name = (BootstrapLabel) findViewById(R.id.textView13);
        image = (ImageView) findViewById(R.id.imageView5);
        btn = (BootstrapButton) findViewById(R.id.button);
        price = (TextView) findViewById(R.id.textView15);
        Intent intent = getIntent();
        final String data_tye = intent.getStringExtra("type");
//        if (data_tye.equals("default")) {

            final String item_price = intent.getStringExtra("item_price");
            String image_url = intent.getStringExtra("image_url");
            final String item_name = intent.getStringExtra("item_name");
            String item_description = intent.getStringExtra("item_description");
            price.setText("KES " + item_price);
            //Toast.makeText(this, item_description, Toast.LENGTH_SHORT).show();
            markdownView.loadMarkdown("## " + item_description);
            name.setText(item_name);
            String final_url = img_url + image_url;

            if (data_tye.equals("default")) {

                Picasso.Builder builder = new Picasso.Builder(Details.this);
                builder.listener(new Picasso.Listener() {

                    @Override
                    public void onImageLoadFailed(Picasso picasso, Uri uri, Exception exception) {
                    }
                });


                builder.build().load(final_url).into(image);

            }
            else
            {


                if (front.length()!= 0)
                {
                    try {


                        front=front.substring(front.indexOf(",")  + 1);
                        byte[] decodedString = Base64.decode(front, Base64.DEFAULT);
                        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                        image.setImageBitmap(decodedByte);
                        back=back.substring(back.indexOf(",")  + 1);
                        byte[] byteback = Base64.decode(back, Base64.DEFAULT);
                        Bitmap bitback = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);




                    } catch (Exception ex)
                    {
                    }



                    markdownView.loadMarkdown("## " + "Custom tshirt design.");
                    name.setText(item_name);

                }

            }
            btn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {

                    Date date = Calendar.getInstance().getTime();
                    DateFormat formatter = new SimpleDateFormat("MMM-dd-yyyy hh:mm:ss aaa");
                    final String today = formatter.format(date);
                    AlertDialog.Builder builder1 = new AlertDialog.Builder(Details.this);
                    builder1.setMessage("Are you sure you want to add the T-shirt to your cart?");
                    builder1.setCancelable(false);
                    builder1.setPositiveButton(
                            "Yes",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();


                                        String shopping_list= sharedPreferences.getString("shopping_list", "null");

                                        if(shopping_list.equals("null"))
                                        {
                                            jsonArray   = new JSONArray();
                                        }
                                        else {

                                            try {
                                                jsonArray = new JSONArray(shopping_list);
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }

                                        }

                                        int amt = Integer.parseInt(item_price);
                                        int qy = 1;
                                        int final_price = amt * qy;
                                        JSONObject current_item = new JSONObject();
                                        try {
                                            current_item.put("id", String.valueOf(item_id));
                                            current_item.put("tshirt_name", item_name);
                                            current_item.put("quantity", String.valueOf(qy));
                                            current_item.put("final_amt", String.valueOf(final_price));
                                            current_item.put("date_time", today);
                                            current_item.put("tshirt_type", data_tye);
                                            if(data_tye.equals("default"))
                                            {

                                            }
                                            else {
                                                current_item.put("front", front);
                                                current_item.put("back", back);
                                            }




                                        } catch (JSONException e) {
                                            // TODO Auto-generated catch block
                                            e.printStackTrace();
                                        }
                                        item_id = item_id + 1;
                                        jsonArray.put(current_item);
                                        // Toast.makeText(Details.this, String.valueOf(jsonArray), Toast.LENGTH_SHORT).show();

                                        SharedPreferences.Editor editor = sharedPreferences.edit();

                                        editor.putString("shopping_list", String.valueOf(jsonArray));
                                        editor.commit();

                                        final AlertDialog.Builder alert = new AlertDialog.Builder(Details.this);
                                        alert.setTitle("Succcess");
                                        alert.setMessage(item_name +" added to the shopping list");
                                        alert.setPositiveButton("Purchase now!", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                startActivity(new Intent(Details.this, Cart.class));


                                            }
                                        }).setNegativeButton("continue", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            }
                                        }).show();








                                }
                            });

                    builder1.setNegativeButton(
                            "View cart",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            });

                    AlertDialog alert11 = builder1.create();
                    alert11.show();



                }
            });





    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_everywhere, menu);
        return true;
    }
    //and this to handle actions


    public void makeorder(final String contents,
                          final String useruser,
                          final String date,
                          final  String price)
    {
        class GetJSON extends AsyncTask<Void, Void, String> {

           ProgressDialog progressDialog = new ProgressDialog(Details.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog.setMessage("Ordering tshirt..");
                progressDialog.show();

            }
            @Override
            protected String doInBackground(Void... params) {

                RequestHandler rh = new RequestHandler();
                HashMap<String,String> parabms = new HashMap<>();
                parabms.put("items",contents);
                parabms.put("price",price);
                parabms.put("date",date);
                parabms.put("trxID",String.valueOf(System.currentTimeMillis()));
                parabms.put("uid",useruser);
                String res = rh.sendPostRequest(URLs.main_url+"saver.php", parabms);
                return res;

            }
            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                progressDialog.dismiss();
                showthem2(s);

            }


        }
        GetJSON jj =new GetJSON();
        jj.execute();
    }

    public void showthem2(String s)
    {
       // Toast.makeText(this, "Error:"+s, Toast.LENGTH_SHORT).show();
        JSONObject jsonObject = null;
        ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

        try {
            jsonObject = new JSONObject(s);
            JSONArray result = jsonObject.getJSONArray("result");

            String itemID="";
            String succes="0";
            for (int i = 0; i < result.length(); i++)
            {  JSONObject jo = result.getJSONObject(i);

                String success=jo.getString("status");

                if(success.equals("0"))
                {

                    new AlertDialog.Builder(Details.this).setMessage("Oops! Something went wrong").show();
                }
                else{
//                    new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
//                            .setTitleText("Hurray!")
//                            .setContentText("Order made successfully")
//                            .show();


                    new AlertDialog.Builder(Details.this).setMessage("Order made successfully").show();
                }

            }
        } catch (JSONException e) {

            Toast.makeText(this, "Ooops! Something went wrong", Toast.LENGTH_SHORT).show();
        }}

}
