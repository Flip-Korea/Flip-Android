package com.team.data.local.dao

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.data.local.FlipDatabase
import com.team.data.testdoubles.local.makeProfileEntityTestData
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject
import javax.inject.Named
import kotlin.random.Random

@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
//    instrumentedPackages = [
//        // required to access final members on androidx.loader.content.ModernAsyncTask
//        "androidx.loader.content"
//    ])
)
class ProfileDaoTest {

    @get:Rule(order = 1)
    var hiltModule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()
    // 안드로이드 구성요소 관련 작업들을 모두 한 스레드에서 실행되게 함
    // 모든 코드가 동기적으로 실행

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var myProfileDao: MyProfileDao

    @Before
    fun setUp() {
        hiltModule.inject()
        myProfileDao = database.myProfileDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `모든 프로필 불러오기 (getAllProfile())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileId2 = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)
        val profileEntity2 = makeProfileEntityTestData(profileId2)

        myProfileDao.upsertProfile(profileEntity)
        myProfileDao.upsertProfile(profileEntity2)

        val profiles = myProfileDao.getAllProfile().first()

        assertEquals(profiles.size, 2)
    }

    @Test
    fun `프로필 1개 불러오기 (getProfileById())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)

        myProfileDao.upsertProfile(profileEntity)

        val profile = myProfileDao.getProfileById(profileId).first()

        assert(profile != null)
        assertEquals(profile!!.profileId, profileId)
    }

    @Test
    fun `프로필 추가 (upsertProfile())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)

        myProfileDao.upsertProfile(profileEntity)

        val profile = myProfileDao.getProfileById(profileId).first()

        assert(profile != null)
        assertEquals(profile!!.profileId, profileId)
    }

    @Test
    fun `여러 프로필 한 번에 추가 (upsertAll())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileId2 = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)
        val profileEntity2 = makeProfileEntityTestData(profileId2)

        val profileEntities = listOf(profileEntity, profileEntity2)

        myProfileDao.upsertAll(profileEntities)

        val profiles = myProfileDao.getAllProfile().first()

        assertEquals(profiles.size, profileEntities.size)
        assert(profiles.contains(profileEntity))
        assert(profiles.contains(profileEntity2))
    }

    @Test
    fun `프로필 삭제 (deleteProfile())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileId2 = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)
        val profileEntity2 = makeProfileEntityTestData(profileId2)

        val profileEntities = listOf(profileEntity, profileEntity2)

        myProfileDao.upsertAll(profileEntities)
        myProfileDao.deleteProfile(profileEntity2)

        val profiles = myProfileDao.getAllProfile().first()

        assertEquals(profiles.size, 1)
        assertEquals(profiles[0].profileId, profileEntity.profileId)
    }

    @Test
    fun `모든 프로필 삭제 (deleteAll())`() = runTest {

        val profileId = "test-profile-${Random.nextInt(1, 50000)}"
        val profileId2 = "test-profile-${Random.nextInt(1, 50000)}"
        val profileEntity = makeProfileEntityTestData(profileId)
        val profileEntity2 = makeProfileEntityTestData(profileId2)

        val profileEntities = listOf(profileEntity, profileEntity2)

        myProfileDao.upsertAll(profileEntities)
        myProfileDao.deleteAll()

        assert(myProfileDao.getAllProfile().first().isEmpty())
    }
}