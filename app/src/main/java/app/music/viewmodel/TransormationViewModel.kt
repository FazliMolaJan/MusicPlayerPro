package app.music.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import app.music.utils.TestLambda

class TransformationViewModel : ViewModel() {
//    private val userLiveData = MutableLiveData<TestLambda.User>()
//    val userAddedData: LiveData<String> = Transformations.map(userLiveData, ::someFunc)
//    private fun someFunc(user: TestLambda.User) = "New user ${user.username} added to database!"
//    fun addNewUser(user: TestLambda.User) = apply { userLiveData.value = user }
}