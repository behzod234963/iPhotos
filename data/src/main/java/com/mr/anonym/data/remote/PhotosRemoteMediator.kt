package com.mr.anonym.data.remote

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import com.mr.anonym.domain.model.HitsItem
import okio.IOException
import retrofit2.HttpException

@OptIn(ExperimentalPagingApi::class)
class PhotosRemoteMediator(
    private val api: PhotosApi
) :RemoteMediator<Int,HitsItem>(){
    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, HitsItem>
    ): MediatorResult {

        return try {
            val loadKey = when(loadType){
                LoadType.REFRESH -> 1
                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null){
                        1
                    }else{
                        (lastItem.id?.div(state.config.pageSize))?.plus(1)
                    }
                }
            }
            val photos = loadKey?.let {
                api.getPhotos(
                    page = it,
                    perPage = 10
                )
            }
            Log.d("NetworkLogging", "load: ${photos?.body()?.hits?.size}")
            val response = photos?.body()?.hits?: emptyList()
            MediatorResult.Success(endOfPaginationReached = response.isEmpty())
        }catch (e:IOException){
            Log.d("NetworkLogging", "load: ${e.message}")
            MediatorResult.Error(e)
        }catch (h:HttpException){
            Log.d("NetworkLogging", "load: ${h.message}")
            MediatorResult.Error(h)
        }
    }
}