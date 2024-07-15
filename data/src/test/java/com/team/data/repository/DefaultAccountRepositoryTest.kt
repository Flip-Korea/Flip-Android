package com.team.data.repository

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import com.team.data.datastore.fake.FakeDataStoreManager
import com.team.data.local.FlipDatabase
import com.team.data.local.dao.MyProfileDao
import com.team.data.network.model.response.TokenResponse
import com.team.data.network.model.response.account.AccountResponse
import com.team.data.network.model.response.account.toDomainModel
import com.team.data.network.retrofit.api.AccountNetworkApi
import com.team.data.network.source.AccountNetworkDataSource
import com.team.data.network.source.fake.FakeAccountNetworkDataSource
import com.team.data.repository.fake.FakeAccountRepository
import com.team.data.testdoubles.network.networkAccountJsonTestData
import com.team.data.testdoubles.network.networkRegisterTestData
import com.team.data.testdoubles.network.networkTokenTestData
import com.team.data.testdoubles.network.toExternal
import com.team.domain.type.DataStoreType
import com.team.domain.type.SocialLoginPlatform
import com.team.domain.util.ErrorType
import com.team.domain.util.Result
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class DefaultAccountRepositoryTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var accountNetworkApi: AccountNetworkApi
    private lateinit var server: MockWebServer
    private lateinit var moshi: Moshi

    private lateinit var accountNetworkDataSource: AccountNetworkDataSource
    private lateinit var accountRepository: FakeAccountRepository
    private val dataStoreManager = FakeDataStoreManager()

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var myProfileDao: MyProfileDao

    @Before
    fun setUp() {
        hiltRule.inject()

        server = MockWebServer()
        server.start()

        moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        accountNetworkApi = Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(server.url("/"))
            .build()
            .create(AccountNetworkApi::class.java)

        myProfileDao = database.myProfileDao()

        accountNetworkDataSource = FakeAccountNetworkDataSource(accountNetworkApi)
        accountRepository = FakeAccountRepository(accountNetworkDataSource, myProfileDao, dataStoreManager)
    }

    @After
    fun teardown() {
        server.shutdown()
        database.close()
    }

    @Test
    fun `사용자 계정 불러오기(accessToken 없는 경우) (getUserAccount())`() = runTest {
        val result = accountRepository.getUserAccount().last()

        assertEquals((result as Result.Error).error, ErrorType.Token.NOT_FOUND)
    }

    @Test
    fun `사용자 계정 불러오기(accessToken 있는 경우) (getUserAccount())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkAccountJsonTestData)
        })

        dataStoreManager.saveData(DataStoreType.TokenType.ACCESS_TOKEN, "aaa.bbb.ccc")
        val result = accountRepository.getUserAccount().last()

        val actualData =
            moshi.adapter(AccountResponse::class.java)
                .fromJson(networkAccountJsonTestData)!!
                .toDomainModel((result as Result.Success).data.profiles)

        val expectedCurrentProfileId =
            dataStoreManager.getData(DataStoreType.AccountType.CURRENT_PROFILE_ID).first()

        assertEquals(result.data, actualData)
        assertEquals(expectedCurrentProfileId, actualData.profiles[0].profileId)
    }

    @Test
    fun `이름 중복 체크 실패 (checkDuplicateName())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val result = accountRepository.checkDuplicateName("honggd").last()

        assertEquals((result as Result.Error).error, ErrorType.Network.NOT_FOUND)
    }

    @Test
    fun `이름 중복 체크 실패(409, Conflict) (checkDuplicateName())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val result = accountRepository.checkDuplicateName("honggd").last()

        assertEquals((result as Result.Error).error, ErrorType.Network.NOT_FOUND)
    }

    @Test
    fun `이름 중복 체크 성공 (checkDuplicateName())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val result = accountRepository.checkDuplicateName("honggd").last()

        assertEquals((result as Result.Success).data, true)
    }

    @Test
    fun `ProfileId 중복 체크 실패 (checkDuplicateProfileId())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val result = accountRepository.checkDuplicateProfileId("testProfileId").last()

        assertEquals((result as Result.Error).error, ErrorType.Network.NOT_FOUND)
    }

    @Test
    fun `ProfileId 중복 체크 실패(409, Conflict) (checkDuplicateProfileId())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(404)
        })

        val result = accountRepository.checkDuplicateProfileId("testProfileId").last()

        assertEquals((result as Result.Error).error, ErrorType.Network.NOT_FOUND)
    }

    @Test
    fun `ProfileId 중복 체크 성공(200, OK) (checkDuplicateProfileId())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
        })

        val result = accountRepository.checkDuplicateProfileId("testProfileId").last()

        assertEquals((result as Result.Success).data, true)
    }

    @Test
    fun `로그인 (login())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(200)
            setBody(networkTokenTestData)
        })

        dataStoreManager.clearAll()
        val result = accountRepository.login(SocialLoginPlatform.KAKAO, "12345").last()

        val expectedAccessToken = moshi.adapter(TokenResponse::class.java)
            .fromJson(networkTokenTestData)!!
            .accessToken
        val expectedRefreshToken = moshi.adapter(TokenResponse::class.java)
            .fromJson(networkTokenTestData)!!
            .refreshToken

        val actualAccessToken = dataStoreManager.getData(DataStoreType.TokenType.ACCESS_TOKEN).first()
        val actualRefreshToken = dataStoreManager.getData(DataStoreType.TokenType.REFRESH_TOKEN).first()

        assertEquals((result as Result.Success).data, true)
        assertEquals(expectedAccessToken, actualAccessToken)
        assertEquals(expectedRefreshToken, actualRefreshToken)
    }

    @Test
    fun `회원가입 (register())`() = runTest {
        server.enqueue(MockResponse().apply {
            setResponseCode(201)
            setBody(networkTokenTestData)
        })

        dataStoreManager.clearAll()
        val result = accountRepository.register(networkRegisterTestData.toExternal()).last()

        val expectedAccessToken = moshi.adapter(TokenResponse::class.java)
            .fromJson(networkTokenTestData)!!
            .accessToken
        val expectedRefreshToken = moshi.adapter(TokenResponse::class.java)
            .fromJson(networkTokenTestData)!!
            .refreshToken

        val actualAccessToken = dataStoreManager.getData(DataStoreType.TokenType.ACCESS_TOKEN).first()
        val actualRefreshToken = dataStoreManager.getData(DataStoreType.TokenType.REFRESH_TOKEN).first()

        assertEquals((result as Result.Success).data, true)
        assertEquals(expectedAccessToken, actualAccessToken)
        assertEquals(expectedRefreshToken, actualRefreshToken)
    }
}