package id.whynot.githubuser.api

import id.whynot.githubuser.model.Person
import id.whynot.githubuser.model.PersonResponse
import id.whynot.githubuser.model.User
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface API {

    @GET("users")
    @Headers("Authorization: token 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")
    fun getUser(): Call<List<User>>

    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUser(
        @Query("q")query: String
    ): Call<PersonResponse>

    @GET("users/{username}")
    @Headers("Authorization: token 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")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<Person>

    @GET("users/{username}/followers")
    @Headers("Authorization: token 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")
    fun getFollowersUser(
        @Path("username")username: String
    ): Call<List<User>>

    @GET("users/{username}/following")
    @Headers("Authorization: token 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")
    fun getFollowingUser(
        @Path("username")username: String
    ): Call<List<User>>


}