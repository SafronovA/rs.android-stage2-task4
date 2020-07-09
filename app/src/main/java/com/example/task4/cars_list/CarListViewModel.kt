package com.example.task4.cars_list

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.task4.database.Car
import com.example.task4.database.CarDatabaseDao
import kotlinx.coroutines.*

/**
 * ViewModel for CarListFragment.
 */
class CarListViewModel(
    val database: CarDatabaseDao,
    application: Application
) : AndroidViewModel(application) {

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    fun getCarsList(orderPreference: String?): LiveData<List<Car>> {
        return when (orderPreference) {
            SortBy.BRAND.order -> database.getAllCarsSortedByBrand()
            SortBy.MODEL.order -> database.getAllCarsSortedByModel()
            SortBy.YEAR.order -> database.getAllCarsSortedByYear()
            else -> database.getAllCarsSortedById()
        }
    }


    /// Create new car card
    private val _navigateToCreateCarCard = MutableLiveData<Boolean>()
    val navigateToCreateCarCard
        get() = _navigateToCreateCarCard

    fun onCreateCarCardClicked() {
        _navigateToCreateCarCard.value = true
    }

    fun onCreateCarCardNavigated() {
        _navigateToCreateCarCard.value = false
    }

    // Navigate To Clear DB
    private val _clearAll = MutableLiveData<Boolean>()
    val clearAll
        get() = _clearAll

    fun onClearAllClicked() {
        _clearAll.value = true
    }

    fun onClearAllProcessed() {
        _clearAll.value = false
    }

    // Navigate To Settings
    private val _navigateToSettings = MutableLiveData<Boolean>()
    val navigateToSettings
        get() = _navigateToSettings

    fun onSettingsClicked() {
        _navigateToSettings.value = true
    }

    fun onSettingsNavigated() {
        _navigateToSettings.value = false
    }

    // Navigate To Update car card
    private val _navigateToUpdateCar = MutableLiveData<Long>()
    val navigateToUpdateCar
        get() = _navigateToUpdateCar

    fun onUpdateCarClicked(carId: Long) {
        _navigateToUpdateCar.value = carId
    }

    fun onUpdateCarNavigated() {
        _navigateToUpdateCar.value = null
    }

    fun deleteCarById(id: Long) {
        uiScope.launch {
            delete(id)
        }
    }

    fun clearDataBase() {
        uiScope.launch {
            clear()
        }
    }

    private suspend fun update(car: Car) {
        withContext(Dispatchers.IO) {
            database.update(car)
        }
    }

    private suspend fun delete(carId: Long) {
        withContext(Dispatchers.IO) {
            database.delete(carId)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}