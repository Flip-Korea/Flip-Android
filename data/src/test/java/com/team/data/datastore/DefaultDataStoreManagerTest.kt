package com.team.data.datastore

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.domain.DataStoreManager
import com.team.domain.type.DataStoreType
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.HiltTestApplication
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import javax.inject.Inject

@ExperimentalCoroutinesApi
@RunWith(RobolectricTestRunner::class)
@HiltAndroidTest
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class DefaultDataStoreManagerTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    lateinit var defaultDataStore: DataStoreManager

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun `데이터 저장 및 불러오기 (String 타입)`() = runTest {
        val expected = "테스트 값"
        var isError: Boolean? = null

        try {
            defaultDataStore.saveData(
                DataStoreType.AccountType.CURRENT_PROFILE_ID,
                expected
            )
        } catch (e: ClassCastException) {
            isError = true
        }

        val actual = defaultDataStore.getStringData(
            DataStoreType.AccountType.CURRENT_PROFILE_ID
        ).first()

        assertNull(isError)
        assertNotNull(actual)
        assertEquals(expected, actual)
    }

    @Test
    fun `데이터 저장 및 불러오기 (Int 타입)`() = runTest {
        val expected = 0
        var isError: Boolean? = null

        try {
            defaultDataStore.saveData(
                DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE,
                expected
            )
        } catch (e: ClassCastException) {
            isError = true
        }

        val actual = defaultDataStore.getIntData(
            DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE
        ).first()

        assertNull(isError)
        assertNotNull(actual)
        assertEquals(expected + 1, actual!! + 1)
    }

    @Test
    fun `데이터 삭제`() = runTest {
        val expected = "테스트 값"
        var isError: Boolean? = null

        try {
            defaultDataStore.saveData(
                DataStoreType.AccountType.CURRENT_PROFILE_ID,
                expected
            )
        } catch (e: ClassCastException) {
            isError = true
        }

        defaultDataStore.deleteData(DataStoreType.AccountType.CURRENT_PROFILE_ID)

        val actual = defaultDataStore.getStringData(
            DataStoreType.AccountType.CURRENT_PROFILE_ID
        ).first()

        assertNull(actual)
    }

    @Test
    fun `데이터 전부 삭제`() = runTest {
        val expected = "테스트 값"
        val expected2 = 0
        var isError: Boolean? = null

        try {
            defaultDataStore.saveData(
                DataStoreType.AccountType.CURRENT_PROFILE_ID,
                expected
            )
            defaultDataStore.saveData(
                DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE,
                expected2
            )
        } catch (e: ClassCastException) {
            isError = true
        }

        defaultDataStore.clearAll()

        val actual = defaultDataStore.getStringData(
            DataStoreType.AccountType.CURRENT_PROFILE_ID
        ).first()
        val actual2 = defaultDataStore.getIntData(
            DataStoreType.CheckType.EDIT_MY_CATEGORIES_SPEECH_BUBBLE
        ).first()

        assertNull(actual)
        assertNull(actual2)
    }
}