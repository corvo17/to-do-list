package com.corvo.demo.base

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.corvo.demo.R
import kotlinx.android.synthetic.main.item_simple_list.view.*

class BaseSimpleListAdapter(private val context: Context, val list: List<String>) : RecyclerView.Adapter<BaseSimpleListAdapter.PaymentListViewHolder>() {

    var clicked : ((Int) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_simple_list,parent,false)
        return PaymentListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PaymentListViewHolder, position: Int) {


        val item  =list[position]



        holder.paymentTitle?.text = item.capitalize()

            holder.itemView.setOnClickListener {
                clicked?.invoke(holder.adapterPosition)
            }
    }

    class PaymentListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var paymentTitle = itemView.title
    }
}