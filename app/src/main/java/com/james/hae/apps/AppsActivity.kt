package com.james.hae.apps

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.james.hae.R
import com.james.hae.databinding.ActivityAppsBinding


class AppsActivity : AppCompatActivity(), AppLauncher {

	private val appAdapter = AppAdapter(this)

	private lateinit var binding: ActivityAppsBinding

	@SuppressLint("QueryPermissionsNeeded")
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		binding = DataBindingUtil.setContentView(this, R.layout.activity_apps)
		binding.lifecycleOwner = this

		binding.appsRecyclerView.apply {
			layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
			adapter = appAdapter
		}

		val intent = Intent(Intent.ACTION_MAIN, null).apply {
			addCategory(Intent.CATEGORY_LAUNCHER)
		}

		packageManager
			.queryIntentActivities(intent, 0)
			.map { resolveInfo ->
				AppInfo(
					resolveInfo.loadLabel(packageManager).toString(),
					resolveInfo.activityInfo.packageName,
					resolveInfo.activityInfo.loadIcon(packageManager),
				)
			}
			.let { appAdapter.submitList(it) }
	}

	override fun launchPackage(packageName: String) {
		packageManager
			.getLaunchIntentForPackage(packageName)
			?.let { startActivity(it) }
	}

}