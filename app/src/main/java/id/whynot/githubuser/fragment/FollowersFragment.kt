package id.whynot.githubuser.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import id.whynot.githubuser.DetailUser
import id.whynot.githubuser.R
import id.whynot.githubuser.adapter.ListAdapter
import id.whynot.githubuser.databinding.FragmentFollowersBinding
import id.whynot.githubuser.model.User

import id.whynot.githubuser.viewmodel.ViewModel

class FollowersFragment : Fragment(R.layout.fragment_followers) {

    private var _binding: FragmentFollowersBinding? = null
    private val binding get() = _binding
    private lateinit var userViewModel : ViewModel
    private lateinit var followerAdapter: ListAdapter
    private lateinit var username: String

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFollowersBinding.bind(view)

        username = arguments?.getString(DetailUser.EXTRA_PERSON).toString()

        followerAdapter = ListAdapter()
        followerAdapter.setOnItemClick(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@FollowersFragment.context, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_PERSON, data.login)
                    startActivity(it)
                }
            }
        })

        Log.d("TAG", "cek $username")

        binding?.apply {
            rvList.layoutManager = LinearLayoutManager(requireContext())
            rvList.adapter = followerAdapter
            rvList.setHasFixedSize(true)
        }

        showLoading(true)
        userViewModel = ViewModelProvider(this).get(
            ViewModel::class.java)

        userViewModel.setDetailFollower(username)
        userViewModel.getDetailFollower().observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(false)
                Log.d("TAG", "cek api $it")
                followerAdapter.setData(it)
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            binding?.progressCircular?.visibility = View.VISIBLE
        } else {
            binding?.progressCircular?.visibility = View.GONE
        }
    }



}