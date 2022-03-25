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
import id.whynot.githubuser.databinding.FragmentFollowingsBinding
import id.whynot.githubuser.model.User
import id.whynot.githubuser.viewmodel.ViewModel

class FollowingsFragment : Fragment(R.layout.fragment_followings) {
    private var _binding: FragmentFollowingsBinding? = null
    private val binding get() = _binding
    private lateinit var userViewModel : ViewModel
    private lateinit var followingAdapter: ListAdapter
    private lateinit var username: String

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = FragmentFollowingsBinding.bind(view)

        username = arguments?.getString(DetailUser.EXTRA_PERSON).toString()

        followingAdapter = ListAdapter()
        followingAdapter.setOnItemClick(object : ListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                Intent(this@FollowingsFragment.context, DetailUser::class.java).also {
                    it.putExtra(DetailUser.EXTRA_PERSON, data.login)
                    startActivity(it)
                }
            }
        })
        Log.d("TAG", "cek $username")

        binding?.apply {
            rvList.layoutManager = LinearLayoutManager(requireContext())
            rvList.adapter = followingAdapter
            rvList.setHasFixedSize(true)
        }

        showLoading(true)
        userViewModel = ViewModelProvider(this).get(ViewModel::class.java)
        userViewModel.setDetailFollowing(username)
        userViewModel.getDetailFollowing().observe(viewLifecycleOwner) {
            if (it != null) {
                showLoading(false)
                Log.d("TAG", "cek api $it")
                binding?.rvList?.visibility = View.VISIBLE
                followingAdapter.setData(it)
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