package com.doxa360.android.dutch;

import android.content.SearchRecentSuggestionsProvider;
import android.provider.SearchRecentSuggestions;

/**
 * Created by Apple on 12/09/16.
 */
public class MySuggestionProvider extends SearchRecentSuggestionsProvider {

    public final static String AUTHORITY = "com.doxa360.android.dutch.MySuggestionProvider";
    public final static int MODE = DATABASE_MODE_QUERIES;

    public MySuggestionProvider() {
        setupSuggestions(AUTHORITY, MODE);
    }

}
