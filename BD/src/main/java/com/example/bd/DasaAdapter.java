package com.example.bd;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DasaAdapter extends RecyclerView.Adapter<DasaAdapter.ViewHolder> {
    private final LayoutInflater inflater;
    private final List<DasaBannih> bases;
    public Boolean isFirst = true;
    private GridLayoutManager manager;

    DasaAdapter(Context context, List<DasaBannih> bases, GridLayoutManager manager) {
        this.bases = bases;
        this.inflater = LayoutInflater.from(context);
        this.manager = manager;
    }
    @Override
    public DasaAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(manager.getSpanCount() == 1) {
            View view = inflater.inflate(R.layout.list_item, parent, false);
            return new ViewHolder(view);
        }
        else {
            View view = inflater.inflate(R.layout.grid_item, parent, false);
            return new ViewHolder(view);
        }
    }
    @Override
    public void onBindViewHolder(DasaAdapter.ViewHolder holder, int position) {
            DasaBannih basa = bases.get(position);
            if (basa.getFirst() == null) holder.firstView.setVisibility(View.GONE);
            else holder.firstView.setText(basa.getFirst());
            if (basa.getSecond() == null) holder.secondView.setVisibility(View.GONE);
            else holder.secondView.setText(basa.getSecond());
            if (basa.getThird() == null) holder.thirdView.setVisibility(View.GONE);
            else holder.thirdView.setText(basa.getThird());
            if (basa.getFourth() == null) holder.fourthView.setVisibility(View.GONE);
            else holder.fourthView.setText(basa.getFourth());
            if (basa.getFifth() == null) holder.fifthView.setVisibility(View.GONE);
            else holder.fifthView.setText(basa.getFifth());
            if(isFirst) {
                holder.firstView.setTypeface(null, Typeface.BOLD);
                holder.secondView.setTypeface(null, Typeface.BOLD);
                holder.thirdView.setTypeface(null, Typeface.BOLD);
                holder.fourthView.setTypeface(null, Typeface.BOLD);
                holder.fifthView.setTypeface(null, Typeface.BOLD);
                isFirst = false;
            }
    }

    @Override
    public int getItemCount() {
        return bases.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView firstView, secondView, thirdView, fourthView, fifthView;

        ViewHolder(View view) {
            super(view);
            firstView = view.findViewById(R.id.first_st);
            secondView = view.findViewById(R.id.second_st);
            thirdView = view.findViewById(R.id.third_st);
            fourthView = view.findViewById(R.id.fourth_st);
            fifthView = view.findViewById(R.id.fifth_st);
        }
    }
}
