package com.projects.ingilizcekelimecalis;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> implements Filterable {

    private ArrayList<Kelime> kelimeList;
    private ArrayList<Kelime> filteredKelimeList;
    private Context context;
    private CustomItemClickListener customItemClickListener;

    private CustomItemLongClickListener customItemLongClickListener;///

    public CustomAdapter(Context context, ArrayList<Kelime> wordArrayList,
                         CustomItemClickListener customItemClickListener, CustomItemLongClickListener customItemLongClickListener) {
        this.context = context;
        this.kelimeList = wordArrayList;
        this.filteredKelimeList = wordArrayList;
        this.customItemClickListener = customItemClickListener;
        this.customItemLongClickListener = customItemLongClickListener;///
    }

    @Override
    public CustomAdapter.MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_item, viewGroup, false);
        final MyViewHolder myViewHolder = new MyViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // for click item listener
                customItemClickListener.onItemClick(filteredKelimeList.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
            }
        });

        view.setOnLongClickListener(new View.OnLongClickListener() {///
            @Override
            public boolean onLongClick(View view) {
                customItemLongClickListener.onItemLongClick(filteredKelimeList.get(myViewHolder.getAdapterPosition()), myViewHolder.getAdapterPosition());
                return false;
            }
        });

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(CustomAdapter.MyViewHolder viewHolder, int position) {
        viewHolder.turkce.setText(filteredKelimeList.get(position).getTurkish());
        viewHolder.ingilizce.setText(filteredKelimeList.get(position).getEnglish());
    }

    @Override
    public int getItemCount() {
        return filteredKelimeList.size();
    }

    public void setKelimeList(ArrayList<Kelime> kelimeList) {
        this.filteredKelimeList = kelimeList;
        this.kelimeList = kelimeList;
        notifyDataSetChanged();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String searchString = charSequence.toString();

                if (searchString.isEmpty()) {
                    filteredKelimeList = kelimeList;
                } else {
                    ArrayList<Kelime> tempFilteredList = new ArrayList<>();

                    for (Kelime kelime : kelimeList) {
                        // search for user name
                        if (kelime.getTurkish().toLowerCase().contains(searchString)) {
                            tempFilteredList.add(kelime);
                        }
                    }

                    filteredKelimeList = tempFilteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredKelimeList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filteredKelimeList = (ArrayList<Kelime>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {
        private TextView turkce;
        private TextView ingilizce;

        public MyViewHolder(View view) {
            super(view);
            turkce = (TextView) view.findViewById(R.id.eng);
            ingilizce = (TextView) view.findViewById(R.id.tur);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            //menu.setHeaderTitle("Kelimeye Uygula");
            menu.add(0, v.getId(), 0, "Sil");//groupId, itemId, order, title
            menu.add(0, v.getId(), 0, "Kopyala");
            menu.add(0, v.getId(), 0, "Paylaş");
        }

    }
}

