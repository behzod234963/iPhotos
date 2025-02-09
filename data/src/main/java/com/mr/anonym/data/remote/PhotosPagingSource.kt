package com.mr.anonym.data.remote

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.mr.anonym.domain.model.HitsItem

class PhotosPagingSource(
    private val api: PhotosApi,
    private val q:String,
    private val order:String
) : PagingSource<Int, HitsItem>() {
    override fun getRefreshKey(state: PagingState<Int, HitsItem>): Int? = state.anchorPosition?.let {
        state.closestPageToPosition(it)?.prevKey?.plus(1)?:state.closestPageToPosition(it)?.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HitsItem> {

        return try {
            val perPage = 10
            val page = params.key ?: 1
            val response = api.getPhotos(
                q = q,
                order = order,
                page = page,
                perPage = perPage
            )
            val nexKey =
                if (!response.isSuccessful) null else response.body()?.hits?.size?.plus(page)
                    ?.plus(1)
            val prevKey = if (page == 1) null else response.body()?.hits?.size?.minus(1)

            LoadResult.Page(
                data = response.body()?.hits ?: emptyList(),
                nextKey = nexKey,
                prevKey = prevKey
            )
        } catch (e: Exception) {
            Log.d("NetworkLogging", "load: ${e.message}")
            LoadResult.Error(e)
        }
    }
}