package com.Andriod.ER.com;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.Andriod.ER.R;

import org.json.JSONArray;
import org.json.JSONException;

public class DataRecyclerView extends RecyclerView.Adapter<DataRecyclerView.MyViewHolder> {
    private Context context;
    private JSONArray jsonArray;

    public DataRecyclerView(Context context, JSONArray jsonArray) {
        this.context = context;
        this.jsonArray = jsonArray;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.batterieslayout, parent, false);
        context = parent.getContext();
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        try {

            holder.nameTV.setText("Battery Name: "+jsonArray.getJSONObject(position).getString("name"));
            holder.usernameTV.setText("User Name: "+jsonArray.getJSONObject(position).getString("userName"));
            holder.normaldateTV.setText("Normal Date: "+jsonArray.getJSONObject(position).getString("normalDate"));
            if (jsonArray.getJSONObject(position).getString("sparkDate")!=null &&jsonArray.getJSONObject(position).getString("sparkDate")!=""){
                holder.sparkdateTV.setVisibility(View.VISIBLE);
                holder.sparkdateTV.setText("Spark Date: "+jsonArray.getJSONObject(position).getString("sparkDate"));
            }else {
                holder.sparkdateTV.setVisibility(View.GONE);

            }

        } catch (JSONException e) {
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public int getItemCount() {
        return (jsonArray == null) ? 0 : jsonArray.length();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView nameTV,usernameTV,normaldateTV,sparkdateTV;
        View view;

        MyViewHolder(View itemView) {
            super(itemView);
            this.view = itemView;
            nameTV = itemView.findViewById(R.id.nameTV);
            usernameTV = itemView.findViewById(R.id.usernameTV);
            normaldateTV = itemView.findViewById(R.id.normaldateTV);
            sparkdateTV = itemView.findViewById(R.id.sparkdateTV);

        }
    }
}
