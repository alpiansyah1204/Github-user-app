package id.whynot.githubuser.model
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Person (
    val id : Int = 0,
    val login: String = "",
    val name: String = "",
    val company: String = "",
    val location: String = "",
    val bio : String = "",
    val html_url: String = "",
    val blog: String ="",
    val avatar_url: String = "",
    val public_repos: Int = 0,
    val followers: Int = 0,
    val following: Int = 0
): Parcelable