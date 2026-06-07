package com.example.scrollbooker.entity.user.notification.di

import com.example.scrollbooker.core.util.Constants
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentBookedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentCanceledNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentReminderNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentRescheduledNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.AppointmentReviewedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.BusinessValidationNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.CommentPostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestAcceptedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestDeniedNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.EmploymentRequestNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.FollowNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.LikePostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.MentionPostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationDto
import com.example.scrollbooker.entity.user.notification.data.remote.NotificationsApiService
import com.example.scrollbooker.entity.user.notification.data.remote.RepostNotificationDataDto
import com.example.scrollbooker.entity.user.notification.data.repository.NotificationRepositoryImpl
import com.example.scrollbooker.entity.user.notification.domain.model.FollowNotificationData
import com.example.scrollbooker.entity.user.notification.domain.repository.NotificationRepository
import com.example.scrollbooker.entity.user.notification.domain.useCase.GetNotificationsUseCase
import com.example.scrollbooker.entity.user.notification.domain.useCase.GetUserNotificationsNumberUseCase
import com.example.scrollbooker.entity.user.userSocial.data.remote.UserSocialDto
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonDeserializer
import com.mapbox.geojson.internal.typeadapters.RuntimeTypeAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NotificationsModule {
    @Provides
    @Singleton
    fun provideGson(): Gson {
        val notificationDeserializer = JsonDeserializer<NotificationDto> { json, _, context ->
            val jsonObject = json.asJsonObject

            val id = jsonObject.get("id").asInt
            val type = jsonObject.get("type").asString
            val senderId = jsonObject.get("sender_id")?.asInt ?: 0
            val receiverId = jsonObject.get("receiver_id").asInt
            val message = jsonObject.get("message")?.asString
            val isRead = jsonObject.get("is_read").asBoolean
            val isDeleted = jsonObject.get("is_deleted").asBoolean
            val isFollow = jsonObject.get("is_follow").asBoolean

            val sender = context.deserialize<UserSocialDto>(jsonObject.get("sender"), UserSocialDto::class.java)

            val dataElement = jsonObject.get("data")
            val notificationData: NotificationDataDto? = if (dataElement != null && !dataElement.isJsonNull) {
                val targetClass = when (type) {
                    "follow" -> FollowNotificationDataDto::class.java
                    "like_post" -> LikePostNotificationDataDto::class.java
                    "comment_post" -> CommentPostNotificationDataDto::class.java
                    "repost" -> RepostNotificationDataDto::class.java
                    "mention_post" -> MentionPostNotificationDataDto::class.java
                    "appointment_booked" -> AppointmentBookedNotificationDataDto::class.java
                    "appointment_canceled" -> AppointmentCanceledNotificationDataDto::class.java
                    "appointment_rescheduled" -> AppointmentRescheduledNotificationDataDto::class.java
                    "appointment_reminder" -> AppointmentReminderNotificationDataDto::class.java
                    "appointment_reviewed" -> AppointmentReviewedNotificationDataDto::class.java
                    "employment_request" -> EmploymentRequestNotificationDataDto::class.java
                    "employment_request_accept" -> EmploymentRequestAcceptedNotificationDataDto::class.java
                    "employment_request_denied" -> EmploymentRequestDeniedNotificationDataDto::class.java
                    "business_validation" -> BusinessValidationNotificationDataDto::class.java
                    else -> null
                }

                if (targetClass != null) {
                    context.deserialize(dataElement, targetClass)
                } else {
                    null
                }
            } else {
                null
            }

            NotificationDto(
                id = id,
                type = type,
                senderId = senderId,
                receiverId = receiverId,
                data = notificationData,
                message = message,
                isRead = isRead,
                isDeleted = isDeleted,
                sender = sender,
                isFollow = isFollow
            )
        }

        return GsonBuilder()
            .registerTypeAdapter(NotificationDto::class.java, notificationDeserializer)
            .create()
    }

    @Provides
    @Singleton
    fun provideNotificationsApiService(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): NotificationsApiService {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
            .create(NotificationsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNotificationsRepository(apiService: NotificationsApiService): NotificationRepository {
        return NotificationRepositoryImpl(apiService)
    }

    @Provides
    @Singleton
    fun provideGetUserNotificationsUseCase(
        repository: NotificationRepository,
    ): GetUserNotificationsNumberUseCase {
        return GetUserNotificationsNumberUseCase(repository)
    }

    @Provides
    @Singleton
    fun provideGetNotificationsUseCase(
        repository: NotificationRepository,
    ): GetNotificationsUseCase {
        return GetNotificationsUseCase(repository)
    }
}