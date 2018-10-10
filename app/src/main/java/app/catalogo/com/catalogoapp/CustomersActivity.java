package app.catalogo.com.catalogoapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.catalogo.com.catalogoapp.Model.Customer;

public class CustomersActivity extends AppCompatActivity {

    TextView textName;
    String mName;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    private RecyclerView mCustomerRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Customer, CustomerViewHolder> mCustomerRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Customers");

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Customers").child(userID);

        textName = findViewById(R.id.textName);

        //"Customers" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Customers");
        mDatabase.keepSynced(true);

        mCustomerRV = (RecyclerView) findViewById(R.id.myRecycleViewCustomers);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        Query personsQuery = personsRef.orderByKey();

        mCustomerRV.hasFixedSize();
        mCustomerRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Customer>().setQuery(personsQuery, Customer.class).build();

        mCustomerRVAdapter = new FirebaseRecyclerAdapter<Customer, CustomerViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CustomerViewHolder holder, int position, @NonNull Customer customer) {
                // Show the products information
                holder.setName(customer.getName());
                holder.setCity(customer.getCity());
                holder.setDirection(customer.getDirection());
                holder.setImage(getBaseContext(), customer.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(CustomersActivity.this, "Holae", Toast.LENGTH_SHORT).show();
                    }
                });
            }


            @Override
            public CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_product, parent, false);

                return new CustomersActivity.CustomerViewHolder(view);
            }
        };

        mCustomerRV.setAdapter(mCustomerRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mCustomerRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mCustomerRVAdapter.stopListening();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public CustomerViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setName(String name) {
            TextView post_name = (TextView) mView.findViewById(R.id.customer_name);
            post_name.setText(name);
        }

        public void setCity(String city) {
            TextView post_city = (TextView) mView.findViewById(R.id.customer_city);
            post_city.setText(city);
        }

        public void setDirection(String direction) {
            TextView post_direction = (TextView) mView.findViewById(R.id.customer_direction);
            post_direction.setText(direction);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.customer_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
