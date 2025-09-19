package com.example.scrollbooker.ui.modules.posts.util

import com.example.scrollbooker.R
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.ui.feed.PostActionButtonUIModel

fun mapPostToButtonUI(post: Post): PostActionButtonUIModel {
    return when {
//        post.lastMinute.isLastMinute -> {
//            PostActionButtonUIModel(
//                title = "Profita de reducere",
//                icon = R.drawable.ic_bolt_solid
//            )
//        }
        post.product?.discount?.toInt()?.let { it > 0 } == true -> {
            PostActionButtonUIModel(
                title = "Profita de reducere",
                icon = R.drawable.ic_percent_badge_outline
            )
        }
        else -> {
            PostActionButtonUIModel(
                title = "Servicii disponibile",
                icon = R.drawable.ic_shopping_outline
            )
        }
    }
}