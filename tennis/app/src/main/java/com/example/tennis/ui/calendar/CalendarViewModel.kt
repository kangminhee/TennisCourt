import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.tennis.ui.calendar.AppDatabase
import com.example.tennis.ui.calendar.ReservationInfo
import com.prolificinteractive.materialcalendarview.CalendarDay
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.threeten.bp.LocalDate

class CalendarViewModel(application: Application) : AndroidViewModel(application) {
    private val database = AppDatabase.getInstance(application)
    private val reservationDao = database.reservationDao()

    private val _reservations = MutableLiveData<List<ReservationInfo>>()
    val reservations: LiveData<List<ReservationInfo>> = _reservations

    init {
        viewModelScope.launch(Dispatchers.IO) {
            _reservations.postValue(reservationDao.getAllReservations())
        }
    }

    fun addReservation(reservationInfo: ReservationInfo) {
        viewModelScope.launch(Dispatchers.IO) {
            reservationDao.insert(reservationInfo)
            _reservations.postValue(reservationDao.getAllReservations())
        }
    }

    fun getReservation(date: CalendarDay): ReservationInfo? {
        return runBlocking(Dispatchers.IO) {
            val result = reservationDao.getReservation(date.toLocalDate())
            Log.d("CalendarViewModel", "getReservation for $date: $result")
            result
        }
    }

    fun getAllReservations(): Map<CalendarDay, ReservationInfo> {
        return runBlocking(Dispatchers.IO) {
            reservationDao.getAllReservations().associate {
                CalendarDay.from(it.date) to it
            }
        }
    }

    fun getAllReservationsForDebug(): List<ReservationInfo> {
        return runBlocking(Dispatchers.IO) {
            val result = reservationDao.getAllReservationsForDebug()
            Log.d("CalendarViewModel", "All reservations: $result")
            result
        }
    }

    // CalendarDay를 LocalDate로 변환하는 확장 함수
    private fun CalendarDay.toLocalDate(): LocalDate {
        return LocalDate.of(year, month, day)
    }

    // LocalDate를 CalendarDay로 변환하는 확장 함수
    private fun LocalDate.toCalendarDay(): CalendarDay {
        return CalendarDay.from(this.year, this.monthValue, this.dayOfMonth)
    }

}