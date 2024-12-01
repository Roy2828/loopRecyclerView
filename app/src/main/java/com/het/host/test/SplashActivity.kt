package com.het.host.test.loop_pager

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.het.attendance.test.R
import com.het.host.test.*
import com.het.host.test.recycler.RecyclerGalleryActivity

class SplashActivity : Activity() {
    private val TAG = "SplashActivity"
    private lateinit var rv: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        rv = findViewById<RecyclerView>(R.id.rv)
        val pre = findViewById<Button>(R.id.pre)
        val next = findViewById<Button>(R.id.next)
        val next2 = findViewById<Button>(R.id.next2)
        val am = findViewById<Button>(R.id.am)
        val tp = findViewById<Button>(R.id.tp)
        val pr = findViewById<GradientRoundProgress>(R.id.pr)
        pr.setMaxProgress(100)

        var layoutManager = InfiniteLayoutManager(this)
        layoutManager.setChangeListener {
            Log.e("TTTTT","change position:"+it)
        }
        rv.layoutManager = layoutManager

        var list = mutableListOf<Int>()
        for (item in Constants.IMG_ARRAY_2) {
            list.add(item)
        }
        var adapter = PaictureAdapter(this, list)
        adapter.setClickListener { position, obj ->
            Log.e("TTTTT","position:"+position)
            Log.e("TTTTT","obj:"+obj)
        }
        rv.adapter = adapter


        pre.setOnClickListener {
            layoutManager.scrollPreItem()
        }
        next.setOnClickListener {
            layoutManager.scrollNextItem()
        }
        tp.setOnClickListener {
            pr.setProgress(28f)
        }
        am.setOnClickListener {
            pr.startToProgress(82f)
        }

        val radiusBar = findViewById<SeekBar>(R.id.radiusBar)
        radiusBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var r = DisplayUtil.dp2px(i*1f)*1f
                pr.setCornerRadius(r)
                val tv = findViewById<TextView>(R.id.tv_radiu)
                tv.text = "角度:" + r
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val timeBar = findViewById<SeekBar>(R.id.timeBar)
        timeBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var r = i*1f
                pr.setDuration(r)
                val tv = findViewById<TextView>(R.id.tv_time)
                tv.text = "时间:" + r
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        val freqBar = findViewById<SeekBar>(R.id.freqBar)
        freqBar.setOnSeekBarChangeListener(object : OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                var r = i
                pr.setFreqment(r)
                val tv = findViewById<TextView>(R.id.tv_freq)
                tv.text = "帧率:" + r
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })


        next2.setOnClickListener {
            startActivity(Intent(this@SplashActivity,RecyclerGalleryActivity::class.java))
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        rv.removeOnPageChangeListener(pageListener)
    }

    private var toast: Toast? = null

//    private val pageListener = object : OnPageChangeListener {
//        override fun onPageSelected(pos: Int) {
//            Log.d(TAG, "selected page: $pos")
//            toast?.cancel()
//            toast = Toast.makeText(this@SplashActivity, "selected --> $pos", Toast.LENGTH_LONG)
//            toast?.show()
//        }
//
//        override fun onPageScrollState(state: Int) {
////            Log.d(TAG, "page scroll state: $state")
//        }
//    }
}