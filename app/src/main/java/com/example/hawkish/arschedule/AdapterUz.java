package com.example.hawkish.arschedule;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class AdapterUz extends RecyclerView.Adapter<AdapterUz.ViewHolder> implements Filterable {

    private Context mContext;
    CustomFilter filter;
    public List<ModelUz> uzList;
    public AdapterUz(Context context, List<ModelUz> list) {
        mContext = context;
        uzList =list;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View view = layoutInflater.inflate(R.layout.lecturer_item,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ModelUz uzItem = uzList.get(position);
        ImageView image;
        TextView name,adres,website,lecture;
        name = holder.item_name;
        adres = holder.item_adres;
        website = holder.item_website;
        image = holder.item_image;
        lecture = holder.item_lecture;
        lecture.setText(uzItem.getLecture());
        name.setText(uzItem.getName());
        adres.setText(uzItem.getNumOfClass());
        website.setText(uzItem.getWebsite());
        Picasso.get().load(uzItem.getImage()).fit().into(image);
        Linkify.addLinks(website, Linkify.WEB_URLS);
    }
    @Override
    public int getItemCount() {
        return uzList.size();
    }

    @Override
    public Filter getFilter() {
        if(filter==null)
        {filter=new CustomFilter(uzList,this); }
        return filter;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView item_image;
        private TextView item_name, item_website,item_adres, item_lecture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            item_image = itemView.findViewById(R.id.item_image);
            item_name = itemView.findViewById(R.id.item_name);
            item_website = itemView.findViewById(R.id.item_website);
            item_adres = itemView.findViewById(R.id.item_adres);
            item_lecture = itemView.findViewById(R.id.item_lecture);
        }
    }
}