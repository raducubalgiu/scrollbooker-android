package com.example.scrollbooker.ui.profile.components

import com.example.scrollbooker.entity.social.post.domain.model.Post

data class SelectedPostUi(
    val post: Post,
    val tab: PostTabEnum,
    val index: Int
)