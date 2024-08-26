package com.example.tennis.ui.calendar

import CalendarViewModel
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.room.Dao
import androidx.room.Database
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.prolificinteractive.materialcalendarview.*
import java.util.*
import com.example.tennis.R
import org.threeten.bp.LocalDate

class CalendarFragment : Fragment(), OnDateSelectedListener {

    private val viewModel: CalendarViewModel by viewModels()
    private lateinit var calendarView: MaterialCalendarView
    private lateinit var tvReservationInfo: TextView
    private lateinit var btnAddReservation: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_calendar, container, false)
        calendarView = view.findViewById(R.id.calendarView)
        tvReservationInfo = view.findViewById(R.id.tvReserveDay) //여기 이상하게 수정하긴 했는데 작동이 되긴함
        btnAddReservation = view.findViewById(R.id.btnAddReservation)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        calendarView.setOnDateChangedListener(this)

        btnAddReservation.setOnClickListener {
            showDatePickerDialog()
        }

        viewModel.reservations.observe(viewLifecycleOwner) {
            updateCalendarView()
        }
        viewModel.getAllReservationsForDebug()
        updateCalendarView()
    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val selectedDate = CalendarDay.from(year, month + 1, day)
                showReservationDialog(selectedDate)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun showReservationDialog(date: CalendarDay) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_reservation, null)
        val timePicker = dialogView.findViewById<TimePicker>(R.id.timePicker)
        val etCourtNumber = dialogView.findViewById<EditText>(R.id.etCourtNumber)
        val radioGroup = dialogView.findViewById<RadioGroup>(R.id.radioGroup)
        val etMemo = dialogView.findViewById<EditText>(R.id.etMemo)

        AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("확인") { _, _ ->
                val hour = timePicker.hour
                val minute = timePicker.minute
                val courtNumber = etCourtNumber.text.toString()
                val isReservationDay = radioGroup.checkedRadioButtonId == R.id.rbReservationDate
                val memo = etMemo.text.toString()

                val reservationInfo = ReservationInfo(
                    date = LocalDate.of(date.year, date.month, date.day),
                    hour = hour,
                    minute = minute,
                    courtNumber = courtNumber,
                    isReservationDay = isReservationDay,
                    memo = memo
                )
                viewModel.addReservation(reservationInfo)

                updateCalendarView()
            }
            .setNegativeButton("취소", null)
            .show()
    }

    private fun showSavedReservationDialog(reservationInfo: ReservationInfo) {
        val dialogView = layoutInflater.inflate(R.layout.dialog_show_reservation, null)
        val tvTime = dialogView.findViewById<TextView>(R.id.tvShowReservationTime)
        val tvCourtNumber = dialogView.findViewById<TextView>(R.id.tvShowReservationCourtNumber)
        val tvType = dialogView.findViewById<TextView>(R.id.tvShowReservationType)
        val tvMemo = dialogView.findViewById<TextView>(R.id.tvShowReservationMemo)
        val btnClose = dialogView.findViewById<Button>(R.id.btnClose)

        // 예약 정보 설정
        tvTime.text = "시간: ${reservationInfo.hour}:${String.format("%02d", reservationInfo.minute)}"
        tvCourtNumber.text = "코트 번호: ${reservationInfo.courtNumber}"
        tvType.text = "예약 유형: ${if (reservationInfo.isReservationDay) "예약일" else "예매일"}"
        tvMemo.text = "메모: ${reservationInfo.memo}"

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .create()

        btnClose.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

    private fun updateCalendarView() {
        calendarView.removeDecorators()

        viewModel.getAllReservations().forEach { (date, info) ->
            calendarView.addDecorator(EventDecorator(
                if (info.isReservationDay) Color.BLUE else Color.RED,
                listOf(date)
            ))
        }
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        Log.d("CalendarFragment", "onDateSelected: $date")
        viewModel.getReservation(date)?.let { reservationInfo ->
            Log.d("CalendarFragment", "Reservation found: $reservationInfo")
            showSavedReservationDialog(reservationInfo)
        } ?: run {
            Log.d("CalendarFragment", "No reservation found, showing new reservation dialog")
            showReservationDialog(date)
        }
    }
}

@Entity(tableName = "reservations")
data class ReservationInfo(
    @PrimaryKey val date: LocalDate,
    val hour: Int,
    val minute: Int,
    val courtNumber: String,
    val isReservationDay: Boolean,
    val memo: String
)

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations")
    fun getAllReservations(): List<ReservationInfo>

    @Query("SELECT * FROM reservations WHERE date = :date")
    fun getReservation(date: LocalDate): ReservationInfo?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(reservation: ReservationInfo)

    @Delete
    fun delete(reservation: ReservationInfo)

    @Query("SELECT * FROM reservations")
    fun getAllReservationsForDebug(): List<ReservationInfo>
}

@Database(entities = [ReservationInfo::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reservationDao(): ReservationDao

    companion object {
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "reservation_database"
                ).build()
            }
            return instance!!
        }
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }
}