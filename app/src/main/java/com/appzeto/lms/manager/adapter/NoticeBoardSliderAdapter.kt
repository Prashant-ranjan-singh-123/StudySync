package com.appzeto.lms.manager.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.appzeto.lms.databinding.ItemNoticeBoardBinding
import com.appzeto.lms.manager.Utils
import com.appzeto.lms.model.Notif

class NoticeBoardSliderAdapter(items: List<Notif>) :
    BaseArrayAdapter<Notif, NoticeBoardSliderAdapter.ViewHolder>(items) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemNoticeBoardBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        val binding = holder.binding

        binding.itemNoticeBoardTitleTv.text = item.title
        binding.itemNoticeBoardSenderTv.text = item.sender
        binding.itemNoticeBoardDateTv.text = Utils.getDateFromTimestamp(item.createdAt)
        binding.itemNoticeBoardMessageTv.text = Utils.getTextAsHtml(item.message)
    }

    inner class ViewHolder(val binding: ItemNoticeBoardBinding) :
        RecyclerView.ViewHolder(binding.root)
}