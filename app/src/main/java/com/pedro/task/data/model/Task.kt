package com.pedro.task.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Task (
    var id: String,
    var description: String,

    // Cria a tarefa como TODO
    var status: Status = Status.TODO
): Parcelable