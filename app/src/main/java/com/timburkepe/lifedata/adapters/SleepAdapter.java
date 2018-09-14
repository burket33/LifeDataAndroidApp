package com.timburkepe.lifedata.adapters;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.timburkepe.lifedata.R;
import com.timburkepe.lifedata.SleepModel;
import com.timburkepe.lifedata.databinding.SleepListItemBinding;

import java.util.List;

public class SleepAdapter extends RecyclerView.Adapter<SleepAdapter.ViewHolder> {

    private List<SleepModel> sleeps;
    private Context context;

    public SleepAdapter(List<SleepModel> sleeps, Context context) {
        this.sleeps = sleeps;
        this.context = context;
    }

    @NonNull
    @Override
    public SleepAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        SleepListItemBinding binding = DataBindingUtil
                .inflate(LayoutInflater.from(parent.getContext()),
                        R.layout.sleep_list_item,
                        parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SleepModel sleep = sleeps.get(position);
        holder.sleepListItemBinding.setSleepData(sleep);
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context, "You clicked " + sleep.getDescription(), Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return sleeps.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public RelativeLayout relativeLayout;

        //Binding Variable
        public SleepListItemBinding sleepListItemBinding;

        //Constructor to do view lookus for each subview
        public ViewHolder(SleepListItemBinding sleepLayoutBinding){
            super(sleepLayoutBinding.getRoot());
            sleepListItemBinding = sleepLayoutBinding;
            relativeLayout = itemView.findViewById(R.id.sleepListView);

        }

    }
}
