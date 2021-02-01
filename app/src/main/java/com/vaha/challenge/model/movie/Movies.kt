package com.vaha.challenge.model.movie

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.core.app.JobIntentService.MODE_PRIVATE
import com.vaha.challenge.network.SyncService
import com.vaha.challenge.network.SyncService.Companion.PAGE
import com.vaha.challenge.network.SyncService.Companion.QUERY


/**
 * Top level class that wraps all operations related to a data type (create, read, update, delete, search, etc.)
 */
object Movies {

    fun search(context: Context, query: String, page: Int) {
        val intent = Intent(context, SyncService::class.java)
        intent.putExtra(QUERY, query)
        intent.putExtra(PAGE, page)
        SyncService.enqueueWork(context, intent)
    }

    fun cacheQuery(context: Context, query: String) {
        val sharedPreferences: SharedPreferences =
            context.getSharedPreferences("com.vaha.cache", MODE_PRIVATE)
        if (sharedPreferences.all.values.contains(query)) {
            return
        }
        var edit = sharedPreferences.edit()
        if (sharedPreferences.all.size >= 10) {
            edit.remove(sharedPreferences.all.keys.minOrNull())
        }
        edit.putString("" + System.currentTimeMillis(), query)
        edit.apply()
    }

    fun getQueries(context: Context): List<String> {
        return context.getSharedPreferences(
            "com.vaha.cache",
            MODE_PRIVATE
        ).all.values.toList() as List<String>
    }
}