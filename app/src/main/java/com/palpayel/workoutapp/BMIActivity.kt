package com.palpayel.workoutapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_bmiactivity.*
import java.math.BigDecimal
import java.math.RoundingMode

class BMIActivity : AppCompatActivity() {
    companion object {
        private const val METRIC_UNITS_VIEW = "METRIC_UNIT_VIEW" // Metric Unit View
        private const val US_UNITS_VIEW = "US_UNIT_VIEW" // US Unit View
    }

    private var currentVisibleView: String = METRIC_UNITS_VIEW
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bmiactivity)

        setSupportActionBar(toolbar_bmi_activity)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title="CALCULATE BMI"

        toolbar_bmi_activity.setNavigationOnClickListener {
            onBackPressed()
        }
        makeVisibleMetricUnitsView()

        rgUnits.setOnCheckedChangeListener { radioGroup:RadioGroup, checkedId:Int ->

            if(checkedId==R.id.rbMetricUnits){
                makeVisibleMetricUnitsView()
            }else{
                makeVisibleUSUnitsView()
            }
        }

        btnCalculateUnits.setOnClickListener {

           if(currentVisibleView.equals(METRIC_UNITS_VIEW)){
               if(validateMetricUnits()){
                   val heightValue:Float =etMetricUnitHeight.text.toString().toFloat() / 100
                   val weightValue:Float=etMetricUnitWeight.text.toString().toFloat()

                   val bmi =weightValue/(heightValue*heightValue)
                   displayBMIResult(bmi)
               }else{
                   Toast.makeText(this,"Please enter valid values",Toast.LENGTH_LONG).show()
               }
           }else{
               if(validateUsUnits())
               {
                   val usUnitHeightValueFeet: String =etUsUnitHeightFeet.text.toString()
                   val usUnitHeightValueInch:String =etUsUnitHeightInch.text.toString()
                   val usUnitWeightValue:Float=etUsUnitWeight.text.toString().toFloat()

                   val heightValue=usUnitHeightValueInch.toFloat() + usUnitHeightValueFeet.toFloat()
                   val bmi =703*(usUnitWeightValue/(heightValue*heightValue))
                   displayBMIResult(bmi)
               }else{
                   Toast.makeText(this,"Please enter valid values",Toast.LENGTH_LONG).show()
               }

           }

        }

    }
    private fun makeVisibleMetricUnitsView(){
        currentVisibleView= METRIC_UNITS_VIEW
        llMetricUnitsView.visibility=View.VISIBLE
        llUsUnitsView.visibility = View.GONE

        etMetricUnitHeight.text!!.clear()
        etMetricUnitWeight.text!!.clear()

        tvYourBMI.visibility=View.INVISIBLE
        tvBMIValue.visibility=View.INVISIBLE
        tvBMIType.visibility=View.INVISIBLE
        tvBMIDescription.visibility=View.INVISIBLE
    }

    private fun makeVisibleUSUnitsView(){
        currentVisibleView= US_UNITS_VIEW
        llUsUnitsView.visibility=View.VISIBLE
        llMetricUnitsView.visibility = View.GONE // METRIC UNITS VIEW is hidden
        llUsUnitsView.visibility = View.VISIBLE // US UNITS VIEW is Visible

        etUsUnitWeight.text!!.clear() // weight value is cleared if it is added.
        etUsUnitHeightFeet.text!!.clear() // height feet value is cleared if it is added.
        etUsUnitHeightInch.text!!.clear() // height inch is cleared if it is added.


        tvYourBMI.visibility=View.INVISIBLE
        tvBMIValue.visibility=View.INVISIBLE
        tvBMIType.visibility=View.INVISIBLE
        tvBMIDescription.visibility=View.INVISIBLE
    }
    private fun validateUsUnits():Boolean{
        var isValid=true
        if(etUsUnitWeight.text.toString().isEmpty()){
            isValid=false
        }else if(etUsUnitHeightFeet.text.toString().isEmpty()){
            isValid=false
        }
        return isValid
    }

    private fun validateMetricUnits(): Boolean{
        var isValid=true
        if(etMetricUnitWeight.text.toString().isEmpty()){
            isValid=false
        }else if(etMetricUnitHeight.text.toString().isEmpty()){
            isValid=false
        }
        return isValid
    }
    private fun displayBMIResult(bmi:Float){
        val bmiLabel:String
        val bmiDescription: String

        if (java.lang.Float.compare(bmi, 15f) <= 0) {
            bmiLabel = "Very severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 15f) > 0 && java.lang.Float.compare(
                bmi,
                16f
            ) <= 0
        ) {
            bmiLabel = "Severely underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 16f) > 0 && java.lang.Float.compare(
                bmi,
                18.5f
            ) <= 0
        ) {
            bmiLabel = "Underweight"
            bmiDescription = "Oops! You really need to take care of your better! Eat more!"
        } else if (java.lang.Float.compare(bmi, 18.5f) > 0 && java.lang.Float.compare(
                bmi,
                25f
            ) <= 0
        ) {
            bmiLabel = "Normal"
            bmiDescription = "Congratulations! You are in a good shape!"
        } else if (java.lang.Float.compare(bmi, 25f) > 0 && java.lang.Float.compare(
                bmi,
                30f
            ) <= 0
        ) {
            bmiLabel = "Overweight"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 30f) > 0 && java.lang.Float.compare(
                bmi,
                35f
            ) <= 0
        ) {
            bmiLabel = "Obese Class | (Moderately obese)"
            bmiDescription = "Oops! You really need to take care of your yourself! Workout maybe!"
        } else if (java.lang.Float.compare(bmi, 35f) > 0 && java.lang.Float.compare(
                bmi,
                40f
            ) <= 0
        ) {
            bmiLabel = "Obese Class || (Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        } else {
            bmiLabel = "Obese Class ||| (Very Severely obese)"
            bmiDescription = "OMG! You are in a very dangerous condition! Act now!"
        }

        tvYourBMI.visibility = View.VISIBLE
        tvBMIValue.visibility = View.VISIBLE
        tvBMIType.visibility = View.VISIBLE
        tvBMIDescription.visibility = View.VISIBLE




        val bmiValue=BigDecimal(bmi.toDouble()).setScale(2,RoundingMode.HALF_EVEN).toString()

        tvYourBMI.text=bmiValue
        tvBMIType.text=bmiLabel
        tvBMIDescription.text=bmiDescription

    }



}