package app.catalogo.com.catalogoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Product;

public class BuyProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    ImageView productImage;
    TextView productName, productPrice, productDescription;
    TextView customerName;
    Button chooseCustomer, btnPay;

    int amountProduct = 0;
    String nameIntent, descriptionIntent, priceIntent, imageIntent, customerNameIntent, productKeyIntent, amountProductIntent;
    String customerKey = "", customerAddress, customerCity, customerPhone, customerImage;

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    RadioGroup radioGroup;
    RadioButton rbCashPayment, rbCreditPayment;
    String paymentMethod = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_product);

        // Init view
        productImage = findViewById(R.id.imageProduct);
        productName = findViewById(R.id.nameProduct);
        productPrice = findViewById(R.id.priceProduct);
        productDescription = findViewById(R.id.descriptionProduct);

        customerName = findViewById(R.id.nameCustomer);
        chooseCustomer = findViewById(R.id.chooseCustomer);

        radioGroup = findViewById(R.id.radioGroup);
        rbCashPayment = findViewById(R.id.rbCashPayment);
        rbCreditPayment = findViewById(R.id.rbCreditPayment);
        btnPay = findViewById(R.id.btnPay);

        productKeyIntent = getIntent().getExtras().getString("productKey");
        nameIntent = getIntent().getExtras().getString("productName");
        priceIntent = getIntent().getExtras().getString("productPrice");
        descriptionIntent = getIntent().getExtras().getString("productDescription");
        amountProductIntent = getIntent().getExtras().getString("productAmount");
        imageIntent = getIntent().getExtras().getString("productImage");

        amountProduct = Integer.parseInt(amountProductIntent);

        customerKey = getIntent().getExtras().getString("customerKey");
        customerAddress = getIntent().getExtras().getString("customerAddress");
        customerPhone = getIntent().getExtras().getString("customerPhone");
        customerCity = getIntent().getExtras().getString("customerCity");
        customerImage = getIntent().getExtras().getString("customerImage");
        customerNameIntent = getIntent().getExtras().getString("customerName");
        if(customerNameIntent == null)
        {
            customerName.setText("Customer: -choose customer-");
        }
        else
        {
            customerName.setText("Customer: " + customerNameIntent);
        }

        // Load the image
        Glide.with(this).load(imageIntent).into(productImage);

        productName.setText("Product name: " + nameIntent);
        productPrice.setText("$ " + priceIntent + " MXN");
        productDescription.setText("Description: " + descriptionIntent);


        chooseCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(BuyProductActivity.this, ChooseCustomerActivity.class);
                intent.putExtra("productImage", imageIntent);
                intent.putExtra("productName", nameIntent);
                intent.putExtra("productPrice", priceIntent);
                intent.putExtra("productKey", productKeyIntent);
                intent.putExtra("productAmount", amountProductIntent);
                intent.putExtra("productDescription", descriptionIntent);
                startActivity(intent);
                finish();
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rbCashPayment.isChecked())
                {
                    recordSale();
                }
                else if(rbCreditPayment.isChecked())
                {
                    Toast.makeText(BuyProductActivity.this, "Funtion not available", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Buy product");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }

    private void recordSale() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("cashPurchase");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerKey).child("cashPurchase");
        DatabaseReference cashPurchase = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
        DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("Products");
        String requestId = cashPurchase.push().getKey();
        sellerRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);

        HashMap map = new HashMap();
        map.put("customerAddress", customerAddress);
        map.put("customerCity", customerCity);
        map.put("customerKey", customerKey);
        map.put("sellerId", userID);
        map.put("customerName", customerNameIntent);
        map.put("sellerName", mName);
        map.put("customerPhone", customerPhone);
        map.put("price", priceIntent);
        map.put("product", nameIntent);
        map.put("productKey", productKeyIntent);
        map.put("imageProduct", imageIntent);
        map.put("customerImage", customerImage);
        cashPurchase.child(requestId).updateChildren(map);

        // Subtract 1 from the product sold and update in the database
        amountProduct--;
        products.child(productKeyIntent)
                .child("amount")
                .setValue(String.valueOf(amountProduct))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(BuyProductActivity.this, "Purchase made", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(BuyProductActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(BuyProductActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG)
                                .show();
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
        } else if (id == R.id.nav_customers) {
            Intent intent = new Intent(this, AllCustomersActivity.class);
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
