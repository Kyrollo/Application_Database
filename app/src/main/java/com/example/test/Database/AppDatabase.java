package com.example.test.Database;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.test.Tabels.Category;
import com.example.test.Dao.CategoryDao;
import com.example.test.Dao.ItemDao;
import com.example.test.Tabels.Item;

@Database(entities = {Category.class, Item.class}, version = 18)
public abstract class AppDatabase extends RoomDatabase {
    public abstract CategoryDao categoryDao();
    public abstract ItemDao itemDao();

    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    AppDatabase.class, "app_database")
                            .allowMainThreadQueries()
                            .fallbackToDestructiveMigration()
//                            .addCallback(new RoomDatabase.Callback() {
//                                @Override
//                                public void onOpen(@NonNull SupportSQLiteDatabase db) {
//                                    super.onOpen(db);
//                                    db.execSQL("PRAGMA foreign_keys=ON;");
//                                }
//                            })
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
