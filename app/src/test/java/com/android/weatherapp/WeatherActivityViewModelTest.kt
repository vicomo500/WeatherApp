package com.android.weatherapp

import com.android.weatherapp.viewmodels.WeatherActivityViewModel
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.Rule
import org.junit.Test
import androidx.arch.core.executor.testing.InstantTaskExecutorRule

class WeatherActivityViewModelTest {

    @get:Rule var instantExecutorRule = InstantTaskExecutorRule()

    @Test
    fun loadCacheOnStart(){
        // GIVEN - viewModel with cached data
        val repo = FakeWeatherRepo(cachedData, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // THEN - viewModel has loaded cache data
        MatcherAssert.assertThat(viewModel.data.value?.id, CoreMatchers.`is`(cachedData.id))
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(cachedData.city.name))
        MatcherAssert.assertThat(viewModel.data.value?.list?.size, CoreMatchers.`is`(cachedData.list.size))
        MatcherAssert.assertThat(viewModel.data.value?.code, CoreMatchers.`is`(cachedData.code))
    }

    @Test
    fun loadData(){
        val city = "Lagos"
        // GIVEN - viewModel
        val repo = FakeWeatherRepo(null, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN - load remote data
        viewModel.loadData(city)
        // THEN - view model has the data
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(city))
        MatcherAssert.assertThat(city, CoreMatchers.`is`(remoteData.city.name))
        MatcherAssert.assertThat(viewModel.data.value?.id, CoreMatchers.`is`(remoteData.id))
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(remoteData.city.name))
    }

    @Test
    fun cityNotFound(){
        val city = "Abuja"
        // GIVEN - viewModel
        val repo = FakeWeatherRepo(null, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN - load remote data
        viewModel.loadData(city)
        // THEN - view model does not have any data
        MatcherAssert.assertThat(viewModel.data.value == null, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.errorMsg.value, CoreMatchers.`is`(repo.errorMsg))
    }

    @Test
    fun invalidCityPattern(){
        val city = "Abu2"
        // GIVEN - viewModel
        val repo = FakeWeatherRepo(null, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN - load remote data
        viewModel.loadData(city)
        // THEN - view model has detected invalid city pattern
        MatcherAssert.assertThat(viewModel.data.value == null, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.errorMsg.value, CoreMatchers.`is`(WeatherActivityViewModel.CITY_VALIDATION_ERR))
    }

    @Test
    fun emptyRepoData_showMessage(){
        val city = "Lagos"
        // GIVEN - viewModel
        val repo = FakeWeatherRepo(null, null)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN - load remote data
        viewModel.loadData(city)
        // THEN - view model has detected invalid city pattern
        MatcherAssert.assertThat(viewModel.data.value == null, CoreMatchers.`is`(true))
        MatcherAssert.assertThat(viewModel.errorMsg.value == repo.errorMsg ||
                viewModel.errorMsg.value == WeatherActivityViewModel.EMPTY_DATA_MSG , CoreMatchers.`is`(true))
    }

    @Test
    fun continueWithCachedData_onError(){
        // GIVEN
        val repo = FakeWeatherRepo(cachedData, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN
       viewModel.loadData("XYZ")
        // THEN
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(cachedData.city.name))
        MatcherAssert.assertThat(viewModel.data.value?.city?.id, CoreMatchers.`is`(cachedData.city.id))
    }

    @Test
    fun continueWithPreviousData_onError(){
        // GIVEN
        val repo = FakeWeatherRepo(cachedData, remoteData)
        val viewModel = WeatherActivityViewModel(repo)
        // WHEN
        viewModel.loadData("Lagos")
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`("Lagos"))
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(remoteData.city.name))
        viewModel.loadData("Abuja")
        // THEN
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`("Lagos"))
        MatcherAssert.assertThat(viewModel.data.value?.city?.name, CoreMatchers.`is`(remoteData.city.name))
        MatcherAssert.assertThat(viewModel.data.value?.city?.id, CoreMatchers.`is`(remoteData.city.id))
    }

}
