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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Customer;
import app.catalogo.com.catalogoapp.Model.Product;

public class AddCustomersActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, textEmail;
    String mName, mEmail;

    EditText customerName;
    EditText customerCity;
    EditText customerDirection;
    EditText customerPhone;
    EditText customerEmail;
    EditText customerImage;

    Button addCustomer;

    FirebaseDatabase db;
    DatabaseReference customers;
    DatabaseReference keyP;
    DatabaseReference keyCustomer;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    String keyDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_customers);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Add customer");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        customerName = findViewById(R.id.customerName);
        customerCity = findViewById(R.id.customerCity);
        customerDirection = findViewById(R.id.customerDirection);
        customerPhone = findViewById(R.id.customerPhone);
        customerEmail = findViewById(R.id.customerEmail);
        customerImage = findViewById(R.id.customerImage);

        // Init firebase
        db = FirebaseDatabase.getInstance();
        customers = db.getReference("Customers");
        keyP = customers;
        // Create a empty field(key) in the child Customers
        keyCustomer = keyP.push();

        addCustomer = findViewById(R.id.addCustomer);
        addCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyDb = keyCustomer.getKey();
                // Save customer to db
                Customer customer = new Customer();
                customer.setCustomerKey(keyDb);
                customer.setName(customerName.getText().toString());
                customer.setCity(customerCity.getText().toString());
                customer.setDirection(customerDirection.getText().toString());
                customer.setPhoneNumber(customerPhone.getText().toString());
                customer.setEmail(customerEmail.getText().toString());
                customer.setImage(customerImage.getText().toString());

                // Save the product
                customers.child(keyDb)
                        .setValue(customer)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddCustomersActivity.this, "Successful registration!", Toast.LENGTH_LONG)
                                        .show();
                                Intent intent = new Intent(AddCustomersActivity.this, AllCustomersActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddCustomersActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_search) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
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
}
