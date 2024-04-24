package com.team.data.network.di

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.BuildConfig
import com.team.data.datastore.DataStoreManager
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.network.retrofit.TokenAuthenticator
import com.team.data.network.retrofit.TokenInterceptor
import com.team.data.network.retrofit.api.CategoryNetworkApi
import com.team.data.network.retrofit.api.PostNetworkApi
import com.team.data.network.retrofit.api.UserNetworkApi
import com.team.data.network.source.AccountNetworkDataSource
import com.team.data.network.source.AccountNetworkDataSourceImpl
import com.team.data.network.source.CategoryNetworkDataSource
import com.team.data.network.source.CategoryNetworkDataSourceImpl
import com.team.data.network.source.PostNetworkDataSource
import com.team.data.network.source.PostNetworkDataSourceImpl
import com.team.data.network.source.UserNetworkDataSource
import com.team.data.network.source.UserNetworkDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    // TODO OkHttpClient 에 Timeout 추가 하기

    /** Interceptor Module **/
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            //TODO AGP 8.0부터는 BuildConfig 기본 비활성화, 9.0부터는 삭제 예정
                if (BuildConfig.DEBUG) {
                    setLevel(HttpLoggingInterceptor.Level.BODY)
                } else {
                    setLevel(HttpLoggingInterceptor.Level.NONE)
                }
        }
    }

    /** OkHttpClient Module **/
    @LoggingOkHttpClient
    @Singleton
    @Provides
    fun provideOkHttpClient(httpLoggingInterceptor: HttpLoggingInterceptor): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(httpLoggingInterceptor)
//        .callTimeout(1, TimeUnit.MINUTES)
        .readTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    @TokenOkHttpClient
    @Singleton
    @Provides
    fun provideTokenOkHttpClient(
        tokenAuthenticator: TokenAuthenticator,
        tokenInterceptor: TokenInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .authenticator(tokenAuthenticator)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(httpLoggingInterceptor)
            .build()
    }

    /** TokenInterceptor & TokenAuthentication **/
    @Singleton
    @Provides
    fun provideTokenAuthentication(
        dataStoreManager: DataStoreManager,
        authNetworkApi: AccountNetworkApi,
    ): TokenAuthenticator = TokenAuthenticator(dataStoreManager, authNetworkApi)

    @Singleton
    @Provides
    fun provideTokenInterceptor(
        dataStoreManager: DataStoreManager,
    ): TokenInterceptor = TokenInterceptor(dataStoreManager)

    /** Retrofit Instance **/
    @DefaultRetrofitBuilder
    @Singleton
    @Provides
    fun provideRetrofitBuilder(): Retrofit.Builder {
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(TestBaseUrl.TEST_BASE_URL)
    }

    /** ApiService **/
    @Singleton
    @Provides
    fun provideAuthApiService(
        @LoggingOkHttpClient loggingOkHttpClient: OkHttpClient,
        @DefaultRetrofitBuilder retrofit: Retrofit.Builder,
    ): AccountNetworkApi = retrofit
        .client(loggingOkHttpClient)
        .build()
        .create(AccountNetworkApi::class.java)

    @Singleton
    @Provides
    fun provideUserApiService(
        @TokenOkHttpClient tokenOkHttpClient: OkHttpClient,
        @DefaultRetrofitBuilder retrofit: Retrofit.Builder,
    ): UserNetworkApi = retrofit
        .client(tokenOkHttpClient)
        .build()
        .create(UserNetworkApi::class.java)

    @Singleton
    @Provides
    fun providePostApiService(
        @TokenOkHttpClient tokenOkHttpClient: OkHttpClient,
        @DefaultRetrofitBuilder retrofit: Retrofit.Builder,
    ): PostNetworkApi = retrofit
        .client(tokenOkHttpClient)
        .build()
        .create(PostNetworkApi::class.java)

    /** DataSource **/
    @Singleton
    @Provides
    fun provideAuthNetworkDataSource(authNetworkApi: AccountNetworkApi): AccountNetworkDataSource {
        return AccountNetworkDataSourceImpl(authNetworkApi)
    }

    @Singleton
    @Provides
    fun provideUserNetworkDataSource(userNetworkApi: UserNetworkApi): UserNetworkDataSource {
        return UserNetworkDataSourceImpl(userNetworkApi)
    }

    @Singleton
    @Provides
    fun provideCategoryNetworkDataSource(categoryNetworkApi: CategoryNetworkApi): CategoryNetworkDataSource {
        return CategoryNetworkDataSourceImpl(categoryNetworkApi)
    }

    @Singleton
    @Provides
    fun providePostNetworkDataSource(postNetworkApi: PostNetworkApi): PostNetworkDataSource {
        return PostNetworkDataSourceImpl(postNetworkApi)
    }
}

/** Qualifier **/
@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class LoggingOkHttpClient

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultRetrofitBuilder

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class TokenOkHttpClient

// TODO 나중에 BuildConfig 로 숨겨 놓기
object TestBaseUrl {
    const val TEST_BASE_URL = "https://1504-115-95-115-150.ngrok-free.app"
}