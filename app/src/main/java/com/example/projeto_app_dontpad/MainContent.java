package com.example.projeto_app_dontpad;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.ViewCompat;

import com.bumptech.glide.Glide;
import com.firebase.ui.storage.images.FirebaseImageLoader;
import com.google.android.gms.common.internal.Constants;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;

import static android.widget.Toast.makeText;

public class MainContent extends AppCompatActivity {

    private static final int RESULT_LOAD_IMG = 1;
    private static final int GET_FROM_GALLERY = 1;
    private static final int GALLERY_REQUEST = 1;
    private Button textButton;
    private EditText editText2;
    private ImageView ivDisplayMain;
    private FirebaseAuth mAuth;
    private static final int REQ_CODE_CAMERA = 1001;
    GridView gridView;

    private ClipboardManager myClipboard;
    private ClipData myClip;

 Bitmap [] photos = new Bitmap[12];



   //int [] photos = {R.drawable.palio,R.drawable.palio2};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setLogo(R.drawable.icon_main);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        setContentView(R.layout.content_main);
        ivDisplayMain = findViewById(R.id.ivDisplayMain);
        mAuth = FirebaseAuth.getInstance();
        editText2 = findViewById(R.id.editText2);





        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {

            /* perform your actions here*/


        } else {
            signInAsAnonymous();
        }

        gridView = findViewById(R.id.gridImagesId);
        ViewCompat.setNestedScrollingEnabled(gridView, true);

        Adapter adapter = new Adapter(getApplicationContext(), photos);
        gridView.setAdapter(adapter);
        adapter.notifyDataSetChanged();




        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(photos[position]!=null){

                Intent i = new Intent(getApplicationContext(), MainContentFull.class);
                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                photos[position].compress(Bitmap.CompressFormat.JPEG, 5, bs);
                i.putExtra("byteArray", bs.toByteArray());
                startActivity(i);

               /*Intent fullImage = new Intent(getApplicationContext(), MainContentFull.class);
                fullImage.putExtra("imageId", photos[position]);
                startActivity(fullImage);*/
                }else{
                    Toast.makeText(getApplicationContext(), "Nenhuma foto armazenada!", Toast.LENGTH_LONG).show();
                }

            }
        });

        //botão camera


       /* camButton = findViewById(R.id.camButton);
        camButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (camera.resolveActivity(getPackageManager())!=null) {
                    startActivityForResult(camera, REQ_CODE_CAMERA);
                }
            }
        });  */


        //botão texto

        textButton = findViewById(R.id.textButton);
        textButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent imageScreen = new Intent(getBaseContext(), MainActivity.class);
                startActivity(imageScreen);
            }
        });

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

    public void takePhoto(View view) {

        if(editText2.getText().toString().trim().length()>0) {

            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, REQ_CODE_CAMERA);
            } else {
                makeText(
                        this,
                        getString(R.string.takePhoto),
                        Toast.LENGTH_SHORT
                ).show();
            }
        }else{
            Toast.makeText(getApplicationContext(), "Digite uma TAG!", Toast.LENGTH_SHORT).show();
        }

    }

   public void takeDownload(View view) {

       /* editText2 = findViewById(R.id.editText2);
        ivDisplayMain = findViewById(R.id.ivDisplayMain);

        StorageReference mImageRef =
                FirebaseStorage.getInstance().getReference().child("");

        Glide.with(MainContent.this)
                .load(mImageRef)
                .into(ivDisplayMain);
        */

       if(editText2.getText().toString().trim().length()>0) {

           editText2 = findViewById(R.id.editText2);
           ivDisplayMain = findViewById(R.id.ivDisplayMain);

           StorageReference mImageRef =
                   FirebaseStorage.getInstance().getReference(
                           String.format(
                                   Locale.getDefault(),
                                   "images/%s",
                                   editText2.getText().toString().replace("@", "")
                           ));

           final long ONE_MEGABYTE = 1024 * 1024;
           mImageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
               @Override
               public void onSuccess(byte[] bytes) {
                   Bitmap bm = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                   DisplayMetrics dm = new DisplayMetrics();
                   getWindowManager().getDefaultDisplay().getMetrics(dm);

                   ivDisplayMain.setMinimumHeight(dm.heightPixels);
                   ivDisplayMain.setMinimumWidth(dm.widthPixels);
                   ivDisplayMain.setImageBitmap(bm);
                   for (int i = 0; i < photos.length; i++) {
                       if (photos[i] == null) {
                           photos[i] = bm;
                           break;
                       }


                   }
               }
           }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception exception) {
                   // Handle any errors
               }
           });

       }else{
           Toast.makeText(getApplicationContext(), "Digite uma TAG!", Toast.LENGTH_SHORT).show();
       }














    }

    public void takeGallery(View view){

        if(editText2.getText().toString().trim().length()>0) {

            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_REQUEST);

        }else{
            Toast.makeText(getApplicationContext(), "Digite uma TAG!", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPicture(Bitmap picture) {
        Random gerador = new Random();
        int aleatorio = (gerador.nextInt(100)+1);
        StorageReference pictureStorageReference = FirebaseStorage.getInstance().getReference(
                String.format(
                        Locale.getDefault(),
                        "images/%s/"+editText2.getText().toString()+aleatorio+".jpg",
                        editText2.getText().toString().replace("@", "")
                ));


        ByteArrayOutputStream convert = new ByteArrayOutputStream();
        picture.compress(Bitmap.CompressFormat.JPEG, 5, convert);
        byte[] bytes = convert.toByteArray();
        pictureStorageReference.putBytes(bytes);


       Toast.makeText(getApplicationContext(), "A foto foi enviada!", Toast.LENGTH_SHORT).show();

        myClipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
        String text;
        text = editText2.getText().toString()+"/"+editText2.getText().toString()+aleatorio+".jpg";

        myClip = ClipData.newPlainText("text", text);
        myClipboard.setPrimaryClip(myClip);

        Toast.makeText(getApplicationContext(), "A TAG foi copiada para a área de transferência.",Toast.LENGTH_SHORT).show();


    }

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data) {

        ivDisplayMain = findViewById(R.id.ivDisplayMain);

        if (requestCode == REQ_CODE_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    Bitmap photo = (Bitmap)
                            data.getExtras().get("data");

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    photo.compress(Bitmap.CompressFormat.PNG, 5, baos);


                    uploadPicture(photo);
                    for (int i=0; i<photos.length; i++) {
                        if (photos[i] == null) {
                            photos[i] = photo;
                            break;
                        }

                    }
                } else {
                    Toast.makeText(this,
                            getString(R.string.noPhoto),
                            Toast.LENGTH_SHORT
                    ).show();
                }
            }
        }


        if (requestCode == GET_FROM_GALLERY) {
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    switch (requestCode){
                        case GALLERY_REQUEST:
                            Uri selectedImage = data.getData();
                            try {
                                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImage);

                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 5, baos);
                                uploadPicture(bitmap);

                                    for (int i=0; i<photos.length; i++) {
                                    if (photos[i] == null) {
                                        photos[i] = bitmap;
                                        break;
                                    }

                                }
                            } catch (IOException e) {
                                Log.i("TAG", "Erro ao carregar foto! " + e);
                            }
                            break;
                    }

                } else {

                }
            } else {
                Toast.makeText(getApplicationContext(), "Você não carregou uma foto!", Toast.LENGTH_LONG).show();
            }


        }super.onActivityResult(requestCode, resultCode, data);

    }


}

