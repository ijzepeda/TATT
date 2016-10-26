package com.ijzepeda.tatt;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import static android.R.attr.order;
import static android.os.Build.ID;
import static com.google.android.gms.analytics.internal.zzy.t;

/**
 * Created by Ivan on 9/29/2016.
 */

public class OrdersRecyclerViewAdapter extends RecyclerView.Adapter<OrdersRecyclerViewAdapter.ViewHolder> {
//   private String[] mDataset;
    List<Order> ordersList;

private static String ORDER_KEY="order_key";
    public OrdersRecyclerViewAdapter(List mOrdersList){//String[] myDataset){
        ordersList = mOrdersList;
        Log.e("<~recyclerAdapter~>","orderrecyclerviewadapter");

    }



    @Override
    public OrdersRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.e("<~recyclerAdapter~>","oncreaterViewHolder");

        //create new view
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card,parent,false);

        //ViewHolder vh = new ViewHolder( view);
//        return vh;
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
//        Log.e("<~recyclerAdapter~>","onbind");
//get element from yoy dataset at this position
        //replace the contents of the view withthat element
//        holder.mTextView.setText(mDataset[position]);
Order order=  ordersList.get(position);
        holder.mOrderName.setText(order.getOrderName());
//        holder.mOrderId.setText(order.getOrderNo());
        holder.mOrderId.setVisibility(View.INVISIBLE);//todo changed
        holder.mOrderCost.setText("$"+order.getCost());
        holder.mOrderOriginStreet.setText(order.getOriginStreet());
holder.childID=order.getOrderNo();//"Nuevo childID";
    }

    @Override
    public int getItemCount() {
        Log.e("<~recyclerAdapter~>","getitemcount"+ordersList.size());

        return ordersList.size(); //size?
    }
// Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
    public TextView mTextView;

    private TextView mOrderName;
    private TextView mOrderId;
    private TextView mOrderCost;
    private TextView mOrderOriginStreet;

    public ViewHolder(View v) {
        super(v);
        Log.e("<~recyclerAdapter~>","viewholder");

        //link resources ids, to view
        mOrderName=(TextView)v.findViewById(R.id.order_name);
        mOrderId=(TextView)v.findViewById(R.id.order_id);
        mOrderCost=(TextView)v.findViewById(R.id.order_cost);
        mOrderOriginStreet=(TextView)v.findViewById(R.id.order_origin);
//todo, create a space for the id?

        v.setOnClickListener(this);
    }
String childID;
    @Override
    public void onClick(View view) {
        Log.e("~~onclick","view is:"+view.toString());
        Context context = itemView.getContext();
        Intent showOrderIntent = new Intent(context, ViewOrderActivity.class);
        showOrderIntent.putExtra(ORDER_KEY, mOrderId.getText());

        showOrderIntent.putExtra("orderUID", childID);
        //Log.d("User ref", childSnapshot.getRef().toString());//   D/User ref: https://tatt-5dc00.firebaseio.com/Ordenes/-KSZqD6W_kjmOPKwh3i8
Log.e("~~onclick","Encviaria el child ID guardado"+childID);
        context.startActivity(showOrderIntent);
    }

//    public ViewHolder(View view) {
//        super(view);
//
//    }
}
}
