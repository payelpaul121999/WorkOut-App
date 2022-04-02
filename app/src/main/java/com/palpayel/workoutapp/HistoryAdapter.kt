package com.palpayel.workoutapp

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_history_row.view.*

class HistoryAdapter(val context: Context, val items:ArrayList<String>)
    : RecyclerView.Adapter<HistoryAdapter.ViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_history_row,parent,false))
    }

    override fun onBindViewHolder(holder: HistoryAdapter.ViewHolder, position: Int) {
       val date:String =items.get(position)
        holder.tvPosition.text=(position+1).toString()
        holder.tvItem.text=date
        if(position % 2==0){
            holder.llHistoryItemMain.setBackgroundColor(Color.parseColor("#EBEBEB"))
        }else{
            holder.llHistoryItemMain.setBackgroundColor(Color.parseColor("#FFFFFF"))
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
    class ViewHolder(view : View):RecyclerView.ViewHolder(view) {
        val llHistoryItemMain=view.ll_history_item_main!!
        val tvItem=view.tvItem!!
        val tvPosition = view.tvPosition!!
    }
}