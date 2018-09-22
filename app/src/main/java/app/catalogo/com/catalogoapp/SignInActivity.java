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

import dmax.dialog.SpotsDialog;

public class SignInActivity extends AppCompatActivity {
    RelativeLayout rootLayout;
    private EditText edtEmail, edtPassword;
    private Button btnSignIn, btnBack;

    FirebaseAuth auth;
    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        rootLayout = findViewById(R.id.rootLayout);

        edtEmail = findViewById(R.id.email);
        edtPassword = findViewById(R.id.password);

        btnSignIn = findViewById(R.id.signIn);
        btnBack = findViewById(R.id.back);

        // Init firebase
        auth = FirebaseAuth.getInstance();
        db = FirebaseDatabase.getInstance();
        users = db.getReference("Users");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hide the keyboard when the button is pressed
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                assert inputMethodManager != null;
                inputMethodManager.hideSoftInputFromWindow(edtPassword.getWindowToken(), 0);

                logIn();
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(SignInActivity.this, MainActivity.class);
                startActivity(back);
                finish();
                return;
            }
        });
    }

    private void logIn() {
        boolean email = true, password = true;

        // Validations
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
        } else {
            password = true;
        }

        final SpotsDialog waitingDialog = new SpotsDialog(SignInActivity.this);

        if (email && password) {
            // Waiting dialog
            waitingDialog.show();

            // Login
            auth.signInWithEmailAndPassword(edtEmail.getText().toString(), edtPassword.getText().toString())
                    .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            waitingDialog.dismiss();
                            Intent intent = new Intent(SignInActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                            return;
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    waitingDialog.dismiss();
                    Snackbar.make(rootLayout, "Failed " + e.getMessage(), Snackbar.LENGTH_LONG)
                            .show();
                }
            });
        }
    }
}
