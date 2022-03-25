package id.whynot.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.StringRes
import androidx.appcompat.app.ActionBar
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import id.whynot.githubuser.adapter.SectionPager
import id.whynot.githubuser.databinding.ActivityDetailUserBinding
import id.whynot.githubuser.viewmodel.ViewModel


class DetailUser : AppCompatActivity() {

    companion object{
        const val EXTRA_USER_DATA = "users"
        const val EXTRA_PERSON = "username"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.txt_followers,
            R.string.txt_following
        )
    }

    private lateinit var binding: ActivityDetailUserBinding
    private lateinit var viewModel: ViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val username = intent.getStringExtra(EXTRA_PERSON)

        viewModel = ViewModelProvider(this).get(ViewModel::class.java)

        username?.let { viewModel.setDetailUser(it) }
        showTabLayout()

        viewModel.getUserDetail().observe(this){
            binding.tvUsernameDetail.text = it.login
            binding.tvName.text = nullitem(it.name)
            binding.tvFollowingDetail.text = "Following\n ${ it.following.toString() }"
            binding.tvFollowerDetail.text = "Follower\n ${ it.followers.toString() }"

            binding.tvRespositoryDetail.text = "Repository \n ${ it.public_repos.toString() }"
            binding.tvCompany.text = nullitem(it.company)
            binding.tvBlog.text = nullitem(it.blog)
            Glide.with(this)
                .load(it.avatar_url)
                .centerCrop()
                .into(binding.ivItemAvatar)
        }

        if (supportActionBar != null) {
            (supportActionBar as ActionBar).title = "Detail User"
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    private fun nullitem(item: String): String {
       var text : String=""
        if (item == null){
            text = "-"
        }
        else if(item != null){
            text = item
        }
        return text
    }
    private fun showTabLayout(){
        val username = intent.getStringExtra(EXTRA_PERSON)
        val bundle = Bundle()
        bundle.putString(EXTRA_PERSON, username)
        val sectionsPagerAdapter = SectionPager(this, bundle)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabs
        TabLayoutMediator(tabs, viewPager){ tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()

        return true
    }


}

