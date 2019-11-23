package com.example.myapplication6;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication6.Database.Database;
import com.example.myapplication6.ViewHolder.CartAdapter;
import com.example.myapplication6.model.Order;
import com.example.myapplication6.model.Request;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

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
                showAlertDialog();



            }
        });
        LoadListFood();

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialog=new AlertDialog.Builder(Cart.this);
        alertDialog.setTitle("one more step");
        alertDialog.setMessage("enter your table number and your comment :  ");
        LayoutInflater inflater = this.getLayoutInflater();
        View order_address_comment =inflater.inflate(R.layout.order_table_comment,null);
        MaterialEditText edttable=(MaterialEditText)order_address_comment.findViewById(R.id.edtTable);
        MaterialEditText edtComment=(MaterialEditText)order_address_comment.findViewById(R.id.edtComment);
        alertDialog.setView(order_address_comment);//add edit text to alert dialog

        alertDialog.setIcon(R.drawable.ic_shopping_cart_black_24dp);
        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //Creat new Request
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    // Name, email address, and profile photo Url
                     name = user.getDisplayName();
                     email = user.getEmail();
                }
                Request request=new Request(
                        //Common.currentUser.getPhone(),
                       // Common.currentUser.getName(),
                        email,
                        edttable.getText().toString(),
                        txtTotalPrice.getText().toString(),
                        edtComment.getText().toString(),
                        cart
                );
                //submit to firebase
                //We will using System to
                requests.child(String.valueOf(System.currentTimeMillis())).setValue(request);
                //Delet Cart
                new Database(getBaseContext()).CleanCart();
                Toast.makeText(Cart.this,"Thank you ,order Place",Toast.LENGTH_SHORT).show();
                finish();
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
        recyclerView.setAdapter(adapter);
        //calculate Totale Price
        int Totale = 0;
        for (Order order : cart)
            Totale += (Integer.parseInt(order.getPrice())) * (Integer.parseInt(order.getQuantity()));
        Locale locale = new Locale("en", "TN");
        NumberFormat fmt = NumberFormat.getCurrencyInstance(locale);
        txtTotalPrice.setText(fmt.format(Totale));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent home = new Intent(Cart.this, Home.class);
        startActivity(home);
        finish();
    }
}
