package com.sutrix.uatest.database

import androidx.room.*
import com.sutrix.uatest.model.models.Burger

@Dao
interface BurgerDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertBurger(burger: Burger)

    @Query("SELECT * FROM burger_table ")
    fun getAllBurger(): List<Burger>
}