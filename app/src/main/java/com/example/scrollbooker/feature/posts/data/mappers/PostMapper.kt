package com.example.scrollbooker.feature.posts.data.mappers
import com.example.scrollbooker.feature.posts.data.remote.FixedSlotsDto
import com.example.scrollbooker.feature.posts.data.remote.HashtagDto
import com.example.scrollbooker.feature.posts.data.remote.LastMinuteDto
import com.example.scrollbooker.feature.posts.data.remote.PostCountersDto
import com.example.scrollbooker.feature.posts.data.remote.PostDto
import com.example.scrollbooker.feature.posts.data.remote.PostMediaFileDto
import com.example.scrollbooker.feature.posts.data.remote.PostProductDto
import com.example.scrollbooker.feature.posts.data.remote.UserPostActionsDto
import com.example.scrollbooker.feature.posts.domain.model.FixedSlots
import com.example.scrollbooker.feature.posts.domain.model.Hashtag
import com.example.scrollbooker.feature.posts.domain.model.LastMinute
import com.example.scrollbooker.feature.posts.domain.model.Post
import com.example.scrollbooker.feature.posts.domain.model.PostCounters
import com.example.scrollbooker.feature.posts.domain.model.PostMediaFile
import com.example.scrollbooker.feature.posts.domain.model.PostProduct
import com.example.scrollbooker.feature.posts.domain.model.UserPostActions

fun PostDto.toDomain(): Post {
    return Post(
        id = id,
        description = description,
        user = user,
        product = product.toDomain(),
        userActions = userActions.toDomain(),
        mediaFiles = mediaFiles.map { it.toDomain() },
        counters = counters.toDomain(),
        mentions = mentions,
        hashtags = hashtags.map { it.toDomain() },
        bookable = bookable,
        instantBooking = instantBooking,
        lastMinute = lastMinute.toDomain(),
        createdAt = createdAt,
    )
}

fun UserPostActionsDto.toDomain(): UserPostActions {
    return UserPostActions(
        isLiked = isLiked,
        isBookmarked = isBookmarked,
        isReposted = isReposted
    )
}

fun PostMediaFileDto.toDomain(): PostMediaFile {
    return PostMediaFile(
        id = id,
        url = url,
        type = type,
        thumbnailUrl = thumbnailUrl,
        duration = duration,
        postId = postId,
        orderIndex = orderIndex
    )
}

fun HashtagDto.toDomain(): Hashtag {
    return Hashtag(
        id = id,
        name = name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun PostProductDto.toDomain(): PostProduct {
    return PostProduct(
        id = id,
        name = name,
        description = description,
        duration = duration,
        price = price,
        priceWithDiscount = priceWithDiscount,
        discount = discount,
        currency = currency
    )
}

fun PostCountersDto.toDomain(): PostCounters {
    return PostCounters(
        commentCount = commentCount,
        likeCount = likeCount,
        bookmarkCount = bookmarkCount,
        shareCount = shareCount
    )
}

fun FixedSlotsDto.toDomain(): FixedSlots {
    return FixedSlots(
        startTime = startTime,
        endTime = endTime,
        isBooked = isBooked
    )
}

fun LastMinuteDto.toDomain(): LastMinute {
    return LastMinute(
        isLastMinute = isLastMinute,
        lastMinuteEnd = lastMinuteEnd,
        hasFixedSlots = hasFixedSlots,
        fixedSlots = fixedSlots.map { it.toDomain() }
    )
}