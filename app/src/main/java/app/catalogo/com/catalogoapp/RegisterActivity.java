package app.catalogo.com.catalogoapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import app.catalogo.com.catalogoapp.Model.User;

public class RegisterActivity extends AppCompatActivity {
    RelativeLayout rootLayout;
    private EditText edtName, edtEmail, edtPassword;
    private Button btnRegister, btnBack;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        rootLayout = findViewById(R.id.rootLayout);

        edtName = findViewById(R.id.name);
        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        btnRegister = findViewById(R.id.register);
        btnBack = findViewById(R.id.back);

        // Init firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard when the button is pressed
                InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);

                registerUser();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(RegisterActivity.this, MainActivity.class);
                startActivity(back);
                finish();
                return;
            }
        });
    }

    private void registerUser() {
        boolean name = true, email = true, password = true, passwordLong = true;

        // Validations
        if (TextUtils.isEmpty(edtName.getText().toString())) {
            Toast.makeText(this, "Please enter name", Toast.LENGTH_SHORT)
                    .show();
            name = false;
        } else {
            name = true;
        }

        if (TextUtils.isEmpty(edtEmail.getText().toString())) {
            Toast.makeText(this, "Please enter email address", Toast.LENGTH_SHORT)
                    .show();
            email = false;
        } else {
            email = true;
        }

        if (TextUtils.isEmpty(edtPassword.getText().toString())) {
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT)
                    .show();
            password = false;
        } else if (edtPassword.getText().toString().length() < 6) {
            Toast.makeText(this, "Password too short, use 6 characters", Toast.LENGTH_SHORT)
                    .show();
            passwordLong = false;
        } else {
            password = true;
            passwordLong = true;
        }

        if (name && email && password && passwordLong) {
            auth.createUserWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            // Save user to db
                            User user = new User();
                            user.setName(edtName.getText().toString());
                            user.setEmail(edtEmail.getText().toString());
                            user.setPassword(edtPassword.getText().toString());

                            users.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(RegisterActivity.this, "Successful registration!", Toast.LENGTH_LONG)
                                                    .show();
                                            Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                            return;
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG)
                                                    .show();
                                        }
                                    });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG)
                                    .show();
                        }
                    });
        }
    }
}
