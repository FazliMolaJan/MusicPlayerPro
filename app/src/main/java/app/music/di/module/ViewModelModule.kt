package app.music.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import app.music.viewmodel.ChooseThemeActivityViewModel
import app.music.viewmodel.HomeActivityViewModel
import app.music.viewmodel.OnlineHomeActivityViewModel
import app.music.viewmodel.SettingActivityViewModel
import dagger.Binds
import dagger.MapKey
import dagger.Module
import dagger.multibindings.IntoMap
import javax.inject.Inject
import javax.inject.Provider
import kotlin.reflect.KClass

//@Singleton
class ViewModelFactory @Inject constructor(
        private val viewModels: MutableMap<Class<out ViewModel>, Provider<ViewModel>>)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T = viewModels[modelClass]?.get() as T
}

@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
@MapKey
internal annotation class ViewModelKey(val value: KClass<out ViewModel>)

@Module
abstract class ViewModelModule {

    @Binds
    internal abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(HomeActivityViewModel::class)
    internal abstract fun homeActivityViewModel(viewModel: HomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(OnlineHomeActivityViewModel::class)
    internal abstract fun onlineHomeActivityViewModel(viewModel: OnlineHomeActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SettingActivityViewModel::class)
    internal abstract fun settingActivityViewModel(viewModel: SettingActivityViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ChooseThemeActivityViewModel::class)
    internal abstract fun chooseThemeActivityViewModel(viewModel: ChooseThemeActivityViewModel): ViewModel

    //Add more ViewModels here
}