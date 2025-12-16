package ru.practicum.sprint_11_koh_33

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.practicum.sprint_11_koh_31.R
import java.text.DateFormat

class NewsAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var items: List<NewsItem> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            SCIENCE_VIEW_TYPE -> SinceItemViewHolder(parent)
            SPORT_VIEW_TYPE -> SportItemViewHolder(parent)
            DEFAULT_VIEW_TYPE -> DefaultItemViewHolder(parent)
            else -> throw IllegalArgumentException("Unknown type")
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = items[position]) {
            is NewsItem.Science -> (holder as SinceItemViewHolder).bind(item)
            is NewsItem.Sport -> (holder as SportItemViewHolder).bind(item)
            is NewsItem.Default -> (holder as DefaultItemViewHolder).bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(items[position]) {
            is NewsItem.Science -> SCIENCE_VIEW_TYPE
            is NewsItem.Sport -> SPORT_VIEW_TYPE
            is NewsItem.Default -> DEFAULT_VIEW_TYPE
        }
    }

    companion object {
        private const val SPORT_VIEW_TYPE =0
        private const val SCIENCE_VIEW_TYPE =1
        private const val DEFAULT_VIEW_TYPE =2
    }
}


class SinceItemViewHolder(
    parentView: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.since_news_item, parentView, false)
) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val created: TextView = itemView.findViewById(R.id.created)
    private val scienceField: ImageView = itemView.findViewById(R.id.science_img)

    fun bind(item: NewsItem.Science) {
        title.text = item.title
        created.text =
            DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT
            ).format(item.created)

        Glide.with(itemView)
            .load(item.specificPropertyForScience)
            .into(scienceField)
    }

}

class DefaultItemViewHolder(
    parentView: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.since_news_item, parentView, false)
) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val created: TextView = itemView.findViewById(R.id.created)
    private val scienceField: ImageView = itemView.findViewById(R.id.science_img)

    fun bind(item: NewsItem.Default) {
        title.text = item.title
        created.text =
            DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT
            ).format(item.created)
        scienceField.visibility = View.GONE
    }

}

class SportItemViewHolder(
    parentView: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.sport_news_item, parentView, false)
) {

    private val title: TextView = itemView.findViewById(R.id.title)
    private val created: TextView = itemView.findViewById(R.id.created)
    private val sportField: TextView = itemView.findViewById(R.id.sport_teams)

    fun bind(item: NewsItem.Sport) {
        title.text = item.title
        created.text =
            DateFormat.getDateTimeInstance(
                DateFormat.SHORT,
                DateFormat.SHORT
            ).format(item.created)

        sportField.text = item.specificPropertyForSport
    }

}