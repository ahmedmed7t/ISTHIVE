package com.isthive.ist.questionnaire.questionnaireModule.di

import com.isthive.ist.questionnaire.questionnaireModule.data.localDataSource.QuestionnaireLocalDataSource
import com.isthive.ist.questionnaire.questionnaireModule.data.localDataSource.QuestionnaireLocalDataSourceImp
import com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource.QuestionnaireRemoteDataSource
import com.isthive.ist.questionnaire.questionnaireModule.data.remoteDataSource.QuestionnaireRemoteDataSourceImp
import com.isthive.ist.questionnaire.questionnaireModule.data.repository.QuestionnaireRepositoryImp
import com.isthive.ist.questionnaire.questionnaireModule.domain.repository.QuestionnaireRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@InstallIn(ViewModelComponent::class)
@Module
internal abstract class QuestionnaireModule {

    @Binds
    abstract fun bindRemoteDataSource(remoteDataSourceImp: QuestionnaireRemoteDataSourceImp): QuestionnaireRemoteDataSource

    @Binds
    abstract fun bindLocalDataSource(localDataSourceImp: QuestionnaireLocalDataSourceImp): QuestionnaireLocalDataSource


    @Binds
    abstract fun bindRepository(RepositoryImp: QuestionnaireRepositoryImp): QuestionnaireRepository
}