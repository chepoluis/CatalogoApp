package app.catalogo.com.catalogoapp;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
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

import app.catalogo.com.catalogoapp.Model.Customer;

public class CustomerInformationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    ImageView image;

    EditText customerName;
    EditText customerCity;
    EditText customerAddress;
    EditText customerPhone;
    EditText customerEmail;
    EditText customerImage;

    String nameIntent, cityIntent, addressIntent, phoneIntent, emailIntent, imageIntent;

    FirebaseDatabase db;
    DatabaseReference customers;
    String customer_key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_information);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit customer");
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
        customers = FirebaseDatabase.getInstance().getReference().child("Customers");

        image = findViewById(R.id.customerImageU);

        customerName = findViewById(R.id.customer_name);
        customerCity = findViewById(R.id.customer_city);
        customerAddress = findViewById(R.id.customer_address);
        customerPhone = findViewById(R.id.customer_phone);
        customerEmail = findViewById(R.id.customer_email);
        customerImage = findViewById(R.id.customer_image);

        customer_key = getIntent().getExtras().getString("customerKey");
        nameIntent = getIntent().getExtras().getString("customerName");
        cityIntent = getIntent().getExtras().getString("customerCity");
        addressIntent = getIntent().getExtras().getString("customerAddress");
        phoneIntent = getIntent().getExtras().getString("customerPhone");
        emailIntent = getIntent().getExtras().getString("customerEmail");
        imageIntent = getIntent().getExtras().getString("customerImage");

        // Load the image
        Glide.with(this).load(imageIntent).into(image);

        customerName.setText(nameIntent);
        customerCity.setText(cityIntent);
        customerAddress.setText(addressIntent);
        customerPhone.setText(phoneIntent);
        customerEmail.setText(emailIntent);
        customerImage.setText(imageIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_customer, menu);
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
            saveChangesCustomer();
        } else if (id == R.id.action_delete) {
            deleteCustomer();
        } else if (id == R.id.action_search) {
            Intent intent = new Intent(this, WebViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.action_call) {
            callCustomer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void callCustomer() {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneIntent));
        if(ActivityCompat.checkSelfPermission(CustomerInformationActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(CustomerInformationActivity.this, "Please, activate the permissions", Toast.LENGTH_LONG).show();
            return;
        }else{
            startActivity(intent);
        }
        toastMessage("Calling " + nameIntent);
    }

    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private void deleteCustomer() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        Toast.makeText(CustomerInformationActivity.this, "Customer eliminated", Toast.LENGTH_SHORT).show();
                        customers.child(customer_key).removeValue();
                        Intent intent = new Intent(CustomerInformationActivity.this, AllCustomersActivity.class);
                        startActivity(intent);
                        finish();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(CustomerInformationActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(CustomerInformationActivity.this);
        builder.setMessage("Do you want to eliminate " + customerName.getText().toString()+"?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    private void saveChangesCustomer() {
        // Save customer to db
        Customer customer = new Customer();
        customer.setCustomerKey(customer_key);
        customer.setName(customerName.getText().toString());
        customer.setCity(customerCity.getText().toString());
        customer.setAddress(customerAddress.getText().toString());
        customer.setPhoneNumber(customerPhone.getText().toString());
        customer.setEmail(customerEmail.getText().toString());
        customer.setImage(customerImage.getText().toString());

        customers.child(customer_key)
                .setValue(customer)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CustomerInformationActivity.this, "Changes done", Toast.LENGTH_LONG)
                                .show();
                        Intent intent = new Intent(CustomerInformationActivity.this, AllCustomersActivity.class);
                        startActivity(intent);
                        finish();
                        return;
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CustomerInformationActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
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
