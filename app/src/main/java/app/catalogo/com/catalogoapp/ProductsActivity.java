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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

import app.catalogo.com.catalogoapp.Model.Product;

public class ProductsActivity extends AppCompatActivity {
    private RecyclerView mProductRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Product, ProductsViewHolder> mProductRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Products");

        //"Products" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabase.keepSynced(true);

        mProductRV = (RecyclerView) findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Query personsQuery = personsRef.orderByKey();

        mProductRV.hasFixedSize();
        mProductRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Product>().setQuery(personsQuery, Product.class).build();

        mProductRVAdapter = new FirebaseRecyclerAdapter<Product, ProductsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsViewHolder holder, int position, @NonNull Product product) {
                // Show the products information
                holder.setTitle(product.getName());
                holder.setPrice(product.getPrice());
                holder.setAmount(product.getAmount());
                holder.setImage(getBaseContext(), product.getImage());

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ProductsActivity.this, "Hola", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_product, parent, false);

                return new ProductsViewHolder(view);
            }
        };

        mProductRV.setAdapter(mProductRVAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        mProductRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mProductRVAdapter.stopListening();


    }

    public static class ProductsViewHolder extends RecyclerView.ViewHolder{
        View mView;

        public ProductsViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title){
            TextView post_title = (TextView)mView.findViewById(R.id.product_name);
            post_title.setText(title);
        }

        public void setPrice(String desc){
            TextView post_desc = (TextView)mView.findViewById(R.id.product_price);
            post_desc.setText(desc);
        }

        public void setAmount(String amount){
            TextView post_amount = (TextView)mView.findViewById(R.id.product_amount);
            post_amount.setText(amount);
        }

        public void setImage(Context ctx, String image){
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }
}

