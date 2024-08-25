package com.example.tennis.ui.calendar

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.prolificinteractive.materialcalendarview.*
import java.util.*
import com.example.tennis.R

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

                val reservationInfo = ReservationInfo(hour, minute, courtNumber, isReservationDay, memo)
                viewModel.addReservation(date, reservationInfo)

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
        tvTime.text = "시간: ${reservationInfo.hour}:${reservationInfo.minute}"
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

        val reservationDays = viewModel.getAllReservations().filter { it.value.isReservationDay }.keys
        val bookingDays = viewModel.getAllReservations().filter { !it.value.isReservationDay }.keys

        calendarView.addDecorator(EventDecorator(Color.BLUE, reservationDays))
        calendarView.addDecorator(EventDecorator(Color.RED, bookingDays))
    }

    override fun onDateSelected(widget: MaterialCalendarView, date: CalendarDay, selected: Boolean) {
        val reservationInfo = viewModel.getReservation(date)
        if (reservationInfo != null) {
            // 예약 정보가 있을 때는 저장된 예약 정보를 보여주는 다이얼로그를 띄움
            showSavedReservationDialog(reservationInfo)
        } else {
            // 예약 정보가 없을 때는 새 예약을 추가하는 다이얼로그를 띄움
            showReservationDialog(date)
        }
    }
}

data class ReservationInfo(
    val hour: Int,
    val minute: Int,
    val courtNumber: String,
    val isReservationDay: Boolean,
    val memo: String
)
