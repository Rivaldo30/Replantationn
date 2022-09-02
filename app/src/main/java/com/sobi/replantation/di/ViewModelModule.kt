package com.sobi.replantation.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sobi.replantation.viewmodel.*
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class ViewModelModule {
    @Binds
    @IntoMap
    @ViewModelKey(LoginViewModel::class)
    abstract fun bindLoginViewModel(viewModel: LoginViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskListViewModel::class)
    abstract fun bindTaskListViewModel(viewModel: TaskListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(TaskDetailViewModel::class)
    abstract fun bindTaskDetailViewModel(viewModel: TaskDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(AreaDetailViewModel::class)
    abstract fun bindAreaDetailViewModel(viewModel: AreaDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(MemberDetailViewModel::class)
    abstract fun bindMemberDetailViewModel(viewModel: MemberDetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(PhotoSerahTerimaViewModel::class)
    abstract fun bindPhotoSerahTerimaViewModel(viewModel: PhotoSerahTerimaViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(ListSerahTerimaViewModel::class)
    abstract fun bindListSerahTerimaViewModel(viewModel: ListSerahTerimaViewModel): ViewModel

    @Binds
    abstract fun bindViewModelFactory(factory: ViewModelFactory): ViewModelProvider.Factory
}