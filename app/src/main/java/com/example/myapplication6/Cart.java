package com.example.myapplication6;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.myapplication6.Database.Database;
import com.example.myapplication6.ViewHolder.CartAdapter;
import com.example.myapplication6.common.Common;
import com.example.myapplication6.model.Order;
import com.example.myapplication6.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Cart extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    FirebaseDatabase database;
    DatabaseReference requests;
    TextView txtTotalPrice;
    String name,email;
    Button btnplace;
    List<Order> cart = new ArrayList<>();
    CartAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        //Firebase
        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        //Init
        recyclerView = (RecyclerView) findViewById(R.id.ListCart);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        txtTotalPrice = (TextView) findViewById(R.id.total);
        //name = (TextView) findViewById(R.id.editText2);
        btnplace = (Button) findViewById(R.id.btnplaceOrder);
        btnplace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Creat new Request
                if(cart.size()>0) {
                    showAlertDialog();

                }
                else{
                    Toast.makeText(Cart.this,"your cart is empty !!",Toast.LENGTH_SHORT).show();

                }



            }
        });
        LoadListFood();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("one more step");
        alertDialog.setMessage("enter your table number:  ");
        final EditText edttable = new EditText(Cart.this);
        LinearLayout.LayoutParams lp=new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        );
        edttable.setLayoutParams(lp);
        alertDialog.setView(edttable);//add edit text to alert dialog
        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Creat new Request
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                String uid="";
                if (user != null) {
                    // Name, email address, and profile photo Url
                    //name = user.getDisplayName();
                    uid = user.getUid();
                    email = user.getEmail();
                }

                Request request = new Request(
                            //Common.currentUser.getPhone(),
                            // Common.currentUser.getName(),
                            email,
                            edttable.getText().toString(),
                            txtTotalPrice.getText().toString(),
                            cart
                );
                    //submit to firebase
                    //We will using System to
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);


                JSONObject obj =new JSONObject();

                JSONArray array = new JSONArray();
                try {
                    obj.put("email1","wassim.b.slimene@gmail.com");
                    obj.put("email",email);
                    obj.put("table",edttable.getText().toString());
                    for(Order o:cart){
                        JSONObject item =new JSONObject();
                        item.put("foodname",o.getProductName());
                        item.put("quantity",o.getQuantity());
                        item.put("comments",o.getComment());
                        array.put(item);
                    }
                    obj.put("orders", array);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
                // Instantiate the RequestQueue.
                RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
                String url ="http://eat4up-env.hk7hshzsff.eu-west-1.elasticbeanstalk.com/email.php";

// Request a string response from the provided URL.
                JsonObjectRequest stringRequest = new JsonObjectRequest(url, obj,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Display the first 500 characters of the response string.
                                Toast.makeText(Cart.this, "order successf", Toast.LENGTH_SHORT).show();
                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       // System.out.println(error.networkResponse.data.toString());
                        error.printStackTrace();
                    }
                });

// Add the request to the RequestQueue.
                queue.add(stringRequest);

                //Delet Cart
                new Database(getBaseContext()).CleanCart();
                Toast.makeText(Cart.this,"Thank you ,order Place",Toast.LENGTH_SHORT).show();
                LoadListFood();


            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();

            }
        });
        alertDialog.show();

    }

    private void LoadListFood() {
        cart = new Database(this).getCarts();

        //adapter = new CartAdapter(cart);
        adapter=new CartAdapter(cart,this);
        adapter.notifyDataSetChanged();
        recyclerView.setAdapter(adapter);
        //calculate Totale Price
        float Totale = 0;
        for (Order order : cart)
            Totale += (Float.parseFloat(order.getPrice())) * (Float.parseFloat(order.getQuantity()));
        Locale locale = new Locale("en", "TN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(Totale));
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getTitle().equals(Common.DELETE))
            deleteCart(item.getOrder());
        return true;
    }
    private void deleteCart(int position){
        cart.remove(position);
        new Database(this).CleanCart();
        for (Order item:cart)
            new  Database(this).addToCart(item);

        LoadListFood();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        //Intent home = new Intent(Cart.this, Home.class);
        //startActivity(home);
        finish();
    }
}
