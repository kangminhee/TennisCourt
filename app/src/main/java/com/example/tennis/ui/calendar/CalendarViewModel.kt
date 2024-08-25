package com.example.tennis.ui.calendar

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay

class CalendarViewModel : ViewModel() {

    // 예약 정보를 저장하는 MutableLiveData
    private val _reservations = MutableLiveData<MutableMap<CalendarDay, ReservationInfo>>()
    val reservations: LiveData<MutableMap<CalendarDay, ReservationInfo>> get() = _reservations

    init {
        // 초기화 시 빈 예약 정보 맵 생성
        _reservations.value = mutableMapOf()
    }

    // 특정 날짜에 예약 추가
    fun addReservation(date: CalendarDay, reservationInfo: ReservationInfo) {
        val currentReservations = _reservations.value ?: mutableMapOf()
        currentReservations[date] = reservationInfo
        _reservations.value = currentReservations
    }

    // 특정 날짜의 예약 정보 반환
    fun getReservation(date: CalendarDay): ReservationInfo? {
        return _reservations.value?.get(date)
    }

    // 모든 예약 정보 반환
    fun getAllReservations(): Map<CalendarDay, ReservationInfo> {
        return _reservations.value ?: emptyMap()
    }
}
