package com.project.seorigami.model

import java.io.Serializable

data class HistoryChatModel(
    var id: Int? = 0,
    var name: String? = "",
    var lastMessage: String? = "",
): Serializable
