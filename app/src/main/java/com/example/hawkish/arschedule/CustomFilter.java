package com.example.hawkish.arschedule;

import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;

public class CustomFilter extends Filter{

    AdapterUz adapter;
    List<ModelUz> uzList ;

    public CustomFilter(List<ModelUz> uzList, AdapterUz adapter)
    {
        this.adapter=adapter;
        this.uzList =uzList;
    }
    //FILTERING OCURS
    @Override
    protected FilterResults performFiltering(CharSequence constraint) {
        FilterResults results=new FilterResults();
        //CHECK CONSTRAINT VALIDITY
        if(constraint != null && constraint.length() > 0)
        {
            //CHANGE TO UPPER
            constraint=constraint.toString().toUpperCase();
            //STORE OUR FILTERED
            ArrayList<ModelUz> filteredWineries=new ArrayList<>();

            for (int i = 0; i< uzList.size(); i++)
            {    //CHECK
                if(uzList.get(i).getName().toUpperCase().contains(constraint))
                {//ADD TO FILTERED
                    filteredWineries.add(uzList.get(i));
                }
            }
            results.count=filteredWineries.size();
            results.values=filteredWineries;
        }else
        {
            results.count= uzList.size();
            results.values= uzList;
        }

        return results;
    }

    @Override
    protected void publishResults(CharSequence constraint, FilterResults results) {
        adapter.uzList= (ArrayList<ModelUz>) results.values;
        //REFRESH
        adapter.notifyDataSetChanged();
    }
}