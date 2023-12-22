package com.ppb.travellin.admin

import android.content.Context
import android.preference.Preference
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ppb.travellin.databinding.ItemUserBinding
import com.ppb.travellin.services.ApplicationPreferencesManager
import com.ppb.travellin.services.api.FireAuth
import com.ppb.travellin.services.model.User
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class UsersManagerAdapter(
    private val scope: CoroutineScope
) : ListAdapter<User, UsersManagerAdapter.ItemUserViewHolder>(UserDiffUtil()) {
    inner class ItemUserViewHolder(
        private val binding : ItemUserBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            with(binding) {
                textViewUsernameItem.text = user.username

                buttonRole.text = user.role.removePrefix("Role_").lowercase()
                buttonRole.setOnClickListener {
                    scope.launch {
                        if (user.id == ApplicationPreferencesManager(binding.root.context).usernameId) {
                            Toast.makeText(binding.root.context, "Tidak bisa mengubah role sendiri", Toast.LENGTH_SHORT).show()
                            return@launch
                        }
                        val newRole = FireAuth().updateUserRole(user.id, user.role)
                        user.role = newRole
                        buttonRole.text = newRole.removePrefix("Role_").lowercase()
                    }
                }
                buttonDeleteUser.setOnClickListener {
                    scope.launch {
                        val result = FireAuth().deleteUser(user.id)
                        if (result) {
                            val position = currentList.indexOf(user)
                            val updatedList = currentList.toMutableList()
                            updatedList.removeAt(position)
                            submitList(updatedList)
                            Toast.makeText(binding.root.context, "User ${user.username} berhasil dihapus", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(binding.root.context, "User ${user.username} gagal dihapus", Toast.LENGTH_SHORT).show()
                        }

                    }
                }



            }
        }

    }

    class UserDiffUtil : DiffUtil.ItemCallback<User>() {
        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }
        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemUserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )

        return ItemUserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}