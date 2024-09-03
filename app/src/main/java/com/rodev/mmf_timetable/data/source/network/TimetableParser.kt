package com.rodev.mmf_timetable.data.source.network

import com.rodev.mmf_timetable.data.source.network.model.NetworkLesson
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.safety.Safelist

class TimetableParser(
    private val document: Document
) {

    fun parse(): List<NetworkLesson> {
        document.getElementsByTag("table").forEach { table ->
            return table
                .getElementsByTag("tbody")
                .first()
                ?.children()
                ?.mapNotNull { entry ->
                    entry.toLessonDto()
                }
                ?: emptyList()
        }

        return emptyList()
    }

    private fun Element.toLessonDto(): NetworkLesson? {
        val weekdayElement = getFirstByClass("weekday") ?: return null
        val time = getFirstByClass("time")?.text() ?: return null
        val remarks = getFirstByClass("remarks")?.text() ?: return null
        val subjectAndTeacherElement = getFirstByClass("subject-teachers") ?: return null
        val lecturePractice = getFirstByClass("lecture-practice")?.text() ?: return null
        val room = getFirstByClass("room")?.text() ?: return null

        val split = Jsoup.clean(
            subjectAndTeacherElement.html(),
            "",
            Safelist.none(),
            Document.OutputSettings().prettyPrint(false)
        )

        return NetworkLesson(
            weekday = weekdayElement.text(),
            time = time,
            remarks = remarks,
            subjectTeachers = split,
            lecturePractice = lecturePractice,
            room = room
        )
    }

    private fun Element.getFirstByClass(className: String): Element? = getElementsByClass(className).firstOrNull()
}

fun Document.parseLessons(): List<NetworkLesson> {
    return TimetableParser(this).parse()
}