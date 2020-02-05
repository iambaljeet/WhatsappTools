package com.app.wptools.NoNumberMessage.Adapters;

import android.content.Context;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.wptools.NoNumberMessage.Activities.SavedNumbersActivity;
import com.app.wptools.NoNumberMessage.Models.SqLiteText;
import com.app.wptools.R;

import java.util.ArrayList;

/**
 * Created by Baljeet on 04-02-2018.
 */

public class NumbersAdapter extends RecyclerView.Adapter<NumbersAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<SqLiteText> numberList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public RelativeLayout viewBackground, viewForeground;

        public MyViewHolder(View view) {
            super(view);
            number = view.findViewById(R.id.number);
            viewBackground = view.findViewById(R.id.view_background);
            viewForeground = view.findViewById(R.id.view_foreground);
        }
    }


    public NumbersAdapter(Context context, ArrayList<SqLiteText> numberList) {
        this.context = context;
        this.numberList = numberList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_numbers, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final SqLiteText item = numberList.get(position);
        holder.number.setText(item.getNumber());

        holder.viewForeground.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SavedNumbersActivity)context).finish();

                Intent intent = new Intent("saved_number");
                intent.putExtra("number", item.getNumber());
                intent.putExtra("code", item.getCode());
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberList.size();
    }

    public void removeItem(int position) {
        numberList.remove(position);
        // notify the item removed by position
        // to perform recycler view delete animations
        // NOTE: don't call notifyDataSetChanged()
        notifyItemRemoved(position);
    }

    public void restoreItem(SqLiteText item, int position) {
        numberList.add(position, item);
        // notify item added by position
        notifyItemInserted(position);
    }

    public void dataAdded() {
        notifyDataSetChanged();
    }
}
