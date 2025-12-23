package com.example.scrollbooker.entity.social.post.data.mappers
import com.example.scrollbooker.entity.social.post.data.remote.FixedSlotsDto
import com.example.scrollbooker.entity.social.post.data.remote.HashtagDto
import com.example.scrollbooker.entity.social.post.data.remote.LastMinuteDto
import com.example.scrollbooker.entity.social.post.data.remote.PostBusinessOwnerDto
import com.example.scrollbooker.entity.social.post.data.remote.PostCountersDto
import com.example.scrollbooker.entity.social.post.data.remote.PostDto
import com.example.scrollbooker.entity.social.post.data.remote.PostEmployeeDto
import com.example.scrollbooker.entity.social.post.data.remote.PostMediaFileDto
import com.example.scrollbooker.entity.social.post.data.remote.PostProductCurrencyDto
import com.example.scrollbooker.entity.social.post.data.remote.PostProductDto
import com.example.scrollbooker.entity.social.post.data.remote.PostUserDto
import com.example.scrollbooker.entity.social.post.data.remote.UserPostActionsDto
import com.example.scrollbooker.entity.social.post.domain.model.FixedSlots
import com.example.scrollbooker.entity.social.post.domain.model.Hashtag
import com.example.scrollbooker.entity.social.post.domain.model.LastMinute
import com.example.scrollbooker.entity.social.post.domain.model.Post
import com.example.scrollbooker.entity.social.post.domain.model.PostBusinessOwner
import com.example.scrollbooker.entity.social.post.domain.model.PostCounters
import com.example.scrollbooker.entity.social.post.domain.model.PostEmployee
import com.example.scrollbooker.entity.social.post.domain.model.PostMediaFile
import com.example.scrollbooker.entity.social.post.domain.model.PostProduct
import com.example.scrollbooker.entity.social.post.domain.model.PostProductCurrency
import com.example.scrollbooker.entity.social.post.domain.model.PostUser
import com.example.scrollbooker.entity.social.post.domain.model.UserPostActions
import com.example.scrollbooker.ui.shared.posts.PostActionUiState

fun PostDto.toDomain(): Post {
    return Post(
        id = id,
        description = description,
        user = user.toDomain(),
        businessOwner = businessOwner.toDomain(),
        employee = employee?.toDomain(),
        userActions = userActions.toDomain(),
        mediaFiles = mediaFiles.map { it.toDomain() },
        counters = counters.toDomain(),
        hashtags = hashtags.orEmpty().map { it.toDomain() },
        isVideoReview = isVideoReview,
        bookable = bookable,
        rating = rating,
        businessId = businessId,
        lastMinute = lastMinute.toDomain(),
        createdAt = createdAt,
    )
}

fun PostUserDto.toDomain(): PostUser {
    return PostUser(
        id = id,
        fullName = fullName,
        username = username,
        avatar = avatar,
        isFollow = isFollow,
        profession = profession,
        ratingsAverage = ratingsAverage,
        ratingsCount = ratingsCount
    )
}

fun PostBusinessOwnerDto.toDomain(): PostBusinessOwner {
    return PostBusinessOwner(
        id = id,
        fullName = fullName,
        avatar = avatar,
        ratingsAverage = ratingsAverage
    )
}

fun PostEmployeeDto.toDomain(): PostEmployee {
    return PostEmployee(
        id = id,
        fullName = fullName,
        avatar = avatar
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
        currency = currency.toDomain()
    )
}

fun PostProductCurrencyDto.toDomain(): PostProductCurrency {
    return PostProductCurrency(
        id = id,
        name = name,
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

fun PostCountersDto.toDomain(): PostCounters {
    return PostCounters(
        commentCount = commentCount,
        likeCount = likeCount,
        bookmarkCount = bookmarkCount,
        repostCount = repostCount,
        bookingsCount = bookingsCount
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
        fixedSlots = fixedSlots.orEmpty().map { it.toDomain() }
    )
}

fun UserPostActions.applyUiState(ui: PostActionUiState): UserPostActions =
    copy(
        isLiked = ui.isLiked ?: isLiked,
        isBookmarked = ui.isBookmarked ?: isBookmarked,
        isReposted = ui.isReposted ?: isReposted
    )

fun PostCounters.applyUiState(ui: PostActionUiState): PostCounters =
    copy(
        likeCount = likeCount + ui.likeCountDelta,
        bookingsCount = bookingsCount + ui.bookmarkCountDelta,
        repostCount = repostCount + ui.reportCountDelta,
        commentCount = commentCount + ui.commentCountDelta
    )