package com.example.blockcall.activity;

import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import com.example.blockcall.R;
import com.example.blockcall.adapter.ContactAdapter;
import com.example.blockcall.controller.ItemClickListener;
import com.example.blockcall.model.ContactObj;

import java.util.ArrayList;
import java.util.List;

public class ContactActivity extends AppCompatActivity implements ItemClickListener {

    RecyclerView rvContact;
    List<ContactObj> listContact;
    Toolbar toolbar;
    ContentResolver contentResolver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        rvContact = (RecyclerView) findViewById(R.id.rv_contact);

        listContact = getListContacts();
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvContact.setLayoutManager(mLayoutManager);
        ContactAdapter contactAdapter = new ContactAdapter(listContact, ContactActivity.this, this);
        rvContact.setAdapter(contactAdapter);
    }

    private List<ContactObj> getListContacts() {
        List<ContactObj> listData = new ArrayList<>();
        ContentResolver cr = getContentResolver();
        Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);

        if ((cur != null ? cur.getCount() : 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                ContactObj contactObj = new ContactObj();
                String id = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID));
                String name = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
                contactObj.setUserName(name);
                if (cur.getInt(cur.getColumnIndex(
                        ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                            new String[]{id}, null);
                    while (pCur.moveToNext()) {
                        String phoneNo = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        contactObj.setPhoneNum(phoneNo);
                    }
                    pCur.close();
                }

                listData.add(contactObj);
            }
        }
        if(cur!=null){
            cur.close();
        }


        return listData;
    }

    @Override
    public void onCheckClick(int position) {

    }

    @Override
    public void onUncheckClick(int position) {

    }
}
