package com.example.projeto_app_dontpad;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    //private Button textButton;
    private EditText editText;
    private EditText tagText;
    private ImageView sendTextButton;
    private ImageView ivDisplayMain;
    private Button photoButton;
    private FirebaseAuth mAuth;
    private Button button;
    private CollectionReference colMessagesReference;

    private ClipboardManager myClipboard;
    private ClipData myClip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_main);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.editText);
        tagText = findViewById(R.id.tagText);
        sendTextButton = findViewById(R.id.sendTextButton);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            /* perform your actions here*/


        } else {
            signInAsAnonymous();
        }


        photoButton = findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageScreen = new Intent(getBaseContext(),MainContent.class);
                startActivity(imageScreen);
            }
        });


        mAuth.signOut();

    }





    private void signInAsAnonymous() {

        mAuth.signInAnonymously().addOnSuccessListener(this, new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                /* perform your actions here*/

            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e("MainActivity", "signFailed****** ", exception);
                    }
                });
    }


    public void buttonClicked(View view){
        if(editText.getText().toString().trim().length()>0) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference myRef = database.getReference(String.format(
                    Locale.getDefault(),
                    "%s",
                    editText.getText().toString().replace("@", "")
            ));


            EditText tagText = findViewById(R.id.tagText);
            String message = tagText.getText().toString();
            myRef.setValue(message);
            Toast.makeText(getApplicationContext(), "O texto foi enviado!", Toast.LENGTH_SHORT).show();

            myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
            String text;
            text = message;

            myClip = ClipData.newPlainText("text", text);
            myClipboard.setPrimaryClip(myClip);

            Toast.makeText(getApplicationContext(), "A TAG foi copiada para a área de transferência.",Toast.LENGTH_SHORT).show();


        }else{
            Toast.makeText(getApplicationContext(), "Digite uma TAG!", Toast.LENGTH_SHORT).show();
        }
    }

    public void buttonClickedSearch(View view){

        if(editText.getText().toString().trim().length()>0) {

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            final DatabaseReference myRef = database.getReference(String.format(
                    Locale.getDefault(),
                    "%s",
                    editText.getText().toString().replace("@", "")
            ));

            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    EditText mName = findViewById(R.id.tagText);

                    mName.setText(dataSnapshot.getValue(String.class));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });




        }else{



            Toast.makeText(getApplicationContext(), "Digite uma TAG!", Toast.LENGTH_SHORT).show();


        }

        }

}