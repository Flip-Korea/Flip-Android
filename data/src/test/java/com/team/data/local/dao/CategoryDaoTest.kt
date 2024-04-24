package com.team.data.local.dao

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.team.data.local.FlipDatabase
import com.team.data.testdoubles.local.makeCategoriesTestData
import com.team.data.testdoubles.local.makeCategoryTestData
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


@ExperimentalCoroutinesApi
@HiltAndroidTest
@RunWith(RobolectricTestRunner::class)
@Config(
    manifest = Config.NONE,
    sdk = [Build.VERSION_CODES.R],
    application = HiltTestApplication::class,
)
class CategoryDaoTest {

    @get:Rule(order = 1)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: FlipDatabase
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setUp() {
        hiltRule.inject()
        categoryDao = database.categoryDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `모든 카테고리 가져오기 (getCategories())`() = runTest {
        val categoryIds = mutableListOf<Int>()
        repeat(3) { id -> categoryIds.add(id) }

        categoryDao.upsertCategories(makeCategoriesTestData(categoryIds))

        val categories = categoryDao.getCategories().first()

        assertEquals(categories.size, 3)
    }

    @Test
    fun `ID로 카테고리 가져오기 (getCategoryById())`() = runTest {
        categoryDao.upsertCategory(makeCategoryTestData(1))

        val categoryEntity = categoryDao.getCategoryById(1).first()

        assert(categoryEntity != null)
        assertEquals(categoryEntity!!.id, 1)
    }

    @Test
    fun `ID로 카테고리 여러 개 가져오기 (getCategoryById())`() = runTest {
        val categoryIds = mutableListOf<Int>()
        repeat(5) { id -> categoryIds.add(id) }
        categoryDao.upsertCategories(makeCategoriesTestData(categoryIds))

        val findIds = listOf(1,2,3,7) // Not Exists Data Id is 7
        val findCategories = categoryDao.getCategoryByIds(findIds).first()

        assertEquals(findCategories.size, 3)
    }

    @Test
    fun `존재하지 않은 ID로 카테고리 가져오기 (getCategoryById())`() = runTest {
        categoryDao.upsertCategory(makeCategoryTestData(1))

        val categoryEntity = categoryDao.getCategoryById(3).first()

        assert(categoryEntity == null)
    }

    @Test
    fun `카테고리 삭제 (deleteCategory())`() = runTest {
        val categoryEntity = makeCategoryTestData(1)
        categoryDao.upsertCategory(categoryEntity)
        categoryDao.deleteCategory(categoryEntity)

        assert(categoryDao.getCategories().first().isEmpty())
    }

    @Test
    fun `모든 카테고리 삭제 (clearAll())`() = runTest {
        val categoryIds = mutableListOf<Int>()
        repeat(3) { id -> categoryIds.add(id) }
        val categories = makeCategoriesTestData(categoryIds)
        categoryDao.upsertCategories(categories)

        categoryDao.clearAll()

        assert(categoryDao.getCategories().first().isEmpty())
    }
}