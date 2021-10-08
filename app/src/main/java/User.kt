import android.graphics.Bitmap
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) var id : Int,
    var name : String?,
    var zip : String?,
    var age: Int?,
    var sex: String?,
    var height: Double?,
    var weight: Double?,
    var profilePicturePath: String?,
)