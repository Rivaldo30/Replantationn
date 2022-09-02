package com.sobi.replantation.di

import com.sobi.replantation.ui.ListSerahTerimaActivity
import com.sobi.replantation.ui.*
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class ActivityBuilder{
    @ContributesAndroidInjector
    internal abstract fun bindLoginActivity(): LoginActivity

    @ContributesAndroidInjector
    internal abstract fun bindTaskListActivity(): TaskListActivity

    @ContributesAndroidInjector
    internal abstract fun bindTaskDetailActivity() : TaskDetailActivity

    @ContributesAndroidInjector
    internal abstract fun bindAreaDetailActivity() : AreaDetailActivity

    @ContributesAndroidInjector
    internal abstract fun bindMemberDetailActivity() : MemberDetailActivity


    @ContributesAndroidInjector
    internal abstract fun bindListSerahTerimaActivity(): ListSerahTerimaActivity

}