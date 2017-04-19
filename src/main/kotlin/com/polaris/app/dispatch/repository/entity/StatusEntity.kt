package com.polaris.app.dispatch.repository.entity

import com.polaris.app.dispatch.controller.adapter.enums.AssignmentState

data class StatusEntity(
        val status: AssignmentState
)