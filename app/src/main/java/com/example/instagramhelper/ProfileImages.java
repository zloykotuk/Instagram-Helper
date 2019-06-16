package com.example.instagramhelper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileImages extends AppCompatActivity {

    private Context mContext = ProfileImages.this;
    public final static int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 1034;
    private static final int GALLERY_REQUEST = 1889;
    private final int NUMBER_GRID_COUNT = 3;

    private final String TAG = "ProfileImages";
    private String USERNAME;

    private ArrayList<String> imagesGallary;
    private ArrayList<String> photoInst = new ArrayList<>();

    private GridImageAdapter adapterGridView = null ;
    private SquareImageView prevSelectItem = null;
    private ImageView save;

    private File photoFile = null;
    private File tmpFile;
    private String imageSaveToInst = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        save = findViewById(R.id.saveChange);
        adapterGridView = new GridImageAdapter(mContext, R.layout.layout_image_gridview, "", new ArrayList<String>(), 0);
        tmpFile = new File(mContext.getFilesDir(), "tmp.txt");

        startApp();

        Button addMore = findViewById(R.id.butAddMore);
        addMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageSaveToInst != null){
                    refreshPhotoInst(imageSaveToInst);
                } else {
                    Toast.makeText(mContext, "Виберіть картинку для завантаження", Toast.LENGTH_LONG).show();
                }
            }
        });

        imagesGallary = getAllShownImagesPath(this);
        for (String iteam :
                imagesGallary) {
            Log.i("Image", iteam);
        }
        initRecyclerView(imagesGallary);
        initSpinner();
    }


    private void startApp(){

        final EditText username = findViewById(R.id.editProfileName);
        final TextView profileName = findViewById(R.id.profileName);
        final ImageView apply = findViewById(R.id.applyChange);

        if(tmpFile.exists() && ReadWriteFile.read(mContext, tmpFile.getName()) != ""){

           USERNAME =  ReadWriteFile.read(mContext, tmpFile.getName());
           username.setVisibility(View.INVISIBLE);
           apply.setVisibility(View.INVISIBLE);
           profileName.setText(USERNAME);
           profileName.setVisibility(View.VISIBLE);
           save.setVisibility(View.VISIBLE);
           tmpImageList();
        }

        apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                USERNAME = username.getText().toString();
                username.setVisibility(View.INVISIBLE);
                apply.setVisibility(View.INVISIBLE);
                profileName.setText(USERNAME);
                profileName.setVisibility(View.VISIBLE);
                save.setVisibility(View.VISIBLE);
                //
                if(!tmpFile.exists()){
                    try {
                        tmpFile.createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }else {
                    ReadWriteFile.write(mContext, tmpFile.getName(), USERNAME);
                    Log.i(TAG, tmpFile.getAbsolutePath());
                    tmpImageList();
                }
            }
        });

        profileName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoInst.clear();
                adapterGridView.notifyDataSetChanged();
                USERNAME = "";
                profileName.setVisibility(View.INVISIBLE);
                save.setVisibility(View.INVISIBLE);
                username.setVisibility(View.VISIBLE);
                apply.setVisibility(View.VISIBLE);
                username.setText("");
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(imageSaveToInst != null){
//                    Uri uri_u = Uri.parse("http://instagram.com/_u/" + "zloykotuk");
//                    Intent share = new Intent(Intent.ACTION_SEND, uri_u);
                    Intent share = new Intent(Intent.ACTION_SEND);
                    share.setType("image/*");
                    share.setPackage("com.instagram.android");
                    File media = new File(imageSaveToInst);
                    Uri uri = Uri.fromFile(media);
                    share.putExtra(Intent.EXTRA_STREAM, uri);
                    startActivity(share);
                } else {
                    Toast.makeText(mContext, "Виберіть картинку для завантаження", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void tmpImageList(){

        NetworkService.getInstance()
                .getJSONApi()
                .getPostWithID(USERNAME, "")
                .enqueue(new Callback<Post>() {
                    @Override
                    public void onResponse(Call<Post> call, Response<Post> response) {
                        Post post = response.body();
                        if(response.code() == 200) {
                            if (post.getGraphql().getUser().getEdge_owner_to_timeline_media().getEdges().size() > 0) {
                                for (Edge iteam : post.getGraphql().getUser().getEdge_owner_to_timeline_media().getEdges()
                                ) {
                                    photoInst.add(iteam.getNode().getDisplay_url());
                                    //Log.i(TAG, iteam.getNode().getDisplay_url());
                                }
                            } else {
                                Toast.makeText(mContext, "Записів поки немає або акаунт закритий", Toast.LENGTH_SHORT).show();
                            }
                            setupImageGrid(photoInst);
                        }
                    }

                    @Override
                    public void onFailure(Call<Post> call, Throwable t) {
                        Toast.makeText(mContext, "Неможливо зєднатися з сервером", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, t.getMessage());
                    }
                });
    }

    private void initSpinner(){
        Spinner spinner = findViewById(R.id.spinner);
        String[] data = {"Recycle Image", "Storage Image", "Camera"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,R.layout.spinner_item, R.id.spinner_custom_item, data);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position){
                    case 0:
                        Log.i("Spinner selected", String.valueOf(position));
                        break;
                    case 1:
                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                        photoPickerIntent.setType("image/*");
                        startActivityForResult(photoPickerIntent, GALLERY_REQUEST);
                        Log.i("Spinner selected", String.valueOf(position));
                        break;
                    case 2:
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        photoFile = getPhotoFileUri("my_images.jpg");
                        Uri fileProvider = FileProvider.getUriForFile(mContext, "com.example.instagramhelper.provider", photoFile);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
                        }
                        Log.i("Spinner selected", String.valueOf(position));
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setupImageGrid(ArrayList<String> imgList){
        GridView gridView = findViewById(R.id.gridView);

        int gridWidht = getResources().getDisplayMetrics().widthPixels;
        int imageWidht = gridWidht/NUMBER_GRID_COUNT;
        gridView.setColumnWidth(imageWidht);

        adapterGridView = new GridImageAdapter(mContext, R.layout.layout_image_gridview, "", imgList, imageWidht);
        adapterGridView.notifyDataSetChanged();
        //gridView.invalidateViews();
        gridView.setAdapter(adapterGridView);

    }

    private void initRecyclerView(final ArrayList<String> imgList){
        LinearLayoutManager layoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(layoutManager);
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(mContext, imgList, new RecyclerViewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(String mImages, SquareImageView images) {
                imageSaveToInst = mImages;
                if(prevSelectItem != null){
                    prevSelectItem.setBackgroundResource(0);
                }
                prevSelectItem = images;
                images.setBackgroundResource(R.drawable.grey_border);
            }
        });
        recyclerView.setAdapter(adapter);
        Log.i("1", String.valueOf(recyclerView.getHeight()));
    }

    private ArrayList<String> getAllShownImagesPath(Activity activity) {
        Uri uri;
        Cursor cursor;
        int column_index_data, column_index_folder_name;
        ArrayList<String> listOfAllImages = new ArrayList<String>();
        String absolutePathOfImage = null;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;

        String[] projection = { MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME };

        cursor = activity.getContentResolver().query(uri, projection, null,
                null, null);

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_folder_name = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        while (cursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);

            listOfAllImages.add(0, absolutePathOfImage);
        }
        return listOfAllImages;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case GALLERY_REQUEST:
                    Uri selectedImage = data.getData();
                    String realPath = ImageFilePath.getPath(mContext, selectedImage);
                    Log.i(TAG, "onActivityResult: file path : " + realPath);
                    refreshPhotoInst(realPath);
                    break;
                case CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE:
                    if(photoFile != null){
                        refreshPhotoInst(photoFile.getAbsolutePath());
                        //Toast.makeText(mContext, "Camera path: " + photoFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
                    }
            }
        } else{

        }
    }

    public File getPhotoFileUri(String fileName) {
        File mediaStorageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), TAG);
        if (!mediaStorageDir.exists() && !mediaStorageDir.mkdirs()){
            Log.d(TAG, "failed to create directory");
        }
        File file = new File(mediaStorageDir.getPath() + File.separator + fileName);
        return file;
    }

    private void refreshPhotoInst(String path){
        if(imageSaveToInst != null){
            photoInst.remove(0);
            photoInst.add(0, "file://" + path);
        } else {
            photoInst.add(0, "file://" + path);
        }
        imageSaveToInst = path;
        adapterGridView.notifyDataSetChanged();

    }


}
