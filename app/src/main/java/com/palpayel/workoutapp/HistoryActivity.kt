package com.palpayel.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.palpayel.workoutapp.Database.SqliteOpenHelper
import kotlinx.android.synthetic.main.activity_history.*

class HistoryActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_history)

        setSupportActionBar(toolbar_history_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true) //set back button
        supportActionBar?.title = "HISTORY" // Setting an title in the action bar.
        toolbar_history_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        getAllCompletedDates()
    }

    private fun getAllCompletedDates() {
        val dbHandler=SqliteOpenHelper(this,null)
        val allCompletDatesList=dbHandler.getAllCompletedDatesList()
        if(allCompletDatesList.size>0){
            tvHistory.visibility= View.VISIBLE
            rvHistory.visibility=View.VISIBLE
            tvNoDataAvailable.visibility=View.GONE
            rvHistory.layoutManager=LinearLayoutManager(this)
            val historyAdapter=HistoryAdapter(this,allCompletDatesList)
            rvHistory.adapter=historyAdapter
        }else{
            tvHistory.visibility= View.GONE
            rvHistory.visibility=View.GONE
            tvNoDataAvailable.visibility=View.VISIBLE
        }
    }
}