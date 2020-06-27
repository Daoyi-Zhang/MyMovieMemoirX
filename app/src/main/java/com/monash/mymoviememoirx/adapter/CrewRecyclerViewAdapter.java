package com.monash.mymoviememoirx.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.monash.mymoviememoirx.R;
import com.monash.mymoviememoirx.pojo.Crew;

import java.util.List;

public class CrewRecyclerViewAdapter extends RecyclerView.Adapter<CrewRecyclerViewAdapter.ViewHolder>{

    private List<Crew> crews;

    public CrewRecyclerViewAdapter(List<Crew> crews) {
        this.crews = crews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView crewImage;
        TextView crewName;
        TextView crewCharacter;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            crewImage = itemView.findViewById(R.id.crew_image);
            crewName = itemView.findViewById(R.id.crew_name);
            crewCharacter = itemView.findViewById(R.id.crew_character);
        }
    }

    public void setCrews(List<Crew> crews) {
        this.crews = crews;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View crewsView = inflater.inflate(R.layout.recycler_view_crew_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(crewsView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Crew crew = crews.get(position);
        holder.crewName.setText(crew.getCrewName());
        holder.crewCharacter.setText(crew.getCrewCharacter());
        holder.crewImage.setImageBitmap(crew.getCrewImage());
    }

    @Override
    public int getItemCount() {
        return crews.size();
    }
}
