package com.sobi.replantation.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sobi.replantation.domain.PreferenceService
import com.sobi.replantation.domain.interactor.*
import com.sobi.replantation.domain.local.*
import com.sobi.replantation.domain.model.HttpUserService
import com.sobi.replantation.domain.model.TokenData
import com.sobi.replantation.domain.model.UserService
import com.sobi.replantation.domain.remote.*
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.inject.Singleton

@Module(includes = [ViewModelModule::class])
class ApplicationModule {
    @Provides
    @Singleton
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    fun providePreferenceService(context: Context): PreferenceService = PreferenceService(context)

    @Singleton
    @Provides
    fun provideBaseUrlLoader(): BaseUrlLoader = BaseUrlLoader()


    @Singleton
    @Provides
    fun provideOkHttpClient(baseUrlLoader: BaseUrlLoader): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val request = it.request()
            val newUrl = request.url().newBuilder()
                .host(baseUrlLoader.host)
                .build()
            it.proceed(request.newBuilder().url(newUrl).build())
        }.build()

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .client(okHttpClient)
        .baseUrl("http://localhost/")
        .addConverterFactory(JacksonConverterFactory.create(jacksonObjectMapper())).build()

    @Singleton
    @Provides
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides
    fun provideUserService(userApi: UserApi): UserService =
        HttpUserService(userApi)

    @Singleton
    @Provides
    fun provideTokenApi(retrofit: Retrofit): TokenApi = retrofit.create(TokenApi::class.java)


    @Singleton
    @Provides
    fun provideMemberApi(retrofit: Retrofit): MemberApi = retrofit.create(MemberApi::class.java)


    @Singleton
    @Provides
    fun provideMemberService(memberApi: MemberApi): MemberService = HttpMemberService(memberApi)

    @Singleton
    @Provides
    fun provideDatabase(context: Context): SobiDatabase = Room.databaseBuilder(
        context,
        SobiDatabase::class.java,
        "sobi_replantation_db")
        .fallbackToDestructiveMigration()
        .build()

    @Singleton
    @Provides
    fun provideAssignmentRepository(
        sobiDatabase: SobiDatabase
    ): AssignmentRepository = RoomAssignmentRepository(sobiDatabase)


    @Singleton
    @Provides
    fun provideAreaRepository(
        sobiDatabase: SobiDatabase
    ): AreaRepository = RoomAreaRepository(sobiDatabase)

    @Singleton
    @Provides
    fun provideSerahTerimaRepository(
        sobiDatabase: SobiDatabase
    ): SerahTerimaRepository = RoomSerahTerimaRepository(sobiDatabase)

    @Singleton
    @Provides
    fun provideBuktiSerahTerimaRepository(
        sobiDatabase: SobiDatabase
    ): BuktiSerahTerimaRepository = RoomBuktiSerahTerimaRepository(sobiDatabase)


    @Singleton
    @Provides
    fun provideDataManager(
        memberService: MemberService,
        assignmentRepository: AssignmentRepository,
        areaRepository: AreaRepository
    ): DataManager =
        DataManager(memberService, assignmentRepository, areaRepository)

    @Provides
    fun provideTokenProvider(tokenApi: TokenApi, preferenceService: PreferenceService): TokenProvider {
        return object : TokenProvider {
            override suspend fun getTokenData(): TokenData? =
                TokenData(1550732366, "Sobi+Apps:3795fc43f64936c3a2812ad9a1914bf1")

        }
    }

    @Provides
    fun provideAccountInteractor(userService: UserService, preferenceService: PreferenceService, tokenProvider: TokenProvider) : AccountInteractor =
        AccountInteractor(preferenceService, userService, tokenProvider)

    @Provides
    fun provideAssignmentInteractor(assignmentRepository: AssignmentRepository,areaRepository: AreaRepository) : AssignmentInteractor =
        AssignmentInteractor(assignmentRepository, areaRepository)

    @Provides
    fun provideAreaInteractor(areaRepository: AreaRepository, preferenceService: PreferenceService) : AreaInteractor =
        AreaInteractor(preferenceService, areaRepository)

    @Provides
    fun provideSerahTerimaInteractor(serahTerimaRepository: SerahTerimaRepository, preferenceService: PreferenceService) : SerahTerimaInteractor =
        SerahTerimaInteractor(preferenceService, serahTerimaRepository)

    @Provides
    fun provideBuktiSerahTerimaInteractor(buktiSerahTerimaRepository: BuktiSerahTerimaRepository, preferenceService: PreferenceService) : BuktiSerahTerimaInteractor =
        BuktiSerahTerimaInteractor(preferenceService, buktiSerahTerimaRepository)

}