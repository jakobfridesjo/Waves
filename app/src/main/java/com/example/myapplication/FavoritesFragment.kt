package com.example.myapplication

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.FragmentFavoritesBinding

/**
 * A simple [Fragment] subclass.
 * Use the [MapFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class FavoritesFragment : Fragment() {

    private val supportFragmentManager = getFragmentManager()
    private var _binding: FragmentFavoritesBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        val recyclerView = binding.favoritesList
        recyclerView.layoutManager = GridLayoutManager(context, 2)
        val data = ArrayList<ItemViewModel>()
        for (i in 1..20) {
            data.add(ItemViewModel(R.drawable.ic_no_image_light, "Channel$i"))
        }
        val adapter = FavoritesAdapter(data)
        recyclerView.adapter = adapter

        // Set listener for short clicks
        adapter.setOnClickListener(object: FavoritesAdapter.OnClickListener {
            override fun onClick(position: Int, item: ItemViewModel) {

                val text = "Not implemented: Should probably play the channel \"${item.text}\""
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()

            }
        })

        // Set listener for long click
        adapter.setOnLongClickListener(object: FavoritesAdapter.OnLongClickListener {
            override fun onLongClick(position: Int, item: ItemViewModel) {

                val text = "Not implemented: Should open a dialog fragment \"${item.text}\""
                val duration = Toast.LENGTH_SHORT
                val toast = Toast.makeText(context, text, duration)
                toast.show()
                
                val newFragment = RemoveFavoriteDialogFragment()
                supportFragmentManager?.let { newFragment.show(it, "test") }
            }
        })
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

class FavoritesAdapter(private val mList: List<ItemViewModel>) : RecyclerView.Adapter<FavoritesAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null
    private var onLongClickListener: OnLongClickListener? = null
    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_channel_item, parent, false)

        return ViewHolder(view)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val item = mList[position]
        val itemViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.imageView.setImageResource(itemViewModel.image)

        // sets the text to the textview from our itemHolder class
        holder.textView.text = itemViewModel.text

        holder.itemView.setOnClickListener {
            if (onClickListener != null) {
                onClickListener!!.onClick(position, item)
            }
        }

        holder.itemView.setOnLongClickListener {
            if (onLongClickListener != null) {
                onLongClickListener!!.onLongClick(position, item)
            }
            true
        }
    }

    // A function to bind the onclickListener.
    fun setOnClickListener(onClickListener: OnClickListener) {
        this.onClickListener = onClickListener
    }

    // A function to bind the onLongClickListener.
    fun setOnLongClickListener(onLongClickListener: OnLongClickListener) {
        this.onLongClickListener = onLongClickListener
    }

    // onClickListener Interface
    interface OnClickListener {
        fun onClick(position: Int, item: ItemViewModel)
    }

    // onLongClickListener Interface
    interface OnLongClickListener {
        fun onLongClick(position: Int, item: ItemViewModel)
    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        val imageView: ImageView = itemView.findViewById(R.id.channelImage)
        val textView: TextView = itemView.findViewById(R.id.channelText)
    }
 }

data class ItemViewModel(val image: Int, val text: String) {
}