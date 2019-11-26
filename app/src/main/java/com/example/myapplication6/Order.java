package com.example.myapplication6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.myapplication6.Interface.ItemClickListener;
import com.example.myapplication6.ViewHolder.MenuViewHolder;
import com.example.myapplication6.ViewHolder.OrderViewHolder;
import com.example.myapplication6.model.Category;
import com.example.myapplication6.model.Request;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Order extends AppCompatActivity {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    String name,email;
    FirebaseRecyclerAdapter<Request, OrderViewHolder> adapter;

    FirebaseDatabase database;
    DatabaseReference requests;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
        //Firebase
        database = FirebaseDatabase.getInstance();
        requests=database.getReference("Requests");
        //Init
        recyclerView = (RecyclerView) findViewById(R.id.ListOrders);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // Name, email address, and profile photo Url
            //name = user.getDisplayName();
            email = user.getEmail();
        }
        loadOrders(email);
    }
    private void loadOrders(String email){
        FirebaseRecyclerOptions<Request> options = new FirebaseRecyclerOptions.Builder<Request>()
                .setQuery(requests.orderByChild("name").equalTo(email), Request.class
                        )
                .build();
        adapter=new FirebaseRecyclerAdapter<Request, OrderViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull OrderViewHolder orderViewHolder, int i, @NonNull Request request) {
                orderViewHolder.txtOrderId.setText(adapter.getRef(i).getKey());
                orderViewHolder.txtOrderTable.setText(request.getTable());
                orderViewHolder.txtOrderEmail.setText(request.getName());
                orderViewHolder.txtOrderPrice.setText(request.getTotale());


            }

            @NonNull
            @Override
            public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View itemView = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.order_layout, parent, false);
                return new OrderViewHolder(itemView);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    public void onBackPressed() {
        Intent home = new Intent(Order.this, Home.class);

        startActivity(home);
    }
}
