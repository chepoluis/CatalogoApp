package app.catalogo.com.catalogoapp.historyCashPurchase;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import app.catalogo.com.catalogoapp.R;

public class HistoryCashActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    private RecyclerView mCashRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<HistoryCashObject, SalesViewHolder> mSaleRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Sales");

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        // Get user name and it's set in the navigation view
        //getUserName();

        //"Products" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("cashPurchase");
        mDatabase.keepSynced(true);

        mCashRV = (RecyclerView) findViewById(R.id.myRecycleView); //myRecyclerViewCash - activity_sales_made

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
        Query personsQuery = personsRef.orderByKey();

        mCashRV.hasFixedSize();
        mCashRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions saleOptions = new FirebaseRecyclerOptions.Builder<HistoryCashObject>().setQuery(personsQuery, HistoryCashObject.class).build();

        mSaleRVAdapter = new FirebaseRecyclerAdapter<HistoryCashObject, SalesViewHolder>(saleOptions) {
            @Override
            protected void onBindViewHolder(@NonNull SalesViewHolder holder, int position, @NonNull HistoryCashObject model) {
                // Show the sales information
                holder.setCustomerName(model.getCustomerName());
                holder.setProductName(model.getProductPurchased());
                holder.setProductPrice(model.getProductCost());
                holder.setImage(getBaseContext(), model.getCustomerImage());

                holder.mViewCash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(HistoryCashActivity.this, "Holae", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public SalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_sales_cash, parent, false);

                return new SalesViewHolder(view);
            }
        };

        mCashRV.setAdapter(mSaleRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mSaleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSaleRVAdapter.stopListening();


    }

    /*private void getUserName() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                textName = (TextView) headerView.findViewById(R.id.textName);

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if (map.get("name") != null) {
                    mName = map.get("name").toString();
                    textName.setText(mName);
                }
                *//*mName = dataSnapshot.child("Users").child(userID).child("name").getRef().toString();
                textName.setText(mName);
*//*
                    *//*if(map.get("profileImageUrl") != null) {
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(mProfileImageUrl).into(mProfileImage);
                    }*//*
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error: ", databaseError.toString());
            }
        });
    }*/

    public static class SalesViewHolder extends RecyclerView.ViewHolder {
        public View mViewCash;

        public SalesViewHolder(View itemView) {
            super(itemView);
            mViewCash = itemView;
        }

        public void setCustomerName(String name) {
            TextView post_name = (TextView) mViewCash.findViewById(R.id.customer_name_cash);
            post_name.setText(name);
        }

        public void setProductName(String productName) {
            TextView post_product = (TextView) mViewCash.findViewById(R.id.product_cash);
            post_product.setText(productName);
        }

        public void setProductPrice(String productPrice) {
            TextView post_price = (TextView) mViewCash.findViewById(R.id.product_cost_cash);
            post_price.setText(productPrice);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mViewCash.findViewById(R.id.product_image_cash);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}
