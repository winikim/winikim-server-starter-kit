package me.winikim.user.infrastructure.external.auth.kakao.dto

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class KakaoUserInfoResponse(
    val id: Long,
    val connected_at: String,
    val properties: KakaoUserProperties
)

data class KakaoUserAccount(
    val profile_needs_agreement: Boolean,
    val profile: KakaoUserProfile,
    val has_email: Boolean,
    val is_email_valid: Boolean,
    val is_email_verfied: Boolean,
    val email: String,
    val has_age_range: Boolean,
    val age_range_needs_agreement: Boolean,
    val age_range: String,
    val has_birthday: Boolean,
    val birthday_needs_agreement: Boolean,
    val birthday: String,
    val birthday_type: String,
    val has_gender: Boolean,
    val gender_needs_agreement: Boolean,
    val gender: String
)

data class KakaoUserProperties(
    val nickname: String?,
    val profile_image: String?,
    val thumbnail_image: String?
)

data class KakaoUserProfile(
    val nickname: String,
    val thumbnail_image_url: String,
    val profile_image_url: String
)

