package com.example.quagnitia.messaging_app.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.quagnitia.messaging_app.Model.Data;
import com.example.quagnitia.messaging_app.R;

import java.util.ArrayList;

//nikita- new changes for pagination
public class MessageAdaptor extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    ArrayList<Data> activePickupList;
    Context context;
    LayoutInflater lf;

    private static final int ITEM = 0;
    private static final int LOADING = 1;
    private boolean isLoadingAdded = false;

    public MessageAdaptor(Context activity, ArrayList<Data> activePickupList) {
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
                View v2 = inflater.inflate(R.layout.row_active, parent, false);
                viewHolder = new MessageAdaptor.LoadingVH(v2);
                break;
        }
        return viewHolder;
    }

    @NonNull
    private RecyclerView.ViewHolder getViewHolder(ViewGroup parent, LayoutInflater inflater) {
        RecyclerView.ViewHolder viewHolder;
        View v1 = inflater.inflate(R.layout.row_message, parent, false);
        viewHolder = new MessageAdaptor.MovieVH(v1);
        return viewHolder;
    }

    /**
     * Main list's content ViewHolder
     */
    protected class MovieVH extends RecyclerView.ViewHolder {
        TextView txtdate;
        WebView txtmsg, txtmsgdetail;
        ImageView imgarrow;
        LinearLayout lindetail;

        public MovieVH(View convertView) {
            super(convertView);
            txtmsgdetail = convertView.findViewById(R.id.txtmsgdetail);
            txtdate = convertView.findViewById(R.id.txtdate);
            txtmsg = convertView.findViewById(R.id.txtmsg);
            imgarrow = convertView.findViewById(R.id.imgarrow);
            lindetail = convertView.findViewById(R.id.lindetail);
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
                try {
                    final MessageAdaptor.MovieVH holders = (MessageAdaptor.MovieVH)holder;

                    holders.txtdate.setText(activePickupList.get(position).getAqiDateTime());
                    holders.txtmsg.loadData(activePickupList.get(position).getMessageName(), "text/html", "utf-8");
                    holders.txtmsgdetail.loadData(activePickupList.get(position).getMessage(), "text/html", "utf-8");

//                if (activePickupList.get(position).isIs_open()) {
//                    holders.imgarrow.setImageResource(R.drawable.dropup);
//                    holders.lindetail.setVisibility(View.VISIBLE);
//                } else {
//                    holders.lindetail.setVisibility(View.GONE);
//                    holders.imgarrow.setImageResource(R.drawable.drop_down);
//                }

                    //nikita
                    holders.imgarrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (activePickupList.get(position).isIs_open()) {
                                holders.imgarrow.setImageResource(R.drawable.drop_down);
                                holders.lindetail.setVisibility(View.GONE);
                                activePickupList.get(position).setIs_open(false);
                            } else {
                                holders.lindetail.setVisibility(View.VISIBLE);
                                holders.imgarrow.setImageResource(R.drawable.dropup);
                                activePickupList.get(position).setIs_open(true);
                            }
//                        notifyDataSetChanged();
                        }
                    });

                }catch (Exception ex){
                    ex.printStackTrace();
                }
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
