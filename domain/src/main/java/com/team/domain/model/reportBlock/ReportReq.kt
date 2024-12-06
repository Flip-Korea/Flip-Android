package com.team.domain.model.reportBlock

import com.team.domain.type.ReportType

data class ReportReq(
    val reportType: ReportType,
    val reportId: String,
    val reporterId: String,
    val postId: Long?,
    val commentId: Long?,
)
