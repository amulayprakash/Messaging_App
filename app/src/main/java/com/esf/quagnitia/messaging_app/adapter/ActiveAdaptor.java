package com.esf.quagnitia.messaging_app.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.esf.quagnitia.messaging_app.Activity.MessageListActivity;
import com.esf.quagnitia.messaging_app.Model.Data;
import com.esf.quagnitia.messaging_app.R;
import com.esf.quagnitia.messaging_app.Storage.Preferences;

import java.util.ArrayList;

//nikita- new changes for pagination
public class ActiveAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Data> activePickupList;
    Context context;
    LayoutInflater lf;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public ActiveAdaptor(Context activity, ArrayList<Data> activePickupList) {
        this.context = activity;
        this.activePickupList = activePickupList;
        lf = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public long getItemId(int position) {

        return position;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ITEM:
                viewHolder = getViewHolder(parent, inflater);
                break;

            case LOADING:
                View v1 = inflater.inflate(R.layout.row_active, parent, false);
                viewHolder = new ActiveAdaptor.LoadingVH(v1);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.row_active, parent, false);
        viewHolder = new ActiveAdaptor.MovieVH(v1);
        return viewHolder;
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        TextView txtJobName,txtbadge;

        public MovieVH(View convertView) {
            super(convertView);
            txtbadge = convertView.findViewById(R.id.txtbadge2);
            txtJobName = convertView.findViewById(R.id.txtJobName);
        }
    }

    protected class LoadingVH extends RecyclerView.ViewHolder {

        public LoadingVH(View itemView) {
            super(itemView);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {


        switch (getItemViewType(position)) {
            case ITEM:
                ActiveAdaptor.MovieVH holders = (ActiveAdaptor.MovieVH)holder;

                holders.txtJobName.setText(activePickupList.get(position).getSchoolName());
                if(activePickupList.get(position).getNotificationCount()>0) {
                    holders.txtbadge.setText(""+activePickupList.get(position).getNotificationCount());
                    holders.txtbadge.setVisibility(View.VISIBLE);
                }else{
                    holders.txtbadge.setVisibility(View.GONE);
                }
                //nikita
                holders.txtJobName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new Preferences(context).saveSchool(context, activePickupList.get(position).getSchoolName());
                        Intent in = new Intent(context, MessageListActivity.class);
                        new Preferences(context).putString("schoolId", activePickupList.get(position).getAqiSchoolID());
//                        in.putExtra("schoolId",activePickupList.get(position).getAqiSchoolID());
                        context.startActivity(in);
                    }
                });


                break;
            case LOADING:
//                Do nothing
                break;
        }

    }


    @Override
    public int getItemCount() {
        return activePickupList == null ? 0 : activePickupList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == activePickupList.size() - 1 && isLoadingAdded) ? LOADING : ITEM;
    }

    /*
   Helpers
   _________________________________________________________________________________________________
    */

    public void add(Data mc) {
        activePickupList.add(mc);
        notifyItemInserted(activePickupList.size() - 1);
    }

    public void addAll(ArrayList<Data> mcList) {
        for (Data mc : mcList) {
            add(mc);
        }
    }

    public void replace(ArrayList<Data> mcList) {
        activePickupList.clear();
        activePickupList.addAll(mcList);
        notifyItemInserted(activePickupList.size() - 1);
    }

    public void remove(Data city) {
        int position = activePickupList.indexOf(city);
        if (position > -1) {
            activePickupList.remove(position);
            notifyItemRemoved(position);
        }
    }

    public void clear() {
        isLoadingAdded = false;
        while (getItemCount() > 0) {
            remove(getItem(0));
        }
    }

    public Data getItem(int position) {
        return activePickupList.get(position);
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }


    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new Data());
        notifyDataSetChanged();
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;

        int position = activePickupList.size() - 1;
        if (position != getItemCount()) {
            Data item = getItem(position);

            if (item != null) {
                activePickupList.remove(position);
                notifyItemRemoved(position);
            }
        }
        notifyDataSetChanged();
    }


}
