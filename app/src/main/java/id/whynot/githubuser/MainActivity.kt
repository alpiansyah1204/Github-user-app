package id.whynot.githubuser



import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatDelegate

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.whynot.githubuser.adapter.ListAdapter
import id.whynot.githubuser.darkmode.SettingPreferences
import id.whynot.githubuser.darkmode.ViewModelDarkMode
import id.whynot.githubuser.darkmode.ViewModelFactoryDarkMode
import id.whynot.githubuser.databinding.ActivityMainBinding
import id.whynot.githubuser.model.User
import id.whynot.githubuser.viewmodel.ViewModel


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapterList: ListAdapter
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapterList = ListAdapter()
        adapterList.setOnItemClick(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@MainActivity, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_PERSON, data.login)
                    startActivity(it)
                }
            }
        })


        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ViewModel::class.java
        )

        binding.apply {
            rvList.layoutManager = LinearLayoutManager(this@MainActivity)
            rvList.adapter = adapterList
            rvList.setHasFixedSize(true)

            searchView.setOnQueryTextListener(object :
                androidx.appcompat.widget.SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    if (query != null && query.isNotEmpty()) {
                        searchUser()
                    }
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    return false
                }
            })

            viewModel.getSearchUser().observe(this@MainActivity) {
                if (it != null) {
                    showLoading(false)
                    adapterList.setData(it)
                    binding.rvList.visibility = View.VISIBLE
                }
            }
        }

        viewModel.setUser()
        viewModel.getSearchUser().observe(this){
            if (it != null) {
                showLoading(false)
                Log.d("TAG", "cek api $it")
                adapterList.setData(it)
            }
        }

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelFactoryDarkMode(pref)).get(
            ViewModelDarkMode::class.java
        )
        mainViewModel.getThemeSettings().observe(this,
            { isDarkModeActive: Boolean ->
                if (isDarkModeActive) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

                }
            })

    }

    private fun searchUser() {
        binding.apply {
            val query = searchView.query.toString()
            if (query.isEmpty()) return
            binding.rvList.visibility = View.GONE
            showLoading(true)
            viewModel.setSearchUser(query)

        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding.progressCircular.visibility = View.VISIBLE
        } else {
            binding.progressCircular.visibility = View.GONE
        }
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val moveIntent = Intent(this@MainActivity, DarkModeSetting::class.java)
        startActivity(moveIntent)
        return super.onOptionsItemSelected(item)
    }

}