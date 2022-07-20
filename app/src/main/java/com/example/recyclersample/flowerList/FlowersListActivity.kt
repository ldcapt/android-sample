/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.recyclersample.flowerList

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.activity.viewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.recyclersample.addFlower.AddFlowerActivity
import com.example.recyclersample.flowerDetail.FlowerDetailActivity
import com.example.recyclersample.R
import com.example.recyclersample.addFlower.FLOWER_DESCRIPTION
import com.example.recyclersample.addFlower.FLOWER_NAME
import com.example.recyclersample.app.MyApplication
import com.example.recyclersample.data.Flower
import com.example.recyclersample.data.Song
import com.example.recyclersample.fragment.MainFragment
import com.example.recyclersample.service.MediaService

const val FLOWER_ID = "flower id"

class FlowersListActivity : AppCompatActivity() {
    private var playButton: Button? = null
    private var isPlay: Boolean = false
    private var intentTemp: Intent? = null
    private val newFlowerActivityRequestCode = 1
    private val flowersListViewModel by viewModels<FlowersListViewModel> {
        FlowersListViewModelFactory(this)
    }

    companion object {
        const val CHANNEL_ID: String = "channel_service"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        playButton = findViewById<View>(R.id.btn_play) as Button
        playButton!!.setOnClickListener {
            playMusic()
        }
        val fab: View = findViewById(R.id.fab)
        val btnFragment = findViewById<View>(R.id.btn_fragment) as Button
        val headerAdapter = HeaderAdapter()
        val flowersAdapter = FlowersAdapter { flower -> adapterOnClick(flower) }
        val concatAdapter = ConcatAdapter(headerAdapter, flowersAdapter)
        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = concatAdapter

        flowersListViewModel.flowersLiveData.observe(this) {
            it?.let {
                flowersAdapter.submitList(it as MutableList<Flower>)
                headerAdapter.updateFlowerCount(it.size)
            }
        }
        fab.setOnClickListener {
            fabOnClick()
        }
        btnFragment.setOnClickListener {
            fragOnClick()
        }

    }

    private fun fragOnClick() {
        startActivity(Intent(this, MainFragment()::class.java))
    }

    /* Opens FlowerDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(flower: Flower) {
        val intent = Intent(this, FlowerDetailActivity()::class.java)
        intent.putExtra(FLOWER_ID, flower.id)
        startActivity(intent)
    }

    /* Adds flower to flowerList when FAB is clicked. */
    private fun fabOnClick() {
        val intent = Intent(this, AddFlowerActivity::class.java)
        startActivityForResult(intent, newFlowerActivityRequestCode)
    }

    private fun playMusic() {
        if (isPlay) {
            isPlay = false
            playButton?.setBackgroundResource(R.drawable.ic_vector_play)
//            if (intentTemp != null) {
//                stopService(intentTemp)
//            }

            stopService(Intent(this, MediaService::class.java))
            Log.d("ServiceMedia", "Stop service")
        } else {
            isPlay = true
            playButton?.setBackgroundResource(R.drawable.ic_vector_pause)
//            val song = Song(
//                "Radio mùa ngâu",
//                R.drawable.ic_music,
//                "Halmet Trương Radio",
//                R.raw.radio_mua_ngau
//            )
//            val bundle = Bundle()
//            bundle.putSerializable("object song", song)
//            intentTemp = Intent(this, MediaService::class.java)
//            intentTemp?.putExtras(bundle)
//            startService(intentTemp)

            startService(Intent(this, MediaService::class.java))
            Log.d("ServiceMedia", "Start service")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts flower into viewModel. */
        if (requestCode == newFlowerActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val flowerName = data.getStringExtra(FLOWER_NAME)
                val flowerDescription = data.getStringExtra(FLOWER_DESCRIPTION)

                flowersListViewModel.insertFlower(flowerName, flowerDescription)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, MediaService::class.java))
    }
}