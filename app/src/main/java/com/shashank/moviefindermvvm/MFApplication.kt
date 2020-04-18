package com.shashank.moviefindermvvm

import android.app.Application
import com.shashank.moviefindermvvm.data.network.ApiInterface
import com.shashank.moviefindermvvm.data.network.NetworkConnectionInterceptor
import com.shashank.moviefindermvvm.data.repositories.HomeRepository
import com.shashank.moviefindermvvm.data.repositories.MovieDetailRepository
import com.shashank.moviefindermvvm.ui.home.HomeViewModelFactory
import com.shashank.moviefindermvvm.ui.moviedetail.MovieDetailViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class MFApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@MFApplication))

        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiInterface(instance()) }
        bind() from singleton { HomeRepository(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
        bind() from singleton { MovieDetailRepository(instance()) }
        bind() from provider { MovieDetailViewModelFactory(instance()) }


    }

}