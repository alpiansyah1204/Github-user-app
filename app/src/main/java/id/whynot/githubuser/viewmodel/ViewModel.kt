package id.whynot.githubuser.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import id.whynot.githubuser.api.Retrofit
import id.whynot.githubuser.model.Person
import id.whynot.githubuser.model.PersonResponse
import id.whynot.githubuser.model.User
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ViewModel : ViewModel() {
    companion object {
        const val RESPONSE = "response code"
        const val FAIL = "Failure"
    }

    val listUsers = MutableLiveData<List<User>>()
    val followingUsers = MutableLiveData<List<User>>()
    val followersUser = MutableLiveData<List<User>>()
    val detailUsers = MutableLiveData<Person>()


    fun setUser() {
        Retrofit.API_INSTANCE
            .getUser()
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    listUsers.postValue(response.body())
                    Log.d(RESPONSE, response.code().toString())
                    Log.d("TAG", "onResponse dong: ${response.body()} ")
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })
    }
    fun getUser(): LiveData<List<User>> {
        return listUsers
    }

    fun setSearchUser(query: String) {
        Retrofit.API_INSTANCE
            .getSearchUser(query)
            .enqueue(object : Callback<PersonResponse> {
                override fun onResponse(
                    call: Call<PersonResponse>,
                    response: Response<PersonResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items)
                        Log.d(RESPONSE, response.code().toString())
                    }
                }

                override fun onFailure(call: Call<PersonResponse>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })
    }

    fun getSearchUser(): LiveData<List<User>> {
        return listUsers
    }


    fun setDetailUser(username: String) {
        Retrofit.API_INSTANCE
            .getDetailUser(username)
            .enqueue(object : Callback<Person> {
                override fun onResponse(call: Call<Person>, response: Response<Person>) {
                    if (response.isSuccessful) {
                        detailUsers.postValue(response.body())

                    }
                }

                override fun onFailure(call: Call<Person>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })
    }

    fun getUserDetail(): LiveData<Person> {
        return detailUsers
    }


    fun setDetailFollowing(username: String) {
        Retrofit.API_INSTANCE
            .getFollowingUser(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    followingUsers.postValue(response.body())
                    Log.d(RESPONSE, response.code().toString())
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })
    }

    fun getDetailFollowing(): LiveData<List<User>> {
        return followingUsers
    }
    fun getMainDetailFollowing(): LiveData<List<User>> {

        return followingUsers
    }

    fun setDetailFollower(username: String) {
        Retrofit.API_INSTANCE
            .getFollowersUser(username)
            .enqueue(object : Callback<List<User>> {
                override fun onResponse(call: Call<List<User>>, response: Response<List<User>>) {
                    followersUser.postValue(response.body())
                    Log.d(RESPONSE, response.code().toString())
                }

                override fun onFailure(call: Call<List<User>>, t: Throwable) {
                    t.message?.let { Log.d(FAIL, it) }
                }
            })
    }

    fun getDetailFollower(): LiveData<List<User>> {
        return followersUser
    }

}