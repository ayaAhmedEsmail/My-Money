package com.example.mymoney

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.timepicker.MaterialTimePicker
import kotlinx.android.synthetic.main.activity_reminder.*
import java.text.SimpleDateFormat
import java.util.*

class ReminderActivity : AppCompatActivity() {

    private lateinit var picker:MaterialTimePicker
    private lateinit var alarmReceiver: AlarmReceiver
    private lateinit var alarmManager: AlarmManager
    private lateinit var pendingIntent: PendingIntent
    private lateinit var calendar:Calendar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reminder)
        calendar= Calendar.getInstance()
        createNotification()

        btn_EveningSwitchEvening.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableLayoutEvening()
                Log.i("test","Switch is on")
            } else {
                Log.i("test","Switch is off")
                unableLayoutEvening()

                txt_reminderTimeEvening.setTextColor(Color.LTGRAY)

            }
        }

        btn_MorningSwitch.setOnCheckedChangeListener { _, isChecked ->
           /* if (isChecked) {
                Log.i("test","Switch is on")
                enableLayoutMorning()
                txt_reminderTimeMorning.setTextColor(Color.GREEN)

            } else {
                Log.i("test","Switch is off")
                unableLayoutMorning()
                txt_reminderTimeMorning.setTextColor(Color.LTGRAY)

            }*/
            isChecked(isChecked)
            setAlarm()

        }


        txt_reminderTimeMorning.setOnClickListener{
            calendar= Calendar.getInstance()
            val timeSetListener =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR,hour)
              calendar.set(Calendar.MINUTE,minute)
                txt_reminderTimeMorning.text = SimpleDateFormat("h:mm a").format(calendar.time)
            }
            TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR),calendar.get(Calendar.HOUR_OF_DAY),false).show()


        }

        eveningReminderLayout.setOnClickListener{
            var calendar= Calendar.getInstance()
            val timeSetListener =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                calendar.set(Calendar.HOUR,hour)
                calendar.set(Calendar.MINUTE,minute)
                txt_reminderTimeMorning.text = SimpleDateFormat("h:mm a").format(calendar.time)
            }
            TimePickerDialog(this,timeSetListener,calendar.get(Calendar.HOUR),calendar.get(Calendar.HOUR_OF_DAY),false).show()
        }
    }

    private fun setAlarm() {
        alarmManager =getSystemService(ALARM_SERVICE)as AlarmManager
        val intent=Intent(this,AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.setInexactRepeating(
            AlarmManager.RTC_WAKEUP,calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,pendingIntent
        )
        Toast.makeText(this,"Alarm set successfully",Toast.LENGTH_LONG).show()
    }
    /* private fun showTimePicker(){
         val timeSetListener =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
         picker = MaterialTimePicker.Builder()
             .setHour(Calendar.HOUR)
             .setMinute(Calendar.MINUTE)
             .setTitleText("Select Time")
             .build()

             picker.show(supportFragmentManager,"My-Money")
             picker.addOnPositiveButtonClickListener{
                 if (picker.hour>12){
                     String.format("%02d",picker.hour -12 )+ " : "+ String.format("%02d", picker.minute) +"PM"
                 }
                 else{
                     String.format("%02d",picker.hour )+ " : "+ String.format("%02d", picker.minute) +"AM"

                 }
                 val calendar= Calendar.getInstance()
                 val timeSetListener =TimePickerDialog.OnTimeSetListener { timePicker, hour, minute ->
                     calendar.set(Calendar.HOUR,hour)
                     calendar.set(Calendar.MINUTE,minute)
                     txt_reminderTimeMorning.setText(SimpleDateFormat("h:mm a").format(calendar.time))
                 }


             }

             }

     }*/

    private fun createNotification() {
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.O){
            val name:CharSequence = "MyMoneyNotificationChannel"
            val description = "Channel for Alarm Manager"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("My-Money", name,importance)
            channel.description= description
            val notificationManager=getSystemService(
                NotificationManager::class .java
            )
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun isChecked(isChecked:Boolean =false){

        if (isChecked) {
            Log.i("test","Switch is on")
            enableLayoutMorning()
            setAlarm()
            txt_reminderTimeMorning.setTextColor(Color.GREEN)

        } else {
            Log.i("test","Switch is off")
            unableLayoutMorning()
            cancelAlarm()
            txt_reminderTimeMorning.setTextColor(Color.LTGRAY)

        }
    }

    private fun cancelAlarm() {
        alarmManager =getSystemService(ALARM_SERVICE)as AlarmManager
        val intent=Intent(this,AlarmReceiver::class.java)

        pendingIntent = PendingIntent.getBroadcast(this,0,intent,0)
        alarmManager.cancel(pendingIntent)
        Toast.makeText(this,"Alarm is canceled",Toast.LENGTH_LONG).show()

    }


    private fun enableLayoutMorning(){
        morningReminderLayout.isClickable = true
        txt_reminderTimeMorning.isClickable = true
        txt_reminderMeAtMorning.isClickable=true
        txt_reminderTimeMorning.setTextColor(Color.LTGRAY)
    }

    private  fun unableLayoutMorning(){
        morningReminderLayout.isClickable = false
        txt_reminderTimeMorning.isClickable = false
        txt_reminderMeAtMorning.isClickable= false
        txt_reminderTimeMorning.setTextColor(Color.GREEN)
    }
    private fun enableLayoutEvening(){
        eveningReminderLayout.isClickable = true
        txt_reminderTimeEvening.isClickable = true
        txt_reminderMeAtEvening.isClickable= true
        txt_reminderTimeEvening.setTextColor(Color.GREEN)
    }

    private  fun unableLayoutEvening(){
        eveningReminderLayout.isClickable = false
        txt_reminderTimeEvening.isClickable = false
        txt_reminderMeAtEvening.isClickable=false
        txt_reminderTimeEvening.setTextColor(Color.LTGRAY)

    }
}
