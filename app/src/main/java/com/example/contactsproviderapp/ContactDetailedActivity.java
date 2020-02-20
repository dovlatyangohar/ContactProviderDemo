package com.example.contactsproviderapp;

import android.Manifest;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactsproviderapp.adapter.NumberAdapter;
import com.example.contactsproviderapp.model.NumberItem;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.example.contactsproviderapp.MainActivity.EXTRA_CONTACT_ID;
import static com.example.contactsproviderapp.MainActivity.EXTRA_NAME;


public class ContactDetailedActivity extends AppCompatActivity {
    private static final String TAG = "DETAILED";
    private TextView tvContactName;
    private ImageView addNumber;
    private ImageView startCallImg;
    private List<NumberItem> numberList;
    private String number;
    private String id;
    private String contactID;
    private boolean isSaved;
    private String addedNumber;
    private static final int REQUEST_CALL = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_detailed);

        Intent intent = getIntent();
        String contactName = intent.getStringExtra(EXTRA_NAME);
        tvContactName = findViewById(R.id.contact_name);
        tvContactName.setText(contactName);

        contactID = intent.getStringExtra(EXTRA_CONTACT_ID);

        addNumber = findViewById(R.id.add_number_img);

        fetchContactNumber();
        displayNumber();
        insertNumber();

//        View phone = getLayoutInflater().inflate(R.layout.item_contact_number,
//                null, false);
//        startCallImg = phone.findViewById(R.id.call_img);
//        startCallImg.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                makePhoneCall();
//            }
//        });
    }


    private void fetchContactNumber() {
        numberList = new ArrayList<>();

        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = {ContactsContract.CommonDataKinds.Phone.NUMBER,
                ContactsContract.RawContacts.CONTACT_ID
        };

        Cursor cursorDetailed = getContentResolver().query(uri,
                projection,
                null,
                null,
                null);
        try {

            while (cursorDetailed != null && cursorDetailed.moveToNext()) {
                number = cursorDetailed.getString(cursorDetailed.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                id = cursorDetailed.getString(cursorDetailed.getColumnIndex(ContactsContract.RawContacts.CONTACT_ID));

                if (id.equals(contactID)) {
                    NumberItem numberItem = new NumberItem(number, id);
                    numberList.add(numberItem);
                }
            }
        } finally {
            if (cursorDetailed != null) {
                cursorDetailed.close();
            }
        }
    }

    private void displayNumber() {
        RecyclerView recyclerView = findViewById(R.id.rv_contact_number);
        NumberAdapter numberAdapter = new NumberAdapter(numberList, this);
        recyclerView.setAdapter(numberAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

//        ListView listView = findViewById(R.id.rv_contact_number);
//        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, numberList);  // didn't work neither with R.layout.simple_list_item_1 nor R.layout.item_contact_number
//
//        listView.setAdapter(arrayAdapter);
    }

    private void insertNumber() {

        addNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


    }

    private void showDialog() {
        final Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_number_dialog);
        dialog.setTitle("Add new number");

        Button saveButton = dialog.findViewById(R.id.save_btn);
        Button cancelButton = dialog.findViewById(R.id.cancel_btn);

        final EditText newNumberEditText = dialog.findViewById(R.id.dialog_add_num);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = true;
                addedNumber = newNumberEditText.getText().toString();
                if (!addedNumber.equals("")) {
                    LinkedList<Long> lcv = new LinkedList<>();
                    ContentResolver cr = getContentResolver();
                    Uri newUri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
                            Uri.encode(addedNumber));
                    Cursor c = cr.query(newUri,
                            null,
                            null,
                            null,
                            null);
                    try {
                        while (c != null && c.moveToNext()) {
                            Uri lookupUri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_LOOKUP_URI,
                                    c.getString(c.getColumnIndex(ContactsContract.PhoneLookup.LOOKUP_KEY)));
                            Cursor c2 = getContentResolver().query(lookupUri, new String[]{ContactsContract.Data.RAW_CONTACT_ID, ContactsContract.Contacts.DISPLAY_NAME},
                                    null, null, null);
                            try {
                                if (c2 != null && c2.moveToNext()) {
                                    Log.i(TAG, "found: " + c2.getLong(c2.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)) + ", " + c2.getString(c2.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));
                                    lcv.add(c2.getLong(c2.getColumnIndex(ContactsContract.Data.RAW_CONTACT_ID)));
                                } else {
                                    Log.e(TAG, "failed to lookup");
                                }
                            } finally {
                                if (c2 != null) {
                                    c2.close();
                                }
                            }
                        }
                    } finally {
                        if (c != null) {
                            c.close();
                        }
                    }
                    for (Long rawId : lcv) {
                        Uri rawContactUri = ContactsContract.RawContacts.CONTENT_URI;
                        Cursor c3 = cr.query(rawContactUri, null,
                                ContactsContract.RawContacts.CONTACT_ID + "=?",
                                new String[]{rawId + ""}, null);
                        if (c3 != null && c3.moveToNext()) {
                            Log.e(TAG, "c3 get string: " + c3.getString(c3.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                        } else {
                            Log.e(TAG, "error");
                        }
                        ContentValues cv = new ContentValues();
                        cv.put(ContactsContract.Data.RAW_CONTACT_ID, rawId + "");
                        cv.put(ContactsContract.Data.DATA1, "mydata");

                        Uri newIns = cr.insert(ContactsContract.Data.CONTENT_URI, cv);
                        Log.i(TAG, "insert: " + newIns + ", " + addedNumber);
                    }

                }
                displayNumber();

                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isSaved = false;
                dialog.dismiss();
            }
        });
        dialog.show();

    }

    private void makePhoneCall() {

    }


    private void updateName() {
    }

    private void updateNumber() {
    }

    private void deleteNumber() {
    }


}
