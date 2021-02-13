package com.scitalys.ui.morphInputText

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.widget.MultiAutoCompleteTextView

object NoSpaceCommaTokenizer : MultiAutoCompleteTextView.Tokenizer {

    override fun findTokenStart(charSequence: CharSequence, cursor: Int): Int {
        var i = cursor

        while (i > 0 && charSequence[i - 1] != ',') {
            i--
        }
        while (i < cursor && charSequence[i] == ',') {
            i++
        }

        return i
    }

    override fun findTokenEnd(charSequence: CharSequence, cursor: Int): Int {
        var i = cursor
        val len = charSequence.length

        while (i < len) {
            if (charSequence[i] == ',') {
                return i
            } else {
                i++
            }
        }

        return len
    }

    override fun terminateToken(charSequence: CharSequence): CharSequence {
        var i = charSequence.length

        while (i > 0 && charSequence[i - 1] == ',') {
            i--
        }

        if (i > 0 && charSequence[i - 1] == ',') {
            return charSequence
        } else if (charSequence is Spanned) {
            val sp = SpannableString("$charSequence,")
            TextUtils.copySpansFrom(
                charSequence,
                0,
                charSequence.length,
                Object::class.java,
                sp,
                0
            )
            return sp
        }
        return "$charSequence,"
    }
}