package com.team.data.network.model.request

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import com.team.domain.model.report_block.ReportReq

/** reportId, reporterId는 profileId 형태 **/
@JsonClass(generateAdapter = true)
data class ReportRequest(
    @Json(name = "blame_type") val blameType: String,
    @Json(name = "report_id") val reportId: String,
    @Json(name = "reporter_id") val reporterId: String,
    @Json(name = "post_id") val postId: Long?,
    @Json(name = "comment_id") val commentId: Long?,
)

fun ReportReq.toNetwork(): ReportRequest =
    ReportRequest(
        blameType = reportType.asString(),
        reportId, reporterId, postId, commentId
    )