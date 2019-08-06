package kenneth.com.refardenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 1002215 on 20/7/19.
 */

public class NotificationsFragment extends Fragment implements NotificationsAdapter.OnNotifClickListener {

    private static final String TAG = "Notifications Fragment";
    private ArrayList<TradeItem> mNotificationsList = new ArrayList<>();
    private RecyclerView mNotifRecyclerView;
    private RecyclerView.Adapter mNotifAdapter;
    private RecyclerView.LayoutManager mNotifLayoutManager;
    private FirebaseAuth mAuth;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);

        //Set Fragment Title
        getActivity().setTitle("Notifications");

        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference();

        myRef.child("Trading Platform").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //Call another function to update the UI on the screen by passing in datasnapshot which is like the info
                extractTradeList(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        mNotifRecyclerView = view.findViewById(R.id.notificationsRecyclerView);
        mNotifRecyclerView.setHasFixedSize(true);
        mNotifLayoutManager = new LinearLayoutManager(this.getContext());
        mNotifAdapter = new NotificationsAdapter(mNotificationsList, this);

        mNotifRecyclerView.setLayoutManager(mNotifLayoutManager);
        mNotifRecyclerView.setAdapter(mNotifAdapter);

        return view;
    }

    public void changeActivity() {
        Intent intent = new Intent(this.getActivity(), NewTradePostActivity.class);
        startActivity(intent);
    }

    @Override
    public void onNotifClick(int position) {
        Intent intent = new Intent(this.getActivity(), NotificationsItemActivity.class);
        intent.putExtra("uid", mNotificationsList.get(position).getmUid());
        intent.putExtra("timeStamp", mNotificationsList.get(position).getmTimeStamp());
        intent.putExtra("itemOwner", mNotificationsList.get(position).getmItemOwner());
        intent.putExtra("request", mNotificationsList.get(position).getmRequest());
        intent.putExtra("requester", mNotificationsList.get(position).getmRequester());
        intent.putExtra("plantImage", mNotificationsList.get(position).getmPlantImage());
        intent.putExtra("plantType", mNotificationsList.get(position).getmPlantType());
        intent.putExtra("plantsWanted", mNotificationsList.get(position).getmPlantsWanted());
        intent.putExtra("description", mNotificationsList.get(position).getmDescription());
        intent.putExtra("star1", mNotificationsList.get(position).getmStar1());
        intent.putExtra("star2", mNotificationsList.get(position).getmStar2());
        intent.putExtra("star3", mNotificationsList.get(position).getmStar3());
        intent.putExtra("star4", mNotificationsList.get(position).getmStar4());
        intent.putExtra("star5", mNotificationsList.get(position).getmStar5());
        startActivity(intent);
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new TradeItemFragment()).commit();
    }

    private void extractTradeList(DataSnapshot dataSnapshot) {

        mNotificationsList = new ArrayList<>();
        //Running through "Trading Platform" folder
        for (DataSnapshot ds1 : dataSnapshot.getChildren()) {
            //Running through "UID" folder
            int plantImage = R.drawable.melon;
            String uid = "Nil";
            String timeStamp = "Nil";
            String plantType = "Nil";
            String plantsWanted = "Nil";
            String description = "Nil";
            String itemOwner = "Nil";
            String request = "Nil";
            HashMap<String, String> requester = new HashMap<>();
            int star1 = 0;
            int star2 = 0;
            int star3 = 0;
            int star4 = 0;
            int star5 = 0;

            Log.d(TAG, "uid is:  " + ds1.getKey());
            Log.d(TAG, "currentuser is:  " + mAuth.getCurrentUser().getUid());
            uid = ds1.getKey();
            //Checks if the trade item is the user's, if it is not, dont add it to notif list
            if (uid.equals(mAuth.getCurrentUser().getUid())) {
                for (DataSnapshot ds2 : ds1.getChildren()) {
                    Log.d(TAG, "timeStamp is:  " + ds2.getKey());
                    timeStamp = ds2.getKey();
                    for (DataSnapshot ds3 : ds2.getChildren()) {
                        Log.d(TAG, "Info header is:  " + ds3.getKey());
                        if (ds3.getKey().equals("Description")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                description = ds3.getValue().toString();
                            }
                        } else if (ds3.getKey().equals("Name")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                itemOwner = ds3.getValue().toString();
                            }
                        } else if (ds3.getKey().equals("Plant Type")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                plantType = ds3.getValue().toString();
                            }

                        } else if (ds3.getKey().equals("Plants Wanted")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                plantsWanted = ds3.getValue().toString();
                            }
                        } else if (ds3.getKey().equals("Request")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                request = ds3.getValue().toString();
//                                Log.d(TAG, "ds3.getvalue() is:  " + ds3.getValue().toString());
//                                if (ds3.getValue().toString().equals("yes")) {
//                                    request = ds3.getValue().toString();
//                                } else {
//                                    //This checks if my own post have any requests, if no, dont add to notif list.
//                                    Log.d(TAG, "BREAKS");
//                                    break;
//                                }
                            }
                        } else if (ds3.getKey().equals("Requester")) {
                            if (ds3.getValue().toString().equals(null)) {
                                requester = null;
                            } else {
                                for (DataSnapshot ds4 : ds3.getChildren()) {
                                    Log.d(TAG, "ds4 is:  " + ds4.getKey());
                                    requester.put(ds4.getKey(),ds4.getValue().toString());
                                }

                            }
                        } else if (ds3.getKey().equals("Stars")) {
                            if (ds3.getValue().toString().equals(null)) {
                            } else {
                                if (ds3.getValue().toString().equals("5")) {
                                    star1 = R.drawable.ic_full_star;
                                    star2 = R.drawable.ic_full_star;
                                    star3 = R.drawable.ic_full_star;
                                    star4 = R.drawable.ic_full_star;
                                    star5 = R.drawable.ic_full_star;
                                } else if (ds3.getValue().toString().equals("4")) {
                                    star1 = R.drawable.ic_full_star;
                                    star2 = R.drawable.ic_full_star;
                                    star3 = R.drawable.ic_full_star;
                                    star4 = R.drawable.ic_full_star;
                                    star5 = R.drawable.ic_empty_star;
                                } else if (ds3.getValue().toString().equals("3")) {
                                    star1 = R.drawable.ic_full_star;
                                    star2 = R.drawable.ic_full_star;
                                    star3 = R.drawable.ic_full_star;
                                    star4 = R.drawable.ic_empty_star;
                                    star5 = R.drawable.ic_empty_star;
                                } else if (ds3.getValue().toString().equals("2")) {
                                    star1 = R.drawable.ic_full_star;
                                    star2 = R.drawable.ic_full_star;
                                    star3 = R.drawable.ic_empty_star;
                                    star4 = R.drawable.ic_empty_star;
                                    star5 = R.drawable.ic_empty_star;
                                } else if (ds3.getValue().toString().equals("1")) {
                                    star1 = R.drawable.ic_full_star;
                                    star2 = R.drawable.ic_empty_star;
                                    star3 = R.drawable.ic_empty_star;
                                    star4 = R.drawable.ic_empty_star;
                                    star5 = R.drawable.ic_empty_star;
                                } else {
                                    star1 = R.drawable.ic_empty_star;
                                    star2 = R.drawable.ic_empty_star;
                                    star3 = R.drawable.ic_empty_star;
                                    star4 = R.drawable.ic_empty_star;
                                    star5 = R.drawable.ic_empty_star;
                                }
                            }
                        }

                    }
                    if (requester != null) {
                        HashMap<String, String> tempRequester = new HashMap<>();
                        for (HashMap.Entry<String, String> entry : requester.entrySet()) {
                            Log.d(TAG, "requester uid is:  " + entry.getKey() + " requester name is: " + entry.getValue());
                            tempRequester.put(entry.getKey(),entry.getValue());
                            mNotificationsList.add((new TradeItem(uid, timeStamp, plantImage, plantType, plantsWanted, description, itemOwner, request, tempRequester, star1, star2, star3, star4, star5)));
                            Log.d(TAG, "mNotifList is:  " + mNotificationsList);
                        }
                    }
                }
            } else {
                continue;
            }

        }
        mNotifAdapter = new NotificationsAdapter(mNotificationsList, this);
        mNotifRecyclerView.setLayoutManager(mNotifLayoutManager);
        mNotifRecyclerView.setAdapter(mNotifAdapter);
    }
}
