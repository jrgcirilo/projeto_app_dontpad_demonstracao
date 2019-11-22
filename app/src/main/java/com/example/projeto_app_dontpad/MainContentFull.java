package com.example.projeto_app_dontpad;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;

public class MainContentFull extends AppCompatActivity {

    private Button textButton;
    private Button photoButton;
    private ImageView backButton;
    private ImageView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_main);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.content_main_full);



        if(getIntent().hasExtra("byteArray")) {
            fullImage = findViewById(R.id.fullImageId);
            Bitmap b = BitmapFactory.decodeByteArray(
                    getIntent().getByteArrayExtra("byteArray"),0,getIntent()
                            .getByteArrayExtra("byteArray").length);
            fullImage.setImageBitmap(b);

        }

       /* int imageId = getIntent().getIntExtra("imageId", R.drawable.ic_launcher_background);
        fullImage.setImageResource(imageId);*/

        //botão voltar
        backButton = findViewById(R.id.backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //botão texto
        textButton = findViewById(R.id.textButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageScreen = new Intent(getBaseContext(), MainActivity.class);
                startActivity(imageScreen);
            }
        });

        //botão fotos
        photoButton = findViewById(R.id.photoButton);
        photoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent imageScreen = new Intent(getBaseContext(), MainContent.class);
                startActivity(imageScreen);
            }
        });
    }
}
