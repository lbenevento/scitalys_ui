package com.scitalys.ui

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.scitalys.ui.utils.dp
import com.scitalys.ui.utils.getOverlayAlpha
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class GetOverlayAlpha {

    @Test
    fun test() {
        assertEquals(0f, getOverlayAlpha(0f.dp))
        assertEquals(.05f, getOverlayAlpha(1f.dp))
        assertEquals(.07f, getOverlayAlpha(2f.dp))
        assertEquals(.10f, getOverlayAlpha(5f.dp))
        assertEquals(.14f, getOverlayAlpha(12f.dp))
        assertEquals(.16f, getOverlayAlpha(24f.dp))
    }

}