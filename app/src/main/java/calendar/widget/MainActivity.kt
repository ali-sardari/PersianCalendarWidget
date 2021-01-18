package calendar.widget

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import calendar.widget.databinding.ActivityMainBinding
import calendar.widget.utils.PersianCalendar
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.button.setOnClickListener {
            val pCal = PersianCalendar()

            val calendar: Calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
                set(Calendar.YEAR,2021)
                set(Calendar.MONTH,1)
                set(Calendar.DATE,17)
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 1)
                set(Calendar.MILLISECOND, 0)
            }

            Log.w("DateTime","")

//            pCal= calendar as PersianCalendar

            pCal.set(
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH)-1,
                calendar.get(Calendar.DATE),
                calendar.get(Calendar.HOUR),
                calendar.get(Calendar.MINUTE),
                calendar.get(Calendar.SECOND),
            )

            binding.tvText.text = pCal.persianLongDateAndTime
        }
    }
}