package kenneth.com.refardenapp;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by 1002215 on 27/7/19.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.NotificationsViewHolder> {

    private ArrayList<TradeItem> mNotificationsList;
    private OnNotifClickListener mOnNotifClickListener;
    private String requesterUid;
    private String requesterName;

    public static class NotificationsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView mNotifImageView;
        public TextView mNotifTextView;
//        public TextView mDescriptionTextView;
//        public ImageView mStar1;
//        public ImageView mStar2;
//        public ImageView mStar3;
//        public ImageView mStar4;
//        public ImageView mStar5;
        OnNotifClickListener onNotifClickListener;

        public NotificationsViewHolder(View itemView, OnNotifClickListener onNotifClickListener) {
            super(itemView);
            mNotifImageView = itemView.findViewById(R.id.notificationImage);
            mNotifTextView = itemView.findViewById(R.id.notificationText);
//            mDescriptionTextView = itemView.findViewById(R.id.tradeDescription);
//            mStar1 = itemView.findViewById(R.id.tradeStar1);
//            mStar2 = itemView.findViewById(R.id.tradeStar2);
//            mStar3 = itemView.findViewById(R.id.tradeStar3);
//            mStar4 = itemView.findViewById(R.id.tradeStar4);
//            mStar5 = itemView.findViewById(R.id.tradeStar5);

            this.onNotifClickListener = onNotifClickListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            onNotifClickListener.onNotifClick(getAdapterPosition());
        }
    }

    public NotificationsAdapter(ArrayList<TradeItem> tradeList, OnNotifClickListener onNotifClickListener) {

        mNotificationsList = tradeList;
        mOnNotifClickListener = onNotifClickListener;
    }

    @Override
    public NotificationsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifications_item, parent, false);
        NotificationsViewHolder tvh = new NotificationsViewHolder(view, mOnNotifClickListener);
        return (tvh);
    }

    @Override
    public void onBindViewHolder(NotificationsViewHolder holder, int position) {
        TradeItem currentItem = mNotificationsList.get(position);

        holder.mNotifImageView.setImageResource(currentItem.getmPlantImage());

        for (HashMap.Entry<String, String> entry : currentItem.getmRequester().entrySet()) {
            requesterUid = entry.getKey();
            requesterName = entry.getValue();
        }
        holder.mNotifTextView.setText("You have a request from: " + requesterName);

//        holder.mDescriptionTextView.setText(currentItem.getmDescription());
//        holder.mStar1.setImageResource(currentItem.getmStar1());
//        holder.mStar2.setImageResource(currentItem.getmStar2());
//        holder.mStar3.setImageResource(currentItem.getmStar3());
//        holder.mStar4.setImageResource(currentItem.getmStar4());
//        holder.mStar5.setImageResource(currentItem.getmStar5());
    }

    @Override
    public int getItemCount() {
        return mNotificationsList.size();
    }

    public interface OnNotifClickListener {
        void onNotifClick(int position);
    }

}
