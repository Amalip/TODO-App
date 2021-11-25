package com.amalip.todoapp2.domain.usecase

import com.amalip.todoapp2.core.exception.Failure
import com.amalip.todoapp2.core.functional.Either
import com.amalip.todoapp2.core.interactor.UseCase
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.repository.TaskRepository
import com.amalip.todoapp2.presentation.tasklist.ListFailure
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

class GetPendingTasks @Inject constructor(private val taskRepository: TaskRepository) :
    UseCase<List<Task>, UseCase.None>() {

    override suspend fun run(params: None): Either<Failure, List<Task>> {
        val list = taskRepository.getPendingTasks()

        return if (list.isEmpty()) Either.Left(ListFailure.NoInfo)
        else Either.Right(list)

    }

}