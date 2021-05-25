package com.trollingcont.shifttest.usermanager

import android.content.Context
import android.util.JsonReader
import android.util.JsonWriter
import java.io.EOFException
import java.io.File
import java.io.InputStreamReader
import java.io.OutputStreamWriter
import java.lang.IllegalStateException
import java.security.MessageDigest
import java.util.*

class AppUserManager(private val context: Context) {
    init {
        createFileIfNotExist()
    }

    companion object {
        private const val USER_FILE_NAME = "user"
    }

    fun setUser(entity: UserEntity, password: String): Boolean {
        val writer = JsonWriter(
            OutputStreamWriter(
                context.openFileOutput(USER_FILE_NAME, Context.MODE_PRIVATE)
            )
        )

        writer.beginObject()
        writer.name("name").value(entity.name)
        writer.name("surname").value(entity.surname)
        writer.name("birthDay").value(entity.dayOfBirth)
        writer.name("birthMonth").value(entity.monthOfBirth)
        writer.name("birthYear").value(entity.yearOfBirth)
        writer.name("passwordHash").value(generateStringHash(password))
        writer.endObject()

        writer.close()

        return true
    }

    fun getUser(): UserEntity? {
        val reader = JsonReader(
            InputStreamReader(context.openFileInput(USER_FILE_NAME))
        )

        var name: String? = null
        var surname: String? = null
        var birthDay: Int? = null
        var birthMonth: Int? = null
        var birthYear: Int? = null

        try {
            reader.beginObject()
        } catch (eof: EOFException) {
            return null
        }

        while (reader.hasNext()) {
            when (val prop = reader.nextName()) {
                "name" -> name = reader.nextString()
                "surname" -> surname = reader.nextString()
                "birthDay" -> birthDay = reader.nextInt()
                "birthMonth" -> birthMonth = reader.nextInt()
                "birthYear" -> birthYear = reader.nextInt()
                "passwordHash" -> reader.skipValue()
                else -> throw IllegalStateException("Wrong format of JSON user file: unexpected property $prop")
            }
        }
        reader.endObject()

        if (name != null && surname != null && birthDay != null && birthMonth != null && birthYear != null)
            return UserEntity(name, surname, birthDay, birthMonth, birthYear)

        return null
    }

    private fun createFileIfNotExist() {
        val folder = File("${context.filesDir}")
        val file = File(folder.absolutePath + "/$USER_FILE_NAME")
        if (!folder.exists()) {
            folder.mkdir()
        }
        if (!file.exists()) {
            file.createNewFile()
        }
    }

    private fun generateStringHash(sourceStr: String): String {
        val bytes = MessageDigest
            .getInstance("SHA-256")
            .digest(sourceStr.toByteArray())

        return printHexBinary(bytes).toUpperCase(Locale.ROOT)
    }

    private val hexChars = "0123456789ABCDEF".toCharArray()

    private fun printHexBinary(data: ByteArray): String {
        val r = StringBuilder(data.size * 2)
        data.forEach { b ->
            val i = b.toInt()
            r.append(hexChars[i shr 4 and 0xF])
            r.append(hexChars[i and 0xF])
        }
        return r.toString()
    }
}