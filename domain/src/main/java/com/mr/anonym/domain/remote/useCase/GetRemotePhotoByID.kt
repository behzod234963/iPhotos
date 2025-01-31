package com.mr.anonym.domain.remote.useCase

import android.util.Log
import com.mr.anonym.domain.model.HitsItem
import com.mr.anonym.domain.remote.repository.RemotePhotosRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException

class GetRemotePhotoByID(private val repository: RemotePhotosRepository) {

    operator fun invoke(id:Int): Flow<List<HitsItem>> = flow {

        try {
            val response = repository.getPhotoById(id = id)
            if (response.isSuccessful){
                response.body()?.let { emit(it.hits) }
            }
        }catch (e:HttpException){
            Log.d("NetworkLogging", "GetPhotoByIDNetworkInvoke: ${e.message}")
        }catch (u:Exception){
            Log.d("UtilsLogging", "GetPhotoByIDUtilsInvoke: ${u.message}")
        }
    }
}