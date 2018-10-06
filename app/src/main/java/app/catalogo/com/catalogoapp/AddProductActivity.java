package app.catalogo.com.catalogoapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.catalogo.com.catalogoapp.Model.Product;
import app.catalogo.com.catalogoapp.Model.User;

public class AddProductActivity extends AppCompatActivity {
    EditText productName;
    EditText productDescription;
    EditText productPrice;
    EditText productAmount;
    EditText productImageURL;

    Button addProduct;

    FirebaseDatabase db;
    DatabaseReference products;
    DatabaseReference keyP;
    DatabaseReference keyProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        productName = findViewById(R.id.productName);
        productDescription = findViewById(R.id.productDescription);
        productPrice = findViewById(R.id.productPrice);
        productAmount = findViewById(R.id.productAmount);
        productImageURL = findViewById(R.id.productImage);

        // Init firebase
        db = FirebaseDatabase.getInstance();
        products = db.getReference("Products");
        keyP = products;
        keyProduct = keyP.push();

        addProduct = findViewById(R.id.add);
        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save product to db
                Product product = new Product();
                product.setName(productName.getText().toString());
                product.setDescription(productDescription.getText().toString());
                product.setPrice(productPrice.getText().toString());
                product.setAmount(productAmount.getText().toString());
                product.setImage(productImageURL.getText().toString());

                products.child(keyProduct.getKey())
                        .setValue(product)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(AddProductActivity.this, "Successful registration!", Toast.LENGTH_LONG)
                                        .show();
                                Intent intent = new Intent(AddProductActivity.this, HomeActivity.class);
                                startActivity(intent);
                                finish();
                                return;
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddProductActivity.this, "Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
