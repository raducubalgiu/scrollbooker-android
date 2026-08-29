package com.example.scrollbooker.components.customized.post

import com.example.scrollbooker.components.customized.post.sheets.PostSheetActionEnum
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.*
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.CommentsSheet
import com.example.scrollbooker.components.customized.post.sheets.PostSheetsContent.LinkedProductsSheet
import com.example.scrollbooker.entity.social.post.domain.model.Post

fun handlePostSheetAction(
    action: PostSheetActionEnum,
    post: Post,
    handleOpenSheet: (PostSheetsContent) -> Unit
) {
    when(action) {
        PostSheetActionEnum.OPEN_LINKED_PRODUCTS -> handleOpenSheet(LinkedProductsSheet(post.id))
        PostSheetActionEnum.OPEN_COMMENTS -> handleOpenSheet(CommentsSheet(post.id))
        PostSheetActionEnum.OPEN_MORE -> handleOpenSheet(MoreSheet(post.id))
    }
}