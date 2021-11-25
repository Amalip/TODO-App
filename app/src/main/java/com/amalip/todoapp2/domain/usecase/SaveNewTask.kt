package com.amalip.todoapp2.domain.usecase

import com.amalip.todoapp2.core.exception.Failure
import com.amalip.todoapp2.core.functional.Either
import com.amalip.todoapp2.core.interactor.UseCase
import com.amalip.todoapp2.domain.model.Task
import com.amalip.todoapp2.domain.repository.TaskRepository
import com.amalip.todoapp2.presentation.form.FormFailure
import javax.inject.Inject

/**
 * Created by Amalip on 11/23/2021.
 */

class SaveNewTask @Inject constructor(private val taskRepository: TaskRepository) : UseCase<Long, Task>()  {

    override suspend fun run(params: Task): Either<Failure, Long> {
        val result = taskRepository.saveNewTask(params)

        return if (result == -1L) Either.Left(FormFailure.CouldNotCreate)
        else Either.Right(result)
    }

}