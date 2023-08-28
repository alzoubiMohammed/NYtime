package com.deloitte.mostpopular.ui.home.dashboard.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.deloitte.mostpopular.data.model.News
import com.deloitte.mostpopular.databinding.ItemNewsBinding
import com.deloitte.mostpopular.ui.util.extension.getTimeAgo
import com.deloitte.mostpopular.ui.util.extension.parseDateString
import com.deloitte.mostpopular.ui.util.extension.setOnSingleClickListener

class NewsAdapter(
    private val requestManager: RequestManager,
    private val onClick: (News) -> Unit
) :
    ListAdapter<News, NewsAdapter.NewsViewHolder>(COMPAEATOR) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        return NewsViewHolder(
            requestManager,
            ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false), onClick
        )

    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        holder.bind( currentList[position])
    }




    inner class NewsViewHolder(
        private val requestManager: RequestManager,
        private val binding: ItemNewsBinding,
        private val onClick: (News) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var item: News? = null
            private set

        fun bind(news: News) {
            this.item = news
            binding.run {
                requestManager.load(news.imageUrl).centerCrop().into(newsImage)
                newsTitle.text = news.title
                newsData.text = news.date.parseDateString()?.getTimeAgo(root.context) ?: ""

            }

        }

        init {
            binding.run {
                root.setOnSingleClickListener {
                    item?.let { onClick.invoke(it) }
                }
            }
        }
    }

    companion object {
        private val COMPAEATOR = object : DiffUtil.ItemCallback<News>() {

            override fun areItemsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem.id == newItem.id
            }


            override fun areContentsTheSame(oldItem: News, newItem: News): Boolean {
                return oldItem == newItem
            }

        }

    }
}