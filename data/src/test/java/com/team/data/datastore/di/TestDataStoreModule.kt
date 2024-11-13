package com.team.data.datastore.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

private val TEST_DATASTORE_NAME = "test_datastore"
@OptIn(ExperimentalCoroutinesApi::class)
private val testCoroutineDispatcher = UnconfinedTestDispatcher()
@OptIn(ExperimentalCoroutinesApi::class)
private val testCoroutineScope = TestCoroutineScope(testCoroutineDispatcher + Job())
private val Context.testDataStore: DataStore<Preferences> by preferencesDataStore(
    TEST_DATASTORE_NAME
)
//    PreferenceDataStoreFactory.create(
//        scope = testCoroutineScope,
//        produceFile =
//        { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
//    )

