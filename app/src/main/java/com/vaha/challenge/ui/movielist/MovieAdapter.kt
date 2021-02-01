import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.FitCenter
import com.vaha.challenge.R
import com.vaha.challenge.model.movie.MovieCache
import com.vaha.challenge.network.SyncService.Companion.ID
import com.vaha.challenge.network.SyncService.Companion.posterUrl
import com.vaha.challenge.ui.GraphicUtil
import com.vaha.challenge.ui.GraphicUtil.getRecommendedPosterWidth
import com.vaha.challenge.ui.moviedetail.MovieDetail
import com.vaha.challenge.ui.movielist.MainActivity
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*


class MovieAdapter internal constructor(val itemList: ArrayList<Movie>) :
    RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {

    @NonNull
    override fun onCreateViewHolder(@NonNull parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(@NonNull viewHolder: MovieViewHolder, position: Int) {
        viewHolder.bind(itemList[position])
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    class MovieViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val width = getRecommendedPosterWidth(MainActivity.width / 3)

        fun bind(movie: Movie) {
            itemView.poster.layoutParams.width = width
            itemView.title.text = movie.title
            itemView.rating.text = "${movie.vote_average}"
            if (movie.poster_path != null) {
                Glide.with(itemView.context)
                    .load(posterUrl + width + movie.poster_path)
                    .transform(FitCenter())
                    .placeholder(R.drawable.ic_movie_white_48dp)
                    .error(R.drawable.ic_broken_image_white_48dp)
                    .fallback(R.drawable.ic_movie_white_48dp)
                    .into(itemView.poster)
            }
            itemView.setOnClickListener {
                MovieCache.cache(movie)
                val intent = Intent(itemView.context, MovieDetail::class.java)
                intent.putExtra(ID, movie.id)
                itemView.context.startActivity(intent)
                (itemView.context as Activity).overridePendingTransition(
                    R.anim.slide_from_right,
                    R.anim.slide_to_left
                )
            }
        }

    }
}