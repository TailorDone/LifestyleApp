import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*

@Dao
interface DAO {
    @Query("SELECT * FROM User")
    fun getUsers(): User

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addUser(user: User)

//    @Query("DELETE FROM User")
//    suspend fun deleteUser(user: User)


}