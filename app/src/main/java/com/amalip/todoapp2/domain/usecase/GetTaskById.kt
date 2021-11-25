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
class GetTaskById @Inject constructor(private val taskRepository: TaskRepository) : UseCase<Task, Long>() {

    override suspend fun run(params: Long): Either<Failure, Task> {
        val result = taskRepository.getTaskById(params)

        return if (result == null) Either.Left(ListFailure.NoInfo)
        else Either.Right(result)
    }

}