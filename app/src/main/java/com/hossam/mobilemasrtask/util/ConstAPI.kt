package com.hossam.mobilemasrtask.util

object ConstAPI {

    const val BASE_URL_V1 = "http://api.escuelajs.co/api/v1/"

    //Auth
    const val POST_LOGIN = "auth/login"

    const val GET_PRODUCTS_V1 = "products"
    const val GET_PRODUCT_DETAIL_V1 = "products/{id}" //Add @Path-Variable there
    const val POST_CREATE_PRODUCT_V1 = "products"
    const val PUT_UPDATE_PRODUCT_V1 = "products" //Add @Path-Variable there
    const val DELETE_PRODUCT_V1 = "products" //Add @Path-Variable there
    const val GET_PRODUCTS_PAGINATED_V1 = "products" //Add 2 @QueryParams there (products?offset=0&limit=10)

}