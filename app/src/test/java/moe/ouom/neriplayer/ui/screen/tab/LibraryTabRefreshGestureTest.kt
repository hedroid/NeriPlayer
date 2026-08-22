package moe.ouom.neriplayer.ui.screen.tab

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LibraryTabRefreshGestureTest {
    @Test
    fun `second quick tap on selected refreshable tab refreshes`() {
        assertTrue(
            shouldRefreshLibraryTabOnTap(
                selectedTabIndex = 2,
                tappedTabIndex = 2,
                refreshEnabled = true,
                previousTappedTabIndex = 2,
                elapsedSincePreviousTapMs = 180L
            )
        )
    }

    @Test
    fun `tap on a different tab only selects it`() {
        assertFalse(
            shouldRefreshLibraryTabOnTap(
                selectedTabIndex = 1,
                tappedTabIndex = 2,
                refreshEnabled = true,
                previousTappedTabIndex = 2,
                elapsedSincePreviousTapMs = 180L
            )
        )
    }

    @Test
    fun `slow second tap does not refresh`() {
        assertFalse(
            shouldRefreshLibraryTabOnTap(
                selectedTabIndex = 2,
                tappedTabIndex = 2,
                refreshEnabled = true,
                previousTappedTabIndex = 2,
                elapsedSincePreviousTapMs = 500L
            )
        )
    }

    @Test
    fun `non refreshable selected tab does not refresh`() {
        assertFalse(
            shouldRefreshLibraryTabOnTap(
                selectedTabIndex = 0,
                tappedTabIndex = 0,
                refreshEnabled = false,
                previousTappedTabIndex = 0,
                elapsedSincePreviousTapMs = 180L
            )
        )
    }
}
