package com.eventer.app.Write;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.eventer.app.R;
import com.eventer.app.model.Event;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Gulzar on 27-10-2016.
 */
public class WriteActivity extends AppCompatActivity {
    @BindView(R.id.w_eventTitle)EditText w_eventTitle;
    @BindView(R.id.w_eventBody)EditText w_eventBody;
    @BindView(R.id.w_eventRules)EditText w_eventRules;
    @BindView(R.id.w_addcontactsbtn)Button w_addcontactsbtn;
    @BindView(R.id.w_clearcontactsbtn)Button w_clearcontactsbtn;
    @BindView(R.id.w_eventVenue)EditText w_eventVenue;
    @BindView(R.id.w_contacts)TextView w_contacts;
    @BindView(R.id.w_minreg)EditText w_minreg;
    @BindView(R.id.w_maxreg)EditText w_maxreg;
    @BindView(R.id.w_eventDate)EditText w_eventDate;
    String eventTitle,eventBody,eventRules,eventVenue,eventminReg,eventMaxReg,date;
    public static final int CONTACT_PICKER_RESULT = 1001;
    ArrayList<String> contact=new ArrayList<>();
    ArrayList<String> userKey=new ArrayList<>();
    ArrayList<String> organizer=new ArrayList<>();
    String logourl="https://firebasestorage.googleapis.com/v0/b/eventer-app-b1654.appspot.com/o/drawing.png?alt=media&token=d363c215-321e-4a8d-b9fd-75f006c236bb";
    String downloadurl="https://firebasestorage.googleapis.com/v0/b/eventer-app-b1654.appspot.com/o/drawing.png?alt=media&token=d363c215-321e-4a8d-b9fd-75f006c236bb";
    String key;
    DatabaseReference mDatabase= FirebaseDatabase.getInstance().getReference();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_activity);
        ButterKnife.bind(this);
        w_contacts.setText(" ");
    }

    @OnClick(R.id.w_addcontactsbtn) void AddContact()
    {
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, CONTACT_PICKER_RESULT);
    }
    @OnClick(R.id.w_clearcontactsbtn) void ClearContact()
    {
        contact.clear();
        w_contacts.setText("");
    }

    @OnClick(R.id.w_submit) void Submit()
    {
        eventTitle=w_eventTitle.getText().toString();
        eventBody=w_eventBody.getText().toString();
        eventVenue=w_eventVenue.getText().toString();
        eventMaxReg=w_maxreg.getText().toString();
        eventminReg=w_minreg.getText().toString();
        eventRules=w_eventRules.getText().toString();
        date=w_eventDate.getText().toString();
        //Contacts ArrayList On Top
        createEvent();

    }
    void createEvent()
    {
        userKey.add("4liHTV3xMIVuZzHJFhesEFZW4K52");
        organizer.add("Gaurav Sehgal");

        key = mDatabase.child("events").push().getKey();
        Event event = new Event(key,userKey, eventTitle, eventBody, date, eventVenue, eventRules,organizer, downloadurl,logourl,"100 Rs",1415565454,1,1);
        Map<String, Object> eventValues = event.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/events/" + key, eventValues);
        mDatabase.updateChildren(childUpdates);
    }

    //Contact Picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK)
        {
            switch (requestCode)
            {
                case CONTACT_PICKER_RESULT:
                    Cursor cursor = null;

                    try {
                        Uri result = data.getData();
                        String id = result.getLastPathSegment();

                        //Get Name
                        cursor = getContentResolver().query(result, null, null, null, null);
                        if (cursor.moveToFirst()) {
                        String    contactName = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));//Display Name
                        String    contactPhone=cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                           contact.add(contactName+":"+contactPhone);
                            String temp= (String) w_contacts.getText();
                            w_contacts.setText(temp+"\n"+contactName+":"+contactPhone);

                        }} catch (Exception e) { }
                    break;
            }
        }
    }

}
