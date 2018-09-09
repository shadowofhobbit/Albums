package test.task.albums.search;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import test.task.albums.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class AlbumsActivityTest {
    @Rule
    public ActivityTestRule<AlbumsActivity> rule = new ActivityTestRule<>(AlbumsActivity.class);

    @Test
    public void checkViews() {
        onView(withId(R.id.albumsView)).check(matches(not(isDisplayed())));
        onView(withId(R.id.progressBar)).check(matches(not(isDisplayed())));
        onView(withId(R.id.emptyView)).check(matches(isDisplayed()));
        onView(withId(R.id.emptyView)).check(matches(withText(R.string.no_albums_search_for_something)));
    }

    @Test
    public void checkSearch() {
        onView(withId(R.id.search)).perform(click());
        onView(withHint(R.string.search_hint)).check(matches(isDisplayed()));
    }

}