package com.example.myapplication

import com.example.myapplication.db.DBRepository
import org.junit.Assert.assertEquals
import org.junit.Test
import org.mockito.Mockito.*


class ExampleUnitTest {

    @Test
    fun get_mood_from_database() {
        val dbRepository: DBRepository = mock(DBRepository::class.java);
        val moodlist:MutableList<String> = mutableListOf("Ужасное", "Печальное")
        `when`(dbRepository.getMoodByDate("20.02.2021")).thenReturn(moodlist)
        assertEquals(moodlist, dbRepository.getMoodByDate("20.02.2021"))
    }

    @Test
    fun get_state_from_database() {
        val dbRepository: DBRepository = mock(DBRepository::class.java);
        val statelist:MutableList<String> = mutableListOf("Cонное", "Wabba-labba-Dub-Dub")
        `when`(dbRepository.getStateByDate("20.02.2021")).thenReturn(statelist)
        assertEquals(statelist, dbRepository.getStateByDate("20.02.2021"))
    }

    @Test
    fun get_doing_from_database() {
        val dbRepository: DBRepository = mock(DBRepository::class.java);
        val dolist:MutableList<String> = mutableListOf("20:00. Ужин", "21:00. Разглядывание стены", "22:00. Домашнее задание")
        `when`(dbRepository.getDoingByDate("20.02.2021")).thenReturn(dolist)
        assertEquals(dolist, dbRepository.getDoingByDate("20.02.2021"))
    }

    @Test
    fun nothing_in_database() {
        val dbRepository: DBRepository = mock(DBRepository::class.java)
        val empty:MutableList<String> = mutableListOf("")
        `when`(dbRepository.getDoingByDate("03.02.2021")).thenReturn(empty)
        assertEquals(empty, dbRepository.getDoingByDate("03.02.2021"))
    }
}