package app.catalogo.com.catalogoapp;

import android.annotation.SuppressLint;
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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

import app.catalogo.com.catalogoapp.creditPurchase.CreditAdapter;
import app.catalogo.com.catalogoapp.creditPurchase.CreditObject;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashActivity;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashAdapter;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashObject;

public class CreditSalesActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    TextView empty;
    ImageView icon_empty;

/*
    private RecyclerView mCreditSaleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<CreditObject, HistoryCashActivity.SalesViewHolder> mSaleRVAdapter;
*/

    //----
    private String customerOrDriver, userId;

    private RecyclerView mCreditRecyclerView;
    private RecyclerView.Adapter mCreditAdapter;
    private RecyclerView.LayoutManager mCreditLayoutManager;

    //---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_sales);

        mCreditRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerViewCredit);
        // setNestedScrollingEnabled: make the scroll more natural
        mCreditRecyclerView.setNestedScrollingEnabled(false);
        mCreditRecyclerView.setHasFixedSize(true);
        mCreditLayoutManager = new LinearLayoutManager(CreditSalesActivity.this);
        mCreditRecyclerView.setLayoutManager(mCreditLayoutManager);
        mCreditAdapter = new CreditAdapter(getDataSetCredit(), CreditSalesActivity.this);
        mCreditRecyclerView.setAdapter(mCreditAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryIds();

        //----------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Credit sales");
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        empty = findViewById(R.id.empty);
        icon_empty = findViewById(R.id.icon_empty);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(5).setChecked(true);

        // Check if there are sales and show a text
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If cashPurchase child exist
                if (snapshot.hasChild("creditPurchase")) {
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
            return true;
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

                if (map.get("name") != null) {
                    mName = map.get("name").toString();
                    textName.setText(mName);
                }

                if (map.get("email") != null) {
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

    private void getUserHistoryIds() {
        DatabaseReference userCreditDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("creditPurchase");
        userCreditDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot credit : dataSnapshot.getChildren()) {
                        FetchSaleInformation(credit.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchSaleInformation(String saleKey) {
        DatabaseReference creditDatabase = FirebaseDatabase.getInstance().getReference().child("creditPurchase").child(saleKey);
        creditDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String saleId = dataSnapshot.getKey();

                    String customerName = "";
                    String customerAddress = "";
                    String customerCity = "";
                    String customerPhone = "";
                    String customerEmail = "";
                    String seller = "";
                    String productName = "";
                    String productCost = "";
                    String customerImage = "";
                    String productImage = "";
                    String productDescription = "";
                    String saleDate = "";

                    String debt = "";
                    String collectWhen = "";
                    String collectDay = "";
                    String firstHour = "";
                    String secondHour = "";
                    String amountMoney = "";
                    String numberPayments = "";
                    /*
                    for (DataSnapshot child : dataSnapshot.getChildren()) {
                        if (child.getKey().equals("timestamp")) {
                            timestamp = Long.valueOf(child.getValue().toString());
                        }
                    }
                    */

                    if (dataSnapshot.child("customerName").getValue() != null) {
                        customerName = dataSnapshot.child("customerName").getValue().toString();
                    }

                    if (dataSnapshot.child("customerCity").getValue() != null) {
                        customerCity = dataSnapshot.child("customerCity").getValue().toString();
                    }

                    if (dataSnapshot.child("customerAddress").getValue() != null) {
                        customerAddress = dataSnapshot.child("customerAddress").getValue().toString();
                    }

                    if (dataSnapshot.child("customerPhone").getValue() != null) {
                        customerPhone = dataSnapshot.child("customerPhone").getValue().toString();
                    }

                    if (dataSnapshot.child("customerEmail").getValue() != null) {
                        customerEmail = dataSnapshot.child("customerEmail").getValue().toString();
                    }

                    if (dataSnapshot.child("sellerName").getValue() != null) {
                        seller = dataSnapshot.child("sellerName").getValue().toString();
                    }

                    if (dataSnapshot.child("product").getValue() != null) {
                        productName = dataSnapshot.child("product").getValue().toString();
                    }

                    if (dataSnapshot.child("price").getValue() != null) {
                        productCost = dataSnapshot.child("price").getValue().toString();
                    }

                    if (dataSnapshot.child("customerImage").getValue() != null) {
                        customerImage = dataSnapshot.child("customerImage").getValue().toString();
                    }

                    if (dataSnapshot.child("imageProduct").getValue() != null) {
                        productImage = dataSnapshot.child("imageProduct").getValue().toString();
                    }

                    if (dataSnapshot.child("saleDate").getValue() != null) {
                        saleDate = dataSnapshot.child("saleDate").getValue().toString();
                    }

                    if (dataSnapshot.child("productDescription").getValue() != null) {
                        productDescription = dataSnapshot.child("productDescription").getValue().toString();
                    }

                    //--------------------

                    if (dataSnapshot.child("debt").getValue() != null) {
                        debt = dataSnapshot.child("debt").getValue().toString();
                    }

                    if (dataSnapshot.child("collectWhen").getValue() != null) {
                        collectWhen = dataSnapshot.child("collectWhen").getValue().toString();
                    }

                    if (dataSnapshot.child("collectDay").getValue() != null) {
                        collectDay = dataSnapshot.child("collectDay").getValue().toString();
                    }

                    if (dataSnapshot.child("firstHour").getValue() != null) {
                        firstHour = dataSnapshot.child("firstHour").getValue().toString();
                    }

                    if (dataSnapshot.child("secondHour").getValue() != null) {
                        secondHour = dataSnapshot.child("secondHour").getValue().toString();
                    }

                    if (dataSnapshot.child("amountMoney").getValue() != null) {
                        amountMoney = dataSnapshot.child("amountMoney").getValue().toString();
                    }

                    if (dataSnapshot.child("numberPayments").getValue() != null) {
                        numberPayments = dataSnapshot.child("numberPayments").getValue().toString();
                    }

                    CreditObject obj = new CreditObject(saleId, customerName, customerAddress, customerCity, customerPhone, customerEmail, seller, productName, productCost, customerImage, productImage, productDescription, saleDate, debt, collectWhen, collectDay, firstHour, secondHour, amountMoney, numberPayments);
                    resultsCredit.add(obj);
                    mCreditAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList resultsCredit = new ArrayList<CreditObject>();

    private ArrayList<CreditObject> getDataSetCredit() {
        return resultsCredit;
    }
}
