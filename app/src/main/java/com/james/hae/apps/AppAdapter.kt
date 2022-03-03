package com.james.hae.apps

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.james.hae.databinding.SingleAppBinding

data class AppInfo(
	val label: String,
	val packageName: String,
	val icon: Drawable,
)

interface AppLauncher {
	fun launchPackage(packageName: String)
}

class AppAdapter(private val launcher: AppLauncher) : ListAdapter<AppInfo, AppAdapter.ViewHolder>(
	DIFF_CALLBACK
) {

	companion object {
		val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppInfo>() {
			override fun areItemsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
				return oldItem.packageName == newItem.packageName
			}

			override fun areContentsTheSame(oldItem: AppInfo, newItem: AppInfo): Boolean {
				return oldItem.hashCode() == newItem.hashCode()
			}
		}
	}

	class ViewHolder(
		private val binding: SingleAppBinding
	) : RecyclerView.ViewHolder(binding.root) {
		fun bind(weather: AppInfo, launchPackage: (String) -> Unit) {
			binding.item = weather
			binding.root.setOnClickListener {
				launchPackage(weather.packageName)
			}
			binding.executePendingBindings()
		}
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		holder.bind(getItem(position)) {
			launcher.launchPackage(it)
		}
	}

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
		val layoutInflater = LayoutInflater.from(parent.context)
		return SingleAppBinding
			.inflate(layoutInflater, parent, false)
			.let { ViewHolder(it) }
	}

}