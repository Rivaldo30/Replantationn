package com.sobi.replantation.di

import android.app.Application
import com.sobi.replantation.ReplantationApplication
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjectionModule
import javax.inject.Singleton


@Singleton
@Component(
    modules = [
        AndroidInjectionModule::class,
        ApplicationModule::class,
        ActivityBuilder::class]
)
interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance fun application(application: Application): AppComponent.Builder
        fun build(): AppComponent
    }

    fun inject(application: ReplantationApplication)
}