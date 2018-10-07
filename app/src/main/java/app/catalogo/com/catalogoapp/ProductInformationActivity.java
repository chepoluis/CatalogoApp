package app.catalogo.com.catalogoapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Product;

public class ProductInformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName;
    String mName;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    private RecyclerView mPeopleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Product, ProductsActivity.ProductsViewHolder> mProductRVAdapter;
    ImageView image;

    EditText productName;
    EditText productDescription;
    EditText productPrice;
    EditText productAmount;
    EditText productImage;

    String nameIntent, descriptionIntent, priceIntent, amountIntent, imageIntent;

    FirebaseDatabase db;
    DatabaseReference products;
    DatabaseReference keyP;
    DatabaseReference keyProduct;
    String product_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_information);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        // Get user name and it's set in the navigation view
        getUserName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit product");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Init firebase
        db = FirebaseDatabase.getInstance();
        products = FirebaseDatabase.getInstance().getReference().child("Products");

        image = findViewById(R.id.product_image);

        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productAmount = findViewById(R.id.productAmount);
        productImage = findViewById(R.id.productImage);

        product_key = getIntent().getExtras().getString("productKey");
        nameIntent = getIntent().getExtras().getString("productName");
        descriptionIntent = getIntent().getExtras().getString("productDescription");
        priceIntent = getIntent().getExtras().getString("productPrice");
        amountIntent = getIntent().getExtras().getString("productAmount");
        imageIntent = getIntent().getExtras().getString("productImage");

        // Load the image
        Glide.with(this).load(imageIntent).into(image);

        productName.setText(nameIntent);
        productDescription.setText(descriptionIntent);
        productPrice.setText(priceIntent);
        productAmount.setText(amountIntent);
        productImage.setText(imageIntent);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_edit) {
            // Save product to db
            Product product = new Product();
            product.setProductKey(product_key);
            product.setName(productName.getText().toString());
            product.setDescription(productDescription.getText().toString());
            product.setPrice(productPrice.getText().toString());
            product.setAmount(productAmount.getText().toString());
            product.setImage(productImage.getText().toString());

            products.child(product_key)
                    .setValue(product)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Toast.makeText(ProductInformationActivity.this, "Changes done", Toast.LENGTH_LONG)
                                    .show();
                            Intent intent = new Intent(ProductInformationActivity.this, AllProductsActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductInformationActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else if (id == R.id.action_delete) {

            DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case DialogInterface.BUTTON_POSITIVE:
                            Toast.makeText(ProductInformationActivity.this, "Product eliminated", Toast.LENGTH_SHORT).show();
                            products.child(product_key).removeValue();
                            Intent intent = new Intent(ProductInformationActivity.this, AllProductsActivity.class);
                            startActivity(intent);
                            finish();
                            break;

                        case DialogInterface.BUTTON_NEGATIVE:
                            Toast.makeText(ProductInformationActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            };

            AlertDialog.Builder builder = new AlertDialog.Builder(ProductInformationActivity.this);
            builder.setMessage("Do you want to eliminate " +productName.getText().toString()+"?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();

        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    private void getUserName() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                textName = (TextView) headerView.findViewById(R.id.textName);

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("name") != null) {
                    mName = map.get("name").toString();
                    textName.setText(mName);
                }
                /*mName = dataSnapshot.child("Users").child(userID).child("name").getRef().toString();
                textName.setText(mName);
*/
                    /*if(map.get("profileImageUrl") != null) {
                        mProfileImageUrl = map.get("profileImageUrl").toString();
                        Glide.with(getApplication()).load(mProfileImageUrl).into(mProfileImage);
                    }*/
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("Error: ", databaseError.toString());
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Toast.makeText(this, "Hola!", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_home) {
            Intent intent = new Intent(this, HomeActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_products) {
            Intent intent = new Intent(this, AllProductsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
