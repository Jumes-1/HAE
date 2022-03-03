package com.james.hae

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.james.hae.databinding.ActivityMainBinding
import kotlinx.coroutines.flow.collect

class MainActivity : AppCompatActivity() {

	private lateinit var binding: ActivityMainBinding

	private val mBatInfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
		override fun onReceive(context: Context?, intent: Intent) {
			val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
			val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
			val batteryPercentage = level * 100 / scale.toFloat()
			binding.battery.progress = batteryPercentage.toInt()
		}
	}

	private val weatherAdapter = WeatherAdapter()

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)

		val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

		binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
		binding.lifecycleOwner = this
		binding.vm = viewModel

		registerReceiver(mBatInfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

		binding.weatherRecyclerView.apply {
			layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
			LinearSnapHelper().attachToRecyclerView(this)
			adapter = weatherAdapter
		}

		lifecycleScope.launchWhenCreated {
			viewModel.cities.collect { weather ->
				weatherAdapter.submitList(weather)
			}
		}

		binding.appsButton.setOnClickListener {
		}
	}

}