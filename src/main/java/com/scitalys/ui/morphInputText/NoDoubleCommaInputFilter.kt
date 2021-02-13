package com.scitalys.ui.morphInputText

import android.text.InputFilter
import android.text.Spanned

object NoDoubleCommaInputFilter : InputFilter {

    override fun filter(
        source: CharSequence,
        start: Int,
        end: Int,
        dest: Spanned?,
        dstart: Int,
        dend: Int
    ): CharSequence? {

        if (source.isNotEmpty()) {
            /**
             * Don't let " " or "," on first char after comma
             */
            if (dest?.isNotEmpty() == true && dstart != 0) {
                if (dest[dstart - 1] == ',' || (dstart == 0 && dend == 1)) {
                    if (source == " " || source == ",") {
                        return ""
                    }
                }
            } else {
                if (source == " " || source == ",") {
                    return ""
                }
            }
        }

        return null
    }
}