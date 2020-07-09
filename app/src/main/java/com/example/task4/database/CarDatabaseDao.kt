package com.example.task4.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

/**
 * Defines methods for using the Car class with Room.
 */
@Dao
interface CarDatabaseDao {

    @Insert
    fun insert(car: Car)

    @Update
    fun update(car: Car)

    /**
     * Selects and returns row in the table by id
     */
    @Query("SELECT * FROM car_table WHERE carId = :id")
    fun get(id: Long): Car?

    /**
     * Selects and returns all rows in the table,
     * sorted by id in descending order.
     */
    @Query("SELECT * FROM car_table ORDER BY carId")
    fun getAllCarsSortedById(): LiveData<List<Car>>

    /**
     * Selects and returns all rows in the table,
     * sorted by brand in descending order.
     */
    @Query("SELECT * FROM car_table ORDER BY brand")
    fun getAllCarsSortedByBrand(): LiveData<List<Car>>

    /**
     * Selects and returns all rows in the table,
     * sorted by model in descending order.
     */
    @Query("SELECT * FROM car_table ORDER BY model")
    fun getAllCarsSortedByModel(): LiveData<List<Car>>

    /**
     * Selects and returns all rows in the table,
     * sorted by year in descending order.
     */
    @Query("SELECT * FROM car_table ORDER BY year")
    fun getAllCarsSortedByYear(): LiveData<List<Car>>

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM car_table")
    fun clear()

    /**
     * Deletes the value by id.
     */
    @Query("DELETE FROM car_table WHERE carId = :id")
    fun delete(id: Long)
}