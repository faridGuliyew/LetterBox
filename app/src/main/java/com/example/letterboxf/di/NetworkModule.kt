package com.example.letterboxf.di

import com.example.letterboxf.api.ApiUtils
import com.example.letterboxf.api.RetrofitClient
import com.example.letterboxf.repository.FirebaseAuthRepository
import com.example.letterboxf.repository.FirebaseDatabaseRepository
import com.example.letterboxf.repository.PopularNetworkRepository
import com.example.letterboxf.repository.SameForAllNetworkRepository
import com.example.letterboxf.repository.SearchNetworkRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Singleton
    @Provides
    fun provideFirebaseAuth() : FirebaseAuth{
        return FirebaseAuth.getInstance()
    }

    @Singleton
    @Provides
    fun provideFirebaseStorage() : FirebaseStorage{
        return FirebaseStorage.getInstance()
    }

    @Singleton
    @Provides
    fun provideRetrofitClient() : Retrofit{
        return RetrofitClient.client
    }

    @Singleton
    @Provides
    fun provideApiUtils(client : Retrofit) : ApiUtils{
        return ApiUtils(client)
    }

    @Singleton
    @Provides
    fun providePopularNetworkRepository(apiUtils : ApiUtils) : PopularNetworkRepository{
        return PopularNetworkRepository(apiUtils)
    }

    @Singleton
    @Provides
    fun provideSameForAllNetworkRepository(apiUtils: ApiUtils) : SameForAllNetworkRepository{
        return SameForAllNetworkRepository(apiUtils)
    }

    @Singleton
    @Provides
    fun provideSearchNetworkRepository(apiUtils: ApiUtils, firestore: FirebaseFirestore) : SearchNetworkRepository {
        return SearchNetworkRepository(apiUtils,firestore)
    }

    @Singleton
    @Provides
    fun provideFirebaseRepository(firebaseAuth : FirebaseAuth, firestore: FirebaseFirestore) : FirebaseAuthRepository{
        return FirebaseAuthRepository(firebaseAuth,firestore)
    }

    @Singleton
    @Provides
    fun provideFireStore() : FirebaseFirestore{
        return Firebase.firestore
    }

    @Singleton
    @Provides
    fun provideFirebaseDatabaseRepository(firebaseFirestore: FirebaseFirestore,firebaseStorage : FirebaseStorage, firebaseAuth: FirebaseAuth) : FirebaseDatabaseRepository{
        return FirebaseDatabaseRepository(firebaseFirestore,firebaseStorage,firebaseAuth)
    }
}