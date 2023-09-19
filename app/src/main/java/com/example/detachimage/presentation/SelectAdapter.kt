package com.example.detachimage.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.detachimage.R
import com.example.detachimage.data.model.Item
import com.example.detachimage.databinding.SelectItemBinding
import getAppDrawable

class SelectAdapter : RecyclerView.Adapter<SelectAdapter.SelectVH>() {

    companion object {
        private const val UPDATE_STATE_PAYLOAD = "UPDATE_STATE_PAYLOAD"
    }

    private var dataList: MutableList<Item> = arrayListOf()

    var listener: ISelectListener? = null

    init {
        for (i in 0 until 5) {
            val isSelect = i == 0
            dataList.add(Item(index = i, isSelect = isSelect))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectVH {
        return SelectVH(
            SelectItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount() = dataList.size

    override fun onBindViewHolder(holder: SelectVH, position: Int) {
        holder.onBind(data = dataList[position])
    }

    override fun onBindViewHolder(holder: SelectVH, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position)
        } else {
            holder.onBind(data = dataList[position], payloads)
        }
    }

    inner class SelectVH(private val binding: SelectItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.tvSelect.setOnClickListener {
                val oldIndex = dataList.indexOfFirst {
                    it.isSelect == true
                }
                if (oldIndex >= 0) {
                    dataList[oldIndex].isSelect = false
                    notifyItemChanged(oldIndex, UPDATE_STATE_PAYLOAD)
                }
                dataList[absoluteAdapterPosition].isSelect = true
                listener?.onSelect(dataList[absoluteAdapterPosition].index!!)
                notifyItemChanged(absoluteAdapterPosition, UPDATE_STATE_PAYLOAD)
            }
        }

        fun onBind(data: Item) {
            binding.tvSelect.text = (data.index!! + 1).toString()
            checkStateSelect(data)
        }

        fun onBind(data: Item, payloads: MutableList<Any>) {
            payloads.forEach {
                when (it) {
                    UPDATE_STATE_PAYLOAD -> checkStateSelect(data)
                }
            }
        }

        private fun checkStateSelect(data: Item) {
            if (data.isSelect == true) {
                binding.tvSelect.background = getAppDrawable(R.drawable.oval_bg_yellow)
            } else {
                binding.tvSelect.background = null
            }
        }
    }

    interface ISelectListener {
        fun onSelect(index: Int)
    }
}
