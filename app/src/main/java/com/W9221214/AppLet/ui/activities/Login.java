package com.W9221214.AppLet.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.W9221214.AppLet.R;

import java.util.Objects;

import static com.W9221214.AppLet.utils.Utils.isEmpty;

public class Login extends AppCompatActivity {

    private TextView registerTextView;
    private Button logInBtn;
    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private EditText emailEditText;
    private EditText passwordEditText;
    private TextView dontRememberPasswordTextView;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        setViews();
        setListeners();
    }

    private void setViews() {
        registerTextView = findViewById(R.id.activity_log_in_register_text_view);
        logInBtn = findViewById(R.id.activity_log_in_button);
        emailEditText = findViewById(R.id.activity_log_in_email_edit_text);
        passwordEditText = findViewById(R.id.activity_log_in_password_edit_text);
        dontRememberPasswordTextView = findViewById(R.id.forgot_password_text_view);
    }

    private void setListeners() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this
                        , Register.class);
                startActivity(intent);
            }
        });

        logInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = emailEditText.getText().toString();
                String password = passwordEditText.getText().toString();
                logInUser(email, password);
            }
        });
        dontRememberPasswordTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (emailEditText.getText().toString().isEmpty()) {
                    Toast.makeText(Login.this, "Email field is empty"
                            , Toast.LENGTH_SHORT).show();
                } else {
                    displayHint();
                }
            }
        });
    }

    private void logInUser(String email, String password) {
        if(!isEmpty(email) && !isEmpty(password)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(Login.this
                                        , "Authentication failed.", Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }else {
            Toast.makeText(Login.this
                    , "Please Enter Both email And Password.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser currentUser) {
        if (currentUser != null) {
            Intent intent = new Intent(Login.this, Navigation.class);
            startActivity(intent);
        }
    }

    private void displayHint() {
        DocumentReference docRef = db.collection("hints").document(
                emailEditText.getText().toString());
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String hint = (String) Objects.requireNonNull(document.getData())
                                .get("hint");
                        Toast.makeText(Login.this, "Hint: " +
                                hint, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(Login.this,
                                getString(R.string.email_not_found)
                                , Toast.LENGTH_SHORT).show();
                    }
                } else Toast.makeText(Login.this,
                        getString(R.string.no_possible_to_acess_hint)
                        , Toast.LENGTH_SHORT).show();
            }
        });

    }
}
