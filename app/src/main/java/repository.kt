import androidx.annotation.WorkerThread
import androidx.lifecycle.MutableLiveData

// Declares the DAO as a private property in the constructor. Pass in the DAO
// instead of the whole database, because you only need access to the DAO
class repository (private val dao: DAO){

    val allUsers: List<User> = dao.getUsers()

    @WorkerThread
    suspend fun insert(user: User){
        dao.addUser(user)
    }
}