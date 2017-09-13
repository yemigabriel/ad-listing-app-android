package com.doxa360.android.dutch.model;
import android.content.SearchRecentSuggestionsProvider;
import static android.content.SearchRecentSuggestionsProvider.DATABASE_MODE_QUERIES;


public class RecentSuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.doxa360.android.dutch.model.RecentSuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES | DATABASE_MODE_2LINES;

    public RecentSuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }


}