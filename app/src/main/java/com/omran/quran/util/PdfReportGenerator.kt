package com.omran.quran.util

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.Typeface
import android.graphics.pdf.PdfDocument
import android.net.Uri
import androidx.core.content.FileProvider
import com.omran.quran.data.model.AttendanceStatus
import com.omran.quran.data.model.Exam
import com.omran.quran.data.model.Memorization
import com.omran.quran.data.model.MemorizationCategory
import com.omran.quran.data.model.Revision
import com.omran.quran.data.model.RevisionCategory
import com.omran.quran.data.model.Student
import com.omran.quran.ui.attendance.AttendanceRow
import com.omran.quran.ui.students.StudentAnalytics
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object PdfReportGenerator {

    private const val PAGE_WIDTH = 595
    private const val PAGE_HEIGHT = 842
    private const val MARGIN = 32f

    private fun getLogoBitmap(context: Context): Bitmap? {
        return try {
            BitmapFactory.decodeResource(context.resources, com.omran.quran.R.drawable.institute_logo)
        } catch (e: Exception) {
            null
        }
    }

    private fun drawCircularLogo(
        canvas: android.graphics.Canvas,
        bitmap: Bitmap,
        rect: RectF,
        borderColor: Int = Color.rgb(217, 119, 6),
        borderWidth: Float = 2.5f,
        backgroundColor: Int = Color.WHITE,
    ) {
        val cx = rect.centerX()
        val cy = rect.centerY()
        val radius = minOf(rect.width(), rect.height()) / 2f

        val p = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }

        // 1. Solid circular background
        p.style = Paint.Style.FILL
        p.color = backgroundColor
        canvas.drawCircle(cx, cy, radius, p)

        // 2. Clipped image inside circle
        val path = android.graphics.Path().apply {
            addCircle(cx, cy, radius - borderWidth, android.graphics.Path.Direction.CW)
        }
        val saveCount = canvas.save()
        canvas.clipPath(path)
        val innerRect = RectF(cx - radius + borderWidth, cy - radius + borderWidth, cx + radius - borderWidth, cy + radius - borderWidth)
        canvas.drawBitmap(bitmap, null, innerRect, p)
        canvas.restoreToCount(saveCount)

        // 3. Circular border
        p.style = Paint.Style.STROKE
        p.strokeWidth = borderWidth
        p.color = borderColor
        canvas.drawCircle(cx, cy, radius - (borderWidth / 2f), p)
    }

    fun generateDailyReportPdf(
        context: Context,
        instituteName: String,
        teacherOrCircleName: String,
        dateText: String,
        rows: List<AttendanceRow>,
        dayMemorizations: List<Memorization>,
        dayExams: List<Exam>,
        dayRevisions: List<Revision>,
        hijriDateText: String? = null,
    ): File {
        val pdfDocument = PdfDocument()
        val logoBitmap = getLogoBitmap(context)

        val presentRows = rows.filter { it.status == AttendanceStatus.PRESENT }
        val absentRows = rows.filter { it.status == AttendanceStatus.ABSENT }
        val lateRows = rows.filter { it.status == AttendanceStatus.LATE }

        val memByStudent = dayMemorizations.groupBy { it.studentId }
        val examByStudent = dayExams.groupBy { it.studentId }
        val revByStudent = dayRevisions.groupBy { it.studentId }

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        fun drawHeader() {
            val headerHeight = 92f
            val headerRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + headerHeight)
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawRoundRect(headerRect, 12f, 12f, paint)

            paint.color = Color.rgb(217, 119, 6)
            canvas.drawRoundRect(RectF(MARGIN + 8f, y + headerHeight - 6f, PAGE_WIDTH - MARGIN - 8f, y + headerHeight - 2f), 3f, 3f, paint)

            if (logoBitmap != null) {
                val logoSize = 64f
                val logoRect = RectF(PAGE_WIDTH - MARGIN - logoSize - 10f, y + 14f, PAGE_WIDTH - MARGIN - 10f, y + 14f + logoSize)
                drawCircularLogo(canvas, logoBitmap, logoRect, borderColor = Color.rgb(217, 119, 6), borderWidth = 2.5f)

                val textStartX = PAGE_WIDTH - MARGIN - logoSize - 20f

                paint.color = Color.WHITE
                paint.textSize = 13.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("👑 تقرير التسميع والسَّبْر اليومي", textStartX, y + 26f, paint)

                paint.color = Color.rgb(203, 213, 225)
                paint.textSize = 10f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText("🌿 $instituteName", textStartX, y + 44f, paint)

                paint.color = Color.rgb(254, 240, 138)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("👨‍🏫 المعلم: $teacherOrCircleName", textStartX, y + 62f, paint)

                val resolvedHijri = hijriDateText?.ifBlank { null }
                    ?: HijriDateHelper.formatHijriFromDateString(dateText, includeDayName = false).ifBlank { null }
                    ?: HijriDateHelper.getTodayHijri(includeDayName = false)
                val fullDisplayDate = "📅 $dateText م  •  🌙 $resolvedHijri"

                paint.color = Color.rgb(226, 232, 240)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(fullDisplayDate, MARGIN + 14f, y + 62f, paint)
            } else {
                paint.color = Color.WHITE
                paint.textSize = 15f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("👑 تقرير التسميع والسَّبْر اليومي 👑", PAGE_WIDTH / 2f, y + 26f, paint)

                paint.textSize = 11f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                val sub = "🌿 $instituteName  •  $teacherOrCircleName 🌿"
                canvas.drawText(sub, PAGE_WIDTH / 2f, y + 46f, paint)

                val resolvedHijri = hijriDateText?.ifBlank { null }
                    ?: HijriDateHelper.formatHijriFromDateString(dateText, includeDayName = false).ifBlank { null }
                    ?: HijriDateHelper.getTodayHijri(includeDayName = false)
                val fullDisplayDate = "📅 $dateText م  •  🌙 $resolvedHijri"

                paint.color = Color.rgb(254, 240, 138)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(fullDisplayDate, PAGE_WIDTH / 2f, y + 66f, paint)
            }

            y += headerHeight + 10f

            paint.color = Color.rgb(241, 245, 249)
            val quoteRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 30f)
            canvas.drawRoundRect(quoteRect, 8f, 8f, paint)

            paint.color = Color.rgb(217, 119, 6)
            canvas.drawRoundRect(RectF(PAGE_WIDTH - MARGIN - 4f, y, PAGE_WIDTH - MARGIN, y + 30f), 4f, 4f, paint)

            paint.color = Color.rgb(30, 41, 59)
            paint.textSize = 9.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("💎 « ليس المهم أن تكون البداية قوية فقط، بل أن يستمر المثابر حتى يصل إلى الإتقان. »", PAGE_WIDTH / 2f, y + 19f, paint)

            y += 40f
        }

        fun drawFooter() {
            paint.color = Color.rgb(148, 163, 184)
            paint.textSize = 8.5f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("👏🏻 بارك الله في جهود أبطالنا ونفع بهم الإسلام والمسلمين  •  تطبيق معهد المقرئ", PAGE_WIDTH / 2f, PAGE_HEIGHT - 18f, paint)
        }

        drawHeader()

        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 12f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("📊 حصيلة إنجاز الطلاب:", PAGE_WIDTH - MARGIN - 4f, y + 14f, paint)
        y += 24f

        presentRows.forEachIndexed { index, row ->
            val stId = row.student.studentId
            val mems = memByStudent[stId].orEmpty()
            val exms = examByStudent[stId].orEmpty()
            val revs = revByStudent[stId].orEmpty()

            val linesCount = listOf(exms.isNotEmpty(), mems.isNotEmpty(), revs.isNotEmpty()).count { it }
            val cardHeight = if (linesCount == 0) 42f else (32f + (linesCount * 18f))

            if (y + cardHeight > PAGE_HEIGHT - 40f) {
                drawFooter()
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN
                drawHeader()
            }

            val cardRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + cardHeight)
            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)

            paint.color = Color.rgb(226, 232, 240)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(cardRect, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            val badgeNumber = "${index + 1}"
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawCircle(PAGE_WIDTH - MARGIN - 18f, y + 18f, 10f, paint)

            paint.color = Color.WHITE
            paint.textSize = 9f
            paint.textAlign = Paint.Align.CENTER
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(badgeNumber, PAGE_WIDTH - MARGIN - 18f, y + 21.5f, paint)

            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 11.5f
            paint.textAlign = Paint.Align.RIGHT
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(row.student.name, PAGE_WIDTH - MARGIN - 34f, y + 22f, paint)

            var lineY = y + 36f

            if (exms.isNotEmpty()) {
                val exmText = exms.joinToString(" — ") { e ->
                    val scoreStr = if (e.score > 0) " (${e.mastery} ${e.score}%)" else " (${e.mastery})"
                    "${e.surah}$scoreStr"
                }
                paint.color = Color.rgb(185, 28, 28)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("🎯 السَّبْر:", PAGE_WIDTH - MARGIN - 16f, lineY, paint)

                paint.color = Color.rgb(51, 65, 85)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(exmText, PAGE_WIDTH - MARGIN - 62f, lineY, paint)
                lineY += 16f
            }

            if (mems.isNotEmpty()) {
                val memItems = mems.map { m ->
                    val amt = m.amount.ifBlank { "" }
                    val base = if (amt.isNotBlank()) "${m.surah} ($amt)" else m.surah
                    if (m.memorizationCategory == MemorizationCategory.OLD) "$base [سابق]" else base
                }
                val rating = mems.firstOrNull()?.rating?.ifBlank { "ممتاز" } ?: "ممتاز"
                val memText = "${memItems.joinToString("، ")} ($rating)"

                paint.color = Color.rgb(16, 185, 129)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("📖 الحفظ:", PAGE_WIDTH - MARGIN - 16f, lineY, paint)

                paint.color = Color.rgb(51, 65, 85)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(memText, PAGE_WIDTH - MARGIN - 62f, lineY, paint)
                lineY += 16f
            }

            if (revs.isNotEmpty()) {
                val revItems = revs.map { r ->
                    if (r.revisionCategory == RevisionCategory.OLD) "${r.content} [قديم]" else r.content
                }
                val rating = revs.firstOrNull()?.rating?.ifBlank { "جيد" } ?: "جيد"
                val revText = "${revItems.joinToString("، ")} ($rating)"

                paint.color = Color.rgb(2, 132, 199)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("🔄 المراجعة:", PAGE_WIDTH - MARGIN - 16f, lineY, paint)

                paint.color = Color.rgb(51, 65, 85)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(revText, PAGE_WIDTH - MARGIN - 72f, lineY, paint)
            }

            if (linesCount == 0) {
                paint.color = Color.rgb(100, 116, 139)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText("حاضر في الحلقة", PAGE_WIDTH - MARGIN - 16f, lineY, paint)
            }

            y += cardHeight + 8f
        }

        if (absentRows.isNotEmpty() || lateRows.isNotEmpty()) {
            val neededHeight = (if (absentRows.isNotEmpty()) 28f + (absentRows.size * 14f) else 0f) +
                    (if (lateRows.isNotEmpty()) 28f + (lateRows.size * 14f) else 0f)

            if (y + neededHeight > PAGE_HEIGHT - 40f) {
                drawFooter()
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN
                drawHeader()
            }

            if (absentRows.isNotEmpty()) {
                y += 6f
                paint.color = Color.rgb(255, 241, 242)
                val absentBox = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 24f + (absentRows.size * 14f))
                canvas.drawRoundRect(absentBox, 8f, 8f, paint)

                paint.color = Color.rgb(225, 29, 72)
                paint.textSize = 10.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("❌ الطلاب الغائبون:", PAGE_WIDTH - MARGIN - 12f, y + 16f, paint)

                var absY = y + 32f
                paint.color = Color.rgb(71, 85, 105)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                absentRows.forEach { r ->
                    canvas.drawText("• ${r.student.name}", PAGE_WIDTH - MARGIN - 18f, absY, paint)
                    absY += 14f
                }
                y = absentBox.bottom + 6f
            }

            if (lateRows.isNotEmpty()) {
                paint.color = Color.rgb(254, 252, 232)
                val lateBox = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 24f + (lateRows.size * 14f))
                canvas.drawRoundRect(lateBox, 8f, 8f, paint)

                paint.color = Color.rgb(217, 119, 6)
                paint.textSize = 10.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("⏰ الطلاب المتأخرون:", PAGE_WIDTH - MARGIN - 12f, y + 16f, paint)

                var lateY = y + 32f
                paint.color = Color.rgb(71, 85, 105)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                lateRows.forEach { r ->
                    canvas.drawText("• ${r.student.name}", PAGE_WIDTH - MARGIN - 18f, lateY, paint)
                    lateY += 14f
                }
                y = lateBox.bottom + 6f
            }
        }

        drawFooter()
        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val sanitizedDate = dateText.replace("/", "-").replace(" ", "_").replace(":", "")
        val outputFile = File(reportsDir, "daily_report_${sanitizedDate}.pdf")

        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }

    fun generateStudentProfilePdf(
        context: Context,
        student: Student,
        analytics: StudentAnalytics,
        memorizations: List<Memorization>,
        exams: List<Exam>,
        revisions: List<Revision>,
        instituteName: String,
    ): File {
        val pdfDocument = PdfDocument()
        val logoBitmap = getLogoBitmap(context)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }

        val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar"))
        val todayText = dateFormat.format(Date())
        val hijriText = HijriDateHelper.getTodayHijri(includeDayName = false)
        val fullDateDisplay = "$todayText م  •  🌙 $hijriText"

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        fun drawHeader() {
            val headerHeight = 90f
            val headerRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + headerHeight)
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawRoundRect(headerRect, 12f, 12f, paint)

            paint.color = Color.rgb(217, 119, 6)
            canvas.drawRoundRect(RectF(MARGIN + 8f, y + headerHeight - 6f, PAGE_WIDTH - MARGIN - 8f, y + headerHeight - 2f), 3f, 3f, paint)

            if (logoBitmap != null) {
                val logoSize = 64f
                val logoRect = RectF(PAGE_WIDTH - MARGIN - logoSize - 10f, y + 14f, PAGE_WIDTH - MARGIN - 10f, y + 14f + logoSize)
                drawCircularLogo(canvas, logoBitmap, logoRect, borderColor = Color.rgb(217, 119, 6), borderWidth = 2.5f)

                val textStartX = PAGE_WIDTH - MARGIN - logoSize - 20f

                paint.color = Color.WHITE
                paint.textSize = 13.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("🌟 تقرير إنجاز الطالب القرآني", textStartX, y + 26f, paint)

                paint.color = Color.rgb(203, 213, 225)
                paint.textSize = 10f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText("🌿 $instituteName", textStartX, y + 44f, paint)

                val teacherDisplay = student.teacherName.ifBlank { "معلم الحلقة" }
                paint.color = Color.rgb(254, 240, 138)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("👨‍🏫 المعلم المشرف: $teacherDisplay", textStartX, y + 62f, paint)

                paint.color = Color.rgb(226, 232, 240)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("📅 $fullDateDisplay", MARGIN + 14f, y + 62f, paint)
            } else {
                paint.color = Color.WHITE
                paint.textSize = 15f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("🌟 تقرير إنجاز الطالب القرآني الشامل 🌟", PAGE_WIDTH / 2f, y + 26f, paint)

                paint.textSize = 11f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                val sub = "🌿 $instituteName  •  حلقة القرآن الكريم 🌿"
                canvas.drawText(sub, PAGE_WIDTH / 2f, y + 46f, paint)

                paint.color = Color.rgb(254, 240, 138)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("📅 $fullDateDisplay", PAGE_WIDTH / 2f, y + 66f, paint)
            }

            y += headerHeight + 10f
        }

        fun drawFooter() {
            paint.color = Color.rgb(148, 163, 184)
            paint.textSize = 8.5f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("👏🏻 بارك الله في جهود الطالب ووفقه لخدمة كتاب الله  •  تطبيق معهد المقرئ", PAGE_WIDTH / 2f, PAGE_HEIGHT - 18f, paint)
        }

        drawHeader()

        // 1. بطاقة الطالب الرئيسية
        val studentCardRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 46f)
        paint.color = Color.rgb(248, 250, 252)
        canvas.drawRoundRect(studentCardRect, 8f, 8f, paint)

        paint.color = Color.rgb(226, 232, 240)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(studentCardRect, 8f, 8f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 12.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("👤 الطالب: ${student.name}", PAGE_WIDTH - MARGIN - 14f, y + 18f, paint)

        paint.color = Color.rgb(71, 85, 105)
        paint.textSize = 9.5f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val details = buildString {
            if (student.age > 0) append("العمر: ${student.age} سنة    |    ")
            if (student.parentPhone.isNotBlank()) append("ولي الأمر: ${student.parentPhone}")
        }.ifBlank { "طالب مسجل في المعهد" }
        canvas.drawText(details, PAGE_WIDTH - MARGIN - 14f, y + 34f, paint)

        y += 54f

        // 2. إحصاءات الإنجاز السريعة
        val pillWidth = (PAGE_WIDTH - (MARGIN * 2) - 24f) / 4f
        val pillHeight = 46f

        val ajzaaDisplay = if (analytics.distinctAjzaaCount == 30) "30 (كامل)" else if (analytics.distinctAjzaaCount > 0) "${analytics.distinctAjzaaCount} من 30" else "0"
        val surahsDisplay = if (analytics.distinctSurahsCount == 114) "114 سورة" else "${analytics.distinctSurahsCount}"

        val stats = listOf(
            Triple("الأجزاء المنجزة", ajzaaDisplay, Color.rgb(217, 119, 6)),
            Triple("السور المتقنة", surahsDisplay, Color.rgb(16, 185, 129)),
            Triple("متوسط السَّبْر", if (analytics.totalExams > 0) "${analytics.averageScore.toInt()}%" else "—", Color.rgb(185, 28, 28)),
            Triple("المراجعات", "${analytics.totalRevisions}", Color.rgb(2, 132, 199)),
        )

        stats.forEachIndexed { i, stat ->
            val px = PAGE_WIDTH - MARGIN - ((i + 1) * pillWidth) - (i * 8f)
            val pRect = RectF(px, y, px + pillWidth, y + pillHeight)

            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(pRect, 8f, 8f, paint)

            paint.color = Color.rgb(226, 232, 240)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(pRect, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            paint.color = stat.third
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(stat.second, px + (pillWidth / 2f), y + 18f, paint)

            paint.color = Color.rgb(100, 116, 139)
            paint.textSize = 8f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(stat.first, px + (pillWidth / 2f), y + 36f, paint)
        }

        y += pillHeight + 14f

        // 3. الأجزاء والسور المنجزة
        if (analytics.distinctAjzaaCount == 30) {
            val khatmRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 36f)
            paint.color = Color.rgb(254, 243, 199)
            canvas.drawRoundRect(khatmRect, 8f, 8f, paint)

            paint.color = Color.rgb(217, 119, 6)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1.5f
            canvas.drawRoundRect(khatmRect, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            paint.color = Color.rgb(146, 64, 14)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("👑 بفضل الله ومنته، أتم الطالب إنجاز وحفظ القرآن الكريم كاملاً (30 جزءاً - 114 سورة) 🌿", PAGE_WIDTH / 2f, y + 22f, paint)

            y += 48f
        } else {
            if (analytics.distinctAjzaaList.isNotEmpty()) {
                val ajzaaText = analytics.distinctAjzaaList.joinToString(" • ")
                paint.color = Color.rgb(180, 83, 9)
                paint.textSize = 10.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("🕋 الأجزاء المنجزة (${analytics.distinctAjzaaCount} من 30 جزءاً - إتمام ${analytics.quranCompletionPercentage}%):", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
                y += 18f

                val ajzaaRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 28f)
                paint.color = Color.rgb(254, 243, 199)
                canvas.drawRoundRect(ajzaaRect, 6f, 6f, paint)

                paint.color = Color.rgb(120, 53, 15)
                paint.textSize = 9f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText(ajzaaText, PAGE_WIDTH - MARGIN - 12f, y + 18f, paint)

                y += 36f
            }

            if (analytics.distinctSurahsList.isNotEmpty()) {
                val surahsText = analytics.distinctSurahsList.joinToString(" • ")
                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 10.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("📖 السور المتقنة (${analytics.distinctSurahsCount} سورة):", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
                y += 18f

                val surahsRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 32f)
                paint.color = Color.rgb(241, 245, 249)
                canvas.drawRoundRect(surahsRect, 6f, 6f, paint)

                paint.color = Color.rgb(30, 41, 59)
                paint.textSize = 9f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText(surahsText, PAGE_WIDTH - MARGIN - 12f, y + 20f, paint)

                y += 40f
            }
        }

        // 4. سجل الحفظ
        if (memorizations.isNotEmpty()) {
            paint.color = Color.rgb(16, 185, 129)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("📖 آخر عمليات الحفظ والتسميع:", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
            y += 18f

            memorizations.take(6).forEach { mem ->
                if (y + 24f > PAGE_HEIGHT - 40f) {
                    drawFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 22f)
                paint.color = Color.rgb(248, 250, 252)
                canvas.drawRoundRect(rowRect, 4f, 4f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val targetName = QuranData.formatQuranTargetName(mem.surah)
                val amt = mem.amount.ifBlank { "الآيات ${mem.fromVerse}-${mem.toVerse}" }
                val tag = if (mem.memorizationCategory == MemorizationCategory.OLD) "[سابق] " else ""
                canvas.drawText("$tag $targetName ($amt)", PAGE_WIDTH - MARGIN - 12f, y + 15f, paint)

                paint.color = Color.rgb(16, 185, 129)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("التقييم: ${mem.rating}  •  ${mem.date.toDateText()}", MARGIN + 12f, y + 15f, paint)

                y += 26f
            }
            y += 8f
        }

        // 5. سجل السَّبْر
        if (exams.isNotEmpty()) {
            paint.color = Color.rgb(185, 28, 28)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("🎯 آخر اختبارات السَّبْر:", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
            y += 18f

            exams.take(6).forEach { ex ->
                if (y + 24f > PAGE_HEIGHT - 40f) {
                    drawFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 22f)
                paint.color = Color.rgb(255, 241, 242)
                canvas.drawRoundRect(rowRect, 4f, 4f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val targetName = QuranData.formatQuranTargetName(ex.surah)
                canvas.drawText("$targetName (الدرجة: ${ex.score}/100)", PAGE_WIDTH - MARGIN - 12f, y + 15f, paint)

                paint.color = Color.rgb(185, 28, 28)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("الإتقان: ${ex.mastery}  •  ${ex.date.toDateText()}", MARGIN + 12f, y + 15f, paint)

                y += 26f
            }
            y += 8f
        }

        // 6. سجل المراجعة
        if (revisions.isNotEmpty()) {
            paint.color = Color.rgb(2, 132, 199)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("🔄 آخر عمليات المراجعة والتمكين:", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
            y += 18f

            revisions.take(6).forEach { rev ->
                if (y + 24f > PAGE_HEIGHT - 40f) {
                    drawFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 22f)
                paint.color = Color.rgb(240, 249, 255)
                canvas.drawRoundRect(rowRect, 4f, 4f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val tag = if (rev.revisionCategory == RevisionCategory.OLD) "[قديم] " else ""
                canvas.drawText("$tag ${rev.content}", PAGE_WIDTH - MARGIN - 12f, y + 15f, paint)

                paint.color = Color.rgb(2, 132, 199)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("التقييم: ${rev.rating} (${rev.level})  •  ${rev.date.toDateText()}", MARGIN + 12f, y + 15f, paint)

                y += 26f
            }
            y += 8f
        }

        drawFooter()
        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val sanitizedName = student.name.replace(" ", "_")
        val outputFile = File(reportsDir, "student_report_${sanitizedName}.pdf")

        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }

    fun generateStudentAdminPdf(
        context: Context,
        student: Student,
        analytics: StudentAnalytics,
        memorizations: List<Memorization>,
        exams: List<Exam>,
        revisions: List<Revision>,
        instituteName: String,
    ): File {
        val pdfDocument = PdfDocument()
        val logoBitmap = getLogoBitmap(context)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }

        val dateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val todayText = dateFormat.format(Date())
        val hijriAdminText = HijriDateHelper.getTodayHijri(includeDayName = false)

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        fun drawAdminHeader() {
            val headerHeight = 88f
            val headerRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + headerHeight)
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawRoundRect(headerRect, 10f, 10f, paint)

            paint.color = Color.rgb(56, 189, 248)
            canvas.drawRoundRect(RectF(MARGIN + 6f, y + headerHeight - 5f, PAGE_WIDTH - MARGIN - 6f, y + headerHeight - 2f), 2f, 2f, paint)

            if (logoBitmap != null) {
                val logoSize = 60f
                val logoRect = RectF(PAGE_WIDTH - MARGIN - logoSize - 10f, y + 14f, PAGE_WIDTH - MARGIN - 10f, y + 14f + logoSize)
                drawCircularLogo(canvas, logoBitmap, logoRect, borderColor = Color.rgb(56, 189, 248), borderWidth = 2.5f)

                val textStartX = PAGE_WIDTH - MARGIN - logoSize - 18f

                paint.color = Color.WHITE
                paint.textSize = 13f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("📋 التقرير الأكاديمي الإداري للطالب", textStartX, y + 24f, paint)

                paint.color = Color.rgb(203, 213, 225)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                canvas.drawText("🏢 $instituteName  •  السجل الأكاديمي الرسمي", textStartX, y + 42f, paint)

                val teacherDisplay = student.teacherName.ifBlank { "غير محدد" }
                paint.color = Color.rgb(56, 189, 248)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("👨‍🏫 المعلم المشرف: $teacherDisplay", textStartX, y + 58f, paint)

                paint.color = Color.rgb(226, 232, 240)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText("تاريخ الإصدار: $todayText م  •  🌙 $hijriAdminText", MARGIN + 12f, y + 58f, paint)
            } else {
                paint.color = Color.WHITE
                paint.textSize = 14f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.CENTER
                canvas.drawText("📋 التقرير الأكاديمي الإداري للطالب", PAGE_WIDTH / 2f, y + 24f, paint)

                paint.textSize = 10f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                val sub = "🏢 $instituteName  •  السجل الأكاديمي الرسمي"
                canvas.drawText(sub, PAGE_WIDTH / 2f, y + 42f, paint)

                paint.color = Color.rgb(226, 232, 240)
                paint.textSize = 9f
                canvas.drawText("تاريخ الإصدار: $todayText م  •  🌙 $hijriAdminText", PAGE_WIDTH / 2f, y + 56f, paint)
            }

            y += headerHeight + 10f
        }

        fun drawAdminFooter() {
            paint.color = Color.rgb(148, 163, 184)
            paint.textSize = 8f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("سجل إداري معتمد  •  تطبيق معهد المقرئ  •  صفحة $pageNumber", PAGE_WIDTH / 2f, PAGE_HEIGHT - 16f, paint)
        }

        drawAdminHeader()

        // 1. جدول بيانات الطالب
        val infoRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 42f)
        paint.color = Color.rgb(241, 245, 249)
        canvas.drawRoundRect(infoRect, 6f, 6f, paint)

        paint.color = Color.rgb(203, 213, 225)
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 1f
        canvas.drawRoundRect(infoRect, 6f, 6f, paint)
        paint.style = Paint.Style.FILL

        paint.color = Color.rgb(15, 23, 42)
        paint.textSize = 11f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
        paint.textAlign = Paint.Align.RIGHT
        canvas.drawText("الطالب: ${student.name}", PAGE_WIDTH - MARGIN - 12f, y + 16f, paint)

        paint.color = Color.rgb(71, 85, 105)
        paint.textSize = 9f
        paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
        val line1 = "العمر: ${if (student.age > 0) "${student.age} سنة" else "—"}    |    هاتف ولي الأمر: ${student.parentPhone.ifBlank { "—" }}"
        canvas.drawText(line1, PAGE_WIDTH - MARGIN - 12f, y + 32f, paint)

        y += 50f

        // 2. الخلاصة الرقمية (KPI Grid)
        val pillWidth = (PAGE_WIDTH - (MARGIN * 2) - 24f) / 4f
        val pillHeight = 40f

        val stats = listOf(
            Triple("الأجزاء المنجزة", if (analytics.distinctAjzaaCount > 0) "${analytics.distinctAjzaaCount}/30" else "0", Color.rgb(15, 23, 42)),
            Triple("السور المتقنة", "${analytics.distinctSurahsCount}", Color.rgb(16, 185, 129)),
            Triple("متوسط السَّبْر", if (analytics.totalExams > 0) "${analytics.averageScore.toInt()}% (${analytics.totalExams})" else "—", Color.rgb(185, 28, 28)),
            Triple("المراجعات", "${analytics.totalRevisions}", Color.rgb(2, 132, 199)),
        )

        stats.forEachIndexed { i, stat ->
            val px = PAGE_WIDTH - MARGIN - ((i + 1) * pillWidth) - (i * 8f)
            val pRect = RectF(px, y, px + pillWidth, y + pillHeight)

            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(pRect, 6f, 6f, paint)

            paint.color = Color.rgb(226, 232, 240)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(pRect, 6f, 6f, paint)
            paint.style = Paint.Style.FILL

            paint.color = stat.third
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(stat.second, px + (pillWidth / 2f), y + 16f, paint)

            paint.color = Color.rgb(100, 116, 139)
            paint.textSize = 7.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(stat.first, px + (pillWidth / 2f), y + 30f, paint)
        }

        y += pillHeight + 12f

        // 3. الأجزاء والسور المنجزة
        if (analytics.distinctAjzaaList.isNotEmpty()) {
            paint.color = Color.rgb(180, 83, 9)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("الأجزاء المنجزة (${analytics.distinctAjzaaCount} من 30 جزءاً):", PAGE_WIDTH - MARGIN - 4f, y + 10f, paint)
            y += 16f

            val ajzaaText = analytics.distinctAjzaaList.joinToString(" • ")
            val ajzaaRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 26f)
            paint.color = Color.rgb(254, 243, 199)
            canvas.drawRoundRect(ajzaaRect, 4f, 4f, paint)

            paint.color = Color.rgb(120, 53, 15)
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            canvas.drawText(ajzaaText, PAGE_WIDTH - MARGIN - 8f, y + 16f, paint)

            y += 34f
        }

        if (analytics.distinctSurahsList.isNotEmpty()) {
            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("السور المتقنة (${analytics.distinctSurahsCount}):", PAGE_WIDTH - MARGIN - 4f, y + 10f, paint)
            y += 16f

            val surahsText = analytics.distinctSurahsList.joinToString(" • ")
            val surahsRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 30f)
            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(surahsRect, 4f, 4f, paint)

            paint.color = Color.rgb(51, 65, 85)
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(surahsText, PAGE_WIDTH - MARGIN - 8f, y + 18f, paint)

            y += 38f
        }

        // 4. سجل السبر
        if (exams.isNotEmpty()) {
            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("سجل اختبارات السَّبْر (${exams.size} اختبار):", PAGE_WIDTH - MARGIN - 4f, y + 10f, paint)
            y += 16f

            exams.take(8).forEach { ex ->
                if (y + 20f > PAGE_HEIGHT - 60f) {
                    drawAdminFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawAdminHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 18f)
                paint.color = Color.rgb(248, 250, 252)
                canvas.drawRoundRect(rowRect, 3f, 3f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val targetName = QuranData.formatQuranTargetName(ex.surah)
                canvas.drawText("• $targetName — الدرجة: ${ex.score}% (${ex.mastery})", PAGE_WIDTH - MARGIN - 8f, y + 12f, paint)

                paint.color = Color.rgb(100, 116, 139)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(ex.date.toDateText(), MARGIN + 8f, y + 12f, paint)

                y += 22f
            }
            y += 6f
        }

        // 5. سجل الحفظ
        if (memorizations.isNotEmpty()) {
            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("سجل الحفظ والتسميع (${memorizations.size} عملية):", PAGE_WIDTH - MARGIN - 4f, y + 10f, paint)
            y += 16f

            memorizations.take(8).forEach { mem ->
                if (y + 20f > PAGE_HEIGHT - 60f) {
                    drawAdminFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawAdminHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 18f)
                paint.color = Color.rgb(248, 250, 252)
                canvas.drawRoundRect(rowRect, 3f, 3f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val targetName = QuranData.formatQuranTargetName(mem.surah)
                val amt = mem.amount.ifBlank { "الآيات ${mem.fromVerse}-${mem.toVerse}" }
                val tag = if (mem.memorizationCategory == MemorizationCategory.OLD) "[سابق] " else "[جديد] "
                canvas.drawText("• $tag $targetName ($amt) — ${mem.rating}", PAGE_WIDTH - MARGIN - 8f, y + 12f, paint)

                paint.color = Color.rgb(100, 116, 139)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(mem.date.toDateText(), MARGIN + 8f, y + 12f, paint)

                y += 22f
            }
            y += 6f
        }

        // 6. سجل المراجعة
        if (revisions.isNotEmpty()) {
            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("سجل المراجعة والتمكين (${revisions.size} عملية):", PAGE_WIDTH - MARGIN - 4f, y + 10f, paint)
            y += 16f

            revisions.take(8).forEach { rev ->
                if (y + 20f > PAGE_HEIGHT - 60f) {
                    drawAdminFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawAdminHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 18f)
                paint.color = Color.rgb(248, 250, 252)
                canvas.drawRoundRect(rowRect, 3f, 3f, paint)

                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 8.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                paint.textAlign = Paint.Align.RIGHT
                val tag = if (rev.revisionCategory == RevisionCategory.OLD) "[قديم] " else "[جديد] "
                canvas.drawText("• $tag ${rev.content} — ${rev.rating} (${rev.level})", PAGE_WIDTH - MARGIN - 8f, y + 12f, paint)

                paint.color = Color.rgb(100, 116, 139)
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.LEFT
                canvas.drawText(rev.date.toDateText(), MARGIN + 8f, y + 12f, paint)

                y += 22f
            }
            y += 6f
        }

        // 7. توقيع وختم الإدارة
        if (y + 40f <= PAGE_HEIGHT - 30f) {
            y += 10f
            paint.color = Color.rgb(100, 116, 139)
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("اعتماد المعلم المشرف: ....................", PAGE_WIDTH - MARGIN - 12f, y + 16f, paint)

            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("ختم واعتماد إدارة المعهد: ....................", MARGIN + 12f, y + 16f, paint)
        }

        drawAdminFooter()
        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val sanitizedName = student.name.replace(" ", "_")
        val outputFile = File(reportsDir, "admin_report_${sanitizedName}.pdf")

        FileOutputStream(outputFile).use { out ->
            pdfDocument.writeTo(out)
        }
        pdfDocument.close()

        return outputFile
    }

    /** توليد تقرير PDF شامل لحلقة واحدة أو كامل المعهد */
    fun generateCircleReportPdf(
        context: Context,
        instituteName: String,
        circleName: String,        // "المعهد بأكمله" أو اسم الحلقة
        teacherName: String,       // اسم المعلم أو ""
        stats: com.omran.quran.data.model.CircleStats,
        students: List<com.omran.quran.data.model.Student>,
        memCounts: Map<String, Int>,   // studentId -> count
        revCounts: Map<String, Int>,
        examCounts: Map<String, Int>,
        genderFilter: String? = null,  // null = الكل, "MALE" = بنين, "FEMALE" = بنات
    ): File {
        val pdfDocument = PdfDocument()
        val logoBitmap = getLogoBitmap(context)
        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply { isFilterBitmap = true }
        val dateFormat = SimpleDateFormat("EEEE d MMMM yyyy", Locale("ar"))
        val todayText = dateFormat.format(Date())
        val hijriCircleText = HijriDateHelper.getTodayHijri(includeDayName = false)
        val fullCircleDate = "$todayText م  •  🌙 $hijriCircleText"

        var pageNumber = 1
        var pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
        var page = pdfDocument.startPage(pageInfo)
        var canvas = page.canvas
        var y = MARGIN

        val genderSuffix = when (genderFilter) {
            "MALE" -> " (بنين)"
            "FEMALE" -> " (بنات)"
            else -> ""
        }

        fun drawHeader() {
            val headerHeight = 96f
            val headerRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + headerHeight)
            // خلفية داكنة نيلية
            paint.color = Color.rgb(15, 23, 42)
            canvas.drawRoundRect(headerRect, 12f, 12f, paint)
            // شريط ذهبي سفلي
            paint.color = Color.rgb(217, 119, 6)
            canvas.drawRoundRect(RectF(MARGIN + 8f, y + headerHeight - 6f, PAGE_WIDTH - MARGIN - 8f, y + headerHeight - 2f), 3f, 3f, paint)

            val textStartX = PAGE_WIDTH - MARGIN - 14f

            if (logoBitmap != null) {
                val logoSize = 68f
                val logoRect = RectF(MARGIN + 10f, y + 14f, MARGIN + 10f + logoSize, y + 14f + logoSize)
                drawCircularLogo(canvas, logoBitmap, logoRect, borderColor = Color.rgb(217, 119, 6), borderWidth = 2.5f)
            }

            paint.color = Color.WHITE
            paint.textSize = 13.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("📊 تقرير حلقة: $circleName$genderSuffix", textStartX, y + 26f, paint)

            paint.color = Color.rgb(203, 213, 225)
            paint.textSize = 10f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText("🌿 $instituteName", textStartX, y + 44f, paint)

            if (teacherName.isNotBlank()) {
                paint.color = Color.rgb(254, 240, 138)
                paint.textSize = 9.5f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("👨‍🏫 المعلم: $teacherName", textStartX, y + 62f, paint)
            }

            paint.color = Color.rgb(226, 232, 240)
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("📅 $fullCircleDate", MARGIN + 90f, y + 80f, paint)

            y += headerHeight + 12f
        }

        fun drawFooter() {
            paint.color = Color.rgb(148, 163, 184)
            paint.textSize = 8f
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("📋 تقرير إداري شامل  •  تطبيق معهد المقرئ  •  صفحة $pageNumber", PAGE_WIDTH / 2f, PAGE_HEIGHT - 16f, paint)
        }

        drawHeader()

        // ── قسم الإحصائيات الكلية ──────────────────────────────────────────────
        val studentPillLabel = when (genderFilter) {
            "MALE" -> "👦 بنين: ${stats.studentCount}"
            "FEMALE" -> "👧 بنات: ${stats.studentCount}"
            else -> "👥 طلاب (${stats.maleStudentCount}بنين • ${stats.femaleStudentCount}بنات)"
        }

        val pillW = (PAGE_WIDTH - MARGIN * 2 - 24f) / 4f
        val pillH = 52f
        val pillData = listOf(
            Triple(studentPillLabel, "${stats.studentCount}", Color.rgb(99, 102, 241)),
            Triple("📖 جلسات الحفظ", "${stats.memorizationCount}", Color.rgb(16, 185, 129)),
            Triple("🔄 المراجعات", "${stats.revisionCount}", Color.rgb(217, 119, 6)),
            Triple("🎯 اختبارات السبر", "${stats.examCount}", Color.rgb(220, 38, 38)),
        )

        pillData.forEachIndexed { i, (label, value, color) ->
            val px = PAGE_WIDTH - MARGIN - ((i + 1) * pillW) - (i * 8f)
            val pRect = RectF(px, y, px + pillW, y + pillH)

            paint.color = Color.rgb(248, 250, 252)
            canvas.drawRoundRect(pRect, 8f, 8f, paint)
            paint.color = Color.rgb(226, 232, 240)
            paint.style = Paint.Style.STROKE
            paint.strokeWidth = 1f
            canvas.drawRoundRect(pRect, 8f, 8f, paint)
            paint.style = Paint.Style.FILL

            paint.color = color
            paint.textSize = 15f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText(value, px + pillW / 2f, y + 22f, paint)

            paint.color = Color.rgb(71, 85, 105)
            paint.textSize = 7f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            canvas.drawText(label, px + pillW / 2f, y + 40f, paint)
        }

        y += pillH + 14f

        // ── قسم قائمة الطلاب ──────────────────────────────────────────────────
        fun renderStudentGroup(groupTitle: String, groupStudents: List<com.omran.quran.data.model.Student>) {
            if (groupStudents.isEmpty()) return

            if (y + 50f > PAGE_HEIGHT - 40f) {
                drawFooter()
                pdfDocument.finishPage(page)
                pageNumber++
                pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                page = pdfDocument.startPage(pageInfo)
                canvas = page.canvas
                y = MARGIN
                drawHeader()
            }

            paint.color = Color.rgb(15, 23, 42)
            paint.textSize = 11f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("$groupTitle (${groupStudents.size}):", PAGE_WIDTH - MARGIN - 4f, y + 12f, paint)
            y += 20f

            // رأس الجدول
            val headerRow = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + 20f)
            paint.color = Color.rgb(30, 41, 59)
            canvas.drawRoundRect(headerRow, 4f, 4f, paint)

            paint.color = Color.WHITE
            paint.textSize = 8.5f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
            paint.textAlign = Paint.Align.CENTER
            canvas.drawText("اسم الطالب", PAGE_WIDTH - MARGIN - 80f, y + 13f, paint)
            canvas.drawText("الحفظ", PAGE_WIDTH - MARGIN - 190f, y + 13f, paint)
            canvas.drawText("المراجعة", PAGE_WIDTH - MARGIN - 270f, y + 13f, paint)
            canvas.drawText("السبر", PAGE_WIDTH - MARGIN - 350f, y + 13f, paint)
            canvas.drawText("الحلقة", MARGIN + 60f, y + 13f, paint)
            y += 24f

            groupStudents.forEachIndexed { index, student ->
                val mem = memCounts[student.studentId] ?: 0
                val rev = revCounts[student.studentId] ?: 0
                val exam = examCounts[student.studentId] ?: 0

                val rowH = 22f
                if (y + rowH > PAGE_HEIGHT - 40f) {
                    drawFooter()
                    pdfDocument.finishPage(page)
                    pageNumber++
                    pageInfo = PdfDocument.PageInfo.Builder(PAGE_WIDTH, PAGE_HEIGHT, pageNumber).create()
                    page = pdfDocument.startPage(pageInfo)
                    canvas = page.canvas
                    y = MARGIN
                    drawHeader()
                }

                val rowRect = RectF(MARGIN, y, PAGE_WIDTH - MARGIN, y + rowH)
                paint.color = if (index % 2 == 0) Color.rgb(248, 250, 252) else Color.WHITE
                canvas.drawRoundRect(rowRect, 3f, 3f, paint)

                // رقم
                paint.color = Color.rgb(148, 163, 184)
                paint.textSize = 8f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.RIGHT
                canvas.drawText("${index + 1}", PAGE_WIDTH - MARGIN - 8f, y + 14f, paint)

                // اسم الطالب مع أيقونة الجنس
                val genderIcon = if (student.isFemale) "👧" else "👦"
                paint.color = Color.rgb(15, 23, 42)
                paint.textSize = 9f
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD)
                canvas.drawText("$genderIcon ${student.name}", PAGE_WIDTH - MARGIN - 24f, y + 14f, paint)

                // الإحصائيات
                paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
                paint.textAlign = Paint.Align.CENTER
                paint.color = Color.rgb(16, 185, 129)
                canvas.drawText("$mem", PAGE_WIDTH - MARGIN - 190f, y + 14f, paint)
                paint.color = Color.rgb(217, 119, 6)
                canvas.drawText("$rev", PAGE_WIDTH - MARGIN - 270f, y + 14f, paint)
                paint.color = Color.rgb(220, 38, 38)
                canvas.drawText("$exam", PAGE_WIDTH - MARGIN - 350f, y + 14f, paint)

                // الحلقة
                paint.color = Color.rgb(100, 116, 139)
                paint.textSize = 7.5f
                canvas.drawText(
                    student.circleName.ifBlank { student.teacherName }.take(20),
                    MARGIN + 60f, y + 14f, paint
                )

                y += rowH + 2f
            }
            y += 10f
        }

        if (genderFilter == null) {
            val boys = students.filter { it.isMale }
            val girls = students.filter { it.isFemale }
            if (boys.isNotEmpty()) {
                renderStudentGroup("👦 قسم الطلاب (بنين)", boys)
            }
            if (girls.isNotEmpty()) {
                renderStudentGroup("👧 قسم الطالبات (بنات)", girls)
            }
        } else {
            val title = if (genderFilter == "FEMALE") "👧 قائمة الطالبات (بنات)" else "👦 قائمة الطلاب (بنين)"
            renderStudentGroup(title, students)
        }

        // ── توقيع الاعتماد ────────────────────────────────────────────────────
        if (y + 36f <= PAGE_HEIGHT - 30f) {
            y += 12f
            paint.color = Color.rgb(100, 116, 139)
            paint.textSize = 9f
            paint.typeface = Typeface.create(Typeface.DEFAULT, Typeface.NORMAL)
            paint.textAlign = Paint.Align.RIGHT
            canvas.drawText("اعتماد المعلم المشرف: ....................", PAGE_WIDTH - MARGIN - 12f, y + 14f, paint)
            paint.textAlign = Paint.Align.LEFT
            canvas.drawText("ختم إدارة المعهد: ....................", MARGIN + 12f, y + 14f, paint)
        }

        drawFooter()
        pdfDocument.finishPage(page)

        val reportsDir = File(context.cacheDir, "reports").apply { mkdirs() }
        val safeName = circleName.replace(" ", "_").take(30)
        val outputFile = File(reportsDir, "circle_report_$safeName.pdf")
        FileOutputStream(outputFile).use { out -> pdfDocument.writeTo(out) }
        pdfDocument.close()
        return outputFile
    }

    fun sharePdf(context: Context, pdfFile: File) {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            pdfFile
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "application/pdf"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, "تقرير إنجاز الطالب القرآني")
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "مشاركة تقرير PDF"))
    }
}
