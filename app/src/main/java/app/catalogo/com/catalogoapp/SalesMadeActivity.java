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
import android.view.LayoutInflater;
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

import java.util.ArrayList;
import java.util.Map;

import app.catalogo.com.catalogoapp.Model.Product;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashActivity;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashAdapter;
import app.catalogo.com.catalogoapp.historyCashPurchase.HistoryCashObject;

public class SalesMadeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    TextView empty;
    ImageView icon_empty;

    private RecyclerView mSaleRV;
    private DatabaseReference mDatabase;
    private FirebaseRecyclerAdapter<HistoryCashObject, HistoryCashActivity.SalesViewHolder> mSaleRVAdapter;

    //----
    private String customerOrDriver, userId;

    private RecyclerView mHistoryRecyclerView;
    private RecyclerView.Adapter mHistoryAdapter;
    private RecyclerView.LayoutManager mHistoryLayoutManager;

    //---

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sales_made);

        //----------

        mHistoryRecyclerView = (RecyclerView) findViewById(R.id.historyRecyclerView);
        // setNestedScrollingEnabled: make the scroll more natural
        mHistoryRecyclerView.setNestedScrollingEnabled(false);
        mHistoryRecyclerView.setHasFixedSize(true);
        mHistoryLayoutManager = new LinearLayoutManager(SalesMadeActivity.this);
        mHistoryRecyclerView.setLayoutManager(mHistoryLayoutManager);
        mHistoryAdapter = new HistoryCashAdapter(getDataSetHistory(), SalesMadeActivity.this);
        mHistoryRecyclerView.setAdapter(mHistoryAdapter);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        getUserHistoryIds();

        //----------

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Sales made");
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
        navigationView.getMenu().getItem(4).setChecked(true);

        // Check if there are sales and show a text
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If cashPurchase child exist
                if (snapshot.hasChild("cashPurchase")) {
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

        /*//"Sales" here will reflect what you have called your database in Firebase.
        mDatabase = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
        mDatabase.keepSynced(true);

        // Check if there are sales and show a text
        final DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);
        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // If Sales child exist
                if (snapshot.hasChild("cashPurchase")) {
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

        mSaleRV = (RecyclerView) findViewById(R.id.myRecycleViewCash);

        DatabaseReference personsRef = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
        Query personsQuery = personsRef.orderByKey();

        mSaleRV.hasFixedSize();
        mSaleRV.setLayoutManager(new LinearLayoutManager(this));

        FirebaseRecyclerOptions salesOptions = new FirebaseRecyclerOptions.Builder<HistoryCashObject>()
                .setQuery(personsQuery, HistoryCashObject.class).build();

        mSaleRVAdapter = new FirebaseRecyclerAdapter<HistoryCashObject, HistoryCashActivity.SalesViewHolder>(salesOptions) {
            @Override
            protected void onBindViewHolder(@NonNull HistoryCashActivity.SalesViewHolder holder, int position, @NonNull HistoryCashObject sale) {
                holder.setCustomerName(sale.getCustomerName());
                holder.setProductName(sale.getProductPurchased());
                holder.setProductPrice("$ " + sale.getProductCost() + " MXN");
                holder.setCustomerImage(getBaseContext(), sale.getCustomerImage());

                holder.mViewCash.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(SalesMadeActivity.this, "Works!", Toast.LENGTH_SHORT).show();
                        *//*Intent intent = new Intent(HomeActivity.this, BuyProductActivity.class);
                        intent.putExtra("productKey", product.getProductKey());
                        intent.putExtra("productName", product.getName());
                        intent.putExtra("productDescription", product.getDescription());
                        intent.putExtra("productPrice", product.getPrice());
                        intent.putExtra("productAmount", product.getAmount());
                        intent.putExtra("productImage", product.getCustomerImage());
                        startActivity(intent);*//*
                    }
                });
            }

            @Override
            public HistoryCashActivity.SalesViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.view_sales_cash, parent, false);

                return new HistoryCashActivity.SalesViewHolder(view);
            }
        };

        mSaleRV.setAdapter(mSaleRVAdapter);*/
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
        } else if (id == R.id.nav_sales_made) {
            return true;
        } else if (id == R.id.nav_purchases_by_user) {

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

   /* @Override
    public void onStart() {
        super.onStart();
        mSaleRVAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mSaleRVAdapter.stopListening();
    }*/

    //---------
    private void getUserHistoryIds() {
        DatabaseReference userHistoryDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("cashPurchase");
        userHistoryDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot history : dataSnapshot.getChildren()) {
                        FetchSaleInformation(history.getKey());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void FetchSaleInformation(String saleKey) {
        DatabaseReference historyDatabase = FirebaseDatabase.getInstance().getReference().child("cashPurchase").child(saleKey);
        historyDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String saleId = dataSnapshot.getKey();

                    String customerName = "";
                    String customerAddress = "";
                    String customerCity = "";
                    String customerPhone = "";
                    String seller = "";
                    String productName = "";
                    String productCost = "";
                    String customerImage = "";
                    String productImage = "";
                    String productDescription = "";
                    String saleDate = "";

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

                    HistoryCashObject obj = new HistoryCashObject(saleId, customerName, customerAddress, customerCity, customerPhone, seller, productName, productCost, customerImage, productImage, productDescription, saleDate);
                    resultsHistory.add(obj);
                    mHistoryAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private ArrayList resultsHistory = new ArrayList<HistoryCashObject>();

    private ArrayList<HistoryCashObject> getDataSetHistory() {
        return resultsHistory;
    }
}
