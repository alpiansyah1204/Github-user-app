package id.whynot.githubuser.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import id.whynot.githubuser.databinding.CardItemBinding
import id.whynot.githubuser.model.User

class ListAdapter : RecyclerView.Adapter<ListAdapter.ListViewHolder>() {

    private val listUser = ArrayList<User>()

    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClick (onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }

    fun setData(user: List<User>){
        listUser.clear()
        listUser.addAll(user)
        notifyDataSetChanged()
        Log.d("TAG", "cek $listUser")
    }

    inner class ListViewHolder(private val binding: CardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        internal fun bind(user: User){
            binding.root.setOnClickListener { onItemClickCallback?.onItemClicked(user) }
            binding.tvUsername.text = user.login
            Glide.with(itemView.context)
                .load(user.avatar_url)
                .centerCrop()
                .apply(RequestOptions().override(60,60))
                .into(binding.ivItemAvatar)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val view = CardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listUser[position]
        holder.bind(user)
    }

    override fun getItemCount(): Int = listUser.size

    interface OnItemClickCallback{
        fun onItemClicked(data: User)
    }
}
