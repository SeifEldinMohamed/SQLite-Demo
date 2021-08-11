package com.seif.sqlite

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.seif.sqlite.databinding.ItemRowBinding
import com.seif.sqlite.models.EmpModelClass


class ItemAdapter(private val context: Context, private val items: ArrayList<EmpModelClass>) :
    RecyclerView.Adapter<ItemAdapter.ViewHolder>() {
    /**
     * Inflates the item views which is designed in the XML layout file
     *
     * create a new
     * {@link ViewHolder} and initializes some private fields to be used by RecyclerView.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(context),parent,false)
        return ViewHolder(binding)

    }

    /**
     * Binds each item in the ArrayList to a view
     *
     * Called when RecyclerView needs a new {@link ViewHolder} of the given type to represent
     * an item.
     *
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.tvName.text = item.name
        holder.tvEmail.text = item.email

        // Updating the background color according to the odd/even positions in list.
        if (position % 2 == 0) {
            holder.llMain.setBackgroundColor(
                ContextCompat.getColor(
                    context,
                    R.color.light_gray
                )
            )
        } else {
            holder.llMain.setBackgroundColor(ContextCompat.getColor(context, R.color.white))
        }

        holder.ivEdit.setOnClickListener { view ->
            if (context is MainActivity) {
                context.updateRecordDialog(item)
            }
        }
        holder.ivDelete.setOnClickListener { view ->
            if (context is MainActivity) {
                context.deleteRecordAlertDialog(item)
            }
        }

    }

    /**
     * Gets the number of items in the list
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * A ViewHolder describes an item view and metadata about its place within the RecyclerView.
     */

    class ViewHolder(private val binding:ItemRowBinding) : RecyclerView.ViewHolder(binding.root) {
        // Holds the TextView that will add each item to
        val llMain: LinearLayout = binding.llMain
        var tvName: TextView = binding.tvName
        var tvEmail: TextView = binding.tvEmail
        val ivEdit = binding.ivEdit
        val ivDelete = binding.ivDelete
    }
}