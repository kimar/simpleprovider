package de.triplet.simpleprovider;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Build;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(RobolectricTestRunner.class)
@Config(emulateSdk = Build.VERSION_CODES.JELLY_BEAN_MR2) // FIXME: 4.4 is not yet supported
public class AbstractProviderTest {

    private TestProvider mProvider;
    private SQLiteDatabase mDatabase;

    @Before
    public void setUp() {
        mProvider = new TestProvider();
        mDatabase = mock(SQLiteDatabase.class);
    }

    @Test
    public void onCreate() {
        mProvider.onCreate(mDatabase);

        verify(mDatabase).execSQL("CREATE TABLE foos (bar TEXT PRIMARY KEY, late FLOAT);");
    }

    @Test
    public void onUpgrade() {
        mProvider.onUpgrade(mDatabase, 1, 2);

        verify(mDatabase).execSQL("ALTER TABLE foos ADD COLUMN late FLOAT;");
    }

    @Test
    public void onUpgradeEmpty() {
        mProvider.onUpgrade(mDatabase, 2, 3);

        verifyNoMoreInteractions(mDatabase);
    }

    @Test(expected = SQLiteException.class)
    public void onDowngrade() {
        mProvider.onDowngrade(mDatabase, 2, 1);
    }

}