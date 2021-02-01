import com.github.kittinunf.fuel.core.ResponseDeserializable
import com.google.gson.Gson

data class Movie(
    var adult: Boolean,
    var backdrop_path: String?,
    var genre_ids: List<Int>,
    var id: Int,
    var original_language: String,
    var original_title: String,
    var overview: String,
    var popularity: Double,
    var poster_path: String?,
    var release_date: String,
    var title: String,
    var video: Boolean,
    var vote_average: Double,
    var vote_count: Int
)

data class MovieSearchResult(
    var page: Int,
    var results: List<Movie>,
    var total_pages: Int,
    var total_results: Int
) {
    class Deserializer : ResponseDeserializable<MovieSearchResult> {
        override fun deserialize(content: String): MovieSearchResult? =
            Gson().fromJson(content, MovieSearchResult::class.java)
    }
}
