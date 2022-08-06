package com.hani.relyon.test

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.hani.relyon.databinding.ActivityTestBinding
import com.hani.relyon.view.RecordProgressView
import com.hani.relyon.view.TimeRangeView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

/**
 * @author : Create by DS-F
 * @date : 2022/8/3
 * description: 临时测试
 **/
class TestActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTestBinding
    private var mRecordJob: Job? = null
    private var mIsRecording = false
    private var path = "/storage/emulated/0/DCIM/GimiGimi/中等质量.mp4"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.mProgressView.setListener(object : RecordProgressView.ActionListener{
            override fun onStartRecord() {
                if (mRecordJob?.isActive == true){
                    mRecordJob?.cancel()
                }
                countTime()
            }

            override fun onStopRecord() {
                binding.mProgressView.mIsRecording = false
            }
        } )
        binding.mSliderBarView.setMinSelectRatio(0.1f)
        binding.mSliderBarView.setListener(object : TimeRangeView.SeekListener{
            override fun onSelectRangeChange(
                startProgress: Float,
                endProgress: Float,
                isLeft: Boolean
            ) {

            }

            override fun onSeekBarChange(process: Float) {

            }

            override fun onReachMin() {
                Toast.makeText(this@TestActivity,"视频不可小于1秒",Toast.LENGTH_SHORT).show()
            }
        } )
    }

    private fun countTime(){
        mRecordJob = lifecycleScope.launch {
            flow {
                for (i in 0..100 step 1) {
                    emit(i)
                    if (i != 0) delay(100)
                }
            }.flowOn(Dispatchers.Main.immediate)
                .onStart {
                    mIsRecording = true
                    binding.mProgressView.mIsRecording = true
                }
                .onCompletion { cause ->
                    if (cause == null) {
                        Log.w("lala","error: $cause")
                        stopRecord()
                    } else {
                        stopRecord()
                    }
                    binding.mProgressView.mIsRecording = false
                }
                .onEach {
                    binding.mProgressView.setProgress( it / 100f)
                }
                .launchIn(this)
        }
    }

    private fun stopRecord() {
        mIsRecording = false
    }

}