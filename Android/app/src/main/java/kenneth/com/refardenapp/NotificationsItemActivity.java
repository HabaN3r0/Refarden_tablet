package kenneth.com.refardenapp;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class NotificationsItemActivity extends AppCompatActivity {

    private static final String TAG = "Notifications Item Activity";
    private FirebaseAuth mAuth;
    private LinearLayout mNotificationsItemBackButtonLinearLayout;
    private Button mNotificationsItemBackButton;
    private TextView mNotificationsItemProfileName;
    private ImageView mNotificationsItemProfileImage;
    private ImageView mNotificationsItemStar1;
    private ImageView mNotificationsItemStar2;
    private ImageView mNotificationsItemStar3;
    private ImageView mNotificationsItemStar4;
    private ImageView mNotificationsItemStar5;
    private TextView mNotificationsItemOwnerItem;
    private ImageView mNotificationsItemOwnerItemImage;
    private TextView mNotificationsItemRequesterHeader;
    private TextView mNotificationsItemRequesterItem;
    private ImageView mNotificationsItemRequesterItemImage;
    private Button mNotificationsItemAcceptButton;
    private Button mNotificationsItemDeclineButton;
    private String userName;
    private String itemOwner;
    private String requesterUid;
    private String requesterName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_item);
        mNotificationsItemBackButtonLinearLayout = findViewById(R.id.notificationsItemBackButtonLinearLayout);
        mNotificationsItemBackButton = findViewById(R.id.notificationsItemBackButton);
        mNotificationsItemProfileName = findViewById(R.id.notificationsItemProfileName);
        mNotificationsItemProfileImage = findViewById(R.id.notificationsItemPostProfileImage);
        mNotificationsItemStar1 = findViewById(R.id.notificationsItemStar1);
        mNotificationsItemStar2 = findViewById(R.id.notificationsItemStar2);
        mNotificationsItemStar3 = findViewById(R.id.notificationsItemStar3);
        mNotificationsItemStar4 = findViewById(R.id.notificationsItemStar4);
        mNotificationsItemStar5 = findViewById(R.id.notificationsItemStar5);
        mNotificationsItemOwnerItem = findViewById(R.id.notificationsItemOwnerItem);
        mNotificationsItemOwnerItemImage = findViewById(R.id.notificationsItemOwnerItemImage);
        mNotificationsItemRequesterHeader = findViewById(R.id.notificationsItemRequesterHeader);
        mNotificationsItemRequesterItem = findViewById(R.id.notificationsItemRequesterItem);
        mNotificationsItemRequesterItemImage = findViewById(R.id.notificationsItemRequesterItemImage);
        mNotificationsItemAcceptButton = findViewById(R.id.notificationsItemAcceptButton);
        mNotificationsItemDeclineButton = findViewById(R.id.notificationsItemDeclineButton);
        mAuth = FirebaseAuth.getInstance();

        Intent intent = getIntent();

        final String uid = intent.getStringExtra("uid");
        final String timeStamp = intent.getStringExtra("timeStamp");
        itemOwner = intent.getStringExtra("itemOwner");
        String request = intent.getStringExtra("request");
        HashMap<String, String> requester = (HashMap<String, String>) intent.getSerializableExtra("requester");
        int plantImage = intent.getIntExtra("plantImage",0);
        final String plantType = intent.getStringExtra("plantType");
        String plantsWanted = intent.getStringExtra("plantsWanted");
        String description = intent.getStringExtra("description");
        int star1 = intent.getIntExtra("star1",0);
        int star2 = intent.getIntExtra("star2",0);
        int star3 = intent.getIntExtra("star3",0);
        int star4 = intent.getIntExtra("star4",0);
        int star5 = intent.getIntExtra("star5",0);

        String requesterUid="";
        String requesterName="";

        for (HashMap.Entry<String, String> entry : requester.entrySet()) {
            requesterUid = entry.getKey();
            requesterName = entry.getValue().toString();
            Log.d(TAG, "requester uid is:  " + entry.getKey() + " requester name is: " + entry.getValue());
        }

        // Make Status bar transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {

            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        mNotificationsItemProfileName.setText(requesterName);
        mNotificationsItemProfileImage.setImageResource(plantImage);
        mNotificationsItemOwnerItem.setText(plantType);
        mNotificationsItemOwnerItemImage.setImageResource(plantImage);
//        mNotificationsItemRequesterHeader.setText("Plants from " + requesterName);
        mNotificationsItemRequesterItem.setText(plantsWanted);
        mNotificationsItemRequesterItemImage.setImageResource(plantImage);
        mNotificationsItemStar1.setImageResource(star1);
        mNotificationsItemStar2.setImageResource(star2);
        mNotificationsItemStar3.setImageResource(star3);
        mNotificationsItemStar4.setImageResource(star4);
        mNotificationsItemStar5.setImageResource(star5);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        mNotificationsItemBackButtonLinearLayout.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );

        mNotificationsItemBackButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                }
        );


        // Read from the database
        myRef.child("User Accounts").child(mAuth.getCurrentUser().getUid()).child("Profile").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Call another function to update the UI on the screen by passing in datasnapshot which is like the info
//                checkTradeUser(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mNotificationsItemAcceptButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myRef.child("Trading Platform").child(uid).child(timeStamp).removeValue();
                        myRef.child("User Accounts").child(uid).child("Trading Platform").child(timeStamp).removeValue();
                        myRef.child("User Accounts").child(uid).child("Profile").child("Plant Types").child(plantType).removeValue();
//                        myRef.child("User Accounts").child(uid).child("Trading Platform").child(timeStamp).child("Request").setValue("yes");
//                        myRef.child("User Accounts").child(uid).child("Trading Platform").child(timeStamp).child("Requester").child(mAuth.getCurrentUser().getUid()).setValue(userName);
//
                        //TODO: Can maybe send a notif to the other user?

                        Toast.makeText(NotificationsItemActivity.this, "Trade accepted!",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        );

        final String finalRequesterUid = requesterUid;
        final String finalRequesterUid1 = requesterUid;
        final String finalRequesterUid2 = requesterUid;
        mNotificationsItemDeclineButton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        myRef.child("Trading Platform").child(uid).child(timeStamp).child("Requester").child(finalRequesterUid).removeValue();
                        myRef.child("User Accounts").child(uid).child("Trading Platform").child(timeStamp).child("Requester").child(finalRequesterUid2).removeValue();
//                        myRef.child("Trading Platform").child(uid).child(timeStamp).child("Requester").child(mAuth.getCurrentUser().getUid()).setValue(userName);
//                        myRef.child("User Accounts").child(uid).child("Trading Platform").child(timeStamp).child("Request").setValue("yes");
//
                        //TODO: Can maybe send a notif to the other user?

                        Toast.makeText(NotificationsItemActivity.this, "Trade declined!",
                                Toast.LENGTH_LONG).show();
                        finish();
                    }
                }
        );
    }

//    private void checkTradeUser(DataSnapshot dataSnapshot) {
//        for (DataSnapshot ds : dataSnapshot.getChildren()) {
//            if (ds.getKey().equals("Name")) {
//                Log.w(TAG, "ds: " + ds.getKey());
//                if (ds.getValue()== null){} else {
//                    userName = ds.getValue().toString();
//                    if (ds.getValue().toString().equals(itemOwner)) {
//                        Log.w(TAG, "ds: " + ds.getValue().toString() +" userName: " + userName);
//                        mTradeItemTradeButton.setVisibility(View.INVISIBLE);
//                    }
//                }
//            }
//        }
//    }


}
