package com.appzeto.lms.model.view

import android.content.Context
import android.os.Parcel
import android.os.Parcelable
import com.appzeto.lms.R
import com.appzeto.lms.manager.Utils.toBoolean
import com.appzeto.lms.model.ChapterFileItem
import com.appzeto.lms.model.ChapterItem

class FileChapterListItem : CourseCommonItem {

    private var chapterItem: ChapterItem

    constructor(chpaterItem: ChapterItem) {
        chapterItem = chpaterItem
    }

    constructor(parcel: Parcel) {
        chapterItem = parcel.readParcelable(ChapterItem::class.java.classLoader)!!
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(chapterItem, flags)
    }

    companion object CREATOR : Parcelable.Creator<FileChapterListItem> {
        override fun createFromParcel(parcel: Parcel): FileChapterListItem {
            return FileChapterListItem(parcel)
        }

        override fun newArray(size: Int): Array<FileChapterListItem?> {
            return arrayOfNulls(size)
        }
    }

    override fun title(context: Context): String {
        return chapterItem.title
    }

    override fun desc(context: Context): String {
        if(chapterItem.volume.startsWith("0 "))
            return ""

        return chapterItem.volume
    }

    override fun imgResource(context: Context): Int {
        if (chapterItem.storage == ChapterFileItem.Storage.UPLOAD.value &&
            chapterItem.downloadable.toBoolean()
        ) {
            return R.drawable.ic_download_white
        }
        return R.drawable.ic_play2
    }

    override fun imgBgResource(context: Context): Int {
        return R.drawable.round_view_light_green_corner10
    }

    override fun passed(): Boolean {
        return chapterItem.authHasRead
    }

}