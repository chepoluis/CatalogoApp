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
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class CreditSingleActivity extends AppCompatActivity
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

    TextView debt;
    TextView collectWhen;
    TextView collectDay;
    TextView firstHour;
    TextView secondHour;
    TextView amountMoney;
    TextView initialPay;
    TextView numberPayments;

    Button abonar;
    private String m_Abonar;

    String customerNameIntent, customerCityIntent, customerAddressIntent,
            customerPhoneIntent, customerEmailIntent, sellerIntent, productIntent, productPriceIntent,
            saleDateIntent, imageCustomerIntent, productImageIntent, productDescriptionIntent,
            debtIntent, collectWhenIntent, collectDayIntent, firstHourIntent, secondHourIntent,
            amountMoneyIntent, initialPayIntent, numberPaymentsIntent;

    FirebaseDatabase db;
    DatabaseReference sale;
    String sale_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_single);

        customerImage = findViewById(R.id.customerImageSaleCredit);
        productImage = findViewById(R.id.productImageSaleCredit);

        customerName = findViewById(R.id.customerSaleCredit);
        customerCity = findViewById(R.id.customerCitySaleCredit);
        customerAddress = findViewById(R.id.customerAddressSaleCredit);
        customerPhone = findViewById(R.id.customerPhoneSaleCredit);
        customerEmail = findViewById(R.id.customerEmailSaleCredit);
        seller = findViewById(R.id.sellerSaleCredit);
        product = findViewById(R.id.productSaleCredit);
        productDescription = findViewById(R.id.productDescriptionSaleCredit);
        productPrice = findViewById(R.id.productPriceSaleCredit);
        saleDate = findViewById(R.id.dateSaleCredit);

        debt = findViewById(R.id.debtCredit);
        collectWhen = findViewById(R.id.collectWhenCredit);
        collectDay = findViewById(R.id.collectDayCredit);
        firstHour = findViewById(R.id.firstHourCredit);
        secondHour = findViewById(R.id.secondHourCredit);
        amountMoney = findViewById(R.id.amountMoneyCredit);
        initialPay = findViewById(R.id.initialPayCredit);
        numberPayments = findViewById(R.id.numberPaymentsCredit);

        abonar = findViewById(R.id.abonar);

        abonar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount = amountMoneyIntent;
                String[] s = amount.split(" ");
                // Get the amount of abono  FIX!
                final int abonoInt = Integer.valueOf(s[2]);

                AlertDialog.Builder abono = new AlertDialog.Builder(CreditSingleActivity.this);
                abono.setTitle("Abono");

                // Set up the input
                final EditText input = new EditText(CreditSingleActivity.this);
                // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
                input.setInputType(InputType.TYPE_CLASS_NUMBER);
                abono.setView(input);
                input.setText(String.valueOf(abonoInt));

                // Set up the buttons
                abono.setPositiveButton("Abonar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        m_Abonar = input.getText().toString();
                        abonar(Integer.valueOf(m_Abonar));
                        Toast.makeText(CreditSingleActivity.this, m_Abonar, Toast.LENGTH_SHORT).show();
                    }
                });
                abono.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                abono.show();
            }
        });

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

        debtIntent = getIntent().getExtras().getString("debt");
        collectWhenIntent = getIntent().getExtras().getString("collectWhen");
        collectDayIntent = getIntent().getExtras().getString("collectDay");
        firstHourIntent = getIntent().getExtras().getString("firstHour");
        secondHourIntent = getIntent().getExtras().getString("secondHour");
        amountMoneyIntent = getIntent().getExtras().getString("amountMoney");
        initialPayIntent = getIntent().getExtras().getString("initialPay");
        numberPaymentsIntent = getIntent().getExtras().getString("numberPayments");

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

        debt.setText(debtIntent);
        Toast.makeText(this, debtIntent, Toast.LENGTH_SHORT).show();
        if(debtIntent.equals("Product paid"))
        {
            debt.setText("Product paid");
            abonar.setVisibility(View.GONE);
        }

        collectWhen.setText(collectWhenIntent);
        collectDay.setText(collectDayIntent);
        firstHour.setText(firstHourIntent);
        secondHour.setText(secondHourIntent);
        amountMoney.setText(amountMoneyIntent);
        initialPay.setText(initialPayIntent);
        numberPayments.setText(numberPaymentsIntent);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mUserDatabase = FirebaseDatabase.getInstance().getReference().child("Users").child(userID);

        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        // Get user name and it's set in the navigation view
        getUserName();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Credit sale");
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
        sale = FirebaseDatabase.getInstance().getReference().child("creditPurchase");
    }

    private void abonar(Integer cantidadAbono) {
        //DatabaseReference customerRef = FirebaseDatabase.getInstance().getReference().child("Customers").child(customerKey).child("cashPurchase");
        DatabaseReference creditSale = FirebaseDatabase.getInstance().getReference().child("creditPurchase");
        sale_key = getIntent().getExtras().getString("saleId");

        // Split the string and obtain the cost
        String debtS = debtIntent;
        String[] s = debtS.split(" ");
        int debtI = Integer.parseInt(s[2]);

        final int debtResult = debtI - cantidadAbono;
        creditSale.child(sale_key)
                .child("debt")
                .setValue(debtResult)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(CreditSingleActivity.this, "Abono realizado: $ " + String.valueOf(debtResult) + " MXN", Toast.LENGTH_LONG)
                                .show();
                        if(debtResult == 0)
                        {
                            debt.setText("Product paid");
                            abonar.setVisibility(View.GONE);
                        }
                        else
                        {
                            debt.setText("Debt: $ " + String.valueOf(debtResult) + " MXN");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreditSingleActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_LONG)
                                .show();
                    }
                });
    }

    /*private void refreshActivity() {
        Intent refresh = new Intent(this, CreditSingleActivity.class);
        startActivity(refresh);//Start the same Activity
        finish(); //finish Activity.
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.credit_sales_call, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_call) {
            callCustomer();
        }

        return super.onOptionsItemSelected(item);
    }

    private void callCustomer() {
        String string = customerPhoneIntent;
        String[] parts = string.split(" ");
        String phoneNumber = parts[1];

        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        if(ActivityCompat.checkSelfPermission(CreditSingleActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(CreditSingleActivity.this, "Please, activate the permissions", Toast.LENGTH_LONG).show();
            return;
        }else{
            startActivity(intent);
        }
        toastMessage("Calling " + customerNameIntent);
    }

    public void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
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
}
