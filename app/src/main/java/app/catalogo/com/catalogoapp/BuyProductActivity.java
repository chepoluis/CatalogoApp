package app.catalogo.com.catalogoapp;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import app.catalogo.com.catalogoapp.Payments.NumberPayments;
import app.catalogo.com.catalogoapp.Time.TimePickerFragment;

public class BuyProductActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TimePickerDialog.OnTimeSetListener {

    ImageView productImage;
    TextView productName, productPrice, productDescription;
    TextView customerName;
    Button chooseCustomer, btnPay;

    LinearLayout collect;
    LinearLayout dayCollection;
    LinearLayout hourAmid;
    LinearLayout amount;
    LinearLayout payments;

    TextView firstHour, firstMinute;
    String stringFirstHour = "";
    TextView firstPoint;

    TextView secondHour, secondMinute;
    String stringSecondHour = "";
    TextView secondPoint;
    String hour = "";

    EditText amountMoney;
    TextView numberPayments;
    String numberOfPaymentsString;
    String amountOfMoneyString;
    EditText initialPay;
    String initialPayString;

    int amountProduct = 0;
    String nameIntent, descriptionIntent, priceIntent, imageIntent, customerNameIntent,
            productKeyIntent, amountProductIntent;
    String customerKey = "", customerAddress, customerCity, customerPhone,
            customerEmail, customerImage;

    TextView textName, textEmail;
    String mName, mEmail;

    private FirebaseAuth mAuth;
    private DatabaseReference mUserDatabase;
    private String userID;

    RadioGroup radioGroup;
    RadioButton rbCashPayment, rbCreditPayment;
    String paymentMethod = "";

    Spinner collectWhen;
    String collectWhenString;
    Spinner dayCollect;
    String dayCollectString;

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

        collectWhen = findViewById(R.id.spinnerCollect);
        ArrayAdapter<CharSequence> collectWhenAdapter = ArrayAdapter.createFromResource(this, R.array.collect, android.R.layout.simple_spinner_item);
        collectWhenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        collectWhen.setAdapter(collectWhenAdapter);

        collectWhen.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                collectWhenString = parent.getItemAtPosition(position).toString();
                //Toast.makeText(BuyProductActivity.this, collectWhenString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        dayCollect = findViewById(R.id.spinnerDay);
        ArrayAdapter<CharSequence> dayAdapter = ArrayAdapter.createFromResource(this, R.array.day, android.R.layout.simple_spinner_item);
        dayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dayCollect.setAdapter(dayAdapter);
        dayCollect.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                dayCollectString = parent.getItemAtPosition(position).toString();
                //Toast.makeText(BuyProductActivity.this, dayCollectString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnPay = findViewById(R.id.btnPay);

        firstHour = findViewById(R.id.firstHour);
        firstMinute = findViewById(R.id.firstMinute);
        firstPoint = findViewById(R.id.firstDoublePoint);
        secondHour = findViewById(R.id.secondHour);
        secondMinute = findViewById(R.id.secondMinute);
        secondPoint = findViewById(R.id.secondDoublePoint);
        firstHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = "first";
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        secondHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hour = "second";
                android.support.v4.app.DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        amountMoney = findViewById(R.id.amountMoney);
        numberPayments = findViewById(R.id.numberPayments);
        initialPay = findViewById(R.id.initialPay);

