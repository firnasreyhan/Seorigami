package com.project.seorigami.model

import java.io.Serializable

data class ChatModel(
    var message: String? = "",
    var sender: Int? = 0
): Serializable
