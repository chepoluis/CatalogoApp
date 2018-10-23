package app.catalogo.com.catalogoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Customer;

public class HistoryCashSingleActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    ImageView customerImage;
    ImageView productImage;

    TextView customerName;
    TextView customerCity;
    TextView customerAddress;
    TextView customerPhone;
    TextView customerEmail;
    TextView seller;
    TextView product;
    TextView productPrice;
    TextView productDescription;
    TextView saleDate;

    String customerNameIntent, customerCityIntent, customerAddressIntent,
            customerPhoneIntent, customerEmailIntent, sellerIntent, productIntent, productPriceIntent,
            saleDateIntent, imageCustomerIntent, productImageIntent, productDescriptionIntent;

    FirebaseDatabase db;
    DatabaseReference sale;
    String sale_key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_cash_single);

        customerImage = findViewById(R.id.customerImageSaleMade);
        productImage = findViewById(R.id.productImageSaleMade);

        customerName = findViewById(R.id.customerSaleMade);
        customerCity = findViewById(R.id.customerCitySaleMade);
        customerAddress = findViewById(R.id.customerAddressSaleMade);
        customerPhone = findViewById(R.id.customerPhoneSaleMade);
        customerEmail = findViewById(R.id.customerEmailSaleMade);
        seller = findViewById(R.id.sellerSaleMade);
        product = findViewById(R.id.productSaleMade);
        productDescription = findViewById(R.id.productDescriptionSaleMade);
        productPrice = findViewById(R.id.productPriceSaleMade);
        saleDate = findViewById(R.id.dateSaleMade);

        sale_key = getIntent().getExtras().getString("saleId");
        customerNameIntent = getIntent().getExtras().getString("customerName");
        customerCityIntent = getIntent().getExtras().getString("customerCity");
        customerAddressIntent = getIntent().getExtras().getString("customerAddress");
        customerPhoneIntent = getIntent().getExtras().getString("customerPhone");
        customerEmailIntent = getIntent().getExtras().getString("customerEmail");
        sellerIntent = getIntent().getExtras().getString("seller");
        productIntent = getIntent().getExtras().getString("productPurchase");
        productDescriptionIntent = getIntent().getExtras().getString("productDescription");
        productPriceIntent = getIntent().getExtras().getString("productCost");
        saleDateIntent = getIntent().getExtras().getString("saleDate");
        imageCustomerIntent = getIntent().getExtras().getString("customerImage");
        productImageIntent = getIntent().getExtras().getString("productImage");

        // Load images
        Glide.with(this).load(imageCustomerIntent).into(customerImage);
        Glide.with(this).load(productImageIntent).into(productImage);

        customerName.setText(customerNameIntent);
        customerCity.setText(customerCityIntent);
        customerAddress.setText(customerAddressIntent);
        customerPhone.setText(customerPhoneIntent);
        customerEmail.setText(customerEmailIntent);
        seller.setText(sellerIntent);
        product.setText(productIntent);
        productDescription.setText(productDescriptionIntent);
        productPrice.setText(productPriceIntent);
        saleDate.setText(saleDateIntent);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Cash sale");
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
        sale = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
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
        }
        if (id == R.id.nav_products) {
            Intent intent = new Intent(this, AllProductsActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_customers) {
            Intent intent = new Intent(this, AllCustomersActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_sales_made) {
            Intent intent = new Intent(this, SalesMadeActivity.class);
            startActivity(intent);
            finish();
        } else if(id == R.id.nav_credit_sales) {
            Intent intent = new Intent(this, CreditSalesActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void getUserName() {
        mUserDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
                View headerView = navigationView.getHeaderView(0);
                textName = (TextView) headerView.findViewById(R.id.textName);
                textEmail = (TextView) headerView.findViewById(R.id.textEmail);

                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                if(map.get("name") != null) {
                    mName = map.get("name").toString();
                    textName.setText(mName);
                }

                if(map.get("email") != null){
                    mEmail = map.get("email").toString();
                    textEmail.setText(mEmail);
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
}