        // Get the amount of payments
        amountMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                NumberPayments payments = new NumberPayments();
                amountOfMoneyString = amountMoney.getText().toString();
                numberOfPaymentsString = payments.getNumberPayments(Double.parseDouble(priceIntent), Double.parseDouble(amountMoney.getText().toString()));
                numberPayments.setText(numberOfPaymentsString);
            }
        });

        initialPay.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                initialPayString = initialPay.getText().toString();
                //Toast.makeText(BuyProductActivity.this, initialPayString, Toast.LENGTH_SHORT).show();
            }
        });

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
        customerEmail = getIntent().getExtras().getString("customerEmail");
        customerCity = getIntent().getExtras().getString("customerCity");
        customerImage = getIntent().getExtras().getString("customerImage");
        customerNameIntent = getIntent().getExtras().getString("customerName");
        if (customerNameIntent == null) {
            customerName.setText("Customer: -Choose-");
        } else {
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
            }
        });

        collect = findViewById(R.id.collect);
        dayCollection = findViewById(R.id.dayCollection);
        hourAmid = findViewById(R.id.hourAmid);
        amount = findViewById(R.id.amount);
        payments = findViewById(R.id.payments);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rbCashPayment) {
                    collect.setVisibility(View.GONE);
                    dayCollection.setVisibility(View.GONE);
                    hourAmid.setVisibility(View.GONE);
                    amount.setVisibility(View.GONE);
                    payments.setVisibility(View.GONE);
                } else if (checkedId == R.id.rbCreditPayment) {
                    collect.setVisibility(View.VISIBLE);
                    dayCollection.setVisibility(View.VISIBLE);
                    hourAmid.setVisibility(View.VISIBLE);
                    amount.setVisibility(View.VISIBLE);
                    payments.setVisibility(View.VISIBLE);
                }
            }
        });

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rbCashPayment.isChecked()) {
                    saveCashSale();
                } else if (rbCreditPayment.isChecked()) {
                    saveCreditSale();
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

    private void saveCreditSale() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("creditPurchase");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerKey).child("creditPurchase");
        DatabaseReference creditPurchase = FirebaseDatabase.getInstance().getReference().child("creditPurchase");
        DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("Products");
        String requestId = creditPurchase.push().getKey();
        sellerRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        HashMap map = new HashMap();
        map.put("customerAddress", customerAddress);
        map.put("customerCity", customerCity);
        map.put("customerKey", customerKey);
        map.put("sellerId", userID);
        map.put("customerName", customerNameIntent);
        map.put("sellerName", mName);
        map.put("customerPhone", customerPhone);
        map.put("customerEmail", customerEmail);
        map.put("price", priceIntent);
        map.put("product", nameIntent);
        map.put("productKey", productKeyIntent);
        map.put("imageProduct", imageIntent);
        map.put("customerImage", customerImage);
        map.put("productDescription", descriptionIntent);
        map.put("saleDate", currentDateTimeString);

        map.put("collectWhen", collectWhenString);
        map.put("collectDay", dayCollectString);
        map.put("firstHour", stringFirstHour);
        map.put("secondHour", stringSecondHour);
        map.put("amountMoney", amountOfMoneyString);
        map.put("initialPay", initialPayString);
        map.put("numberPayments", numberOfPaymentsString);

        double initialPayDouble = Double.parseDouble(initialPayString);
        double productCostDouble = Double.parseDouble(priceIntent);
        double debt = (productCostDouble - initialPayDouble);

        map.put("debt", debt);
        creditPurchase.child(requestId).updateChildren(map);

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

    // Save the sale in the database
    private void saveCashSale() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference sellerRef = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("cashPurchase");
        DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerKey).child("cashPurchase");
        DatabaseReference cashPurchase = FirebaseDatabase.getInstance().getReference().child("cashPurchase");
        DatabaseReference products = FirebaseDatabase.getInstance().getReference().child("Products");
        String requestId = cashPurchase.push().getKey();
        sellerRef.child(requestId).setValue(true);
        customerRef.child(requestId).setValue(true);
        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        HashMap map = new HashMap();
        map.put("customerAddress", customerAddress);
        map.put("customerCity", customerCity);
        map.put("customerKey", customerKey);
        map.put("sellerId", userID);
        map.put("customerName", customerNameIntent);
        map.put("sellerName", mName);
        map.put("customerPhone", customerPhone);
        map.put("customerEmail", customerEmail);
        map.put("price", priceIntent);
        map.put("product", nameIntent);
        map.put("productKey", productKeyIntent);
        map.put("imageProduct", imageIntent);
        map.put("customerImage", customerImage);
        map.put("productDescription", descriptionIntent);
        map.put("saleDate", currentDateTimeString);
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

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (hour.equals("first")) {
            stringFirstHour = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            firstHour.setText(String.valueOf(hourOfDay));
            firstPoint.setText(":");
            firstMinute.setText(String.valueOf(minute));
            Toast.makeText(this, stringFirstHour, Toast.LENGTH_SHORT).show();
        } else if (hour.equals("second")) {
            stringSecondHour = String.valueOf(hourOfDay) + ":" + String.valueOf(minute);
            secondHour.setText(String.valueOf(hourOfDay));
            secondPoint.setText(":");
            secondMinute.setText(String.valueOf(minute));
            Toast.makeText(this, stringSecondHour, Toast.LENGTH_SHORT).show();
        }
    }
}
