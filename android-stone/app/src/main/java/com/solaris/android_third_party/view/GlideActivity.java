package com.solaris.android_third_party.view;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.solaris.android_third_party.BaseActivity;
import com.solaris.android_third_party.R;

public class GlideActivity extends BaseActivity {
    private static final int REQUEST_CONTACT = 1;

    private ImageView imageViewContact;
    private ImageView imageViewLookup;
    private ImageView imageViewPhoto;
    private ImageView imageViewDisplayPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_glide);

        imageViewContact = (ImageView) findViewById(R.id.image_contact);
        imageViewLookup = (ImageView) findViewById(R.id.image_lookup);
        imageViewPhoto = (ImageView) findViewById(R.id.image_photo);
        imageViewDisplayPhoto = (ImageView) findViewById(R.id.image_display_photo);

        findViewById(R.id.button_pick_contact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showContact(1);

//                打开通讯录
//                Intent intent = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);

//               期望在活动销毁的时候能够返回一个结果给上一个活动
//                startActivityForResult(intent, REQUEST_CONTACT);
            }
        });
    }

//    下一个活动返回的结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == RESULT_OK) {
            final Cursor cursor = getContentResolver().query(data.getData(), null, null, null, null);
            try {
//                从通讯录返回信息
                if (cursor != null && cursor.moveToFirst()) {
                    final long contactId = cursor.getLong(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    showContact(contactId);
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void showContact(long id) {
//        RequestOptions originalSize = new RequestOptions().override(Target.SIZE_ORIGINAL);

//        Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, id);
        Uri contactUri =  Uri.parse( "http://inthecheesefactory.com/uploads/source/glidepicasso/cover.jpg");
        Glide.with(this).load(contactUri).into(imageViewContact);

//        Uri lookupUri = ContactsContract.Contacts.getLookupUri(getContentResolver(), contactUri);
        Uri lookupUri = Uri.parse("http://nuuneoi.com/uploads/source/playstore/cover.jpg");
        Glide.with(this).load(lookupUri).into(imageViewLookup);

        Uri photoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.CONTENT_DIRECTORY);
        Glide.with(this).load(photoUri).into(imageViewPhoto);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            Uri displayPhotoUri = Uri.withAppendedPath(contactUri, ContactsContract.Contacts.Photo.DISPLAY_PHOTO);
            Glide.with(this).load(displayPhotoUri).into(imageViewDisplayPhoto);
        }
    }

}

