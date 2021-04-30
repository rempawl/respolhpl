package com.example.respolhpl.data.sources.repository.paging

import androidx.paging.PagingState
import com.example.respolhpl.data.product.domain.ProductMinimal
import com.example.respolhpl.data.product.remote.RemoteProductMinimal
import com.example.respolhpl.data.sources.remote.RemoteDataSource
import com.example.respolhpl.utils.mappers.ListMapper
import com.squareup.moshi.JsonDataException
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject


class ProductPagingSourceImpl @Inject constructor(
    private val api: RemoteDataSource,
    private val mapper: ListMapper<RemoteProductMinimal, ProductMinimal>
) : ProductPagingSource() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductMinimal> {
        val position = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = api.getProducts(params.loadSize, position)

            val products = mapper.map(response)

            val nextKey = calculateNextKey(products, position, params.loadSize)

            LoadResult.Page(data = products, prevKey = getPrevKey(position), nextKey = nextKey)
        } catch (exception: IOException) {
            return LoadResult.Error(exception)
        } catch (exception: HttpException) {
            return LoadResult.Error(exception)
        } catch (e: JsonDataException) {
            return LoadResult.Error(e)
        }

    }

    override fun getRefreshKey(state: PagingState<Int, ProductMinimal>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    private fun getPrevKey(position: Int) =
        if (position == STARTING_PAGE_INDEX) null else position - 1

    private fun calculateNextKey(
        products: List<ProductMinimal>,
        position: Int,
        loadSize: Int
    ): Int? = if (products.isEmpty()) {
        null
    } else {
        position + (loadSize / NETWORK_PAGE_SIZE)
    }


    companion object {

        const val STARTING_PAGE_INDEX = 1
        const val NETWORK_PAGE_SIZE = 15
    }

}
