package app.catalogo.com.catalogoapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import app.catalogo.com.catalogoapp.Model.Product;

public class AllProductsActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView empty;
    ImageView icon_empty;

    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Product, ProductsActivity.ProductsViewHolder> mProductRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_products);

        empty = findViewById(R.id.empty);
        icon_empty = findViewById(R.id.icon_empty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("All products");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(2).setChecked(true);

        //

        //"Products" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Products");
        mDatabase.keepSynced(true);

        // Check if there are products and show a text
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If Products child exist
                if (snapshot.hasChild("Products")) {
                    empty.setVisibility(View.GONE);
                    icon_empty.setVisibility(View.GONE);
                } else {
                    empty.setVisibility(View.VISIBLE);
                    icon_empty.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error: ", databaseError.toString());
            }
        });
            
        mPeopleRV = (RecyclerView) findViewById(R.id.myRecycleView);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Products");
        Query personsQuery = personsRef.orderByKey();
        

        mPeopleRV.hasFixedSize();
        mPeopleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Product>()
                .setQuery(personsQuery, Product.class).build();

        mProductRVAdapter = new FirebaseRecyclerAdapter<Product, ProductsActivity.ProductsViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull ProductsActivity.ProductsViewHolder holder,
                                            int position, @NonNull final Product product) {
                // Show the product information
                holder.setTitle(product.getName());
                holder.setPrice("$ " + product.getPrice() + " MXN");
                holder.setAmount("Existing amount: " + product.getAmount());
                holder.setImage(getBaseContext(), product.getImage());

                // Pass the information to ProductInformationActivity
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(AllProductsActivity.this, ProductInformationActivity.class);
                        intent.putExtra("productKey", product.getProductKey());
                        intent.putExtra("productName",product.getName());
                        intent.putExtra("productDescription",product.getDescription());
                        intent.putExtra("productPrice",product.getPrice());
                        intent.putExtra("productAmount",product.getAmount());
                        intent.putExtra("productImage",product.getImage());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public ProductsActivity.ProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_product, parent, false);

                return new ProductsActivity.ProductsViewHolder(view);
            }
        };

        mPeopleRV.setAdapter(mProductRVAdapter);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Hola!", Toast.LENGTH_SHORT).show();
        } else if(id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } if (id == R.id.nav_products) {
            return true;
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    // Is established  the information in the methods
    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        View mView;

        public NewsViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
        }

        public void setTitle(String title) {
            TextView post_title = (TextView) mView.findViewById(R.id.product_name);
            post_title.setText(title);
        }

        public void setPrice(String desc) {
            TextView post_desc = (TextView) mView.findViewById(R.id.product_price);
            post_desc.setText(desc);
        }

        public void setAmount(String amount) {
            TextView post_amount = (TextView) mView.findViewById(R.id.product_amount);
            post_amount.setText(amount);
        }

        public void setImage(Context ctx, String image) {
            ImageView post_image = (ImageView) mView.findViewById(R.id.product_image);
            Picasso.with(ctx).load(image).into(post_image);
        }
    }

    //

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_add) {
            Intent intent = new Intent(this, AddProductActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
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
}
