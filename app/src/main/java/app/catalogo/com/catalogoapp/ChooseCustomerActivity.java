package app.catalogo.com.catalogoapp;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Customer;

public class ChooseCustomerActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String buyImage, buyName, buyPrice, buyDescription;

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    TextView empty;
    ImageView icon_empty;

    private RecyclerView mCustomerRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<Customer, CustomersActivity.CustomerViewHolder> mCustomerRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_customer);

        buyImage = getIntent().getExtras().getString("productImage");
        buyName = getIntent().getExtras().getString("productName");
        buyPrice = getIntent().getExtras().getString("productPrice");
        buyDescription = getIntent().getExtras().getString("productDescription");

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        empty = findViewById(R.id.empty);
        icon_empty = findViewById(R.id.icon_empty);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Customers");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        navigationView.getMenu().getItem(3).setChecked(true);

        //"Customers" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("Customers");
        mDatabase.keepSynced(true);

        // Check if there are customers and show a text
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If Customers child exist
                if (snapshot.hasChild("Customers")) {
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

        mCustomerRV = (RecyclerView) findViewById(R.id.myRecycleViewCustomers);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("Customers");
        Query personsQuery = personsRef.orderByKey();


        mCustomerRV.hasFixedSize();
        mCustomerRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions personsOptions = new FirebaseRecyclerOptions.Builder<Customer>()
                .setQuery(personsQuery, Customer.class).build();

        mCustomerRVAdapter = new FirebaseRecyclerAdapter<Customer, CustomersActivity.CustomerViewHolder>(personsOptions) {
            @Override
            protected void onBindViewHolder(@NonNull CustomersActivity.CustomerViewHolder holder, int position, @NonNull final Customer customer) {
                // Show the customer information
                holder.setName(customer.getName());
                holder.setCity(customer.getCity());
                holder.setDirection(customer.getDirection());
                holder.setImage(getBaseContext(), customer.getImage());

                // Pass the information to BuyProductActivity
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(ChooseCustomerActivity.this, BuyProductActivity.class);
                        intent.putExtra("customerKey", customer.getCustomerKey());
                        intent.putExtra("customerName",customer.getName());
                        intent.putExtra("customerCity",customer.getCity());
                        intent.putExtra("customerDirection",customer.getDirection());
                        intent.putExtra("customerPhone",customer.getPhoneNumber());
                        intent.putExtra("customerEmail",customer.getEmail());
                        intent.putExtra("customerImage",customer.getImage());

                        intent.putExtra("productName", buyName);
                        intent.putExtra("productPrice", buyPrice);
                        intent.putExtra("productDescription", buyDescription);
                        intent.putExtra("productImage", buyImage);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public CustomersActivity.CustomerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_customer, parent, false);

                return new CustomersActivity.CustomerViewHolder(view);
            }
        };

        mCustomerRV.setAdapter(mCustomerRVAdapter);
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
            return true;
        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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

   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar i f it is present.
        getMenuInflater().inflate(R.menu.customers, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == R.id.action_add_customer) {
            Intent intent = new Intent(this, AddCustomersActivity.class);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }*/

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
