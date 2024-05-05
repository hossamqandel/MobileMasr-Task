package com.hossam.mobilemasrtask.common.data.remote

import com.hossam.mobilemasrtask.auth.data.dto.LoginDTO
import com.hossam.mobilemasrtask.auth.data.dto.TokenProvider
import com.hossam.mobilemasrtask.product.data.dto.ProductDTO
import com.hossam.mobilemasrtask.product.data.dto.ProductsDTO
import com.hossam.mobilemasrtask.util.ConstAPI
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {

    @POST(ConstAPI.POST_LOGIN)
    suspend fun login(
        @Body loginDTO: LoginDTO
    ): Response<TokenProvider>


    @GET(ConstAPI.GET_PRODUCTS_PAGINATED_V1)
    suspend fun getProductsPaginated(
        @Query("offset") offset: Int,
        @Query("limit") limit: Int
    ): Response<ProductsDTO>

    @GET(ConstAPI.GET_PRODUCT_DETAIL_V1)
    suspend fun getProductById(
        @Path("id") id: Int
    ): Response<ProductDTO>


}