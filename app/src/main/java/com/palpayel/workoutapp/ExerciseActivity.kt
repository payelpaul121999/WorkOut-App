package com.palpayel.workoutapp

import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.dialog_custom_back_confirmation.*
import java.util.*
import kotlin.collections.ArrayList

class ExerciseActivity : AppCompatActivity(),TextToSpeech.OnInitListener {
    private var restTimer : CountDownTimer?=null
    private var restProgress = 0
    private var exerciseTimer:CountDownTimer?=null
    private var exerciseProgress = 0
    private var exerciseList:ArrayList<ExerciseModel>?=null
    private var currentExercisePostion=-1
    private var tts:TextToSpeech? = null
    private var player:MediaPlayer? = null
    private var exerciseAdapter:ExerciseStatusAdapter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        setSupportActionBar(toolbar_exercise_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toolbar_exercise_activity.setNavigationOnClickListener {
            customDialogForBackButton()
        }
        tts = TextToSpeech(this, this)
        exerciseList = Constant.defaultExerciseList()
        setupRestView()


        setupExerciseStatusRecyclerView()
    }

    public override fun onDestroy() {
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        // TODO (Step 3 - When the activity is destroyed if the media player instance is not null then stop it.)
        // START
        if(player != null){
            player!!.stop()
        }
        // END

        super.onDestroy()
    }

    override fun onInit(status: Int) {

        if (status == TextToSpeech.SUCCESS) {
            val result = tts!!.setLanguage(Locale.US)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The Language specified is not supported!")
            }

        } else {
            Log.e("TTS", "Initialization Failed!")
        }
    }

    private fun setupRestView() {
        try {
            val soundURI =
                Uri.parse("android.resource://com.palpayel.workoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player!!.isLooping = false // Sets the player to be looping or non-looping.
            player!!.start() // Starts Playback.
        } catch (e: Exception) {
            e.printStackTrace()
        }

        llRestView.visibility = View.VISIBLE
        llExerciseView.visibility = View.GONE
        if (restTimer != null) {
            restTimer!!.cancel()
            restProgress = 0
        }

        tvUpcomingExerciseName.text = exerciseList!![currentExercisePostion + 1].getName()
        setRestProgressBar()
    }

    private fun setRestProgressBar() {
        progressBar.progress = restProgress
        restTimer = object : CountDownTimer(10000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                restProgress++
                progressBar.progress = 10 - restProgress
                tvTimer.text =
                    (10 - restProgress).toString()
            }
            override fun onFinish() {
                currentExercisePostion++
                exerciseAdapter!!.notifyDataSetChanged()
                exerciseList!![currentExercisePostion].setIsSelected(true)
                setupExerciseView()
            }
        }.start()
    }

    private fun setupExerciseView() {
        llRestView.visibility = View.GONE
        llExerciseView.visibility = View.VISIBLE
        if (exerciseTimer != null) {
            exerciseTimer!!.cancel()
            exerciseProgress = 0
        }
        ivaImage.setImageResource(exerciseList!![currentExercisePostion].getImage())
        tvExerciseName.text = exerciseList!![currentExercisePostion].getName()
        speakOut(exerciseList!![currentExercisePostion].getName())
        setExerciseProgressBar()
    }

    private fun setExerciseProgressBar() {
        progressBarExercise.progress = exerciseProgress
        exerciseTimer = object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                exerciseProgress++
                progressBarExercise.progress = 30 - exerciseProgress
                tvExerciseTimer.text = (30 - exerciseProgress).toString()
            }
            override fun onFinish() {
                exerciseList!![currentExercisePostion].setIsSelected(false) // exercise is completed so selection is set to false
                exerciseList!![currentExercisePostion].setIsCompleted(true) // updating in the list that this exercise is completed
                exerciseAdapter!!.notifyDataSetChanged() // Notifying to adapter class.

                if (currentExercisePostion < 11) {
                    setupRestView()
                } else {
                    val intent = Intent(this@ExerciseActivity,FinishActivity::class.java)
                    startActivity(intent)
                    finish()

                    Toast.makeText(
                        this@ExerciseActivity,
                        "Congratulations! You have completed the 7 minutes workout.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }.start()
    }


    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }

    private fun setupExerciseStatusRecyclerView(){
        rvExerciseStatus.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        exerciseAdapter= ExerciseStatusAdapter(exerciseList!!,this)
        rvExerciseStatus.adapter=exerciseAdapter
    }
    private fun customDialogForBackButton() {
        val customDialog = Dialog(this)
        customDialog.setContentView(R.layout.dialog_custom_back_confirmation)
        customDialog.tvYes.setOnClickListener {
            finish()
            customDialog.dismiss() // Dialog will be dismissed
        }
        customDialog.tvNo.setOnClickListener {
            customDialog.dismiss()
        }
        //Start the dialog and display it on screen.
        customDialog.show()
    }
}